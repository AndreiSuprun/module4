package com.epam.esm.service.validator;

import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Class for OrderDTO validation
 *
 * @author Andrei Suprun
 */
@Component
public class OrderDTOValidator {

    /**
     * Check if input object is valid
     *
     * @param orderDTO object to validate
     * @throws ValidationException if validation is failed
     */
    public void validate(OrderDTO orderDTO){
        if (orderDTO.getCertificates().isEmpty()){
            throw new ValidationException(ErrorCode.ORDER_ITEMS_NOT_ADDED);
        }
        for(OrderItemDTO orderItemDTO : orderDTO.getCertificates()){
            if (orderItemDTO.getGiftCertificateDTO() == null || orderItemDTO.getGiftCertificateDTO().getId() == null){
                throw new ValidationException(ErrorCode.CERTIFICATES_NOT_ADDED);
            }
        }
    }

    public void validateWithUser(OrderDTO orderDTO){
        if (orderDTO.getUser() == null || orderDTO.getUser().getId() == null){
            throw new ValidationException(ErrorCode.USER_NOT_ADDED);
        }
        if (orderDTO.getCertificates().isEmpty()){
            throw new ValidationException(ErrorCode.ORDER_ITEMS_NOT_ADDED);
        }
        for(OrderItemDTO orderItemDTO : orderDTO.getCertificates()){
            if (orderItemDTO.getGiftCertificateDTO() == null || orderItemDTO.getGiftCertificateDTO().getId() == null){
                throw new ValidationException(ErrorCode.CERTIFICATES_NOT_ADDED);
            }
        }
    }
}
