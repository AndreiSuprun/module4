package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface GiftCertificateDAO extends GenericDao<GiftCertificate> {

    List<GiftCertificate> findByQuery(String sqlQuery, Object[] params);

    List<Tag> getTags(GiftCertificate giftCertificate);

    void addTag(GiftCertificate giftCertificate, Tag tag);

    void clearTags(Long id);
}
