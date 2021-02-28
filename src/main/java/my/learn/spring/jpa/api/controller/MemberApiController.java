package my.learn.spring.jpa.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.learn.spring.jpa.api.domain.Member;
import my.learn.spring.jpa.api.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @PutMapping("/api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
      @RequestBody @Valid UpdateMemberRequest memberRequest) {
    // 김영한 스타일: 업데이트 메소드는 업데이트 기능 한가지만 수행하도록 하기위해 엔티티를 반환하지 않고 void 로 종료
    // 업데이트된 결과를 확인하기 위해 별도의 조회 쿼리를 사용
    memberService.update(id, memberRequest.getName());
    Member findMember = memberService.findOne(id);

    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }

  // bad
  // 엔티티 객체를 바로 응답한다
  @GetMapping("/api/v1/members")
  public List<Member> membersV1() {
    return memberService.findMembers();
  }

  // ok
  // 불필요한 엔티티의 정보가 노출되면 안됨
  // 엔티티에 프리젠테이션 계층을 위한 로직이 추가되면 -단일책임 원칙 위배
  // 엔티티 변경 시 api 스펙에 사이드이펙트가 발생하고 확장이 어려워짐
  // 요청 값을 dto 로 감싸는것처럼 응답값도 dto 객체를 생성하여 반환해야 함
  @GetMapping("/api/v2/members")
  public Result<?> membersV2() {
    List<Member> findMembers = memberService.findMembers();
    List<MemberDto> converts = findMembers.stream()
        .map(m -> new MemberDto(m.getName()))
        .collect(Collectors.toList());

    return new Result<>(converts);
  }

  // api Dto
  @Data
  static class CreateMemberRequest {
    // api 요청값의 검증은 엔티티가 아니라 dto 에서 수행한다
    // 예를들어 디비에서는 nullable 이지만 특정 api 는 널 허용이 안되는 경우, 엔티티의 필드에 널 검증 어노테이션을 붙이는건 좋지 않음
    @NotEmpty
    private String name;
  }

  @Data
  static class UpdateMemberRequest {
    private String name;
  }

  @Data
  @AllArgsConstructor
  static class CreateMemberResponse {
    private Long id;
  }

  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {
    private T data;
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {
    private String name;
  }

}
