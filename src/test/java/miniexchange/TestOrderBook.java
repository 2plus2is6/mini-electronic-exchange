package miniexchange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;




public class TestOrderBook {

    @Test
    void buyOrdersAreAdded(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(105, 20, OrderType.BUY);
        Order order3 = new Order(102, 30, OrderType.BUY);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertEquals(3,book.getBuyOrders().size());
    }

    @Test
    void sellOrdersAreAdded(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.SELL);
        Order order2 = new Order(105, 20, OrderType.SELL);
        Order order3 = new Order(102, 30, OrderType.SELL);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertEquals(3,book.getSellOrders().size());
    }

    @Test 
    void areBuyOrdersSorted(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(105, 20, OrderType.BUY);
        Order order3 = new Order(102, 30, OrderType.BUY);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertEquals(100,book.getBuyOrders().get(2).getPrice());
        assertEquals(102,book.getBuyOrders().get(1).getPrice());
        assertEquals(105,book.getBuyOrders().get(0).getPrice());
    }

    @Test 
    void areSellOrdersSorted(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.SELL);
        Order order2 = new Order(105, 20, OrderType.SELL);
        Order order3 = new Order(102, 30, OrderType.SELL);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertEquals(105,book.getSellOrders().get(2).getPrice());
        assertEquals(102,book.getSellOrders().get(1).getPrice());
        assertEquals(100,book.getSellOrders().get(0).getPrice());
    }

    @Test
    void isTimeOrderPrioritized(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(102, 20, OrderType.BUY);
        Order order3 = new Order(100, 30, OrderType.BUY);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertSame(order2,book.getBuyOrders().get(0));
        assertSame(order1,book.getBuyOrders().get(1));
        assertSame(order3,book.getBuyOrders().get(2));
    }

    @Test
    void isBestBidFetched(){
        OrderBook book = new OrderBook();

        
        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(105, 20, OrderType.BUY);
        Order order3 = new Order(101, 30, OrderType.BUY);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertSame(order2,book.getBestBid());
    }

    @Test
    void isBestAskFetched(){
        OrderBook book = new OrderBook();

        
        Order order1 = new Order(100, 50, OrderType.SELL);
        Order order2 = new Order(105, 20, OrderType.SELL);
        Order order3 = new Order(99, 30, OrderType.SELL);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);

        assertSame(order3,book.getBestAsk());
    }

    @Test
    void isBestAskFetchedWhenNoBids(){
        OrderBook book = new OrderBook();

        assertNull(book.getBestBid());
    }

    @Test
    void isBestBidFetchedWhenNoAsks(){
        OrderBook book = new OrderBook();

        assertNull(book.getBestAsk());
    }

    @Test
    void totalBidQuantityAtPrice(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(105, 20, OrderType.BUY);
        Order order3 = new Order(110, 30, OrderType.BUY);
        Order order4 = new Order(110, 30, OrderType.BUY);
        Order order5 = new Order(100, 21, OrderType.BUY);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);
        book.addOrder(order4);
        book.addOrder(order5);

        assertEquals(71,book.getTotalBidQuantityAtPrice(100));
    }

    @Test
    void totalSellQuantityAtPrice(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.SELL);
        Order order2 = new Order(105, 20, OrderType.SELL);
        Order order3 = new Order(110, 30, OrderType.SELL);
        Order order4 = new Order(110, 30, OrderType.SELL);
        Order order5 = new Order(100, 21, OrderType.SELL);

        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);
        book.addOrder(order4);
        book.addOrder(order5);

        assertEquals(71,book.getTotalAskQuantityAtPrice(100));
    }


    @Test
    void isOrderBookEmpty(){
        OrderBook book = new OrderBook();

        assertTrue(book.isEmpty());
    }

    @Test
    void cancelBuyOrder(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);

        book.addOrder(order1);

        book.cancelOrder(order1.getOrderId());

        assertTrue(book.isEmpty());
    }

    @Test
    void cancelSellOrder(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.SELL);

        book.addOrder(order1);

        book.cancelOrder(order1.getOrderId());

        assertTrue(book.isEmpty());
    }

    @Test
    void cancelNonExistingOrder(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);

        book.addOrder(order1);

        book.cancelOrder(2);

        assertFalse(book.isEmpty());
    }

    @Test
    void spreadOfOrderBook(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY);
        Order order2 = new Order(105, 20, OrderType.SELL);

        book.addOrder(order1);
        book.addOrder(order2);

        assertEquals(5,book.getSpread());
    }

    @Test
    void spreadOfOrderBookWhenNoAsks(){
        OrderBook book = new OrderBook();

        Order order1 = new Order(100, 50, OrderType.BUY); 

        book.addOrder(order1);

        assertEquals(-1,book.getSpread());
    }
}