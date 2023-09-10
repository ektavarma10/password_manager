package com.project.passwordmanager.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "VAULT")
public class VaultEntry {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;

    private String website;

    private String username;

    private String encryptedPassword;
}