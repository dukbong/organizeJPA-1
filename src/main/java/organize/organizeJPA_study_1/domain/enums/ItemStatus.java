package organize.organizeJPA_study_1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
    ON_SALE("판매중"),
    OFF_SALE("판매중지");

    private String status;
}
