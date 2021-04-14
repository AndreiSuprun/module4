package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificatesService {

    GiftCertificate add(GiftCertificate giftCertificate);

    GiftCertificate update(Map<String, String> map, long id);

    void delete(long id);

    GiftCertificate find(long id);

    List<GiftCertificate> findByQuery(Map<String, String> query);

    List<GiftCertificate> findAll();
}
