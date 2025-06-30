package com.example.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false,  unique = true)
    private Long userId ;


    private String userEmail ;

    private Double balance ;

    @CreationTimestamp
    @Column(nullable = false , updatable = false)
    private OffsetDateTime dateCreated ;

    @UpdateTimestamp
    @Column(nullable=false)
    private OffsetDateTime lastUpdated ;
}
