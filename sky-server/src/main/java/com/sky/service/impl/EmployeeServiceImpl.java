package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.AdminBaseContext;
//import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            System.out.println("账号不存在");
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行sha256加密，然后再进行比对,(取消md5加密,安全性不行)
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
        password = DigestUtils.sha256Hex(employeeLoginDTO.getPassword().getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            System.out.println("密码错误");
//            System.out.println("默认密码加密后: " + DigestUtils.sha256Hex("123456"));

            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            System.out.println("账号被锁定");
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 对应 404
    public class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)  // 对应 403
    public class PasswordErrorException extends RuntimeException {
        public PasswordErrorException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.LOCKED)  // 对应 423
    public class AccountLockedException extends RuntimeException {
        public AccountLockedException(String message) {
            super(message);
        }
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号的状态，默认正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，默认密码123456
        employee.setPassword(DigestUtils.sha256Hex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

//        通过ThreadLocal获取用户信息
        Long currentId = AdminBaseContext.getCurrentId();

        //设置当前记录创建人id和修改人id
        employee.setCreateUser(currentId);//目前写个假数据，后期修改
        employee.setUpdateUser(currentId);

        employeeMapper.insert(employee);//后续步骤定义
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Long id = AdminBaseContext.getCurrentId();
        String name = employeeMapper.getById(id).getName();
        if(id == 1){
            //        开始分页查询
            PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

            Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

            long total = page.getTotal();
            List<Employee> records = page.getResult();

            return new PageResult(total, records);
        }else{
            // 普通员工：只查自己
            Employee employee = employeeMapper.getById(id);
            List<Employee> records = new ArrayList<>();
            if (employee != null) {
                records.add(employee);
            }

            return new PageResult(records.size(), records);  // 只有一条或零条
        }
    }

    /**
     * 启用禁用员工账户
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据iD查询用户信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(AdminBaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

}