package com.epam.esm.restapp.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests for gift certificates.
 *
 * @author Andrei Suprun
 */
@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    /**
     * Retrieves gift certificates from repository according to provided request parameters.
     *
     * @param tag      (optional) request parameter for search by tag
     * @param contains (optional) request parameter for search by phrase contained in name or description of gift
     *                 certificate
     * @param order    (optional) request parameter for sorting by name or date, ascending or descending
     * @return List<GiftCertificate> list of gift certificates from repository according to provided query
     * @throws ProjectException if provided query is not valid or gift certificates according to provided query
     *                          are not present in repository
     */
    @GetMapping
    public CollectionModel<EntityModel<GiftCertificateDTO>> getByQuery(@RequestParam(required = false) String tag,
                                                                       @RequestParam(required = false) String contains,
                                                                       @RequestParam(required = false) String order) {
        QueryDTO queryDTO = new QueryDTO(tag, contains, order);
        List<EntityModel<GiftCertificateDTO>> giftCertificates = giftCertificatesService.
                findByQuery(queryDTO).stream()
                .map(certificate -> EntityModel.of(certificate,
                        linkTo(methodOn(GiftCertificatesController.class).getOne(certificate.getId())).withSelfRel(),
                        linkTo(methodOn(GiftCertificatesController.class).getByQuery(tag, contains, order)).withRel("gift_certificates")))
                .collect(Collectors.toList());

        return CollectionModel.of(giftCertificates, linkTo(methodOn(GiftCertificatesController.class).
                getByQuery(tag, contains, order)).withSelfRel());
    }

    /**
     * Adds gift certificate to repository according to provided dto object.
     *
     * @param newCertificate GiftCertificateDTO object on basis of which is created new gift certificate
     *                       in repository
     * @return GiftCertificateDTO gift certificate dto of created in repository gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO object is not valid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO addGiftCertificate(@RequestBody GiftCertificateDTO newCertificate) {
        return giftCertificatesService.add(newCertificate);
    }

    /**
     * Returns GiftCertificateDTO object for gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to find
     * @return GiftCertificateDTO object og gift certificate with provided id in repository
     * @throws ProjectException if gift certificate with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificateDTO> getOne(@PathVariable Long id) {
        GiftCertificateDTO certificateDTO = giftCertificatesService.find(id);
        return EntityModel.of(certificateDTO, linkTo(methodOn(GiftCertificatesController.class).getOne(id)).withSelfRel(),
                linkTo(methodOn(GiftCertificatesController.class).getByQuery()).withRel("gift_certificates"));
    }

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ProjectException if gift certificate with provided id is not present in repository
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        giftCertificatesService.delete(id);
    }

    /**
     * Updates gift certificate according to request body.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object according to which is necessary to update gift certificate
     *                              in repository
     * @param id                    id of updated gift certificate
     * @return GiftCertificateDTO gift certificate dto of updated gift certificate in repository
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     *                          id is not present in repository
     */
    @PutMapping("/{id}")
    public GiftCertificateDTO update(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id) {
        return giftCertificatesService.update(updatedCertificateDTO, id);
    }

    /**
     * Updates gift certificate fields according to provided in request body fields.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object which consist fields to update
     * @param id                    id of updated gift certificate
     * @return GiftCertificateDTO gift certificate dto of updated certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     *                          id is not present in repository
     */
    @PatchMapping("/{id}")
    public GiftCertificateDTO patch(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id) {
        return giftCertificatesService.patch(updatedCertificateDTO, id);
    }
}
