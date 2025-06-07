package com.goga74.platform.DB.entity.fora;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
}
