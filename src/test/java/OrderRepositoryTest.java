import entity.Order;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.internal.matchers.Or;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRepositoryTest {
    @Mock
    private OrderRepository orderRepository;
    private AutoCloseable autoCloseable;


    @BeforeEach
    public void setup() {
        orderRepository = new OrderRepository();
        autoCloseable = MockitoAnnotations.openMocks(OrderRepositoryTest.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
        orderRepository = null;
        autoCloseable.close();
    }

    @Test
    public void whenPerformGetOrderByIdOperationThenOrderWithGivenIdMustBeReturned() {
        Order expectedOrder = new Order(1, null, 0, null);
        orderRepository.addOrder(expectedOrder);
        Order result = orderRepository.getOrderById(1);
        assertEquals(expectedOrder, result);

    }

    @Test
    public void whenPerformGetAllOrdersOperationThenAllOrdersMustBeReturned() {
        Order order1 = new Order(1, null, 0, null);
        Order order2 = new Order(2, null, 0, null);
        Map<Integer, Order> orders = new HashMap<>();
        orders.put(1, order1);
        orders.put(2, order2);
        orderRepository.setOrders(orders);
        Map<Integer, Order> expectedOrders = orders;
        Map<Integer, Order> actualOrders = orderRepository.getAllOrders();

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void whenAddOrderThenOrderMustBeAddedSuccessfully() {
        Order order = new Order(1, null, 0, null);

        Map<Integer, Order> orderMap = Mockito.mock(HashMap.class);
        orderMap.put(order.getId(), order);

        orderRepository.addOrder(order);

        assertEquals(order, orderRepository.getAllOrders().get(order.getId()));

        verify(orderMap, times(1)).put(order.getId(), order);
    }

    @Test
    public void whenPerformDeleteOrderOperationThenOrderMustBeDeleted() {
        Order order = new Order(1, null, 0, null);

        Map<Integer, Order> orderMap = Mockito.mock(HashMap.class);
        orderMap.put(order.getId(), order);
        orderRepository = Mockito.mock(orderRepository.getClass());

        orderRepository.deleteOrder(order.getId());

        assertNull(orderMap.get(order.getId()));
        assertNull(orderRepository.getOrderById(order.getId()));
        verify(orderMap, times(1)).put(order.getId(), order);
        verify(orderRepository, times(1)).deleteOrder(order.getId());
    }

    @Test
    public void whenPerformUpdateOrderOperationThenOrderMustBeUpdated() {
        Order oldOrder = new Order(1, null, 10, null);
        Order newOrder = new Order(2, null, 54, null);

        Map<Integer, Order> orderMap = new HashMap<>();

        orderMap.put(oldOrder.getId(), oldOrder);

        orderRepository.setOrders(orderMap);
        orderRepository.updateOrder(1, newOrder);

        Order expextedOrder = newOrder;
        Order actual = orderMap.get(1);
        assertEquals(expextedOrder, actual);



    }


}
