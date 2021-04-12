package com.epam.esm;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private GiftCertificateDAO giftCertificateDAO;
    private static String NAME = "name";
    private static String DESCRIPTION = "description";
    private static String PRICE = "price";
    private static String DURATION = "duration";
    private static String TAGS = "tags";
    private static String DELIMITER = ",";

    public GiftCertificatesServiceImpl(GiftCertificateDAO giftCertificateDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @Override
    public GiftCertificate add(GiftCertificate giftCertificate) {
        return giftCertificateDAO.insert(giftCertificate);
    }

    @Override
    public Optional<GiftCertificate> update(Map<String, String> map, long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
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
        }
        return certificateOptional;
    }

    @Override
    public void delete(long id) {
        giftCertificateDAO.delete(id);
    }

    @Override
    public Optional<GiftCertificate> find(long id) {
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
