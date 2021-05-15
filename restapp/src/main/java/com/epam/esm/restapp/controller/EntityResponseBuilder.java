package com.epam.esm.restapp.controller;

import com.epam.esm.service.dto.*;
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

    public EntityModel<OrderDTO> getOrderEntityModel(OrderDTO order){
        return EntityModel.of(order, linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).placeOrder(order)).withRel("place_order"),
                linkTo(methodOn(OrdersController.class).update(order, order.getId())).withRel("update"),
                linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete"),
                linkTo(methodOn(OrdersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("users"));
    }

    public PagedModel<EntityModel<OrderDTO>> getOrderPagedModel(List<OrderDTO> orders, PaginationDTO pagination,
                                                            String searchParameters, String orderParameters){
        List<EntityModel<OrderDTO>> entityModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                        linkTo(methodOn(OrdersController.class).placeOrder(order)).withRel("place_order"),
                        linkTo(methodOn(OrdersController.class).update(order, order.getId())).withRel("update"),
                        linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage() - 1 ,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage() + 1,
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getTotalPages(),
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(),
                pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

    public EntityModel<GiftCertificateDTO> getCertificateEntityModel(GiftCertificateDTO giftCertificateDTO){
        return EntityModel.of(giftCertificateDTO, linkTo(methodOn(GiftCertificatesController.class).find(giftCertificateDTO.getId())).withSelfRel(),
                linkTo(methodOn(GiftCertificatesController.class).addGiftCertificate(giftCertificateDTO)).withRel("add"),
                linkTo(methodOn(GiftCertificatesController.class).delete(giftCertificateDTO.getId())).withRel("delete"),
                linkTo(methodOn(GiftCertificatesController.class).update(giftCertificateDTO, giftCertificateDTO.getId())).withRel("update"),
                linkTo(methodOn(GiftCertificatesController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("gift_certificates"));
    }

    public PagedModel<EntityModel<GiftCertificateDTO>> getCertificatePagedModel(List<GiftCertificateDTO> certificates, PaginationDTO pagination,
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

    public EntityModel<UserDTO> getUserEntityModel(UserDTO user){
        return EntityModel.of(user, linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).withRel("users"));
    }

    public PagedModel<EntityModel<UserDTO>> getUserPagedModel(List<UserDTO> users, PaginationDTO pagination,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<UserDTO>> entityModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage() - 1 ,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage() + 1,
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getTotalPages(),
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(),
                pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

    public EntityModel<TagDTO> getTagEntityModel(TagDTO tag){
        return EntityModel.of(tag, linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete"),
                linkTo(methodOn(TagsController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("tags"));
    }

    public PagedModel<EntityModel<TagDTO>> getTagPagedModel(List<TagDTO> tags, PaginationDTO pagination,
                                                          String searchParameters, String orderParameters){
        List<EntityModel<TagDTO>> entityModels = tags.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                        linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                        linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(PaginationDTO.FIRST_PAGE, pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage() - 1 , pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage() + 1, pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getTotalPages(), pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(), pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }

}
