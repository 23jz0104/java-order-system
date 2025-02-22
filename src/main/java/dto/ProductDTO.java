package dto;

import java.io.Serializable;

import model.Call;
import model.Cart;
import model.Category;
import model.Product;
import model.Size;
import model.Status;

public class ProductDTO implements Serializable {
    private Product  product;
    private Category category;
    private Size     size;
    private Cart     cart;
    private Status   status;
    private Call     event;
    private String   formattedOrderDate;
    private int      tableNumber;
    private int      quantity;
    private int      adjustedPrice; //サイズに応じて調整された後の金額

    public ProductDTO() {

    }

    public ProductDTO(Product product, Category category) {
        this.product  = product;
        this.category = category;
    }

    public ProductDTO(Status status, Call event, String formattedOrderDate) {
        super();
        this.status             = status;
        this.event              = event;
        this.formattedOrderDate = formattedOrderDate;
    }

    public ProductDTO(Product product, Size size, Cart cart, String formattedOrderDate, int adjustedPrice) {
        super();
        this.product            = product;
        this.size               = size;
        this.cart               = cart;
        this.formattedOrderDate = formattedOrderDate;
        this.adjustedPrice      = adjustedPrice;
    }

    public ProductDTO(Product product, Category category, Size size, Cart cart, String formattedOrderDate, int tableNumber) {
        super();
        this.product            = product;
        this.category           = category;
        this.size               = size;
        this.cart               = cart;
        this.formattedOrderDate = formattedOrderDate;
        this.tableNumber        = tableNumber;
    }

    public ProductDTO(Product product, Category category, Size size, Cart cart, Status status, String formattedOrderTime,
            int tableNumber) {
        this.product            = product;
        this.category           = category;
        this.size               = size;
        this.cart               = cart;
        this.status             = status;
        this.formattedOrderDate = formattedOrderTime;
        this.tableNumber        = tableNumber;
    }

    //注文管理画面用
    public ProductDTO(Product product, Category category, Size size, Cart cart, Status status, Call event, String formattedOrderTime,
            int tableNumber) {
        this.product            = product;
        this.category           = category;
        this.size               = size;
        this.cart               = cart;
        this.status             = status;
        this.event              = event;
        this.formattedOrderDate = formattedOrderTime;
        this.tableNumber        = tableNumber;
    }

    //商品詳細ページ用
    public ProductDTO(Product product, Category category, Size size, int adjustedPrice) {
        this.product       = product;
        this.category      = category;
        this.size          = size;
        this.adjustedPrice = adjustedPrice;
    }

    //商品カート追加用
    public ProductDTO(Product product, Category category, Size size, int adjustedPrice, int quantity) {
        this.product       = product;
        this.category      = category;
        this.size          = size;
        this.adjustedPrice = adjustedPrice;
        this.quantity      = quantity;
    }

    public ProductDTO(Product product, Size size, int adjustedPrice, int quantity) {
        this.product       = product;
        this.size          = size;
        this.adjustedPrice = adjustedPrice;
        this.quantity      = quantity;
    }

    public ProductDTO(Product product, Category category, Size size, Cart cart, int quantity, int adjustedPrice) {
        super();
        this.product       = product;
        this.category      = category;
        this.size          = size;
        this.cart          = cart;
        this.quantity      = quantity;
        this.adjustedPrice = adjustedPrice;
    }

    public ProductDTO(Product product, Category category, Size size, Cart cart, Status status, Call event, String formattedOrderDate,
            int tableNumber, int quantity, int adjustedPrice) {
        super();
        this.product            = product;
        this.category           = category;
        this.size               = size;
        this.cart               = cart;
        this.status             = status;
        this.event              = event;
        this.formattedOrderDate = formattedOrderDate;
        this.tableNumber        = tableNumber;
        this.quantity           = quantity;
        this.adjustedPrice      = adjustedPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Call getEvent() {
        return event;
    }

    public void setEvent(Call event) {
        this.event = event;
    }

    public String getFormattedOrderDate() {
        return formattedOrderDate;
    }

    public void setFormattedOrderDate(String formattedOrderDate) {
        this.formattedOrderDate = formattedOrderDate;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getAdjustedPrice() {
        return adjustedPrice;
    }

    public void setAdjustedPrice(int adjustedPrice) {
        this.adjustedPrice = adjustedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductDTO [product=" + product + ", category=" + category + ", size=" + size + ", cart=" + cart + ", status=" + status
                + ", formattedOrderDate=" + formattedOrderDate + ", tableNumber=" + tableNumber + ", quantity=" + quantity + ", adjustedPrice="
                + adjustedPrice + "]";
    }

}
