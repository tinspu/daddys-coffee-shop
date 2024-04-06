package com.daddys.coffee.entities;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

public class OrderTest {
    @Test
    public void getTotalAmount() {
        OrderItem orderItem = new OrderItem(1L, BigDecimal.valueOf(20.5), null, 2, null);
        OrderItem orderItem2 = new OrderItem(2L, BigDecimal.valueOf(30.5), null, 1, null);
        Order order = new Order(1L, LocalDateTime.now(), "buyer@gamil.com",
                new HashSet<>(Arrays.asList(orderItem, orderItem2)));

        Assert.assertEquals(order.getTotalAmount(),BigDecimal.valueOf(71.5));
    }
}
