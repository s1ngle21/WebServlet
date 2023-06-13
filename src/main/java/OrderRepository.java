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

    public void deleteOrder(int id) {
        this.orders.remove(id);
    }

    public void updateOrder(int id, Order order) {
        List<Order> orderList = new ArrayList<>(this.orders.values());
        int index = getIndexByOrderId(id);
        orderList.set(index, order);
    }

    public int getIndexByOrderId(int id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == id);
            return i;
        }
        return -1;
    }
}
