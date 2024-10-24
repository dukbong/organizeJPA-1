package organize.organizeJPA_study_1.dto.request;

import organize.organizeJPA_study_1.domain.Item;

public interface UpdateItemRequest {
    void updateDetails(Item item);
}
