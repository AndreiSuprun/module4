package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDBConfig.class})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class TagDAOTest {

    @Autowired
    private TagRepository tagDAO;

    @Autowired
    private CertificateRepository giftCertificateDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindOne(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.save(tag);
        Optional<Tag> tagInDb = tagDAO.findById(tag.getId());

        Assertions.assertEquals(tag.getId(), tagInDb.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOneNotPresent(){
        Optional<Tag> tagInDb = tagDAO.findById(2L);

        Assertions.assertFalse(tagInDb.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByQuery(){
        Tag tag = new Tag("Tag");
        Pageable pageable = PageRequest.of(0, 2);

        tagDAO.save(tag);
        Page<Tag> tagInDb = tagDAO.findByQuery(null, null, pageable);
        Assertions.assertEquals(2, tagInDb.getTotalElements());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteExisting(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.save(tag);
        tagDAO.delete(tag);
        Optional<Tag> tagInDb = tagDAO.findByName(tag.getName());

        Assertions.assertFalse(tagInDb.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testInsert(){
        Tag tag = new Tag("Tag");

        tag = tagDAO.save(tag);
        Optional<Tag> tagInDb = tagDAO.findByName(tag.getName());

        Assertions.assertEquals(tag.getName(), tagInDb.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByName() {
        Tag tag = new Tag("Tag");

        tag = tagDAO.save(tag);
        Optional<Tag> tagByName = tagDAO.findByName(tag.getName());

        Assertions.assertEquals(tag.getName(), tagByName.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testCount(){
        Tag tag = new Tag("Tag");

        tagDAO.save(tag);
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

        GiftCertificate certificateInDB = giftCertificateDAO.save(certificate);
        List<GiftCertificate> certificates = tagDAO.getCertificatesForTag(certificateInDB.getTags().get(0).getId());
        Assertions.assertEquals(certificate.getId(), certificates.get(0).getId());
    }
}
