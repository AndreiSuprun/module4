package com.epam.esm.restapp.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
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
@RequestMapping("/certificates")
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    /**
     * Retrieves gift certificates from repository according to provided request parameters.
     *
     * @param page (optional) request parameter for page number
     * @param size (optional) request parameter for page size
     * @param searchParameters (optional) request parameter for searching
     * @param sortParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<GiftCertificateDTO>> object of gift certificates for returned page from repository
     * @throws ValidationException if provided query is not valid or gift certificates according to provided query
     *                          are not present in repository
     */
    @GetMapping
    public PagedModel<EntityModel<GiftCertificateDTO>> findByQuery(@RequestParam(value = "page", required = false) Long page,
                                                                       @RequestParam(value = "size", required = false) Integer size,
                                                                       @RequestParam(value = "search", required = false) String searchParameters,
                                                                       @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<GiftCertificateDTO> certificateDTOs = giftCertificatesService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                paginationDTO);
        return getPagedModel(certificateDTOs, paginationDTO, searchParameters, orderParameters);
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
        return getEntityModel(giftCertificateDTO);
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
        return getEntityModel(certificateDTO);
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
        return getEntityModel(giftCertificateDTO);
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
        return getEntityModel(giftCertificateDTO);
    }

    private EntityModel<GiftCertificateDTO> getEntityModel(GiftCertificateDTO giftCertificateDTO){
        return EntityModel.of(giftCertificateDTO, linkTo(methodOn(GiftCertificatesController.class).find(giftCertificateDTO.getId())).withSelfRel(),
                linkTo(methodOn(GiftCertificatesController.class).addGiftCertificate(giftCertificateDTO)).withRel("add"),
                linkTo(methodOn(GiftCertificatesController.class).delete(giftCertificateDTO.getId())).withRel("delete"),
                linkTo(methodOn(GiftCertificatesController.class).update(giftCertificateDTO, giftCertificateDTO.getId())).withRel("update"),
                linkTo(methodOn(GiftCertificatesController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("gift_certificates"));
    }

    private PagedModel<EntityModel<GiftCertificateDTO>> getPagedModel(List<GiftCertificateDTO> certificates, PaginationDTO pagination,
                                                            String searchParameters, String orderParameters){
        List<EntityModel<GiftCertificateDTO>> entityModels = certificates.stream()
                .map(certificate -> EntityModel.of(certificate,
                        linkTo(methodOn(GiftCertificatesController.class).find(certificate.getId())).withSelfRel(),
                        linkTo(methodOn(GiftCertificatesController.class).addGiftCertificate(certificate)).withRel("add"),
                        linkTo(methodOn(GiftCertificatesController.class).delete(certificate.getId())).withRel("delete"),
                        linkTo(methodOn(GiftCertificatesController.class).update(certificate, certificate.getId())).withRel("update")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pagination.getPage() - 1 ,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pagination.getPage() + 1,
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pagination.getTotalPages(),
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(),
                pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
