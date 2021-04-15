package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public GiftCertificate update(GiftCertificate certificateDto, Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        GiftCertificate certificateInDB = certificateOptional.get();

        if (certificateDto.getName()!=null) {
            certificateInDB.setName(certificateDto.getName());
        }
        if (certificateDto.getDescription()!=null) {
            certificateInDB.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice()!=null) {
            certificateInDB.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration()!=null) {
            certificateInDB.setDuration(certificateDto.getDuration());
        }
        if (!certificateDto.getTags().isEmpty()) {
            certificateInDB.getTags().clear();
            for (Tag tag : certificateDto.getTags()) {
                certificateInDB.addTag(tag);
            }
        }
        giftCertificateDAO.update(certificateInDB);
        return certificateInDB;
    }

    @Override
    public void delete(Long id) {
        giftCertificateDAO.delete(id);
    }

    @Override
    public GiftCertificate find(Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        return certificateOptional.get();
    }

    @Override
    public List<GiftCertificate> findByQuery(Query query) {
        return giftCertificateDAO.findByQuery(query);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateDAO.findAll();
    }
}
