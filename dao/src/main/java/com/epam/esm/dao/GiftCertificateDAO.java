package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface responsible for additional to CRUD handling operations for gift certificate entities
 *
 * @author Andrei Suprun
 */
public interface GiftCertificateDAO extends GenericDao<GiftCertificate> {

    /**
     * Retrieves gift certificates from repository according to provided query.
     *
     * @param query object for building search query
     * @return List<GiftCertificate> list of gift certificates from repository according to provided query
     */
    List<GiftCertificate> findByQuery(Query query);

    /**
     * Retrieves from repository tags for provided gift certificate
     *
     * @param giftCertificate gift certificate for which is necessary to find tags
     * @return List<Tag> list of tags from repository for provided gift certificate
     */
    List<Tag> getTags(GiftCertificate giftCertificate);

    /**
     * Adds to repository provided tag for provided gift certificate
     *
     * @param giftCertificate gift certificate for which is necessary to add tag
     * @param tag tag to add to provided gift certificate
     */
    void addTag(GiftCertificate giftCertificate, Tag tag);

    /**
     * Removes from repository all tags for gift certificate with provided id
     *
     * @param id id of gift certificate for which is necessary to remove tags
     */
    void clearTags(Long id);

}
