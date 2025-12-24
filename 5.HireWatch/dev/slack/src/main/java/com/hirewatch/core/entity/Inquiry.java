package com.hirewatch.core.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "Inquiry")
public class Inquiry {
    private String name;
    private String title;
    private String cont;
}
