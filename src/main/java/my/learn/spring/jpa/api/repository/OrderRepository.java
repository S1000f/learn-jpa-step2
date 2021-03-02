package my.learn.spring.jpa.api.repository;

import my.learn.spring.jpa.api.domain.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Orders, Long> {

}
