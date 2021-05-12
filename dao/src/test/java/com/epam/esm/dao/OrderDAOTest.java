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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
public class OrderDAOTest {

    @Autowired
    private GiftCertificateDAO certificateDAO;
    @Autowired
    private OrderDAO orderDAO;

    static Stream<Order> defaultOrder() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        User user = new User();
        user.setId(1L);
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(certificate);
        orderItem.setQuantity(1);
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        orderItem.setOrder(order);
        return Stream.of(order);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindOne(Order order){
        order = orderDAO.insert(order);
        Order orderInDb = orderDAO.findOne(order.getId());

        Assertions.assertEquals(order.getId(), orderInDb.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Order orderInDb = orderDAO.findOne(1L);

        Assertions.assertNull(orderInDb);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindByQuery(Order order){
        orderDAO.insert(order);
        List<Order> ordersInDb = orderDAO.findByQuery(null, null, 1L, 10);

        Assertions.assertEquals(1, ordersInDb.size());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testCount(Order order){
        orderDAO.insert(order);
        Long count = orderDAO.count();

        Assertions.assertEquals(1, count);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testDeleteExisting(Order order){
        order = orderDAO.insert(order);
        boolean actual = orderDAO.delete(order.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    @Transactional
    public void testDeleteNotExisting(){
        boolean actual = orderDAO.delete(1L);

        Assertions.assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testInsert(Order order){
        orderDAO.insert(order);
        List<Order> orderInDb = orderDAO.findByQuery(null, null, 1L,10);

        Assertions.assertEquals(order.getId(), orderInDb.get(0).getId());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testUpdate(Order order){
        Order orderInDB = orderDAO.insert(order);
        order.setTotalPrice(BigDecimal.valueOf(4));
        Order updatedOrderInDB = orderDAO.update(order, orderInDB.getId());

        Assertions.assertEquals(order.getTotalPrice(), updatedOrderInDB.getTotalPrice());
    }

    @ParameterizedTest
    @MethodSource("defaultOrder")
    @Transactional
    @Rollback
    public void testFindByNotFoundQuery(Order order) {
        SearchCriteria searchCriteria = new SearchCriteria("totalPrice", SearchOperation.EQUALITY, "3.00");

        orderDAO.insert(order);
        List<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(Lists.list(searchCriteria), null, 1L,10);
        Assertions.assertEquals(0, certificatesByQuery.size());
    }
}
