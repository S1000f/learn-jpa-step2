package my.learn.spring.jpa;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpaApplication.class, args);
  }

  // bad
  // 잭슨맵퍼가 하이버네이트 엔티티 객체를 직렬화, 역직렬화를 바로 할 수 있도록 해주는 라이브러리
  // 하지만 엔티티를 외부에 바로 노출하는 건 좋지 못하므로 이 라이브러리는 조심해서 사용할 것
  @Bean
  public Hibernate5Module hibernate5Module() {
    Hibernate5Module hibernate5Module = new Hibernate5Module();
    // 페치조인이 되어있지 않은 지연로딩 연관 엔티티들을 잭슨맵퍼가 강제로 조회하여 저장하는 옵션 - 사용하지 말 것
//    hibernate5Module.configure(Feature.FORCE_LAZY_LOADING, true);
    return hibernate5Module;
  }

}
