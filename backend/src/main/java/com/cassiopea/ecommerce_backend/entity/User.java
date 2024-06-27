package com.cassiopea.ecommerce_backend.entity;


import com.cassiopea.ecommerce_backend.enums.UserRole;
import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue (
            strategy = GenerationType.IDENTITY
    )
    private Long id ;

    private UserRole role ;

    // credentials :
    private String email ;
    private String password ;

    @Lob
    @Column ( columnDefinition = "longblob")
    private byte [] profileImage ;
}
