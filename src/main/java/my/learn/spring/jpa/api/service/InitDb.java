package my.learn.spring.jpa.api.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Book;
import my.learn.spring.jpa.api.domain.Delivery;
import my.learn.spring.jpa.api.domain.Member;
import my.learn.spring.jpa.api.domain.OrderItem;
import my.learn.spring.jpa.api.domain.Orders;
import my.learn.spring.jpa.api.domain.commons.Address;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInit();
  }

  @RequiredArgsConstructor
  @Transactional
  @Component
  static class InitService {

    private final EntityManager em;

    private Member createMember(String name, String city, String street, String zipcode) {
      Member member = new Member();
      member.setName(name);
      member.setAddress(new Address(city, street, zipcode));

      return member;
    }

    public void dbInit() {
      Member member = createMember("userA", "Daegu", "11", "13143");
      Member member2 = createMember("userB", "Seoul", "22", "22334");
      em.persist(member);
      em.persist(member2);

      Book book = new Book();
      book.setName("JPA1 BOOK");
      book.setPrice(10000);
      book.setStockQuantity(100);
      em.persist(book);

      Book book2 = new Book();
      book2.setName("JPA2 BOOK");
      book2.setPrice(20000);
      book2.setStockQuantity(100);
      em.persist(book2);

      Book book3 = new Book();
      book3.setName("spring1 book");
      book3.setPrice(20000);
      book3.setStockQuantity(200);
      em.persist(book3);

      Book book4 = new Book();
      book4.setName("spring2 book");
      book4.setPrice(40000);
      book4.setStockQuantity(300);
      em.persist(book4);

      OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
      OrderItem orderItem3 = OrderItem.createOrderItem(book3, 20000, 3);
      OrderItem orderItem4 = OrderItem.createOrderItem(book4, 40000, 4);

      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      Orders order = Orders.createOrder(member, delivery, orderItem, orderItem2);
      em.persist(order);

      Delivery delivery2 = new Delivery();
      delivery2.setAddress(member2.getAddress());
      Orders order2 = Orders.createOrder(member2, delivery2, orderItem3, orderItem4);
      em.persist(order2);

    }
  }

}
