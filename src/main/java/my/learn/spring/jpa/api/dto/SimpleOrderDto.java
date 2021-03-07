package my.learn.spring.jpa.api.dto;

import java.time.LocalDateTime;
import lombok.Data;
import my.learn.spring.jpa.api.domain.OrderStatus;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.domain.commons.Address;

@Data
public class SimpleOrderDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  public SimpleOrderDto(Orders orders) {
    this.orderId = orders.getId();
    this.name = orders.getMember().getName(); // 지연로딩 발생 가능성
    this.orderDate = orders.getOrderDate();
    this.orderStatus = orders.getStatus();
    this.address = orders.getDelivery().getAddress(); // 지연로딩 발생 가능성
  }

  public SimpleOrderDto(Long orderId, String name, LocalDateTime orderDate,
      OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }

}
