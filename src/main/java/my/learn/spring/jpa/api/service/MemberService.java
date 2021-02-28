package my.learn.spring.jpa.api.service;

import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Member;
import my.learn.spring.jpa.api.repository.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  public Long join(Member member) {
    Member save = memberRepository.save(member);
    return save.getId();
  }
}
