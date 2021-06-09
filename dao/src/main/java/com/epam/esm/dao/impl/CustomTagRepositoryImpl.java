package com.epam.esm.dao.impl;

import com.epam.esm.dao.CustomTagRepository;
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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomTagRepositoryImpl implements CustomTagRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final CriteriaUtil<Tag> criteriaUtil;

    @Autowired
    public CustomTagRepositoryImpl(CriteriaUtil<Tag> criteriaUtil) {
        this.criteriaUtil = criteriaUtil;
    }

    @Override
    public Page<Tag> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Tag> tags = typedQuery.getResultList();
        Long count = countByQuery(searchParams);
        return new PageImpl<>(tags, pageable, count);
    }

    @Override
    public Long countByQuery(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Tag> root = query.from(Tag.class);
        if (searchParams.length > 0) {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    root));
        }
        query.select(builder.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }

    public List<GiftCertificate> getCertificatesForTag(Long tagId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.equal(certificateRoot.join("tags").get("id"), tagId));
        criteriaQuery.select(certificateRoot);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public Tag findMostWidelyUsedTag() {
        String sql = "SELECT * FROM tags JOIN certificates_tags ON tags.id = certificates_tags.tag_id " +
                "WHERE certificates_tags.certificate_id IN (SELECT orders_certificates.certificate_id " +
                "FROM orders_certificates WHERE orders_certificates.order_id in (SELECT orders.id FROM orders " +
                "WHERE user_id = (SELECT orders.user_id FROM orders GROUP BY orders.user_id " +
                "ORDER BY SUM(orders.total_price) DESC LIMIT 1))) GROUP BY tags.name ORDER BY COUNT(*) DESC LIMIT 1";
        Query query = entityManager.createNativeQuery(sql, Tag.class);
        return (Tag) query.getSingleResult();
    }
}
