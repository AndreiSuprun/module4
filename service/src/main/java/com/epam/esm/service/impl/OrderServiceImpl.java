package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.OrderItemMapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.validator.OrderDTOValidator;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificatesService certificatesService;
    private final OrderDTOValidator orderDTOValidator;
    private final OrderItemValidator orderItemValidator;
    private final OrderMapper mapper;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, GiftCertificatesService certificatesService,
                            OrderDTOValidator orderDTOValidator, OrderItemValidator orderItemValidator,
                            OrderMapper mapper, OrderItemMapper orderItemMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.certificatesService = certificatesService;
        this.orderDTOValidator = orderDTOValidator;
        this.orderItemValidator = orderItemValidator;
        this.mapper = mapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public Page<OrderDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                      Pageable pageable) {
        Page<Order> users = orderRepository.findByQuery(searchParams, orderParams, pageable);
        return users.map(mapper::mapEntityToDTO);
    }

    @Override
    public OrderDTO find(Long id) {
        Optional<Order> user = orderRepository.findById(id);
        return mapper.mapEntityToDTO(user.orElseThrow(() -> new ValidationException(ErrorCode.ORDER_NOT_FOUND, id)));
    }

    @Override
    public Page<OrderDTO> findByUser(Long userId, Pageable pageable) {
        if (userService.find(userId) == null) {
            throw new ValidationException(ErrorCode.USER_NOT_FOUND, userId);
        }
        return orderRepository.findByUserId(userId, pageable).map(mapper::mapEntityToDTO);
    }

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTOValidator.validate(orderDTO);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserDTO user = userService.findByUserName(userDetails.getUsername());
        BigDecimal totalPrice = orderDTO.getCertificates().stream().
                map(orderItemDTO -> certificatesService.find(orderItemDTO.getGiftCertificateDTO().getId()).getPrice().
                        multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setUser(user);
        List<OrderItemDTO> orderItemDTOList = orderDTO.getCertificates();
        Order order = mapper.mapDtoToEntity(orderDTO);
        orderRepository.save(order);
        orderItemDTOList.stream().
                map(orderItemMapper::mapDtoToEntity).
                peek(orderItemValidator::validate).
                peek(orderItem -> orderItem.setOrder(order)).
                forEach(order::addOrderCertificate);
        Order orderInDB = orderRepository.save(order);
        return mapper.mapEntityToDTO(orderInDB);
    }

    @Transactional
    @Override
    public OrderDTO createOrderWithUser(OrderDTO orderDTO) {
        orderDTOValidator.validateWithUser(orderDTO);
        UserDTO user = userService.find(orderDTO.getUser().getId());
        BigDecimal totalPrice = orderDTO.getCertificates().stream().
                map(orderItemDTO -> certificatesService.find(orderItemDTO.getGiftCertificateDTO().getId()).getPrice().
                        multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setUser(user);
        List<OrderItemDTO> orderItemDTOList = orderDTO.getCertificates();
        Order order = mapper.mapDtoToEntity(orderDTO);
        orderRepository.save(order);
        orderItemDTOList.stream().
                map(orderItemMapper::mapDtoToEntity).
                peek(orderItemValidator::validate).
                peek(orderItem -> orderItem.setOrder(order)).
                forEach(order::addOrderCertificate);
        Order orderInDB = orderRepository.save(order);
        return mapper.mapEntityToDTO(orderInDB);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if(!orderRepository.findById(id).isPresent()){
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        orderRepository.deleteById(id);
    }
}
