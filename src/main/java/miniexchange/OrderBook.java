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

    public Order getBestBid() {
        if (buyOrders.isEmpty()) {
            return null;
        }
        return buyOrders.get(0);
    }

    public Order getBestAsk() {
        if (sellOrders.isEmpty()) {
            return null;
        }
        return sellOrders.get(0);
    }

    public int getTotalBidQuantityAtPrice(int price){
        int totalQuantity = 0;
        for(Order order : buyOrders){
            if(order.getPrice() == price){
                totalQuantity += order.getRemainingQuantity();
            }
        }
        return totalQuantity;
    }

    public int getTotalAskQuantityAtPrice(int price){
        int totalQuantity = 0;
        for(Order order : sellOrders){
            if(order.getPrice() == price){
                totalQuantity += order.getRemainingQuantity();
            }
        }
        return totalQuantity;
    }

    public void cancelOrder(int orderId) {
        for (int i = 0; i < buyOrders.size(); i++) {
            if (buyOrders.get(i).getOrderId() == orderId) {
                buyOrders.remove(i);
                return;
            }
        }

        for (int i = 0; i < sellOrders.size(); i++) {
            if (sellOrders.get(i).getOrderId() == orderId) {
                sellOrders.remove(i);
                return;
            }
        }
        
    }

    public int getSpread(){
        if(buyOrders.isEmpty() || sellOrders.isEmpty()){
            return -1;
        }
        return getBestAsk().getPrice() - getBestBid().getPrice();
    }

    public boolean isEmpty() {
        return buyOrders.isEmpty() && sellOrders.isEmpty();
    }
}