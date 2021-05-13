package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaUtil<Tag> criteriaUtil;

    @Override
    public Tag findOne(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag findByName(String name) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        final Root<Tag> root = query.from(Tag.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        List<Tag> tags = typedQuery.getResultList();
        if (!tags.isEmpty()){
            return tags.get(0);
        }
        return null;
    }

    @Override
    public Tag insert(Tag tag) {
        if (findByName(tag.getName()) == null){
            entityManager.persist(tag);
            return tag;
        }
        return null;
    }

    @Override
    public Tag update(Tag obj, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag != null) {
            entityManager.remove(tag);
            return true;
        }
        return false;
    }

    public Long count(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        if (searchParams.length > 0)  {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    builder.createQuery().from(Tag.class)));
        }
        query.select(builder.count(query.from(Tag.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Tag> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Long page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) ((page - 1) * size));
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
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

    @Override
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

