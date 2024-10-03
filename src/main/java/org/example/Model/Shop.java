package org.example.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Shop")
public class Shop {

    @Id
    private String id;


    @Column(columnDefinition = "TEXT")
    private String shopName;

    @Column(columnDefinition = "TEXT")
    private String shopDescription;

    @OneToMany(mappedBy = "shop")
    private Set<Category> categories;

    public Shop(String id, String name, String description, Set<Category> categories) {
        this.id = id;
        this.shopName = name;
        this.shopDescription = description;
        this.categories = categories;
    }
}
