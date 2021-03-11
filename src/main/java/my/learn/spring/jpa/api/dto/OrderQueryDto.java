package my.learn.spring.jpa.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import my.learn.spring.jpa.api.domain.OrderStatus;
import my.learn.spring.jpa.api.domain.commons.Address;

@EqualsAndHashCode(of = "orderId")
@Data
public class OrderQueryDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;
  private List<OrderItemQueryDto> orderItemList;

  public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }

  public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
      OrderStatus orderStatus, Address address,
      List<OrderItemQueryDto> orderItemList) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.orderItemList = orderItemList;
  }
}
