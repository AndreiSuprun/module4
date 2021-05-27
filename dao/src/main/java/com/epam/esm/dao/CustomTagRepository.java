package com.epam.esm.dao;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTagRepository {
    Page<Tag> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Pageable pageable);
    Long countByQuery(List<SearchCriteria>... searchParams);
    List<GiftCertificate> getCertificatesForTag(Long tagId);
    Tag findMostWidelyUsedTag();
}
