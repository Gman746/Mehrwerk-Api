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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
