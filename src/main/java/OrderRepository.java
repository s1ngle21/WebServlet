import entity.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private Map<Integer, Order> orders;

    public OrderRepository() {
        this.orders = new HashMap<>();
    }

    public Order getOrderById(int id) {
        return this.orders.get(id);
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>(orders.values());
        return orderList;
    }

    public void addOrder(Order order) {
        this.orders.put(order.getId(), order);
    }
}
