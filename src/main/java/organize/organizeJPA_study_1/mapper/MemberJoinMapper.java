package organize.organizeJPA_study_1.mapper;

import org.springframework.stereotype.Component;
import organize.organizeJPA_study_1.domain.Member;
import organize.organizeJPA_study_1.domain.embed.Address;
import organize.organizeJPA_study_1.dto.MemberJoinDto;
import organize.organizeJPA_study_1.dto.MemberResponse;

import java.util.List;

@Component
public class MemberJoinMapper implements Mapper<MemberJoinDto, Member, MemberResponse> {


    @Override
    public Member toEntity(MemberJoinDto dto) {
        return Member.of(dto.getName(), new Address(dto.getCity(), dto.getStreet(), dto.getZipcode()));
    }

    @Override
    public MemberJoinDto toDto(Member entity) {
        return MemberJoinDto.builder().name(entity.getName()).address(entity.getAddress()).build();
    }

    @Override
    public List<MemberResponse> toResponseListDto(List<Member> entities) {
        return List.of();
    }

}