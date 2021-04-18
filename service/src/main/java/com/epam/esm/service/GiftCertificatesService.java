package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;

import java.util.List;

public interface GiftCertificatesService {

    GiftCertificateDTO add(GiftCertificateDTO giftCertificate);

    GiftCertificateDTO update(GiftCertificateDTO giftCertificate, Long id);

    void delete(Long id);

    GiftCertificateDTO find(Long id);

    List<GiftCertificateDTO> findByQuery(QueryUtil query);

    List<GiftCertificateDTO> findAll();

}
