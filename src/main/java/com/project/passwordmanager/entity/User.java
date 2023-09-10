package com.project.passwordmanager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "USER")
public class User {
    @Id
    private String id;
    private String masterKey;
    private String hashedPassword;

    public User(String userId, String masterKey, String hashedPassword) {
        this.setId(userId);
        this.setMasterKey(masterKey);
        this.setHashedPassword(hashedPassword);
    }

    public User(String userId) {
        this.setId(userId);
    }
    public User() {
    }
}
