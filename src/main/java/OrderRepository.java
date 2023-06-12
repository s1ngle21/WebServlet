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

    public String getAllOrders() {
        StringBuffer sb = new StringBuffer();
        List<Order> orderList = new ArrayList<>(this.orders.values());
        orderList
                .forEach(order -> sb.append(order).append("\n"));
        return sb.toString();
    }

    public void addOrder(Order order) {
        this.orders.put(order.getId(), order);
    }
}
