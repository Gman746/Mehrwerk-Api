package org.example.Model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name="Shop")
public class Shop {

    @Id
    private String id;


    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy="shop")
    private Set<Category> categories;

    public Shop(String id, String name, String description, Set<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
