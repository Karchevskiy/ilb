package ru.inasan.karchevsky.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.inasan.karchevsky.model.StarSystemGrouped;

@Repository
public interface StarSystemGroupedRepository extends CrudRepository<StarSystemGrouped, Long> {
}
