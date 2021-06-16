package com.epam.esm.dao;

import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class OrderDAOTest {

    @Autowired
    private CertificateRepository certificateDAO;
    @Autowired
    private OrderRepository orderDAO;
    @Autowired
    private UserRepository userDAO;

    static Stream<Order> defaultOrder() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        Order order = new Order();
        order.setOrderCertificates(Lists.list(orderItem));
        order.setTotalPrice(BigDecimal.valueOf(1));
        order.setOrderCertificates(Lists.list(orderItem));
        return Stream.of(order);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindOne(Order order){
        Optional<User> user = userDAO.findById(1L);
        order.setUser(user.get());
        order = orderDAO.save(order);
        Optional<Order> orderInDb = orderDAO.findById(order.getId());

        Assertions.assertEquals(order.getId(), orderInDb.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Optional<Order> orderInDb = orderDAO.findById(1L);

        Assertions.assertFalse(orderInDb.isPresent());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindByQuery(Order order){
        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        Pageable pageable = PageRequest.of(0, 2);
        orderDAO.save(order);
        Page<Order> ordersInDb = orderDAO.findByQuery(null, null, pageable);

        Assertions.assertEquals(1, ordersInDb.getTotalElements());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testCount(Order order){
        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        orderDAO.save(order);
        Long count = orderDAO.count();

        Assertions.assertEquals(1, count);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testDeleteExisting(Order order){
        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        order = orderDAO.save(order);
        orderDAO.delete(order);
        Optional<Order> orderInDb = orderDAO.findById(order.getId());

        Assertions.assertFalse(orderInDb.isPresent());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testInsert(Order order){
        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        Pageable pageable = PageRequest.of(0, 2);
        orderDAO.save(order);
        Page<Order> orderInDb = orderDAO.findByQuery(null, null, pageable);

        Assertions.assertEquals(order.getId(), orderInDb.getContent().get(0).getId());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testUpdate(Order order){
        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        Order orderInDB = orderDAO.save(order);
        order.setTotalPrice(BigDecimal.valueOf(4));
        Order updatedOrderInDB = orderDAO.save(order);

        Assertions.assertEquals(order.getTotalPrice(), updatedOrderInDB.getTotalPrice());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindByNotFoundQuery(Order order) {
        SearchCriteria searchCriteria = new SearchCriteria("totalPrice", SearchOperation.EQUALITY, "3.00");

        Optional<User> user = userDAO.findById(1L);
        Optional<GiftCertificate> giftCertificate = certificateDAO.findById(1L);
        order.setUser(user.get());
        order.getOrderCertificates().get(0).setCertificate(giftCertificate.get());
        Pageable pageable = PageRequest.of(0, 2);
        orderDAO.save(order);
        Page<Order> ordersByQuery = orderDAO.findByQuery(Lists.list(searchCriteria), null, pageable);
        Assertions.assertEquals(0, ordersByQuery.getTotalElements());
    }
}
