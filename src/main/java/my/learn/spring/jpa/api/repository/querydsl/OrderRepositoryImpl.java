package my.learn.spring.jpa.api.repository.querydsl;

import static my.learn.spring.jpa.api.domain.QOrders.orders;
import static my.learn.spring.jpa.api.domain.QMember.member;
import static my.learn.spring.jpa.api.domain.QDelivery.delivery;

import java.util.List;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.repository.querydsl.interfaces.OrdersCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrdersCustom {

  public OrderRepositoryImpl() {
    super(Orders.class);
  }

  @Override
  public List<Orders> findAllFetchQueryDsl() {
    return from(orders)
        .join(orders.member, member).fetchJoin()
        .join(orders.delivery, delivery).fetchJoin()
        .fetch();
  }
}
