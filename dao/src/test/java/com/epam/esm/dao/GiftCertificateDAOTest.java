package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
public class GiftCertificateDAOTest {

    @Autowired
    private GiftCertificateDAO certificateDAO;
    @Autowired
    private TagDAO tagDAO;

    @Test
    @Transactional
    @Rollback
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
    @Rollback
    public void testFindOneNotPresent(){
        Optional<GiftCertificate> certificateInDb = certificateDAO.findOne(1L);

        Assertions.assertFalse(certificateInDb.isPresent());
    }

    @Test
    @Transactional
    @Rollback
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
    @Rollback
    public void testDeleteExisting(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);

        certificate = certificateDAO.insert(certificate);
        boolean actual = certificateDAO.delete(certificate.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteNotExisting(){
        boolean actual = certificateDAO.delete(1L);

        Assertions.assertFalse(actual);
    }

    @Test
    @Transactional
    @Rollback
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
    @Rollback
    public void testAddTagToCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        Tag tag = new Tag();
        tag.setName("Tag");

        tagDAO.insert(tag);
        certificateDAO.addTag(certificateInDB, tag);
        List<Tag> tags = certificateDAO.getTags(certificateInDB);

        Assertions.assertEquals(tag.getName(), tags.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetTagsFromCertificate(){
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Tag tag = new Tag();
        tag.setName("Tag");
        tag.setId(1L);
        certificate.addTag(tag);

        tagDAO.insert(tag);
        GiftCertificate certificateInDB = certificateDAO.insert(certificate);
        certificateDAO.addTag(certificateInDB, tag);
        List<Tag> tags = certificateDAO.getTags(certificateInDB);

        Assertions.assertEquals(tag.getName(), tags.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback
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
    @Rollback
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
    @Rollback
    public void testFindByQuery() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Query query = new Query();
        query.setOrder("name");

        certificateDAO.insert(certificate);
        List<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(query);

        Assertions.assertEquals(certificate.getName(), certificatesByQuery.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByNotFoundQuery() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        certificate.setDescription("Certificate description");
        certificate.setPrice(BigDecimal.valueOf(2.00));
        certificate.setDuration(30);
        Query query = new Query();
        query.setContains("Certificates");

        certificateDAO.insert(certificate);
        List<GiftCertificate> certificatesByQuery = certificateDAO.findByQuery(query);

        Assertions.assertEquals(0, certificatesByQuery.size());
    }
}
