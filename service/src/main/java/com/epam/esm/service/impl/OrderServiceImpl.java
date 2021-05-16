package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.entity.Order;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.OrderItemMapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.validator.OrderDTOValidator;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    private final static String AUDIT = "audit";
    private static final String USER_ID = "user_id";

    private final OrderDAO orderDAO;
    private final UserService userService;
    private final GiftCertificatesService certificatesService;
    private final OrderDTOValidator orderDTOValidator;
    private final OrderItemValidator orderItemValidator;
    private final OrderMapper mapper;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, UserService userService, GiftCertificatesService certificatesService,
                            OrderDTOValidator orderDTOValidator, OrderItemValidator orderItemValidator,
                            OrderMapper mapper, OrderItemMapper orderItemMapper) {
        this.orderDAO = orderDAO;
        this.userService = userService;
        this.certificatesService = certificatesService;
        this.orderDTOValidator = orderDTOValidator;
        this.orderItemValidator = orderItemValidator;
        this.mapper = mapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public List<OrderDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        Long count = orderDAO.count(searchParams);
        checkPageNumber(paginationDTO, count);
        List<Order> orders = orderDAO.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        return orders.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO find(Long id) {
        Order order = orderDAO.findOne(id);
        if (order == null) {
            throw new ValidationException(ErrorCode.ORDER_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(order);
    }

    @Override
    public List<OrderDTO> findByUser(Long userId, PaginationDTO paginationDTO) {
        if (userService.find(userId) == null) {
            throw new ValidationException(ErrorCode.USER_NOT_FOUND, userId);
        }
        SearchCriteria searchCriteria = new SearchCriteria(USER_ID, SearchOperation.EQUALITY, userId);
        searchCriteria.setNestedProperty(true);
        List<SearchCriteria> searchParams = Stream.of(searchCriteria).collect(Collectors.toList());
        return findByQuery(searchParams, null, paginationDTO);
    }

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTOValidator.validate(orderDTO);
        UserDTO user = userService.find(orderDTO.getUser().getId());
        BigDecimal totalPrice = orderDTO.getCertificates().stream().
                map(orderItemDTO -> certificatesService.find(orderItemDTO.getGiftCertificateDTO().getId()).getPrice().
                        multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setUser(user);
        List<OrderItemDTO> orderItemDTOList = orderDTO.getCertificates();
        Order order = mapper.mapDtoToEntity(orderDTO);
        orderDAO.insert(order);
        orderItemDTOList.stream().
                map(orderItemMapper::mapDtoToEntity).
                peek(orderItemValidator::validate).
                peek(orderItem -> orderItem.setOrder(order)).
                forEach(order::addOrderCertificate);
        Order orderInDB = orderDAO.update(order, order.getId());
        return mapper.mapEntityToDTO(orderInDB);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if(!orderDAO.delete(id)){
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
    }
}
