package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {TestDBConfig.class})
public class GiftCertificateDAOTest {

    @Autowired
    private GiftCertificateDAO certificateDAO;

//    @Autowired @Qualifier("testTemplate")
//    NamedParameterJdbcTemplate namedParamJdbcTemplate;

//    @BeforeAll
//    public void setup() {
//        certificateDAO.setNamedParamJdbcTemplate(namedParamJdbcTemplate);
//    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFindOne(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        certificate = certificateDAO.insert(certificate);
        Optional<GiftCertificate> certificateInDb = certificateDAO.findOne(certificate.getId());
        Assertions.assertEquals(certificate.getId(), certificateInDb.get().getId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFindOneNotPresent(){
        Optional<GiftCertificate> certificateInDb = certificateDAO.findOne(1L);
        Assertions.assertEquals(false, certificateInDb.isPresent());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFindAll(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        certificateDAO.insert(certificate);
        List<GiftCertificate> certificateInDb = certificateDAO.findAll();
        Assertions.assertEquals(1, certificateInDb.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteExisting(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        certificate = certificateDAO.insert(certificate);
        boolean actual = certificateDAO.delete(certificate.getId());
        Assertions.assertEquals(true, actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteNotExisting(){
        boolean actual = certificateDAO.delete(1L);
        Assertions.assertEquals(false, actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testAddCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        certificateDAO.insert(certificate);
        List<GiftCertificate> certificateInDb = certificateDAO.findAll();
        Assertions.assertEquals(certificate.getName(), certificateInDb.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testAddTagToCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        Tag tag = new Tag();
        tag.setName("Tag");
        certificateDAO.addTag(certificateInDB, tag);
        List<Tag> tags = certificateDAO.getTags(certificateInDB);
        Assertions.assertEquals(tag.getName(), tags.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetTagsFromCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Tag tag = new Tag();
        tag.setName("Tag");
        certificate.addTag(tag);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        List<Tag> tags = certificateDAO.getTags(certificateInDB);
        Assertions.assertEquals(tag.getName(), tags.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testClearTagsFromCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Tag tag = new Tag();
        tag.setName("Tag");
        certificate.addTag(tag);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        certificateDAO.clearTags(certificateInDB.getId());
        List<Tag> tags = certificateDAO.getTags(certificateInDB);
        Assertions.assertEquals(0, tags.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        certificate.setName("CertificateUpdated");
        GiftCertificate updatedCertificateInDB = certificateDAO.update(certificate, certificateInDB.getId());
        Assertions.assertEquals(certificate.getName(), updatedCertificateInDB.getName());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFindByQuery() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        certificateDAO.insert(certificate);
        Query query = new Query();
        query.setContains("Certificate");
        List<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(query);
        Assertions.assertEquals(certificate.getName(), certificatesByQuery.get(0).getName());
    }

}
