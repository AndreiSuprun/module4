package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String TAGS = "tags";
    private static final String DELIMITER = ",";

    private GiftCertificateDAO giftCertificateDAO;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificateDAO giftCertificateDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @Override
    public GiftCertificate add(GiftCertificate giftCertificate) {
        return giftCertificateDAO.insert(giftCertificate);
    }

    @Override
    public GiftCertificate update(Map<String, String> map, long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()){

        }
        if (certificateOptional.isPresent()) {
            GiftCertificate certificate = certificateOptional.get();
            if (map.containsKey(NAME)) {
                certificate.setName(map.get(NAME));
            }
            if (map.containsKey(DESCRIPTION)) {
                certificate.setDescription(map.get(DESCRIPTION));
            }
            if (map.containsKey(PRICE)) {
                certificate.setPrice(new BigDecimal(map.get(PRICE)));
            }
            if (map.containsKey(DURATION)) {
                certificate.setDuration(Integer.parseInt(map.get(DURATION)));
            }
            if (map.containsKey(TAGS)) {
                String[] tags = map.get(TAGS).split(DELIMITER);
                certificate.getTags().clear();
                for (String tagName : tags) {
                    certificate.addTag(new Tag(tagName));
                }
            }
            giftCertificateDAO.update(certificate);
            return certificate;
        }
    }

    @Override
    public void delete(long id) {
        giftCertificateDAO.delete(id);
    }

    @Override
    public GiftCertificate find(long id) {
        return giftCertificateDAO.findOne(id);
    }

    @Override
    public List<GiftCertificate> findByQuery(Map<String, String> query) {
        return giftCertificateDAO.findByQuery(query);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateDAO.findAll();
    }
}
