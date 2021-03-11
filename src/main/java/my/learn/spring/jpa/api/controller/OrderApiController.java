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
import my.learn.spring.jpa.api.dto.OrderFlatDto;
import my.learn.spring.jpa.api.dto.OrderQueryDto;
import my.learn.spring.jpa.api.repository.OrderQueryRepository;
import my.learn.spring.jpa.api.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderApiController {

  private final OrdersService ordersService;
  private final OrderQueryRepository orderQueryRepository;

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

  // good
  // 컬렉션까지 페치조인을 할 때는 중복되는 row 을 줄여주기 위해 distinct 를 쿼리에 추가한다
  // 중복 row 를 삭제하긴 하지만, 디비에서 중복을 포함하여 모두 가져온 후에 애플리케이션 안에서 제거하는 것이므로 payload 자체가 줄어드는건 아님에 유의
  // 또한 컬렉션이 포함된 페치조인은 페이징 기능을 사용 할 수 없음
  @GetMapping("/api/v3/orders")
  public List<OrdersDto> ordersV3() {
    List<Orders> findOrders = ordersService.findAllWithOrderItem2();

    return findOrders.stream()
        .map(OrdersDto::new)
        .collect(Collectors.toList());
  }

  // good
  // 하이버네이트의 default_batch_fetch_size 혹은 @BatchSize 을 사용하여 컬렉션 조인을 in 구문으로 해결 할 수 있다
  // 하이버네이트 배치사이즈 옵션 사용시 컬렉션 연관관계를 포함해서 페이징 쿼리가 가능하다
  @GetMapping("/api/v3.1/orders")
  public List<OrdersDto> ordersV3paging(@RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<Orders> findOrders = ordersService.findAllPageable(offset, limit);

    return findOrders.stream()
        .map(OrdersDto::new)
        .collect(Collectors.toList());
  }

  // bad
  // 프로젝션 객체를 생성하는 과정에서 n+1 문제 발생
  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderProjecting();
  }

  // good
  // in 조건을 사용하여 n+1 문제 해소
  // 하지만 페이징이 불가능
  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findOrderProjectingV2();
  }

  @GetMapping("/api/v6/orders")
  public List<OrderFlatDto> ordersV6() {
    return orderQueryRepository.findAllByDtoFlat();
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
