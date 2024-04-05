package com.daddys.coffee.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.util.Objects;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class OrderItem {
    private static final int UNSET_QUANTITY = -1;

    @Id
    @SequenceGenerator(name= "ORDER_ITEM_SEQUENCE", sequenceName = "ORDER_ITEM_SEQUENCE_ID", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.AUTO, generator="ORDER_ITEM_SEQUENCE")
    private final long id;

    @Column
    private final BigDecimal price;

    @Getter
    @ManyToOne
    private final Product product;

    @Getter
    @Column(nullable = false)
    private final int quantity;

    @Getter
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private final Order parentOrder;

    public OrderItem(long id, BigDecimal price, Product product, int quantity, Order parentOrder) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.quantity = quantity;
        this.parentOrder = parentOrder;
    }

    public OrderItem() {
        this(0L, BigDecimal.valueOf(0), new Product(), UNSET_QUANTITY, new Order());
    }

    public OrderItem( BigDecimal price, Product product, int quantity, Order parentOrder) {
        this(0L, price, product, quantity, parentOrder);
    }

    BigDecimal getAmount() {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                price.equals(orderItem.price) &&
                quantity == orderItem.quantity &&
                product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
