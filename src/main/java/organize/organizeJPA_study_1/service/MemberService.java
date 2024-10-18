package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Member;
import organize.organizeJPA_study_1.dto.MemberJoinDto;
import organize.organizeJPA_study_1.dto.MemberUpdateDto;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final Mapper<MemberJoinDto, Member> memberJoinMapper;

    public Long saveMember(MemberJoinDto memberDto) {
        validateDuplicateMember(memberDto);
        Member saveMember = memberRepository.save(memberJoinMapper.toEntity(memberDto));
        return saveMember.getId();
    }

    private void validateDuplicateMember(MemberJoinDto memberDto) {
        memberRepository.findByName(memberDto.getName()).ifPresent(member -> {
            throw new IllegalStateException("이미 존재하는 회원명입니다.");
        });
    }

    public Long updateMember(MemberUpdateDto memberUpdateDto) {
        if (memberUpdateDto.getId() == null) {
            throw new IllegalArgumentException("회원 ID는 필수 입니다.");
        }
        Member member = memberRepository.findById(memberUpdateDto.getId()).orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
        return member.updateFromDto(memberUpdateDto);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
        member.inActive();
    }

    // 조회 작업
}
