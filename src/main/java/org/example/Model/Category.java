package org.example.Model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="Categories")
public class Category {

    @Id
    private String id;

    @Column(columnDefinition="TEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name="shop_id", nullable=false)
    private Shop shop;

    public Category(String id, String name, Shop shop) {
        this.id = id;
        this.name = name;
        this.shop = shop;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
