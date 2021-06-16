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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class GiftCertificateDAOTest {

    @Autowired
    private CertificateRepository certificateDAO;
    @Autowired
    private OrderRepository orderDAO;
    @Autowired
    private UserRepository userDAO;

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
        certificate = certificateDAO.save(certificate);
        Optional<GiftCertificate> certificateInDb = certificateDAO.findById(certificate.getId());

        Assertions.assertEquals(certificate.getId(), certificateInDb.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Optional<GiftCertificate> certificateInDb = certificateDAO.findById(2L);

        Assertions.assertFalse(certificateInDb.isPresent());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByQuery(GiftCertificate certificate){
        certificateDAO.save(certificate);
        Pageable pageable = PageRequest.of(0, 2);
        Page<GiftCertificate> certificateInDb = certificateDAO.findByQuery(null, null, pageable);
        Assertions.assertEquals(2, certificateInDb.getTotalElements());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testCount(GiftCertificate certificate){
        certificateDAO.save(certificate);
        Long count = certificateDAO.count();

        Assertions.assertEquals(2, count);
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testDeleteExisting(GiftCertificate certificate){
        certificate = certificateDAO.save(certificate);
        certificateDAO.delete(certificate);

        Optional<GiftCertificate> certificateInDb = certificateDAO.findById(certificate.getId());
        Assertions.assertFalse(certificateInDb.isPresent());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testAddCertificate(GiftCertificate certificate){
        GiftCertificate giftCertificate = certificateDAO.save(certificate);
        Optional<GiftCertificate> certificateInDb = certificateDAO.findById(giftCertificate.getId());

        Assertions.assertEquals(certificate.getName(), certificateInDb.get().getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByName(GiftCertificate certificate) {
        certificateDAO.save(certificate);
        Optional<GiftCertificate> certificateInDB = certificateDAO.findByName(certificate.getName());

        Assertions.assertEquals(certificate.getName(), certificateInDB.get().getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testGetOrdersFromCertificate(GiftCertificate certificate){
        Optional<User> user = userDAO.findById(1L);
        OrderItem orderItem = new OrderItem();
        Optional<GiftCertificate> certificateInDB = certificateDAO.findById(1L);
        orderItem.setCertificate(certificateInDB.get());
        orderItem.setQuantity(1);
        Order order = new Order();
        order.setUser(user.get());
        order.setOrderCertificates(Lists.list(orderItem));
        order.setTotalPrice(BigDecimal.valueOf(1));
        order = orderDAO.save(order);
        orderItem.setOrder(order);
        order = orderDAO.save(order);
        List<Order> orders = certificateDAO.findOrdersForCertificates(certificateInDB.get().getId());
        Assertions.assertEquals(order.getId(), orders.get(0).getId());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testUpdateCertificate(GiftCertificate certificate){
        GiftCertificate certificateInDB = certificateDAO.save(certificate);
        certificate.setName("CertificateUpdated");
        GiftCertificate updatedCertificateInDB = certificateDAO.save(certificate);

        Assertions.assertEquals(certificate.getName(), updatedCertificateInDB.getName());
    }

    @ParameterizedTest
    @MethodSource("defaultGiftCertificate")
    @Transactional
    @Rollback
    public void testFindByNotFoundQuery(GiftCertificate certificate) {
        SearchCriteria searchCriteria = new SearchCriteria("name", SearchOperation.EQUALITY, "Certificate new");
        Pageable pageable = PageRequest.of(0, 2);

        certificateDAO.save(certificate);
        Page<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(Lists.list(searchCriteria), null, pageable);

        Assertions.assertEquals(0, certificatesByQuery.getTotalElements());
    }
}
