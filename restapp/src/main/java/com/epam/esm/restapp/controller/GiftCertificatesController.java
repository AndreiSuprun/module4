package com.epam.esm.restapp.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests for gift certificates.
 *
 * @author Andrei Suprun
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;
    private final EntityResponseBuilder responseBuilder;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService, EntityResponseBuilder responseBuilder) {
        this.giftCertificatesService = giftCertificateService;
        this.responseBuilder = responseBuilder;
    }

    /**
     * Retrieves gift certificates from repository according to provided request parameters.
     *
     * @param pageable (optional) request parameter for page number
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<GiftCertificateDTO>> object of gift certificates for returned page from repository
     * @throws ValidationException if provided query is not valid or gift certificates according to provided query
     *                          are not present in repository
     */
    @GetMapping
    public PagedModel<EntityModel<GiftCertificateDTO>> findByQuery(Pageable pageable,
                                                                       @RequestParam(value = "search", required = false) String searchParameters,
                                                                   @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        Page<GiftCertificateDTO> certificateDTOs = giftCertificatesService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                pageable);
        return responseBuilder.getCertificatePagedModel(certificateDTOs, pageable, searchParameters, orderParameters);
    }

    /**
     * Adds gift certificate to repository according to provided dto object.
     *
     * @param newCertificate GiftCertificateDTO object on basis of which is created new gift certificate
     *                       in repository
     * @return EntityModel<GiftCertificateDTO> object for gift certificate dto of created in repository gift certificate
     * @throws ValidationException if fields in provided GiftCertificateDTO object is not valid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificateDTO> addGiftCertificate(@RequestBody GiftCertificateDTO newCertificate) {
        GiftCertificateDTO giftCertificateDTO = giftCertificatesService.add(newCertificate);
        return responseBuilder.getCertificateEntityModel(giftCertificateDTO);
    }

    /**
     * Returns GiftCertificateDTO object for gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to find
     * @return EntityModel<GiftCertificateDTO> object of gift certificate with provided id in repository
     * @throws ValidationException if gift certificate with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificateDTO> find(@PathVariable Long id) {
        GiftCertificateDTO certificateDTO = giftCertificatesService.find(id);
        return responseBuilder.getCertificateEntityModel(certificateDTO);
    }

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ValidationException if gift certificate with provided id is not present in repository
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        giftCertificatesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates gift certificate according to request body.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object according to which is necessary to update gift certificate
     *                              in repository
     * @param id                    id of updated gift certificate
     * @return EntityModel<GiftCertificateDTO> object of gift certificate dto of updated gift certificate in repository
     * @throws ValidationException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     *                          id is not present in repository
     */
    @PutMapping("/{id}")
    public EntityModel<GiftCertificateDTO> update(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id) {
        GiftCertificateDTO giftCertificateDTO = giftCertificatesService.update(updatedCertificateDTO, id);
        return responseBuilder.getCertificateEntityModel(giftCertificateDTO);
    }

    /**
     * Updates gift certificate fields according to provided in request body fields.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object which consist fields to update
     * @param id                    id of updated gift certificate
     * @return EntityModel<GiftCertificateDTO> object of gift certificate dto of updated certificate
     * @throws ValidationException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     *                          id is not present in repository
     */
    @PatchMapping("/{id}")
    public EntityModel<GiftCertificateDTO> patch(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id) {
        GiftCertificateDTO giftCertificateDTO = giftCertificatesService.patch(updatedCertificateDTO, id);
        return responseBuilder.getCertificateEntityModel(giftCertificateDTO);
    }
}
