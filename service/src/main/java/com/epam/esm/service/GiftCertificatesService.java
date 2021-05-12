package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;

import java.util.List;

/**
 * Service class responsible for processing gift certificate-related operations
 *
 * @author Andrei Suprun
 */
public interface GiftCertificatesService extends GenericService<GiftCertificateDTO>{

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
