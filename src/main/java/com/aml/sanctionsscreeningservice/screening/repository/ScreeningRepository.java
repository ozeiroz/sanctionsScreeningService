package com.aml.sanctionsscreeningservice.screening.repository;

import com.aml.sanctionsscreeningservice.screening.model.ScreeningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScreeningRepository extends JpaRepository<ScreeningResult, UUID> {
}
