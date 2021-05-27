package com.epam.esm.restapp.controller;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EntityResponseBuilder {

    private final int DEFAULT_PAGE_NUMBER = 1;
    private final int DEFAULT_PAGE_SIZE = 20;

    public EntityModel<OrderDTO> getOrderEntityModel(OrderDTO order){
        return EntityModel.of(order, linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).createOrder(order)).withRel("place_order"),
                linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete"),
                linkTo(methodOn(OrdersController.class).
                        findByQuery(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE), null, null)).
                        withRel("users"));
    }

    public PagedModel<EntityModel<OrderDTO>> getOrderPagedModel(Page<OrderDTO> orders, Pageable pageable,
                                                            String searchParameters, String orderParameters){
        List<EntityModel<OrderDTO>> entityModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                        linkTo(methodOn(OrdersController.class).createOrder(order)).withRel("place_order"),
                        linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() > 0){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pageable.first(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pageable.getPageNumber() > 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pageable.previousOrFirst(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(OrdersController.class).findByQuery(pageable,
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (orders.getTotalPages()  > pageable.getPageNumber()){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pageable.next(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (orders.getTotalPages()-1 > pageable.getPageNumber()){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(new PageImpl<>(orders.toList(), pageable, orders.getTotalElements()).getPageable(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(),
                pageable.getPageNumber(), orders.getTotalElements());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

    public EntityModel<GiftCertificateDTO> getCertificateEntityModel(GiftCertificateDTO giftCertificateDTO){
        return EntityModel.of(giftCertificateDTO, linkTo(methodOn(GiftCertificatesController.class).find(giftCertificateDTO.getId())).withSelfRel(),
                linkTo(methodOn(GiftCertificatesController.class).addGiftCertificate(giftCertificateDTO)).withRel("add"),
                linkTo(methodOn(GiftCertificatesController.class).delete(giftCertificateDTO.getId())).withRel("delete"),
                linkTo(methodOn(GiftCertificatesController.class).update(giftCertificateDTO, giftCertificateDTO.getId())).withRel("update"),
                linkTo(methodOn(GiftCertificatesController.class).
                        findByQuery(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE), null, null)).
                        withRel("gift_certificates"));
    }

    public PagedModel<EntityModel<GiftCertificateDTO>> getCertificatePagedModel(Page<GiftCertificateDTO> certificates, Pageable pageable,
                                                                      String searchParameters, String orderParameters){
        List<EntityModel<GiftCertificateDTO>> entityModels = certificates.stream()
                .map(certificate -> EntityModel.of(certificate,
                        linkTo(methodOn(GiftCertificatesController.class).find(certificate.getId())).withSelfRel(),
                        linkTo(methodOn(GiftCertificatesController.class).addGiftCertificate(certificate)).withRel("add"),
                        linkTo(methodOn(GiftCertificatesController.class).delete(certificate.getId())).withRel("delete"),
                        linkTo(methodOn(GiftCertificatesController.class).update(certificate, certificate.getId())).withRel("update")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() > 0){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pageable.first(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pageable.getPageNumber() > 1){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pageable.previousOrFirst(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pageable,
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (certificates.getTotalPages()  > pageable.getPageNumber()){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(pageable.next(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (certificates.getTotalPages()-1 > pageable.getPageNumber()){
            links.add(linkTo(methodOn(GiftCertificatesController.class).findByQuery(new PageImpl<>(certificates.toList(), pageable, certificates.getTotalElements()).getPageable(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(),
                pageable.getPageNumber(), certificates.getTotalElements());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

    public EntityModel<UserDTO> getUserEntityModel(UserDTO user){
        return EntityModel.of(user, linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).
                        findByQuery(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE), null, null)).withRel("users"));
    }

    public PagedModel<EntityModel<UserDTO>> getUserPagedModel(Page<UserDTO> users, Pageable pageable,
                                                              String searchParameters, String orderParameters){
        List<EntityModel<UserDTO>> entityModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() > 0){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pageable.first(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pageable.getPageNumber() > 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pageable.previousOrFirst(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(UsersController.class).findByQuery(pageable,
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (users.getTotalPages()  > pageable.getPageNumber()){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pageable.next(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (users.getTotalPages()-1 > pageable.getPageNumber()){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(new PageImpl<>(users.toList(), pageable, users.getTotalElements()).getPageable(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(),
                pageable.getPageNumber(), users.getTotalElements());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

    public EntityModel<TagDTO> getTagEntityModel(TagDTO tag){
        return EntityModel.of(tag, linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete"),
                linkTo(methodOn(TagsController.class).
                        findByQuery(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE), null, null)).
                        withRel("tags"));
    }

    public PagedModel<EntityModel<TagDTO>> getTagPagedModel(Page<TagDTO> tags, Pageable pageable,
                                                          String searchParameters, String orderParameters){
        List<EntityModel<TagDTO>> entityModels = tags.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                        linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                        linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() > 0){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pageable.first(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pageable.getPageNumber() > 1){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pageable.previousOrFirst(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(TagsController.class).findByQuery(pageable,
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (tags.getTotalPages()  > pageable.getPageNumber()){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pageable.next(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (tags.getTotalPages()-1 > pageable.getPageNumber()){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(new PageImpl<>(tags.toList(), pageable, tags.getTotalElements()).getPageable(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(),
                pageable.getPageNumber(), tags.getTotalElements());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

}
