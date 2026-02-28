package com.aml.sanctionsscreeningservice.sanctions.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SanctionedPerson {
    @Id
    @GeneratedValue
    private Long userId;
    private String fullName;
}
