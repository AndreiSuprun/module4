package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.exception.ProjectException;

import java.util.List;

/**
 * Service class responsible for processing gift certificate-related operations
 *
 * @author Andrei Suprun
 */
public interface GiftCertificatesService {

    /**
     * Adds gift certificate to repository according to provided dto object.
     *
     * @param giftCertificateDTO GiftCertificateDTO object on basis of which is created new gift certificat
     *                           in repository
     * @throws ProjectException if fields in provided GiftCertificateDTO object is not valid
     * @return GiftCertificateDTO gift certificate dto of created in repository gift certificate
     */
    GiftCertificateDTO add(GiftCertificateDTO giftCertificateDTO);

    /**
     * Updates gift certificate according to provided dto object.
     *
     * @param giftCertificateDTO GiftCertificateDTO object according to which is necessary to update gift certificate
     *                              in repository
     * @param id id of updated gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     * id is not present in repository
     * @return GiftCertificateDTO gift certificate dto of updated gift certificate in repository
     */
    GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO, Long id);

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ProjectException if gift certificate with provided id is not present in repository
     */
    void delete(Long id);

    /**
     * Returns GiftCertificate object for gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to find
     * @throws ProjectException if gift certificate with provided id is not present in repository
     * @return GiftCertificateDTO object og gift certificate with provided id in repository
     */
    GiftCertificateDTO find(Long id);

    /**
     * Retrieves gift certificates from repository according to provided query.
     *
     * @param queryDTO QueryDTO object for building search query
     * @throws ProjectException if provided query is not valid or gift certificates according to provided query
     * are not present in repository
     * @return List<GiftCertificates> list of gift certificates from repository according to provided query
     */
    List<GiftCertificateDTO> findByQuery(QueryDTO queryDTO);

    /**
     * Returns all GiftCertificateDTO objects of gift certificates from repository.
     *
     * @return list of GiftCertificatesDTO objects of retrieved gift certificates
     */
    List<GiftCertificateDTO> findAll();

    /**
     * Updates gift certificate fields according to provided dto object.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object which consist fields to update
     * @param id id of updated gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     * id is not present in repository
     * @return GiftCertificateDTO gift certificate dto of updated certificate
     */
    GiftCertificateDTO patch(GiftCertificateDTO updatedCertificateDTO, Long id);
}
