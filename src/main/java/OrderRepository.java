import entity.Order;

import java.util.*;

public class OrderRepository {
    private Map<Integer, Order> orders;

    public OrderRepository() {
        this.orders = new HashMap<>();
    }

    public Order getOrderById(int id) {
        return this.orders.get(id);
    }

    public Map<Integer, Order> getAllOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        Objects.requireNonNull(order);
        this.orders.put(order.getId(), order);
    }

    public void deleteOrder(int id) {
        this.orders.remove(id);
    }

    public void updateOrder(int id, Order order) {
        orders.put(id, order);
    }


    public void setOrders(Map<Integer, Order> orders) {
        this.orders = orders;
    }
}
