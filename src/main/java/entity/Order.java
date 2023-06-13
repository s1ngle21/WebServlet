package entity;

import entity.Product;

import java.time.LocalDate;
import java.util.List;


public class Order {
    private int id;
    private LocalDate date;
    private double cost;
    private List<Product> products;


    public Order() {
    }

    public Order(int id, LocalDate date, double cost, List<Product> products) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "entity.Order{" +
                "id=" + id +
                ", date=" + date +
                ", cost=" + cost +
                ", products=" + products +
                '}';
    }
}
