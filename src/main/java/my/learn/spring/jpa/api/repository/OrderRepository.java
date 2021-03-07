package my.learn.spring.jpa.api.repository;

import java.util.List;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.repository.querydsl.interfaces.OrdersCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long>, OrdersCustom {

  List<Orders> findAll();

}
