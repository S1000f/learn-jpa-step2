package my.learn.spring.jpa.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import my.learn.spring.jpa.api.domain.commons.Address;

@Setter
@Getter
@Entity
public class Member {

  @Column(name = "member_id")
  @GeneratedValue
  @Id
  private Long id;

  private String name;

  @Embedded
  private Address address;

  @JsonIgnore
  @OneToMany(mappedBy = "member")
  private List<Orders> orders = new ArrayList<>();
}
