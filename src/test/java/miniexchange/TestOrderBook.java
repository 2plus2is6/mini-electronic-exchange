package miniexchange;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(2,book.getBuyOrders().get(0).getOrderId());
        assertEquals(1,book.getBuyOrders().get(1).getOrderId());
        assertEquals(3,book.getBuyOrders().get(2).getOrderId());
    }

}