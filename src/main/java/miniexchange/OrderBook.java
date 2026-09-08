package miniexchange;
import java.util.ArrayList;
import java.util.List;

public class OrderBook {

    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public void addOrder(Order order) {

        if (order.getOrderType() == OrderType.BUY) {

            for (int i = 0; i < buyOrders.size(); i++) {
                Order buyOrder = buyOrders.get(i);

                if (order.getPrice() > buyOrder.getPrice()) {
                    buyOrders.add(i, order);
                    return;
                }
            }

            buyOrders.add(order);

        } else {

            for (int i = 0; i < sellOrders.size(); i++) {
                Order sellOrder = sellOrders.get(i);

                if (order.getPrice() < sellOrder.getPrice()) {
                    sellOrders.add(i, order);
                    return;
                }
            }

            sellOrders.add(order);
        }
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}