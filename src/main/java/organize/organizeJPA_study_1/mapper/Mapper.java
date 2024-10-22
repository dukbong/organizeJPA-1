package organize.organizeJPA_study_1.mapper;

import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;

public interface Mapper<D, E, L> {
    E toEntity(D dto);
    D toDto(E entity);
    List<L> toResponseListDto(List<E> entities);
}
