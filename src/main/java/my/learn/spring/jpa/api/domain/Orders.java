package my.learn.spring.jpa.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "orders")
@Entity
public class Orders {

  @Id
  @GeneratedValue
  @Column(name = "orders_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member; //주문 회원

  @JsonIgnore
  @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보

  private LocalDateTime orderDate; //주문시간 @Enumerated(EnumType.STRING)

  private OrderStatus status; //주문상태 [ORDER, CANCEL]

  //==연관관계 메서드==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrders(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrders(this);
  }

  public static Orders createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Orders orders = new Orders();
    orders.setMember(member);
    orders.setDelivery(delivery);
    for (OrderItem orderItem : orderItems) {
      orders.addOrderItem(orderItem);
    }

    orders.setStatus(OrderStatus.ORDER);
    orders.setOrderDate(LocalDateTime.now());

    return orders;
  }

}
