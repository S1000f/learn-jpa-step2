package my.learn.spring.jpa.api.service;

import static my.learn.spring.jpa.api.domain.QDelivery.delivery;
import static my.learn.spring.jpa.api.domain.QMember.member;
import static my.learn.spring.jpa.api.domain.QOrders.orders;
import static my.learn.spring.jpa.api.domain.QOrderItem.orderItem;
import static my.learn.spring.jpa.api.domain.QItem.item;

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
  public List<Orders> findAllWithOrderItem() {
    JPAQuery<Orders> query = new JPAQuery<>(em);

    return query.from(orders)
        .join(orders.member, member).fetchJoin()
        .join(orders.delivery, delivery).fetchJoin()
        .join(orders.orderItems, orderItem).fetchJoin()
        .join(orderItem.item, item).fetchJoin()
        .distinct()
        .fetch();
  }

  @Transactional(readOnly = true)
  public List<Orders> findAllWithOrderItem2() {
    // JPQL 의 distinct 는 데이터베이스의 distinct 추가와 함께 row 가 같은 root 엔티티를 가지고있으면 중복을 제거해준다
    return em.createQuery("select distinct o from Orders o"
        + " join fetch o.member m"
        + " join fetch o.delivery d"
        + " join fetch o.orderItems oi"
        + " join fetch oi.item i", Orders.class)
        .getResultList();
  }

  @Transactional(readOnly = true)
  public List<Orders> findAllPageable(int offset, int limit) {
    return em.createQuery("select o from Orders o"
        + " join fetch o.member m"
        + " join fetch o.delivery d", Orders.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

}
