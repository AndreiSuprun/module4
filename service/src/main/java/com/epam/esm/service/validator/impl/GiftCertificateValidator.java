package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GiftCertificateValidator extends EntityValidator<GiftCertificate> {

    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "name";
    private final static String PRICE_FIELD = "price";
    private final static String DURATION_FIELD = "duration";

    @Autowired
    private TagValidator tagValidator;

    @Override
    public void validate(GiftCertificate giftCertificate) {
        validateField(new NameValidator(),
                giftCertificate.getName(),
                NAME_FIELD, giftCertificate.getName());
        validateField(new DescriptionValidator(),
                giftCertificate.getName(),
                DESCRIPTION_FIELD, giftCertificate.getDescription());
        validateField(new PriceValidator(),
                giftCertificate.getPrice(),
                PRICE_FIELD, giftCertificate.getPrice().toString());
        validateField(new DurationValidator(),
                giftCertificate.getDuration(),
                DURATION_FIELD, giftCertificate.getDuration().toString());
        validateField(new DurationValidator(),
                giftCertificate.getDuration(),
                DURATION_FIELD, giftCertificate.getDuration().toString());
        List<Tag> tags = giftCertificate.getTags();
        for (Tag tag : tags){
            tagValidator.validate(tag);
        }
    }
}
