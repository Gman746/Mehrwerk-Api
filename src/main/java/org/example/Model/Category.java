package org.example.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Categories")
public class Category {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Category(String id, String name, Shop shop) {
        this.id = id;
        this.categoryName = name;
        this.shop = shop;
    }
}
