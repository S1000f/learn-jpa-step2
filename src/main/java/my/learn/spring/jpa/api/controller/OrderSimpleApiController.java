package my.learn.spring.jpa.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  // bad
  // 엔티티를 외부로 노출하지 말 것
  @GetMapping("/api/v1/simple-orders")
  public List<Orders> ordersV1() {
    Iterable<Orders> findAll = orderRepository.findAll();
    return (List<Orders>) findAll;
  }

}
