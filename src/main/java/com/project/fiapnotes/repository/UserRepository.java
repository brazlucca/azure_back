package com.project.fiapnotes.repository;

import com.project.fiapnotes.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findFirstByUsername(String username);
}
