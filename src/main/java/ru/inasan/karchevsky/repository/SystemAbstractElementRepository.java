package ru.inasan.karchevsky.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.inasan.karchevsky.model.SystemAbstractElement;

@Repository
public interface SystemAbstractElementRepository extends CrudRepository<SystemAbstractElement, Long> {

}
