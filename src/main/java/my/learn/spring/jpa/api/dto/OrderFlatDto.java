package my.learn.spring.jpa.api.dto;

import java.time.LocalDateTime;
import lombok.Data;
import my.learn.spring.jpa.api.domain.OrderStatus;
import my.learn.spring.jpa.api.domain.commons.Address;

@Data
public class OrderFlatDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  private String itemName;
  private int orderPrice;
  private int count;

  public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate,
      OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.itemName = itemName;
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
