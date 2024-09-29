package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String content;

    private String type;

    private Date expiryDate;

    public ApiToken(Long id, String content, String type, Date expiryDate) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.expiryDate = expiryDate;
    }
}
