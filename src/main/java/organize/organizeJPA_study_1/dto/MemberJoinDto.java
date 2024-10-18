package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.embed.Address;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDto {

    @NotEmpty(message = "이름은 필수 입력 사항입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;

    @Builder
    public MemberJoinDto(String name, Address address) {
        this.name = name;
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
    }

}
