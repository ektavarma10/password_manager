package com.project.passwordmanager.repository;

import com.project.passwordmanager.entity.VaultEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaultRepository extends CrudRepository<VaultEntry, String> {
    List<VaultEntry> getVaultEntriesByUserId(String user_id);

    VaultEntry getVaultEntryByUserIdAndWebsite(String user_id, String website);
}
