package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.UserBaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.sky.service.CommentService;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.Long;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ZhuanyuanMapper zhuanyuanMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Value("${sky.shop.address}")
    private String shopAddress;

    @Value("${sky.baidu.ak}")
    private String ak;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */

    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 获取收货地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 检查是否超出配送范围
        checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        Long currentId = UserBaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(currentId);

        // 获取购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 构造订单数据
        Orders order = new Orders();
        try {
            BeanUtils.copyProperties(ordersSubmitDTO, order);  // 使用 BeanUtils 进行属性复制
        } catch (Exception e) {
            throw new RuntimeException("订单数据转换失败", e);  // 直接抛出 RuntimeException
        }
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(currentId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 生成订单明细
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCartList.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);  // 复制购物车数据到订单明细
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        });

        // 批量插入订单明细
        orderDetailMapper.insertBatch(orderDetailList);

        // 清空购物车
        shoppingCartMapper.deleteByUserId(currentId);

        // 构建返回结果
        OrderSubmitVO submitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return submitVO;
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     * @throws Exception
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        当前登录用户id
        Long userId = UserBaseContext.getCurrentId();
        User user = userMapper.getById(String.valueOf(userId));

        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Orders orders = orderMapper.getByNumberAndUserId(orderNumber, userId);

//        调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(),
//                orders.getAmount(),
//                "校跑帮订单" + orders.getId(),
//                user.getOpenid()
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
        // 替代微信支付的方法
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        //为替代微信支付成功后的数据库订单状态更新,多定义一个方法进行修改
        Integer OrderPaidStatus = Orders.PAID;  //支付状态,已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED;  //订单状态,待接单

        //发现没有将支付时间 check_out属性赋值,所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();

        //获取订单号码
//        String orderNumber = ordersPaymentDTO.getOrderNumber();

        log.info("调用updtateStatus,用于替换微信支付更新数据库状态的问题");
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderNumber);
//TODO 这边返回的vo实体是空的，数据模拟

//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        vo.setNonceStr(jsonObject.getString("nonce_str"));  // 添加nonceStr
        vo.setPaySign(jsonObject.getString("pay_sign"));    // 添加paySign
        vo.setTimeStamp(jsonObject.getString("time_stamp"));// 添加timeStamp
        vo.setSignType(jsonObject.getString("sign_type"));  // 添加signType


        return vo;
    }

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
//        当前登录用户id
        Long userId = UserBaseContext.getCurrentId();

//        根据订单号查询当前用户的订单
        Orders orderDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

//        根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(orderDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);

        HashMap map = new HashMap();
        map.put("type", 1);
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + outTradeNo);

