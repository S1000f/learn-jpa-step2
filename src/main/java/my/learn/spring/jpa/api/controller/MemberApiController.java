package my.learn.spring.jpa.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Member;
import my.learn.spring.jpa.api.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

  private final MemberService memberService;

  // bad
  // api 요청 값들을 엔티티에 바로 매핑하여 사용
  @PostMapping("/api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  // ok
  // api 전용 dto 객체를 사용하여 요청값을 매핑
  // 절대로 엔티티를 외부로부터 받거나 노출해서는 안된다
  // 엔티티와 api 가 강하게 결합되어, 엔티티가 변경되면 api 스펙이 바껴야만 하는 사이드이펙트 발생 가능성이 매우 높음
  // 또한, 엔티티 객체 하나로 다양한 유형의 요청을 처리하기 힘들고 반대로 api 를 변경하거나 확장할때도 엔티티 때문에 제약이 발생함
  @PostMapping("/api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest memberRequest) {
    // 요청 값은 dto 에 저장하고 dto 에서 값을 꺼내 임시 엔티티를 새로 생성한 후 영속화한다
    // dto 객체는 api 와 엔티티(영속 컨텍스트)간을 이어주는 중간다리 역할을 한다
    Member member = new Member();
    member.setName(memberRequest.getName());

    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  // api Dto
  @Data
  static class CreateMemberRequest {

    @NotEmpty
    private String name;
  }

  @Data
  static class CreateMemberResponse {

    private Long id;

    public CreateMemberResponse(Long id) {
      this.id = id;
    }
  }

}
