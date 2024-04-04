package com.daddys.coffee.entities;

import jakarta.persistence.*;

@Entity
public class Product {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private double price;

    @SuppressWarnings("unused") //needed by Hibernate
    public Product() {
        this("");
    }

    public Product(String name) {
        this(0L, name, 0.0);
    }

    public Product(long id, String name, double currentPrice) {
        this.id = id;
        this.name = name;
        this.price = currentPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
