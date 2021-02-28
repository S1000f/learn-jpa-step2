package my.learn.spring.jpa.api.repository;

import my.learn.spring.jpa.api.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {


}
