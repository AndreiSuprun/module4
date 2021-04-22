//package com.epam.esm.dao;
//
//import com.epam.esm.entity.GiftCertificate;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
////@ContextConfiguration(locations = "classpath:application-context-test.xml")
//public class GiftCertificateDAOTest {
//
//    @Autowired
//    private GiftCertificateDAO certificateDAO;
//
//    @Autowired
//    private TagDAO tagDAO;
//
//    @Test
//    @Transactional
//    @Rollback(true)
//    public void testAddCertificate(){
//
//        GiftCertificate certificate = new GiftCertificate("Certificate", "Certificate description", 2.00, 30);
//        gDAO.addDepartment(certificate);
//
//        Optional<GiftCertificate> certificate = certificateDAO.getAll();
//        Assert.assertEquals(department.getName(), departments.get(0).getName());
//    }
//
//
//}
