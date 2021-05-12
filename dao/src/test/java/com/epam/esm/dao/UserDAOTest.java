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

    static Stream<User> defaultUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("user@mail.com");
        return Stream.of(user);
    }

    @ParameterizedTest
    @MethodSource("defaultUser")
    @Transactional
    @Rollback
    public void testFindOne(User user){
        user = userDAO.insert(user);
        User userInDb = userDAO.findOne(user.getId());

        Assertions.assertEquals(user.getId(), userInDb.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        User userInDb = userDAO.findOne(1L);

        Assertions.assertNull(userInDb);
    }

    @ParameterizedTest
    @MethodSource("defaultUser")
    @Transactional
    @Rollback
    public void testFindByQuery(User user){
        userDAO.insert(user);
        List<User> userInDb = userDAO.findByQuery(null, null, 1L, 10);

        Assertions.assertEquals(1, userInDb.size());
    }

    @ParameterizedTest
    @MethodSource("defaultUser")
    @Transactional
    @Rollback
    public void testCount(User user){
        userDAO.insert(user);
        Long count = userDAO.count();

        Assertions.assertEquals(1, count);
    }
}
