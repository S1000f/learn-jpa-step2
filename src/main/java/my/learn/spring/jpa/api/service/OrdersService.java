package my.learn.spring.jpa.api.service;

import static my.learn.spring.jpa.api.domain.QDelivery.delivery;
import static my.learn.spring.jpa.api.domain.QMember.member;
import static my.learn.spring.jpa.api.domain.QOrders.orders;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.dto.SimpleOrderDto;
import my.learn.spring.jpa.api.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdersService {

  @PersistenceContext
  private EntityManager em;

  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public List<Orders> findAll() {
    return orderRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Orders> findAllFetch() {
    return em.createQuery("select o from Orders o"
        + " join fetch o.member m"
        + " join fetch o.delivery d", Orders.class)
        .getResultList();
  }

  @Transactional(readOnly = true)
  public List<SimpleOrderDto> findOrderDto() {
    return em.createQuery(
        "select new my.learn.spring.jpa.api.dto.SimpleOrderDto("
            + "o.id, m.name, o.orderDate, o.status, m.address) "
            + " from Orders o"
            + " join o.member m"
            + " join o.delivery d", SimpleOrderDto.class)
        .getResultList();
  }

  @Transactional(readOnly = true)
  public List<Orders> findAllFetched() {
    JPAQuery<Orders> query = new JPAQuery<>(em);

    return query.from(orders)
        .join(orders.member, member).fetchJoin()
        .join(orders.delivery, delivery).fetchJoin()
        .fetch();
  }

}
