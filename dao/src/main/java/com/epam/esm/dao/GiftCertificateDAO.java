package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDAO extends GenericDao<GiftCertificate> {

    List<GiftCertificate> findByQuery(Map<String, String> query);
}
