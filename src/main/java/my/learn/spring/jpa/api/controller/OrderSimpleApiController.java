package my.learn.spring.jpa.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.dto.SimpleOrderDto;
import my.learn.spring.jpa.api.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderSimpleApiController {

  private final OrdersService ordersService;

  // bad
  // 엔티티를 외부로 노출하지 말 것
  @GetMapping("/api/v1/simple-orders")
  public List<Orders> ordersV1() {
    return ordersService.findAll();
  }

  // bad
  // n+1 문제 발생
  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {
    return ordersService.findAll()
        .stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  // good
  // 페치조인을 사용하여 n+1 문제 해결
  // 프로젝션 보다는 읽어오는 데이터는 많지만, 다른 api 에서 공용으 사용할 가능성이 높다(재사용성이 좋다)
  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> ordersV3() {
    List<Orders> allFetch = ordersService.findAllFetch();
    return allFetch.stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  // good
  // 프로젝션을 사용하여 통신하는 데이터양을 줄여서 최적화
  // 하지만 엔티티가 아니라 일부 데이터들만 가져왔으므로 다른 api 에서 사용하기 힘들수도 있다(재사용성이 떨어짐)
  @GetMapping("/api/v4/simple-orders")
  public List<SimpleOrderDto> ordersV4() {
    return ordersService.findOrderDto();
  }

  @GetMapping("/api/v5/simple-orders")
  public List<SimpleOrderDto> ordersV5() {
    return ordersService.findAllFetched()
        .stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

}
