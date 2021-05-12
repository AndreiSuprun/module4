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
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindOne(){
        User userInDb = userDAO.findOne(1L);

        Assertions.assertEquals(1L, userInDb.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        User userInDb = userDAO.findOne(2L);

        Assertions.assertNull(userInDb);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByQuery(){
        List<User> userInDb = userDAO.findByQuery(null, null, 1L, 10);

        Assertions.assertEquals(1, userInDb.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testCount(){
        Long count = userDAO.count();

        Assertions.assertEquals(1, count);
    }
}
