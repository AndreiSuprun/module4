package com.epam.esm.service.validator;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for GiftCertificateDTO validation
 *
 * @author Andrei Suprun
 */
@Component
public class GiftCertificateDTOValidator {

    private final GiftCertificateDAO giftCertificateDAO;

    @Autowired
    public GiftCertificateDTOValidator(GiftCertificateDAO giftCertificateDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
    }

    /**
     * Check if input object is valid
     *
     * @param certificateDto object to validate
     * @param id id of updated object in repository
     * @throws ValidationException if validation is failed
     */
    public GiftCertificate validate(GiftCertificateDTO certificateDto, Long id){
        GiftCertificate certificateInDB = giftCertificateDAO.findOne(id);
        if (certificateInDB == null) {
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        if (certificateDto.getName() != null){
            GiftCertificate certificateByName = giftCertificateDAO.findByName(certificateDto.getName());
            if (certificateByName != null && !certificateByName.getId().equals(id)){
                throw new ValidationException(ErrorCode.CERTIFICATE_ALREADY_IN_DB, certificateDto.getName());
            }
        }
        return  certificateInDB;
    }
}