//        通过WebSocket实现来电提醒，向客户端浏览器推送消息
        webSocketServer.sendToAllClient(JSON.toJSONString(map));

    }

    /**
     * 用户端订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(UserBaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);


        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList<>();

        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();

                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
                
                OrderCommentStatusDTO commentStatusDTO = commentMapper.selectOrderCommentStatusById(orderId);
                String commentStatus = (commentStatusDTO != null) ? commentStatusDTO.getCommentStatus() : "暂未评论";


                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVO.setCommentStatus(commentStatus); // ✅ 设置评论状态

                list.add(orderVO);
            }
        }

        return new PageResult(page.getTotal(), list);
    }

    /**
     * 用户端专员接取订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult pageQuery4Zhuhanyuan(int pageNum, int pageSize, Integer status) {
        Long zhuanyuanId = orderMapper.getZhuanyuanIdByUserId(UserBaseContext.getCurrentId());
        if (zhuanyuanId == null) {
            return new PageResult(0L, new ArrayList<>());
        }

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(pageNum);
        ordersPageQueryDTO.setPageSize(pageSize);
        ordersPageQueryDTO.setZhuanyuanId(zhuanyuanId);
        ordersPageQueryDTO.setStatus(status);

        // ✅ PageHelper.startPage 必须紧贴 pageQuery() 调用
//        PageHelper.startPage() 只对紧随其后的第一个 select 语句生效。
        PageHelper.startPage(pageNum, pageSize);
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO); // ✅ 第一个 select，这里才生效

        List<OrderVO> list = new ArrayList<>();
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
                OrderCommentStatusDTO commentStatusDTO = commentMapper.selectOrderCommentStatusById(orderId);
                String commentStatus = (commentStatusDTO != null) ? commentStatusDTO.getCommentStatus() : "暂未评论";

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVO.setCommentStatus(commentStatus);
                list.add(orderVO);
            }
        }

        return new PageResult(page.getTotal(), list);
    }



    /**
     * 用户端可接订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult accept4User(int pageNum, int pageSize, Integer status) {

        //封装所需的请求参数为DTO对象
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(pageNum);
        ordersPageQueryDTO.setPageSize(pageSize);
        ordersPageQueryDTO.setStatus(status);

        System.out.println(ordersPageQueryDTO);

        //需要在查询功能之前开启分页功能：当前页的页码   每页显示的条数
        PageHelper.startPage(pageNum, pageSize);
        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        System.out.println(page.getTotal());
        System.out.println(page.getResult().size());

        //由接口可知需要封装为orderVO类型：订单菜品信息orderDishes，订单详情orderDetailList
        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) { //有订单才有必要接着查询订单详情信息
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 根据订单id,查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void accept4Zhuanyuan(Long id) {
//        根据id查询订单
        Orders orderDB = orderMapper.getById(id);
        System.out.println(orderDB);
//        校验订单是否存在
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
//      订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (orderDB.getStatus().equals(Orders.CANCELLED) || orderDB.getStatus().equals(Orders.PENDING_PAYMENT) ) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(orderDB.getId());

//        订单处于待接单的状态下接取
        if (orderDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            Long userId = UserBaseContext.getCurrentId();
            orders.setZhuanyuanId(orderMapper.getZhuanyuanIdByUserId(userId));

//            支付状态修改为 已接单
            orders.setStatus(Orders.CONFIRMED);
        }
//        订单处于已接单的状态下进行派送
        if (orderDB.getStatus().equals(Orders.CONFIRMED)) {
//            支付状态修改为 派送中
            orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        }

        //        订单处于已接单的状态下完成订单
        if (orderDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
//            支付状态修改为 已完成
            orders.setStatus(Orders.COMPLETED);
            zhuanyuanMapper.updateReward2(orders.getZhuanyuanId(),0,15);
        }
//        更新订单状态
        orderMapper.update(orders);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Long id) {
//        根据id查询订单
        Orders orders = orderMapper.getById(id);

//        查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 3. 查询评论状态
        OrderCommentStatusDTO commentStatusDTO = commentMapper.selectOrderCommentStatusById(id);
        String commentStatus = (commentStatusDTO != null) ? commentStatusDTO.getCommentStatus() : "暂未评论";

//        将订单及其详情封装到OrderVo并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setCommentStatus(commentStatus); // ✅ 设置评论状态，我的精神状态be like😂😊🤣🤣😋🥲🥲🥲

        return orderVO;
    }

    /**
     * 用户取消订单
     * @param id
     */
    @Override
    public void userCancelById(Long id) throws Exception {
//        根据id查询订单
        Orders orderDB = orderMapper.getById(id);

//        校验订单是否存在
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

//      订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (orderDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(orderDB.getId());

//        订单处于待接单的状态下取消，需要进行退款
        if (orderDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
//            调用微信支付退款接口
//            weChatPayUtil.refund(
//                    orderDB.getNumber(),
//                    orderDB.getNumber(),
//                    orders.getAmount(),
//                    orders.getAmount()
//            );
            // 模拟退款逻辑（替代真实的微信退款）
            String refund = "模拟退款成功：订单号=" + orderDB.getNumber() + "，金额=" + orderDB.getAmount();
            log.info("模拟退款：{}", refund);

//            支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

//        更新订单状态，取消原因、时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repetition(Long id) {
//        查询当前用户id
        Long userId = UserBaseContext.getCurrentId();

//        根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

//        将订单详情对象转为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();

//            将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

//        将购物车对象批量添加到购物车
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> pageQuery = orderMapper.pageQuery(ordersPageQueryDTO);

//        部分订单状态，需要额外返回订单菜品信息，将orders转化为orderVo
        List<OrderVO> orderVoList = getOrderVoList(pageQuery);
        return new PageResult(pageQuery.getTotal(), orderVoList);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
//        根据状态，分别查询出接待单，待派送、派送中的订单数量

        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

//        将查询出的数据封装
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }

    /**
     * 接单
     * @param ordersCancelDTO
     */
    @Override
    public void confirm(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
//        根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

//        订单只有存在且状态为2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

//        支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
//            用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    ordersDB.getAmount(),
//                    ordersDB.getAmount()
//            );
            // 模拟退款逻辑（替代真实的微信退款）
            String refund = "模拟退款成功：订单号=" + ordersDB.getNumber() + "，金额=" + ordersDB.getAmount();
//            log.info("模拟退款：{}", refund);
            log.info("申请退款：{}", refund);
        }

//        拒单需要退款，根据订单id更新订单状态，拒单原因，取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
//        根据id查询订单
        Orders orderDB = orderMapper.getById(ordersCancelDTO.getId());
//        System.out.println("支付状态paystatus:"+orderDB);
//        支付状态
        Integer payStatus = orderDB.getPayStatus();
        System.out.println("支付状态paystatus:"+payStatus);
        if (payStatus == 1) {
//            用于已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    orderDB.getNumber(),
//                    orderDB.getNumber(),
//                    orderDB.getAmount(),
//                    orderDB.getAmount()
//            );
//            log.info("申请退款：{}", refund);

            // 模拟退款逻辑（替代真实的微信退款）
            String refund = "模拟退款成功：订单号=" + orderDB.getNumber() + "，金额=" + orderDB.getAmount();
            log.info("模拟退款：{}", refund);
        }

//      管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 客户端派送订单
     * @param id
     */
    @Override
    public void delivery(Long id) {
//        根据id查询订单
        Orders orderDB = orderMapper.getById(id);

//        校验订单是否存在，并且状态为3
        if (orderDB == null || !orderDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(orderDB.getId());

//        更新订单状态，状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * 客户端完成订单
     * @param id
     */
    @Override
    public void complete(Long id) {
//        根据id查询订单
        Orders orderDB = orderMapper.getById(id);

//        校验订单是否存在，并且状态为4
        if (orderDB == null || !orderDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

//        这里不应该创建新对象
//        Orders orders = new Orders();
//        orders.setId(orders.getId());

//        更新订单状态，状态转为完成
        orderDB.setStatus(Orders.COMPLETED);
        System.out.println("订单status:"+orderDB.getStatus());
        orderDB.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orderDB);
    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void reminder(Long id) {
//        查询订单是否存在
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

//        基于WebSocket实现催单
        HashMap map = new HashMap();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号：" + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * 部分订单状态，需要额外返回订单菜品信息，将orders转化为orderVo
     * @param page
     * @return
     */
    private List<OrderVO> getOrderVoList(Page<Orders> page) {
//        需要返回订单菜品信息，自定义OrderVo响应结果
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(orders -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);

                String orderDishStr = getOrderDishStr(orders);
                orderVO.setOrderDishes(orderDishStr);
                orderVOArrayList.add(orderVO);
            });
        }
        return orderVOArrayList;
    }

    /**
     * 根据订单id获取菜品信息字符串
     * @param orders
     * @return
     */
    private String getOrderDishStr(Orders orders) {
//        查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

//        将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> ordewrDishList = orderDetailList.stream().map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber() + ";").collect(Collectors.toList());

//        将该订单对应的所有菜品信息拼接在一起
        return String.join("", ordewrDishList);
    }

    /**
     * 检查客户的收货地址是否超出配送范围
     * 百度地图的用不了了，前端换成腾讯地图了
     * @param address
     */
    private void checkOutOfRange(String address) {
        HashMap<String, String> map = new HashMap<>();
        map.put("address", shopAddress);
        map.put("output", "json");
        map.put("ak", ak);

        try {
            // 获取店铺的经纬度坐标
            String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3?", map);
            JSONObject jsonObject = JSON.parseObject(shopCoordinate);

            if (!jsonObject.getString("status").equals("0")) {
                throw new OrderBusinessException("店铺地址解析失败");
            }

            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject location = result.getJSONObject("location");
            String shopLat = location.getString("lat");
            String shopLng = location.getString("lng");

            System.out.println("店铺地址：");
            System.out.println("纬度：" + shopLat);
            System.out.println("经度：" + shopLng);

            // 店铺经纬度坐标
            String shopLngLat = shopLat + "," + shopLng;

            // 获取用户地址的经纬度坐标
            map.put("address", address);
            String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3?", map);
            JSONObject jsonObject2 = JSON.parseObject(userCoordinate);

            if (!jsonObject2.getString("status").equals("0")) {
                throw new OrderBusinessException("用户地址解析失败");
            }

            JSONObject result2 = jsonObject2.getJSONObject("result");
            JSONObject location2 = result2.getJSONObject("location");
            String userLat = location2.getString("lat");
            String userLng = location2.getString("lng");

            System.out.println("用户地址：");
            System.out.println("纬度：" + userLat);
            System.out.println("经度：" + userLng);

            // 用户收货地址经纬度坐标
            String userLngLat = userLat + "," + userLng;

            map.put("origin", shopLngLat);
            map.put("destination", userLngLat);
            map.put("steps_info", "0");

            // 路线规划
            String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/walking?", map);
            jsonObject = JSON.parseObject(json);

            if (!jsonObject.getString("status").equals("0")) {
                throw new OrderBusinessException("配送线路规划失败");
            }

            JSONArray routes = jsonObject.getJSONObject("result").getJSONArray("routes");
            if (routes != null && !routes.isEmpty()) {
                Integer distance = routes.getJSONObject(0).getInteger("distance");
                if (distance > 5000) {
                    // 配送距离超过5000米
                    throw new OrderBusinessException("超出配送范围");
                }
            } else {
                throw new OrderBusinessException("路线规划数据为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderBusinessException("请求百度API时发生错误：" + e.getMessage());
        }
    }

//    private void checkOutOfRange(String address) {
//        HashMap map = new HashMap();
//        map.put("address", shopAddress);
//        map.put("output", "json");
//        map.put("ak", ak);
//
////        获取店铺的经纬度坐标
//        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3?", map);
//
//        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
//        // 解析JSON数据
////        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
////        JSONObject result = jsonObject.getJSONObject("result");
////        JSONObject location = result.getJSONObject("location");
////        String lat = location.getString("lat");
////        String lng = location.getString("lng");
//
//// 打印结果
////        System.out.println("纬度：" + lat);
////        System.out.println("经度：" + lng);
//        if (!jsonObject.getString("status").equals("0")) {
//            throw new OrderBusinessException("店铺地址解析失败");
//        }
//
////        数据解析
//        JSONObject result = jsonObject.getJSONObject("result");
//        JSONObject location = jsonObject.getJSONObject("location");
//        String lat = location.getString("lat");
//        String lng = location.getString("lng");
//        System.out.println("店铺地址：");
//        System.out.println("纬度：" + lat);
//        System.out.println("经度：" + lng);
////        店铺经纬度坐标
//        String shopLngLat = lat + "," + lng;
//
//        map.put("address", address);
//
////        获取用户地址的经纬度坐标
//        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3?", map);
//
////        数据解析
////        location = JSON.parseObject("result").getJSONObject("location");
////        lat = location.getString("lat");
////        lng = location.getString("lng");
//        JSONObject jsonObject2 = JSON.parseObject(userCoordinate);
////        JSONObject result2 = jsonObject2.getJSONObject("result");
////        location = result2.getJSONObject("location");
////        lat = location.getString("lat");
////        lng = location.getString("lng");
//
//        if (!jsonObject2.getString("status").equals("0")) {
//            throw new OrderBusinessException("用户地址解析失败");
//        }
//        JSONObject result2 = jsonObject2.getJSONObject("result");
//        JSONObject location2 = result2.getJSONObject("location");
//        String userLat = location2.getString("lat");
//        String userLng = location2.getString("lng");
//
//        System.out.println("用户地址：");
//        System.out.println("纬度：" + userLat);
//        System.out.println("经度：" + userLng);
//
//        // 用户收货地址经纬度坐标
//        String userLngLat = userLat + "," + userLng;
//
//        map.put("orgin", shopLngLat);
//        map.put("destination", userLngLat);
//        map.put("steps_info", "0");
//
//        //路线规划
//        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/walking?", map);
////        https://api.map.baidu.com/directionlite/v1/riding?
////        https://api.map.baidu.com/directionlite/v1/walking?
//        jsonObject = JSON.parseObject(json);
//        if (!jsonObject.getString("status").equals("0")) {
//            throw new OrderBusinessException("配送线路规划失败");
//        }
//
////        数据解析
////        JSONObject result = jsonObject.getJSONObject("result");
//        JSONArray jsonArray = (JSONArray) result.get("routes");
//        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
//
//        if(distance > 5000){
//            //配送距离超过5000米
//            throw new OrderBusinessException("超出配送范围");
//        }
//    }
}
