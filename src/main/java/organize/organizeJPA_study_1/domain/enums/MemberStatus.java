package organize.organizeJPA_study_1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    ACTIVE("활성 회원"),
    INACTIVE("비활성 회원");

    private String status;
}
