package com.epam.esm.dao.impl;

import com.epam.esm.dao.CustomCertificateRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomCertificateRepositoryImpl implements CustomCertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final TagRepository tagRepository;
    private final CriteriaUtil<GiftCertificate> criteriaUtil;

    @Autowired
    public CustomCertificateRepositoryImpl(TagRepository tagRepository, CriteriaUtil<GiftCertificate> criteriaUtil) {
        this.tagRepository = tagRepository;
        this.criteriaUtil = criteriaUtil;
    }

    @Override
    public Page<GiftCertificate> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<GiftCertificate> certificates = typedQuery.getResultList();
        Long count = countByQuery(searchParams);
        return new PageImpl<>(certificates, pageable, count);
    }

    @Override
    public Long countByQuery(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        if (searchParams.length > 0) {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    root));
        }
        query.select(builder.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }

    public List<Order> findOrdersForCertificates(Long certificateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.join("orderItems").get("certificate").get("id"), certificateId));
        criteriaQuery.select(orderRoot);
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public void addTags(GiftCertificate certificate) {
        List<Tag> tags = certificate.getTags();
        List<Tag> tagsInDB = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getId() != null) {
                Optional<Tag> tagInDBOpt = tagRepository.findById(tag.getId());
                if (tagInDBOpt.isPresent()) {
                    tagsInDB.add(tagInDBOpt.get());
                } else {
                    tag.setId(null);
                    Tag newTag = tagRepository.save(tag);
                    tagsInDB.add(newTag);
                }
            } else {
                Tag newTag = tagRepository.save(tag);
                tagsInDB.add(newTag);
            }
        }
        certificate.setTags(tagsInDB);
    }
}
