package organize.organizeJPA_study_1.mapper;

public interface Mapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
}
