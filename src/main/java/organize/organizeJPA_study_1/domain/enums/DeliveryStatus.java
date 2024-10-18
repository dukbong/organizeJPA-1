package organize.organizeJPA_study_1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    READY("배송중"),
    COMP("배송완료");

    private String status;

}
