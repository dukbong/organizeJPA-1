package organize.organizeJPA_study_1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateDto {

    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    @Builder
    public MemberUpdateDto(Long id, String name, String city, String street, String zipcode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
