package my.learn.spring.jpa.api.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@DiscriminatorValue("B")
@Entity
public class Book extends Item {

  private String author;
  private String isbn;

}
