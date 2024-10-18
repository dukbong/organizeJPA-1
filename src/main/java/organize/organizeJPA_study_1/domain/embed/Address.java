package organize.organizeJPA_study_1.domain.embed;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void updateCity(String newCity) {
        this.city = newCity;
    }

    public void updateStreet(String newStreet) {
        this.street = newStreet;
    }

    public void updateZipcode(String newZipcode) {
        this.zipcode = newZipcode;
    }
}
