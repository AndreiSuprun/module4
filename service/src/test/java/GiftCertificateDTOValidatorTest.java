import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.GiftCertificateDTOValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GiftCertificateDTOValidatorTest {

    @InjectMocks
    private GiftCertificateDTOValidator certificateDTOValidator;
    @Mock
    private CertificateRepository giftCertificateDAO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateCertificateCorrectTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        when(giftCertificateDAO.findById(id)).thenReturn(Optional.of(certificateInDB));
        when(giftCertificateDAO.findByName(expectedDTO.getName())).thenReturn(Optional.empty());

        GiftCertificate actual = certificateDTOValidator.validate(expectedDTO, id);

        assertEquals(certificateInDB, actual);
        verify(giftCertificateDAO, times(1)).findById(id);
        verify(giftCertificateDAO, times(1)).findByName(expectedDTO.getName());
    }

    @Test
    void updateCertificateNotFoundTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        doThrow(ValidationException.class).when(giftCertificateDAO).findById(id);
        when(giftCertificateDAO.findByName(expectedDTO.getName())).thenReturn(null);

        assertThrows(ValidationException.class, () -> certificateDTOValidator.validate(expectedDTO, id));

        verify(giftCertificateDAO, times(1)).findById(id);
        verify(giftCertificateDAO, never()).findByName(expectedDTO.getName());
    }

    @Test
    void updateCertificateAlreadyInDBTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        when(giftCertificateDAO.findById(id)).thenReturn(Optional.of(certificateInDB));
        doThrow(ValidationException.class).when(giftCertificateDAO).findByName(expectedDTO.getName());
        assertThrows(ValidationException.class, () -> certificateDTOValidator.validate(expectedDTO, id));

        verify(giftCertificateDAO, times(1)).findById(id);
        verify(giftCertificateDAO, times(1)).findByName(expectedDTO.getName());
    }
}