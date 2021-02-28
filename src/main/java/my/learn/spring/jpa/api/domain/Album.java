package my.learn.spring.jpa.api.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@DiscriminatorValue("A")
@Entity
public class Album extends Item {

  private String artist;
  private String etc;

}
