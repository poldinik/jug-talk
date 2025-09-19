package com.nexi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public MyEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MyEntity setName(String name) {
        this.name = name;
        return this;
    }
}
