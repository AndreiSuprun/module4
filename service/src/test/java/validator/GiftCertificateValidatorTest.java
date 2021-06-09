package validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.impl.DescriptionValidator;
import com.epam.esm.service.validator.impl.DurationValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.NameValidator;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import com.epam.esm.service.validator.impl.PriceValidator;
import com.epam.esm.service.validator.impl.QuantityValidator;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GiftCertificateValidatorTest {

    @InjectMocks
    private GiftCertificateValidator giftCertificateValidator;
    @Mock
    private NameValidator nameValidator;
    @Mock
    private DescriptionValidator descriptionValidator;
    @Mock
    private PriceValidator priceValidator;
    @Mock
    private DurationValidator durationValidator;
    @Mock
    private TagValidator tagValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateCorrectTest() {

        GiftCertificate giftCertificate = new GiftCertificate("Certificate", "Certificate description",
                BigDecimal.valueOf(2), 30, null);
        Tag tag = new Tag("Tag");
        giftCertificate.addTag(tag);

        when(nameValidator.isValid(giftCertificate.getName())).thenReturn(true);
        when(descriptionValidator.isValid(giftCertificate.getDescription())).thenReturn(true);
        when(priceValidator.isValid(giftCertificate.getPrice())).thenReturn(true);
        when(durationValidator.isValid(giftCertificate.getDuration())).thenReturn(true);
        doNothing().when(tagValidator).validate(giftCertificate.getTags().get(0));
        giftCertificateValidator.validate(giftCertificate);
        verify(nameValidator).isValid(giftCertificate.getName());
    }

    @Test
    void validatorNotCorrectTest() {
        GiftCertificate giftCertificate = new GiftCertificate("Certificate", "Certificate description",
                BigDecimal.valueOf(2), 30, null);

        when(nameValidator.isValid(giftCertificate.getName())).thenReturn(true);
        when(descriptionValidator.isValid(giftCertificate.getDescription())).thenReturn(true);
        when(priceValidator.isValid(giftCertificate.getPrice())).thenReturn(true);
        when(durationValidator.isValid(giftCertificate.getDuration())).thenReturn(true);
        assertThrows(ValidationException.class,() -> {giftCertificateValidator.validate(giftCertificate);});
        verify(nameValidator).isValid(giftCertificate.getName());
        verify(durationValidator).isValid(giftCertificate.getDuration());
    }
}