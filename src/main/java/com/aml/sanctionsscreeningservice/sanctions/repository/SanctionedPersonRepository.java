package com.aml.sanctionsscreeningservice.sanctions.repository;

import com.aml.sanctionsscreeningservice.sanctions.model.SanctionedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SanctionedPersonRepository extends JpaRepository<SanctionedPerson, Long> {
    Optional<SanctionedPerson> findByFullName(String fullName);
}