package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * カートクラス
 * 
 * @author  23jz 井手
 * @version 1.0 2024/12/04
 */

public class Cart implements Serializable {
    private int           id;
    private int           orderId;
    private int           productId;
    private int           sizeId;
    private int           quantity;
    private int           statusId;
    private LocalDateTime orderTime;

    public Cart() {

    }

    //連番除いたコンストラクタ
    public Cart(int orderId, int productId, int sizeId, int quantity, int statusId, LocalDateTime orderTime) {
        this.orderId   = orderId;
        this.productId = productId;
        this.sizeId    = sizeId;
        this.quantity  = quantity;
        this.statusId  = statusId;
        this.orderTime = orderTime;
    }

    //すべてのフィールド込みコンストラクタ
    public Cart(int id, int orderId, int productId, int sizeId, int quantity, int statusId, LocalDateTime orderTime) {
        this.id        = id;
        this.orderId   = orderId;
        this.productId = productId;
        this.sizeId    = sizeId;
        this.quantity  = quantity;
        this.statusId  = statusId;
        this.orderTime = orderTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", orderId=" + orderId + ", productId=" + productId + ", sizeId=" + sizeId + ", quantity=" + quantity
                + ", statusId=" + statusId + ", orderTime=" + orderTime + "]";
    }

    public String getFormattedOrderDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
