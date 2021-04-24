package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateValidator extends EntityValidator<GiftCertificate> {

    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "name";
    private final static String PRICE_FIELD = "price";
    private final static String DURATION_FIELD = "duration";

    @Override
    public void validate(GiftCertificate giftCertificate) {
        validateField(new NameValidator(),
                giftCertificate.getName(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                NAME_FIELD, giftCertificate.getName());
        validateField(new DescriptionValidator(),
                giftCertificate.getName(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                DESCRIPTION_FIELD, giftCertificate.getDescription());
        validateField(new PriceValidator(),
                giftCertificate.getPrice(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                PRICE_FIELD, giftCertificate.getPrice());
        validateField(new DurationValidator(),
                giftCertificate.getDuration(), ErrorCode.CERTIFICATE_FIELD_INVALID,
                DURATION_FIELD, giftCertificate.getDuration());
        }
}
