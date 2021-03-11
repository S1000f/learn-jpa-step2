package my.learn.spring.jpa.api.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.dto.OrderFlatDto;
import my.learn.spring.jpa.api.dto.OrderItemQueryDto;
import my.learn.spring.jpa.api.dto.OrderQueryDto;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

  private final EntityManager em;

  public List<OrderQueryDto> findOrderProjecting() {
    List<OrderQueryDto> result = findOrder();

    result.forEach(o -> {
      List<OrderItemQueryDto> orderItemQueryDto = findOrderItems(o.getOrderId());
      o.setOrderItemList(orderItemQueryDto);
    });

    return result;
  }

  public List<OrderQueryDto> findOrderProjectingV2() {
    List<OrderQueryDto> result = findOrder();

    Set<Long> idxSet = getOrdersIdxes(result);
    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(idxSet);

    result.forEach(o -> o.setOrderItemList(orderItemMap.get(o.getOrderId())));

    return result;
  }

  public List<OrderFlatDto> findAllByDtoFlat() {
    return em.createQuery("select new my.learn.spring.jpa.api.dto.OrderFlatDto(o.id, m.name, o.orderDate,"
        + " o.status, d.address, i.name, oi.orderPrice, oi.count) "
        + " from Orders o"
        + " join o.member m"
        + " join o.delivery d"
        + " join o.orderItems oi"
        + " join oi.item i", OrderFlatDto.class)
        .getResultList();
  }

  private List<OrderQueryDto> findOrder() {
    return em.createQuery("select new my.learn.spring.jpa.api.dto.OrderQueryDto(o.id, m.name, o.orderDate,"
        + " o.status, d.address)"
        + " from Orders o"
        + " join o.member m"
        + " join o.delivery d", OrderQueryDto.class)
        .getResultList();
  }

  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery("select new my.learn.spring.jpa.api.dto.OrderItemQueryDto(oi.orders.id, i.name,"
        + " oi.orderPrice, oi.count)"
        + " from OrderItem oi"
        + " join oi.item i"
        + " where oi.orders.id = :orderId", OrderItemQueryDto.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(Set<Long> idxSet) {
    List<OrderItemQueryDto> orderItemDto = em
        .createQuery("select new my.learn.spring.jpa.api.dto.OrderItemQueryDto(oi.orders.id, i.name,"
            + " oi.orderPrice, oi.count)"
            + " from OrderItem oi"
            + " join oi.item i"
            + " where oi.orders.id in :orderIds", OrderItemQueryDto.class)
        .setParameter("orderIds", idxSet)
        .getResultList();

    return orderItemDto.stream()
        .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
  }

  private Set<Long> getOrdersIdxes(List<OrderQueryDto> result) {
    return result.stream()
        .map(OrderQueryDto::getOrderId)
        .collect(Collectors.toSet());
  }

}
