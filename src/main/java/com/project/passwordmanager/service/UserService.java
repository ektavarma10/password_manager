package com.project.passwordmanager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.primitives.Bytes;
import com.project.passwordmanager.PasswordManagerApplication;
import com.project.passwordmanager.entity.User;
import com.project.passwordmanager.helper.PasswordHelper;
import com.project.passwordmanager.helper.PasswordManagerHelper;
import com.project.passwordmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Algorithm jwtAlgorithm;

    @Autowired
    private SecretKeyFactory secretKeyFactory;

    public User register(String username, String password) {
        Optional<User> user = userRepository.findById(username);

        if(user.isPresent()) {
            throw new ValidationException("User account is already present");
        }

        byte[] masterKey = PasswordHelper.generateRandomBytes();
        byte[] salt = PasswordHelper.generateRandomBytes();
        byte[] iv = PasswordHelper.generateRandomBytes();

        byte[] encryptedPassword = PasswordManagerHelper.encryptPassword(secretKeyFactory, new String(masterKey), salt, iv, password);

        String encodedEncryptedPassword = Base64.getEncoder().encodeToString(Bytes.concat(salt, iv, encryptedPassword));

        byte[] hashedPassword = PasswordManagerHelper.generateSecretKey(secretKeyFactory, password, new byte[16]).getEncoded();

        return userRepository.save(new User(username, encodedEncryptedPassword, new String(hashedPassword)));
    }


    public String authenticate(String username, String password) {

        validateUsernameAndPassword(username, password);

        return JWT.create()
                .withClaim("username", username)
                .withClaim("password", password)
                .withIssuedAt(Instant.now())
                .sign(jwtAlgorithm);
    }

    //==================================PRIVATE METHOD=================================================


    private void validateUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findById(username);

        if(!user.isPresent()) {
            throw new ValidationException("User account is not registered");
        }

        PasswordManagerHelper.validateLoginPassword(secretKeyFactory, password, user.get().getHashedPassword());
    }
}
