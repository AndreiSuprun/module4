package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserDAOTest {

    @Autowired
    private UserRepository userDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindOne(){
        Optional<User> userInDb = userDAO.findById(1L);

        Assertions.assertEquals(1L, userInDb.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Optional<User> userInDb = userDAO.findById(2L);

        Assertions.assertFalse(userInDb.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByQuery(){
        Page<User> userInDb = userDAO.findByQuery(null, null, Pageable.unpaged());

        Assertions.assertEquals(1, userInDb.getTotalElements());
    }

    @Test
    @Transactional
    @Rollback
    public void testCount(){
        Long count = userDAO.count();

        Assertions.assertEquals(1, count);
    }
}
