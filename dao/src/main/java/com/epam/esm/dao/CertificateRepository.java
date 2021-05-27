package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<GiftCertificate, Long>, CustomCertificateRepository {

    Optional<GiftCertificate> findById(Long aLong);

    Optional<GiftCertificate> findByName(String name);


}
