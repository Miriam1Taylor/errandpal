package com.sky.dto;

public class AssignZhuanyuanRequest {
    private Long orderId;
    private Long zhuanyuanId;

    public AssignZhuanyuanRequest() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getZhuanyuanId() { return zhuanyuanId; }
    public void setZhuanyuanId(Long zhuanyuanId) { this.zhuanyuanId = zhuanyuanId; }

    @Override
    public String toString() {
        return "AssignZhuanyuanRequest{orderId=" + orderId + ", zhuanyuanId=" + zhuanyuanId + "}";
    }
}
