package miniexchange;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatchingEngine {

    @Test
    void buyMatchesCompletelyAgainstSell() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order buyOrder = new Order(100, 50, OrderType.BUY);
        Order sellOrder1 = new Order(95, 50, OrderType.SELL);
        Order sellOrder2 = new Order(105, 50, OrderType.SELL);

        engine.submitOrder(buyOrder, book);

        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));
        Assertions.assertEquals(50, buyOrder.getRemainingQuantity());

        engine.submitOrder(sellOrder1, book);

        Assertions.assertTrue(book.getBuyOrders().isEmpty());
        Assertions.assertTrue(book.getSellOrders().isEmpty());

        Assertions.assertEquals(0, buyOrder.getRemainingQuantity());
        Assertions.assertEquals(0, sellOrder1.getRemainingQuantity());

        engine.submitOrder(sellOrder2, book);

        Assertions.assertSame(sellOrder2, book.getSellOrders().get(0));
        Assertions.assertEquals(50, sellOrder2.getRemainingQuantity());
    }


    @Test
    void buyPartiallyFillsSell() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder = new Order(95, 50, OrderType.SELL);
        Order buyOrder = new Order(100, 30, OrderType.BUY);

        engine.submitOrder(sellOrder, book);

        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));

        engine.submitOrder(buyOrder, book);

        Assertions.assertEquals(0, buyOrder.getRemainingQuantity());
        Assertions.assertEquals(20, sellOrder.getRemainingQuantity());

        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));
    }


    @Test
    void sellPartiallyFillsBuy() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order buyOrder = new Order(100, 50, OrderType.BUY);
        Order sellOrder = new Order(95, 30, OrderType.SELL);

        engine.submitOrder(buyOrder, book);

        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));

        engine.submitOrder(sellOrder, book);

        Assertions.assertEquals(0, sellOrder.getRemainingQuantity());
        Assertions.assertEquals(20, buyOrder.getRemainingQuantity());

        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));
    }


    @Test
    void buyWithNoSell() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order buyOrder = new Order(100, 50, OrderType.BUY);

        engine.submitOrder(buyOrder, book);

        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));
        Assertions.assertTrue(book.getSellOrders().isEmpty());
        Assertions.assertEquals(50, buyOrder.getRemainingQuantity());
    }


    @Test
    void sellWithNoBuy() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder = new Order(100, 50, OrderType.SELL);

        engine.submitOrder(sellOrder, book);

        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));
        Assertions.assertTrue(book.getBuyOrders().isEmpty());
        Assertions.assertEquals(50, sellOrder.getRemainingQuantity());
    }


    @Test
    void buyDoesNotMatchWhenPriceIsTooLow() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder = new Order(100, 50, OrderType.SELL);
        Order buyOrder = new Order(99, 50, OrderType.BUY);

        engine.submitOrder(sellOrder, book);
        engine.submitOrder(buyOrder, book);

        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));
        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));

        Assertions.assertEquals(50, sellOrder.getRemainingQuantity());
        Assertions.assertEquals(50, buyOrder.getRemainingQuantity());
    }


    @Test
    void sellDoesNotMatchWhenPriceIsTooHigh() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order buyOrder = new Order(100, 50, OrderType.BUY);
        Order sellOrder = new Order(101, 50, OrderType.SELL);

        engine.submitOrder(buyOrder, book);
        engine.submitOrder(sellOrder, book);

        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));
        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));

        Assertions.assertEquals(50, buyOrder.getRemainingQuantity());
        Assertions.assertEquals(50, sellOrder.getRemainingQuantity());
    }


    @Test
    void sellConsumesMultipleBuyOrders() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order buyOrder1 = new Order(105, 40, OrderType.BUY);
        Order buyOrder2 = new Order(100, 30, OrderType.BUY);
        Order sellOrder = new Order(95, 100, OrderType.SELL);

        engine.submitOrder(buyOrder1, book);
        engine.submitOrder(buyOrder2, book);
        engine.submitOrder(sellOrder, book);

        Assertions.assertEquals(0, buyOrder1.getRemainingQuantity());
        Assertions.assertEquals(0, buyOrder2.getRemainingQuantity());

        Assertions.assertTrue(book.getBuyOrders().isEmpty());

        Assertions.assertEquals(30, sellOrder.getRemainingQuantity());
        Assertions.assertSame(sellOrder, book.getSellOrders().get(0));
    }


    @Test
    void exactQuantityMatchRemovesRestingOrder() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder = new Order(100, 50, OrderType.SELL);
        Order buyOrder = new Order(105, 50, OrderType.BUY);

        engine.submitOrder(sellOrder, book);
        engine.submitOrder(buyOrder, book);

        Assertions.assertEquals(0, sellOrder.getRemainingQuantity());
        Assertions.assertEquals(0, buyOrder.getRemainingQuantity());

        Assertions.assertTrue(book.getSellOrders().isEmpty());
        Assertions.assertTrue(book.getBuyOrders().isEmpty());
    }


    @Test
    void buyGeneratesTradeAtRestingPrice() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder = new Order(95, 50, OrderType.SELL);
        Order buyOrder = new Order(100, 50, OrderType.BUY);

        engine.submitOrder(sellOrder, book);

        List<Trade> trades = engine.submitOrder(buyOrder, book);

        Assertions.assertEquals(1, trades.size());

        Trade trade = trades.get(0);

        Assertions.assertEquals(buyOrder.getOrderId(), trade.getBuyOrderId());
        Assertions.assertEquals(sellOrder.getOrderId(), trade.getSellOrderId());
        Assertions.assertEquals(95, trade.getPrice());
        Assertions.assertEquals(50, trade.getQuantity());
    }


    @Test
    void buyConsumesMultipleSellOrders() {

        OrderBook book = new OrderBook();
        MatchingEngine engine = new MatchingEngine();

        Order sellOrder1 = new Order(95, 40, OrderType.SELL);
        Order sellOrder2 = new Order(100, 30, OrderType.SELL);
        Order buyOrder = new Order(105, 100, OrderType.BUY);

        engine.submitOrder(sellOrder1, book);
        engine.submitOrder(sellOrder2, book);

        List<Trade> trades = engine.submitOrder(buyOrder, book);

        Assertions.assertEquals(2, trades.size());

        Assertions.assertEquals(40, trades.get(0).getQuantity());
        Assertions.assertEquals(95, trades.get(0).getPrice());

        Assertions.assertEquals(30, trades.get(1).getQuantity());
        Assertions.assertEquals(100, trades.get(1).getPrice());

        Assertions.assertEquals(30, buyOrder.getRemainingQuantity());

        Assertions.assertTrue(book.getSellOrders().isEmpty());
        Assertions.assertSame(buyOrder, book.getBuyOrders().get(0));
    }
}