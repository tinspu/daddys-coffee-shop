package com.daddys.coffee.repositories;

import com.daddys.coffee.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order,Long> {

}
