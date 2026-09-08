package miniexchange;
public class Order {

    private static int nextOrderId = 1;

    private final int orderId;
    private final OrderType orderType;
    private final int price;
    private final int originalQuantity;
    private int remainingQuantity;

    public Order(int price, int originalQuantity, OrderType orderType) {
        this.orderId = nextOrderId++;
        this.orderType = orderType;
        this.price = price;
        this.originalQuantity = originalQuantity;
        this.remainingQuantity = originalQuantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getPrice() {
        return price;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
}