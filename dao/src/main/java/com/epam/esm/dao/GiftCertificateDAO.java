package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * DAO interface responsible for additional to CRUD handling operations for gift certificate entities
 *
 * @author Andrei Suprun
 */
public interface GiftCertificateDAO extends GenericDAO<GiftCertificate> {

    List<Order> getOrdersForCertificates(Long certificateId);
}
