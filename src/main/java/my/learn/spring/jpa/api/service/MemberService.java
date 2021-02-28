package my.learn.spring.jpa.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Member;
import my.learn.spring.jpa.api.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  public List<Member> findMembers() {
    Iterable<Member> all = memberRepository.findAll();
    return (List<Member>) all;
  }

  public Long join(Member member) {
    Member save = memberRepository.save(member);
    return save.getId();
  }

  @Transactional
  public void update(Long id, String name) {
    Optional<Member> find = memberRepository.findById(id);

    if (find.isPresent()) {
      Member findMember = find.get();
      findMember.setName(name);
    }

  }

  public Member findOne(Long id) {
    Optional<Member> find = memberRepository.findById(id);
    return find.orElse(new Member());
  }
}
