package com.aml.sanctionsscreeningservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SanctionedPerson {
    @Id
    @GeneratedValue
    private String userId;
    private String fullName;
}
