package my.learn.spring.jpa.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.learn.spring.jpa.api.domain.OrderItem;
import my.learn.spring.jpa.api.domain.OrderStatus;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.domain.commons.Address;
import my.learn.spring.jpa.api.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderApiController {

  private final OrdersService ordersService;

  // bad
  @GetMapping("/api/v1/orders")
  public List<Orders> ordersV1() {
    return ordersService.findAllWithOrderItem();
  }

  // bad
  // orders 와 member, delivery 사이에는 페치조인이 되었지만 orderItem 의 컬렉션은 페치조인이 되어있지 않음
  @GetMapping("/api/v2/orders")
  public List<OrdersDto> ordersV2() {
    List<Orders> findOrders = ordersService.findAllFetch();

    return findOrders.stream()
        .map(OrdersDto::new)
        .collect(Collectors.toList());
  }

  @GetMapping("/api/v3/orders")
  public List<OrdersDto> ordersV3() {
    List<Orders> findOrders = ordersService.findAllWithOrderItem2();

    return findOrders.stream()
        .map(OrdersDto::new)
        .collect(Collectors.toList());
  }

  @Getter
  static class OrdersDto {

    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;
    private final List<OrderItemDto> orderItems;

    public OrdersDto(Orders orders) {
      this.orderId = orders.getId();
      this.name = orders.getMember().getName();
      this.orderDate = orders.getOrderDate();
      this.orderStatus = orders.getStatus();
      this.address = orders.getDelivery().getAddress();
      this.orderItems = orders.getOrderItems()
          .stream()
          .map(OrderItemDto::new)
          .collect(Collectors.toList());
    }
  }

  @Getter
  static class OrderItemDto {

    private final String itemName;
    private final int orderPrice;
    private final int count;

    public OrderItemDto(OrderItem orderItem) {
      this.itemName = orderItem.getItem().getName();
      this.orderPrice = orderItem.getOrderPrice();
      this.count = orderItem.getCount();
    }
  }

}
