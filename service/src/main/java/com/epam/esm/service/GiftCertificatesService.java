package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificatesService {

    GiftCertificate add(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate, Long id);

    void delete(Long id);

    GiftCertificate find(Long id);

    List<GiftCertificate> findByQuery(Query query);

    List<GiftCertificate> findAll();
}