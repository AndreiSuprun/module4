package com.epam.esm.dao;

import com.epam.esm.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
public class TagDAOTest {

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindOne(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.insert(tag);
        Tag tagInDb = tagDAO.findOne(tag.getId());

        Assertions.assertEquals(tag.getId(), tagInDb.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Tag tagInDb = tagDAO.findOne(2L);

        Assertions.assertNull(tagInDb);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByQuery(){
        Tag tag = new Tag("Tag");

        tagDAO.insert(tag);
        List<Tag> tagInDb = tagDAO.findByQuery(null, null, 1L, 10);
        Assertions.assertEquals(2, tagInDb.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteExisting(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.insert(tag);
        boolean actual = tagDAO.delete(tag.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteNotExisting(){
        boolean actual = tagDAO.delete(2L);

        Assertions.assertFalse(actual);
    }

    @Test
    @Transactional
    @Rollback
    public void testInsert(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.insert(tag);
        Tag tagInDb = tagDAO.findByName(tag.getName());

        Assertions.assertEquals(tag.getName(), tagInDb.getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByName() {
        Tag tag = new Tag("Tag");

        tag = tagDAO.insert(tag);
        Tag tagByName = tagDAO.findByName(tag.getName());

        Assertions.assertEquals(tag.getName(), tagByName.getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testCount(){
        Tag tag = new Tag("Tag");

        tagDAO.insert(tag);
        Long count = tagDAO.count();

        Assertions.assertEquals(2, count);
    }

    @Test
    @Transactional
    @Rollback
    public void testCertificatesForTag(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Tag tag = new Tag("Tag");
        certificate.addTag(tag);

        GiftCertificate certificateInDB = giftCertificateDAO.insert(certificate);
        List<GiftCertificate> certificates = tagDAO.getCertificatesForTag(certificateInDB.getTags().get(0).getId());
        Assertions.assertEquals(certificate.getId(), certificates.get(0).getId());
    }
}
