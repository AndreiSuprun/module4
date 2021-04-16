package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.validator.impl.DescriptionValidator;
import com.epam.esm.service.validator.impl.DurationValidator;
import com.epam.esm.service.validator.impl.NameValidator;
import com.epam.esm.service.validator.impl.PriceValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public class GiftCertificateValidator extends EntityValidator {

    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "name";
    private final static String PRICE_FIELD = "price";
    private final static String DURATION_FIELD = "duration";


    private TagValidator tagValidator;

    public TagValidator getTagValidator() {
        return tagValidator;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    protected void validateObject(List<ProjectException> errors, Object object) {
        if (!(object instanceof GiftCertificateDTO)) {
            throw new IllegalArgumentException("The object has not Periodical type");
        }
        GiftCertificateDTO giftCertificateDTO = (GiftCertificateDTO) object;
        validateField(new NameValidator(),
                giftCertificateDTO.getName(),
                errors, NAME_FIELD, giftCertificateDTO.getName());
        validateField(new DescriptionValidator(),
                giftCertificateDTO.getName(),
                errors, DESCRIPTION_FIELD, giftCertificateDTO.getDescription());
        validateField(new PriceValidator(),
                giftCertificateDTO.getPrice(),
                errors, PRICE_FIELD, giftCertificateDTO.getPrice().toString());
        validateField(new DurationValidator(),
                giftCertificateDTO.getDuration(),
                errors, DURATION_FIELD, giftCertificateDTO.getDuration().toString());
        validateField(new DurationValidator(),
                giftCertificateDTO.getDuration(),
                errors, DURATION_FIELD, giftCertificateDTO.getDuration().toString());
        List<TagDTO> tags = giftCertificateDTO.getTags();
        for (TagDTO tagDTO : tags){
            tagValidator.validateObject(errors, tagDTO);
        }
    }
}
