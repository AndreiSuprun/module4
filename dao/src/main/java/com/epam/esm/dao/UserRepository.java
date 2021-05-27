package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    Page<User> findAll(Pageable pageable);

    Optional<User> findByUserName(String userName);

    User save(User user);

    Boolean existsByUserName(String userName);
}
