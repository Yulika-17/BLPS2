package com.nullnumber1.lab1.repository;

import com.nullnumber1.lab1.model.Inn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InnRepository extends JpaRepository<Inn, Long> {
    boolean existsById(Long inn);
}
