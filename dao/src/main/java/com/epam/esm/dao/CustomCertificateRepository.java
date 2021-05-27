package com.epam.esm.dao;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCertificateRepository {

    Page<GiftCertificate> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Pageable pageable);
    Long countByQuery(List<SearchCriteria>... searchParams);
    List<Order> findOrdersForCertificates(Long certificateId);
    void addTags(GiftCertificate certificate);
}
