package com.project.passwordmanager.service;

import com.google.common.primitives.Bytes;
import com.project.passwordmanager.dto.*;
import com.project.passwordmanager.entity.User;
import com.project.passwordmanager.entity.VaultEntry;
import com.project.passwordmanager.helper.PasswordHelper;
import com.project.passwordmanager.helper.PasswordManagerHelper;
import com.project.passwordmanager.repository.UserRepository;
import com.project.passwordmanager.repository.VaultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PasswordStoreService {

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretKeyFactory secretKeyFactory;

    public GeneratePasswordResponse generatePassword() {
        return new GeneratePasswordResponse(PasswordHelper.generateRandomStrongPassword());
    }

    public void savePassword(PasswordStoreRequest request) {
        VaultEntry vaultEntry = new VaultEntry()
                .setUser(new User(request.getUserId()))
                .setUsername(request.getUsername())
                .setWebsite(request.getWebsite());

        byte[] salt = PasswordHelper.generateRandomBytes();
        byte[] iv = PasswordHelper.generateRandomBytes();

        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) {
            throw new ValidationException("User data is not present");
        }
        PasswordManagerHelper.validateLoginPassword(secretKeyFactory, request.getLoginPassword(), user.get().getHashedPassword());
        String masterKey = getDecryptedPassword(user.get().getMasterKey(), request.getLoginPassword());

        byte[] encryptedPassword = Bytes.concat(salt, iv, getEncryptedPassword(masterKey, request.getPassword(), salt, iv));
        String encodedPassword = Base64.getEncoder().encodeToString(encryptedPassword);

        vaultEntry.setEncryptedPassword(encodedPassword);
        vaultRepository.save(vaultEntry);
    }

    public PasswordStoreResponse getPassword(GetPasswordRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        VaultEntry vaultEntry = vaultRepository.getVaultEntryByUserIdAndWebsite(
                request.getUserId(),
                request.getWebsite()
        );
        if(user.isEmpty()) {
            throw new ValidationException("Current user doesnt exist in database");
        }

        PasswordManagerHelper.validateLoginPassword(secretKeyFactory, request.getLoginPassword(), user.get().getHashedPassword());

        String masterKey = getDecryptedPassword(user.get().getHashedPassword(), request.getLoginPassword());

        String decryptedPassword = getDecryptedPassword(vaultEntry.getEncryptedPassword(), masterKey);

        return new PasswordStoreResponse(request.getWebsite(), vaultEntry.getUsername(), decryptedPassword);

    }

    public List<PasswordStoreResponse> getAllPassword(GetAllPasswordRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        List<VaultEntry> vaultEntries = vaultRepository.getVaultEntriesByUserId(request.getUserId());
        if(user.isEmpty()) {
            throw new ValidationException("Current user doesnt exist in database");
        }

        PasswordManagerHelper.validateLoginPassword(secretKeyFactory, request.getLoginPassword(), user.get().getHashedPassword());

        String masterKey = getDecryptedPassword(user.get().getMasterKey(), request.getLoginPassword());

        List<PasswordStoreResponse> passwordList = new ArrayList<>();
        vaultEntries.forEach(data -> {
            String decryptedPassword = getDecryptedPassword(data.getEncryptedPassword(), masterKey);
            passwordList.add(new PasswordStoreResponse(data.getWebsite(), data.getUsername(), decryptedPassword));
        });

        return passwordList;
    }

    //==================================PRIVATE METHOD=================================================

    private String getDecryptedPassword(String encodedPassword, String masterKey) {
        byte[] decodedPassword = Base64.getDecoder().decode(encodedPassword);

        byte[] salt = Arrays.copyOfRange(decodedPassword, 0, 16);
        byte[] iv = Arrays.copyOfRange(decodedPassword, 16, 32);
        byte[] encryptedPassword = Arrays.copyOfRange(decodedPassword, 32, decodedPassword.length);

        return PasswordManagerHelper.decryptPassword(secretKeyFactory, encryptedPassword, salt, iv, masterKey);
    }

    private byte[] getEncryptedPassword(String masterKey, String password, byte[] salt, byte[] iv) {
        return PasswordManagerHelper.encryptPassword(secretKeyFactory, password, salt, iv, masterKey);
    }

    public PreviewVaultResponse previewVault(String username) {
        List<PreviewVaultResponse.Entry> entries = vaultRepository.getVaultEntriesByUserId(username)
                .stream()
                .map(entry -> new PreviewVaultResponse.Entry(entry.getUsername(), entry.getWebsite()))
                .collect(Collectors.toList());

        return new PreviewVaultResponse(entries);
    }
}
