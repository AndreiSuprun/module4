package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, CustomTagRepository {
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    Tag save(Tag tag);
    void deleteById(Long id);
}
