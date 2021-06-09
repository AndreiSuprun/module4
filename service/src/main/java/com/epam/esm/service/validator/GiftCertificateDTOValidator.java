package com.epam.esm.service.validator;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class for GiftCertificateDTO validation
 *
 * @author Andrei Suprun
 */
@Component
public class GiftCertificateDTOValidator {

    private final CertificateRepository giftCertificateDAO;

    @Autowired
    public GiftCertificateDTOValidator(CertificateRepository giftCertificateDAO) {
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
        Optional<GiftCertificate> certificateInDB = giftCertificateDAO.findById(id);
        if (!certificateInDB.isPresent()) {
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        if (certificateDto.getName() != null){
            Optional<GiftCertificate> certificateByName = giftCertificateDAO.findByName(certificateDto.getName());
            if (certificateByName.isPresent() && !certificateByName.get().getId().equals(id)){
                throw new ValidationException(ErrorCode.CERTIFICATE_ALREADY_IN_DB, certificateDto.getName());
            }
        }
        return  certificateInDB.get();
    }
}
