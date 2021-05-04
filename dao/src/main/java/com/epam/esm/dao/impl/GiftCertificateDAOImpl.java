package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.search.OrderCriteria;
import com.epam.esm.service.search.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    @Autowired
    private CriteriaUtil<GiftCertificate> criteriaUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GiftCertificate findOne(Long id) {
        return entityManager.find(GiftCertificate.class, id);
    }

    @Override
    public GiftCertificate findByName(String name) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        final Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<GiftCertificate> findAll(Integer page, Integer size) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        final Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public boolean delete(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if(giftCertificate != null) {
            entityManager.remove(giftCertificate);
            return true;
        }
        return false;
    }

    public Long count(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        if (searchParams.length > 0)  {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    builder.createQuery().from(GiftCertificate.class)));
        }
        query.select(builder.count(query.from(GiftCertificate.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public GiftCertificate insert(GiftCertificate certificate) {
        certificate.setCreateDate(java.time.LocalDateTime.now());
        certificate.setLastUpdateDate(java.time.LocalDateTime.now());
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate, Long id) {
        certificate.setLastUpdateDate(java.time.LocalDateTime.now());
        certificate.setId(id);
        entityManager.merge(certificate);
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Integer page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }
}
