package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * DAO interface responsible for additional to CRUD handling operations for tag entities
 *
 * @author Andrei Suprun
 */
public interface TagDAO extends GenericDAO<Tag> {

    /**
     * Search most used tag for user with highest total cost of orders.
     *
     * @return Tag tag object of most used tag
     */
    Tag findMostWidelyUsedTag();

    /**
     * Retrieves list of gift certificates from repository for tag with provided id.
     *
     * @param id id of tag for which is necessary to find gift certificates
     * @return List list of gift certificates for tag with provided id
     */
    List<GiftCertificate> getCertificatesForTag(Long tagId);
}
