package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.embed.Address;
import organize.organizeJPA_study_1.domain.enums.MemberStatus;
import organize.organizeJPA_study_1.dto.MemberUpdateDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    private Member(String name, Address address, MemberStatus memberStatus) {
        this.name = name;
        this.address = address;
        this.memberStatus = memberStatus;
    }

    //== 비즈니스 로직 ==//
    public static Member of(String name, Address address) {
        return new Member(name, address, MemberStatus.ACTIVE);
    }

    public Long updateFromDto(@NotNull MemberUpdateDto dto) {
        if(dto.getName() != null && !dto.getName().equals(this.name)) {
            this.name = dto.getName();
        }
        if(dto.getCity() != null && !dto.getCity().equals(this.address.getCity())) {
            this.address.updateCity(dto.getCity());
        }
        if(dto.getStreet() != null && !dto.getStreet().equals(this.address.getStreet())) {
            this.address.updateStreet(dto.getStreet());
        }
        if(dto.getZipcode() != null && !dto.getZipcode().equals(this.address.getZipcode())) {
            this.address.updateZipcode(dto.getZipcode());
        }
        return this.id;
    }

    public void inActive() {
        if(this.memberStatus == MemberStatus.INACTIVE) {
            throw new IllegalStateException("이미 비활성화된 회원입니다.");
        }
        this.memberStatus = MemberStatus.INACTIVE;
    }
}
