package com.aml.sanctionsscreeningservice.repository;

import com.aml.sanctionsscreeningservice.model.SanctionedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SanctionedPersonRepository extends JpaRepository<SanctionedPerson, Long> {
    Optional<SanctionedPerson> findByUserId(String userId);
}