package my.learn.spring.jpa.api.repository.querydsl.interfaces;

import java.util.List;
import my.learn.spring.jpa.api.domain.Orders;

public interface OrdersCustom {

  List<Orders> findAllFetchQueryDsl();
}
