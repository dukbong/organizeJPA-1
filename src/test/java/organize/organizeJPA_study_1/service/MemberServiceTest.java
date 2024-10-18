package organize.organizeJPA_study_1.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Member;
import organize.organizeJPA_study_1.domain.enums.MemberStatus;
import organize.organizeJPA_study_1.dto.MemberJoinDto;
import organize.organizeJPA_study_1.dto.MemberUpdateDto;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    Mapper<MemberJoinDto, Member> memberMapper;

    @Test
    @DisplayName("회원 가입 성공")
    void saveTest_success() {
        // given
        MemberJoinDto memberJoinDto = new MemberJoinDto("test1", "city", "st", "zip");

        // when
        Long id = memberService.saveMember(memberJoinDto);

        // then
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("save search Exception"));
        Assertions.assertThat(id).isEqualTo(member.getId());
        Assertions.assertThat(member.getName()).isEqualTo(memberJoinDto.getName());
    }

    @Test
    @DisplayName("회원 가입 실패 : 중복 아이디")
    void saveTest_failure() {
        // given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("test1", "city", "st", "zip");
        memberService.saveMember(memberJoinDto1);
        MemberJoinDto memberJoinDto2 = new MemberJoinDto("test1", "city", "st", "zip");

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.saveMember(memberJoinDto2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원명입니다.");
    }

    @Test
    @DisplayName("회원 정보 업데이트 성공")
    void updateTest_success() {
        // given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("test1", "city", "st", "zip");
        Long saveMemberId = memberService.saveMember(memberJoinDto1);
        Member findSaveMember = memberRepository.findById(saveMemberId).orElseThrow(() -> new RuntimeException("save search Exception"));
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .id(findSaveMember.getId())
                .name("test2")
                .street("st2")
                .build();

        // when
        Long updateMemberId = memberService.updateMember(memberUpdateDto);

        // then
        Member findUpdateMember = memberRepository.findById(updateMemberId).orElseThrow(() -> new RuntimeException("update search Exception"));
        Assertions.assertThat(saveMemberId).isEqualTo(updateMemberId);

        Assertions.assertThat(findUpdateMember.getName()).isEqualTo(memberUpdateDto.getName());
        Assertions.assertThat(findUpdateMember.getAddress().getCity()).isEqualTo(memberJoinDto1.getCity());
    }

    @Test
    @DisplayName("회원 정보 업데이트 실패 : 회원 ID 필수")
    void updateTest_failure_1() {
        // given
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .name("test2")
                .street("st2")
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.updateMember(memberUpdateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 ID는 필수 입니다.");
    }

    @Test
    @DisplayName("회원 정보 업데이트 실패 : 존재하지 않는 회원")
    void updateTest_failure_2() {
        // given
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .id(2L)
                .name("test2")
                .street("st2")
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.updateMember(memberUpdateDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원 비활성화 성공")
    void deleteTest_success() {
        // given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("test1", "city", "st", "zip");
        Long saveMemberId = memberService.saveMember(memberJoinDto1);

        // when
        memberService.deleteMember(saveMemberId);

        // then
        Member member = memberRepository.findById(saveMemberId)
                .orElseThrow(() -> new RuntimeException("delete search Exception"));
        Assertions.assertThat(member.getId()).isEqualTo(saveMemberId);
        Assertions.assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.INACTIVE);
    }

    @Test
    @DisplayName("회원 비활성화 실패 : 존재하지 않는 회원")
    void deleteTest_failure_1() {
        // given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("test1", "city", "st", "zip");
        memberService.saveMember(memberJoinDto1);

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.deleteMember(100L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("회원이 존재하지 않습니다.");

    }

    @Test
    @DisplayName("회원 비활성화 실패 : 이미 비활성된 회원")
    void deleteTest_failure_2() {
        // given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("test1", "city", "st", "zip");
        Long saveMemberId = memberService.saveMember(memberJoinDto1);
        memberService.deleteMember(saveMemberId);

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.deleteMember(saveMemberId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 비활성화된 회원입니다.");

    }

}