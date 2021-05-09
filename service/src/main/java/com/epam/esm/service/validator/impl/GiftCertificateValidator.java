package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateValidator extends EntityValidator<GiftCertificate> {

    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "name";
    private final static String PRICE_FIELD = "price";
    private final static String DURATION_FIELD = "duration";

    private final NameValidator nameValidator;
    private final DescriptionValidator descriptionValidator;
    private final PriceValidator priceValidator;
    private final DurationValidator durationValidator;
    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateValidator(NameValidator nameValidator, DescriptionValidator descriptionValidator,
                                    PriceValidator priceValidator, DurationValidator durationValidator,
                                    TagValidator tagValidator) {
        this.nameValidator = nameValidator;
        this.descriptionValidator = descriptionValidator;
        this.priceValidator = priceValidator;
        this.durationValidator = durationValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    public void validate(GiftCertificate giftCertificate) {
        validateField(nameValidator,
                giftCertificate.getName(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                NAME_FIELD, giftCertificate.getName());
        validateField(descriptionValidator,
                giftCertificate.getName(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                DESCRIPTION_FIELD, giftCertificate.getDescription());
        validateField(priceValidator,
                giftCertificate.getPrice(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                PRICE_FIELD, giftCertificate.getPrice());
        validateField(durationValidator,
                giftCertificate.getDuration(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                DURATION_FIELD, giftCertificate.getDuration());
        giftCertificate.getTags().forEach(tagValidator::validate);
    }
}
