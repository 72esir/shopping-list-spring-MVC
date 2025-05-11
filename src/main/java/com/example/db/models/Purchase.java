package com.example.db.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NaturalId
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "status", nullable = false)
    private Status status;

    protected Purchase() {}

    public Purchase(String title, int quantity){
        this.quantity = quantity;
        this.title = title;
        status = Status.NOT_BOUGHT;
    }

    public void setBoughtStatus(){
        status = Status.BOUGHT;
    }
}
