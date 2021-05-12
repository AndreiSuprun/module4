package com.epam.esm.dao;

import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.entity.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
public class GiftCertificateDAOTest {

    @Autowired
    private GiftCertificateDAO certificateDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private UserDAO userDAO;

    static Stream<GiftCertificate> defaultGiftCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Tag tag = new Tag("CertificateTag");
        certificate.addTag(tag);
        return Stream.of(certificate);
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindOne(GiftCertificate certificate){
        certificate = certificateDAO.insert(certificate);
        GiftCertificate certificateInDb = certificateDAO.findOne(certificate.getId());

        Assertions.assertEquals(certificate.getId(), certificateInDb.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        GiftCertificate certificateInDb = certificateDAO.findOne(2L);

        Assertions.assertNull(certificateInDb);
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByQuery(GiftCertificate certificate){
        certificateDAO.insert(certificate);
        List<GiftCertificate> certificateInDb = certificateDAO.findByQuery(null, null, 1L, 10);

        Assertions.assertEquals(2, certificateInDb.size());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testCount(GiftCertificate certificate){
        certificateDAO.insert(certificate);
        Long count = certificateDAO.count();

        Assertions.assertEquals(2, count);
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testDeleteExisting(GiftCertificate certificate){
        certificate = certificateDAO.insert(certificate);
        boolean actual = certificateDAO.delete(certificate.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    @Transactional
    public void testDeleteNotExisting(){
        boolean actual = certificateDAO.delete(2L);

        Assertions.assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testAddCertificate(GiftCertificate certificate){
        GiftCertificate giftCertificate = certificateDAO.insert(certificate);
        GiftCertificate certificateInDb = certificateDAO.findOne(giftCertificate.getId());

        Assertions.assertEquals(certificate.getName(), certificateInDb.getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByName(GiftCertificate certificate) {
        certificateDAO.insert(certificate);
        GiftCertificate certificateInDB = certificateDAO.findByName(certificate.getName());

        Assertions.assertEquals(certificate.getName(), certificateInDB.getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testGetOrdersFromCertificate(GiftCertificate certificate){
        User user = userDAO.findOne(1L);
        OrderItem orderItem = new OrderItem();
        GiftCertificate certificateInDB = certificateDAO.findOne(1L);
        orderItem.setCertificate(certificateInDB);
        orderItem.setQuantity(1);
        Order order = new Order();
        order.setUser(user);
        order.setOrderCertificates(Lists.list(orderItem));
        order.setTotalPrice(BigDecimal.valueOf(1));
        orderDAO.insert(order);
        List<Order> tags = certificateDAO.getOrdersForCertificates(certificateInDB.getId());
        Assertions.assertEquals(order.getId(), tags.get(0).getId());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testUpdateCertificate(GiftCertificate certificate){
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        certificate.setName("CertificateUpdated");
        GiftCertificate updatedCertificateInDB = certificateDAO.update(certificate, certificateInDB.getId());

        Assertions.assertEquals(certificate.getName(), updatedCertificateInDB.getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByNotFoundQuery(GiftCertificate certificate) {
        SearchCriteria searchCriteria = new SearchCriteria("name", SearchOperation.EQUALITY, "Certificate new");

        certificateDAO.insert(certificate);
        List<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(Lists.list(searchCriteria), null, 1L,10);

        Assertions.assertEquals(0, certificatesByQuery.size());
    }
}
