package miniexchange;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    public List<Trade> submitOrder(Order order, OrderBook orderBook) {

        List<Trade> trades = new ArrayList<>();

        if (order.getOrderType() == OrderType.BUY) {
            matchBuyOrder(order, orderBook, trades);
        } else {
            matchSellOrder(order, orderBook, trades);
        }

        if (order.getRemainingQuantity() > 0) {
            orderBook.addOrder(order);
        }

        return trades;
    }

    private void matchBuyOrder(
            Order order,
            OrderBook orderBook,
            List<Trade> trades) {

        while (order.getRemainingQuantity() > 0 && !orderBook.getSellOrders().isEmpty()) {

            Order restingOrder = orderBook.getSellOrders().get(0);

            if (order.getPrice() < restingOrder.getPrice()) {
                break;
            }

            int tradedQuantity = Math.min(
                order.getRemainingQuantity(),
                restingOrder.getRemainingQuantity()
            );

            Trade trade = new Trade(
                order.getOrderId(),
                restingOrder.getOrderId(),
                restingOrder.getPrice(),
                tradedQuantity
            );

            trades.add(trade);

            order.setRemainingQuantity(
                order.getRemainingQuantity() - tradedQuantity
            );

            restingOrder.setRemainingQuantity(
                restingOrder.getRemainingQuantity() - tradedQuantity
            );

            if (restingOrder.getRemainingQuantity() == 0) {
                orderBook.getSellOrders().remove(0);
            }
        }
    }

    private void matchSellOrder(
            Order order,
            OrderBook orderBook,
            List<Trade> trades) {

        while (order.getRemainingQuantity() > 0 && !orderBook.getBuyOrders().isEmpty()) {

            Order restingOrder = orderBook.getBuyOrders().get(0);

            if (order.getPrice() > restingOrder.getPrice()) {
                break;
            }

            int tradedQuantity = Math.min(
                order.getRemainingQuantity(),
                restingOrder.getRemainingQuantity()
            );

            Trade trade = new Trade(
                restingOrder.getOrderId(),
                order.getOrderId(),
                restingOrder.getPrice(),
                tradedQuantity
            );

            trades.add(trade);

            order.setRemainingQuantity(
                order.getRemainingQuantity() - tradedQuantity
            );

            restingOrder.setRemainingQuantity(
                restingOrder.getRemainingQuantity() - tradedQuantity
            );

            if (restingOrder.getRemainingQuantity() == 0) {
                orderBook.getBuyOrders().remove(0);
            }
        }
    }
}