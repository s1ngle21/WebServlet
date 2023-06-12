import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/api/order-management/orders/*")
public class OrderController extends HttpServlet {
    private OrderRepository orderRepository;

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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length == 0) {
            try (OutputStream os = resp.getOutputStream()) {
                String allOrders = this.orderRepository.getAllOrders();
                byte[] allOrdersAsBytes = allOrders.getBytes();
                os.write(allOrdersAsBytes);
                os.flush();
                resp.setStatus(200);
                resp.setContentLength(allOrdersAsBytes.length);
                resp.setContentType("text/plain");
            }
        } else {
            String id = parts[parts.length - 1];
                Order order = this.orderRepository.getOrderById(Integer.parseInt(id));
                try (OutputStream os = resp.getOutputStream()){
                    byte[] orderAsBytes = order.toString().getBytes();
                    os.write(orderAsBytes);
                    os.flush();
                    resp.setStatus(200);
                    resp.setContentLength(orderAsBytes.length);
                    resp.setContentType("text/plain");
                }
            }
        }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        LocalDate data = LocalDate.now();
        double cost = Double.parseDouble(req.getParameter("cost"));
        String[] productIds = req.getParameterValues("productIds");
        String[] productNames = req.getParameterValues("productNames");
        String[] productCosts = req.getParameterValues("productCosts");
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productIds.length; i++) {
            int prodcutId = Integer.parseInt(productIds[i]);
            String productName = productNames[i];
            double prodcutCost = Double.parseDouble(productCosts[i]);
            Product product = new Product(prodcutId, productName, prodcutCost);
            products.add(product);
        }

        Order order = new Order(id, data, cost, products);
        this.orderRepository.addOrder(order);
    }


}
