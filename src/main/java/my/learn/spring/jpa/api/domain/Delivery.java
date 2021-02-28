package my.learn.spring.jpa.api.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import my.learn.spring.jpa.api.domain.commons.Address;

@Setter
@Getter
@Entity
public class Delivery {

  @Id
  @GeneratedValue
  @Column(name = "delivery_id")
  private Long id;

  @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
  private Orders orders;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]

}
