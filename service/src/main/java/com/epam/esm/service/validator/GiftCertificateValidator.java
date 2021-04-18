package com.epam.esm.service.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.validator.impl.DescriptionValidator;
import com.epam.esm.service.validator.impl.DurationValidator;
import com.epam.esm.service.validator.impl.NameValidator;
import com.epam.esm.service.validator.impl.PriceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
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
    protected void validateObject(List<String> invalidFields, Object object) {
        if (!(object instanceof GiftCertificate)) {
            throw new IllegalArgumentException("The object has not Periodical type");
        }
        GiftCertificate giftCertificate = (GiftCertificate) object;
        validateField(new NameValidator(),
                giftCertificate.getName(),
                invalidFields, NAME_FIELD, giftCertificate.getName());
        validateField(new DescriptionValidator(),
                giftCertificate.getName(),
                invalidFields, DESCRIPTION_FIELD, giftCertificate.getDescription());
        validateField(new PriceValidator(),
                giftCertificate.getPrice(),
                invalidFields, PRICE_FIELD, giftCertificate.getPrice().toString());
        validateField(new DurationValidator(),
                giftCertificate.getDuration(),
                invalidFields, DURATION_FIELD, giftCertificate.getDuration().toString());
        validateField(new DurationValidator(),
                giftCertificate.getDuration(),
                invalidFields, DURATION_FIELD, giftCertificate.getDuration().toString());
        List<Tag> tags = giftCertificate.getTags();
        for (Tag tag : tags){
            tagValidator.validateObject(invalidFields, tag);
        }
    }
}
