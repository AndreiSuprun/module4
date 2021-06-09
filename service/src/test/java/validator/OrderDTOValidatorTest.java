package validator;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.OrderDTOValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDTOValidatorTest {

    @InjectMocks
    private OrderDTOValidator orderDTOValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateCorrectTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(20));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        orderDTO.setUser(userDTO);
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1L);
        certificateDTO.setPrice(BigDecimal.valueOf(10));
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setGiftCertificateDTO(certificateDTO);
        orderItemDTO.setQuantity(2);
        orderDTO.setCertificates(Lists.list(orderItemDTO));

        assertDoesNotThrow(() -> { orderDTOValidator.validate(orderDTO); });
    }

    @Test
    void validateNullUserTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(20));
        orderDTO.setUser(null);
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1L);
        certificateDTO.setPrice(BigDecimal.valueOf(10));
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setGiftCertificateDTO(certificateDTO);
        orderItemDTO.setQuantity(2);
        orderDTO.setCertificates(Lists.list(orderItemDTO));

        assertThrows(ValidationException.class, () -> {
            orderDTOValidator.validate(orderDTO);
        });
    }

    @Test
    void validateNullOrderItemsTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(20));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        orderDTO.setUser(userDTO);
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1L);
        certificateDTO.setPrice(BigDecimal.valueOf(10));
        orderDTO.setCertificates(Lists.emptyList());

        assertThrows(ValidationException.class, () -> {
            orderDTOValidator.validate(orderDTO);
        });
    }

    @Test
    void validateNullCertificateTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(20));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        orderDTO.setUser(userDTO);
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setGiftCertificateDTO(null);
        orderItemDTO.setQuantity(2);
        orderDTO.setCertificates(Lists.list(orderItemDTO));

        assertThrows(ValidationException.class, () -> {
            orderDTOValidator.validate(orderDTO);
        });
    }
}