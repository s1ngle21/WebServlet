import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.Order;
import entity.Product;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/api/order-management/orders/*")
public class OrderController extends HttpServlet {
    private OrderRepository orderRepository;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //hardcode for educational purposes, just to see something on my web page

        Product product1 = new Product(1, "Apple", 10);
        Product product2 = new Product(2, "Banana", 20);


        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);


        Order order1 = new Order(1, LocalDate.now(), 31.48, products);
        Order order2 = new Order(2, LocalDate.now(), (product1.getCost() + product2.getCost()), products);


        this.orderRepository = new OrderRepository();

        this.orderRepository.addOrder(order1);
        this.orderRepository.addOrder(order2);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length == 0) {
            try (OutputStream os = resp.getOutputStream()) {
                List<Order> orderList = new ArrayList<>(this.orderRepository.getAllOrders().values());
                List<byte[]> allOrdersAsBytesList = new ArrayList<>();
                byte[] orderAsBytes = null;
                for (Order o : orderList) {
                    orderAsBytes = this.objectMapper.writeValueAsBytes(o);
                    allOrdersAsBytesList.add(orderAsBytes);
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                for (byte[] bytesOrder : allOrdersAsBytesList) {
                    byteArrayOutputStream.write(bytesOrder);
                }
                byte[] allOrdersByBytes = byteArrayOutputStream.toByteArray();
                os.write(allOrdersByBytes);
                resp.setStatus(200);
                resp.setContentLength(allOrdersByBytes.length);
                resp.setContentType("application/json");
            }
        } else {
            String id = parts[parts.length - 1];
            Order order = this.orderRepository.getOrderById(Integer.parseInt(id));
            try (OutputStream os = resp.getOutputStream()) {
                byte[] orderAsBytes = this.objectMapper.writeValueAsBytes(order);
                os.write(orderAsBytes);
                os.flush();
                resp.setStatus(200);
                resp.setContentLength(orderAsBytes.length);
                resp.setContentType("application/json");
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (InputStream is = req.getInputStream();
             OutputStream os = resp.getOutputStream()) {
            JsonNode orderJson = this.objectMapper.readTree(is);
            Order order = new Order();
            order.setId(orderJson.get("id").asInt());
            order.setDate(LocalDate.now());
            order.setCost(orderJson.get("cost").asDouble());
            List<Product> products = new ArrayList<>();

            JsonNode productsJson = orderJson.get("products");
            if (productsJson.isArray()) {
                for (JsonNode p : productsJson) {
                    Product product = new Product();
                    product.setId(p.get("id").asInt());
                    product.setName(p.get("name").asText());
                    product.setCost(p.get("cost").asDouble());
                    products.add(product);
                }
            }
            order.setProducts(products);
            this.orderRepository.addOrder(order);

            byte[] createdProduct = this.objectMapper.writeValueAsBytes(order);
            os.write(createdProduct);
            os.flush();
            resp.setContentLength(createdProduct.length);
            resp.setStatus(200);
            resp.setContentType("application/json");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length > 0) {
            int id = Integer.parseInt(parts[parts.length - 1]);
            Order existingOrder = this.orderRepository.getOrderById(id);
            if (existingOrder != null) {
                try (InputStream is = req.getInputStream();
                     OutputStream os = resp.getOutputStream()) {
                    JsonNode orderJson = this.objectMapper.readTree(is);
                    existingOrder.setCost(orderJson.get("cost").asDouble());

                    List<Product> updatedProducts = new ArrayList<>();
                    JsonNode productsJson = orderJson.get("products");
                    if (productsJson.isArray()) {
                        for (JsonNode p : productsJson) {
                            Product product = new Product();
                            product.setId(p.get("id").asInt());
                            product.setName(p.get("name").asText());
                            product.setCost(p.get("cost").asDouble());
                            updatedProducts.add(product);
                        }
                    }
                    existingOrder.setProducts(updatedProducts);
                    this.orderRepository.updateOrder(id, existingOrder);

                    byte[] updatedOrder = this.objectMapper.writeValueAsBytes(existingOrder);
                    os.write(updatedOrder);
                    os.flush();
                    resp.setContentLength(updatedOrder.length);
                    resp.setStatus(200);
                    resp.setContentType("application/json");
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length > 0) {
            int id = Integer.parseInt(parts[parts.length - 1]);
            Order existingOrder = this.orderRepository.getOrderById(id);
            if (existingOrder != null) {
                this.orderRepository.deleteOrder(id);
                resp.setStatus(200);
            }
        }
    }
}
