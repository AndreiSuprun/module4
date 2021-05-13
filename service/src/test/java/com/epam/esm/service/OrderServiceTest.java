package com.epam.esm.service;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.*;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.mapper.impl.OrderItemMapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private GiftCertificatesService giftCertificatesService;
    @Mock
    private UserService userService;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private OrderMapper mapper;
    @Mock
    private OrderItemValidator orderItemValidator;
    @Mock
    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addNotCorrectTest() {
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

        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(20));
        User user = new User();
        user.setId(1L);
        order.setUser(user);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        certificate.setPrice(BigDecimal.valueOf(10));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(certificate);
        orderItem.setQuantity(2);
        order.setOrderCertificates(Lists.list(orderItem));

        when(userService.find(orderDTO.getUser().getId())).thenReturn(userDTO);
        when(giftCertificatesService.find(orderDTO.getCertificates().get(0).getGiftCertificateDTO().getId())).
                thenReturn(certificateDTO);
        when(mapper.mapDtoToEntity(orderDTO)).thenReturn(order);
        when(orderDAO.insert(order)).thenReturn(order);
        when(orderItemMapper.mapDtoToEntity(orderItemDTO)).thenReturn(orderItem);
        doThrow(ValidationException.class).when(orderItemValidator).validate(order.getOrderCertificates().get(0));

        assertThrows(ValidationException.class, () -> {
            orderService.placeOrder(orderDTO);
        });
        verify(orderDAO, times(1)).insert(any(Order.class));
    }

    @Test
    void addCorrectTest() {
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

        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(20));
        order.setId(1L);
        User user = new User();
        user.setId(1L);
        order.setUser(user);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        certificate.setPrice(BigDecimal.valueOf(10));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(certificate);
        orderItem.setQuantity(2);
        order.setOrderCertificates(Lists.list(orderItem));

        when(userService.find(orderDTO.getUser().getId())).thenReturn(userDTO);
        when(giftCertificatesService.find(orderDTO.getCertificates().get(0).getGiftCertificateDTO().getId())).
                thenReturn(certificateDTO);
        when(mapper.mapDtoToEntity(orderDTO)).thenReturn(order);
        when(orderDAO.insert(order)).thenReturn(order);
        when(orderItemMapper.mapDtoToEntity(orderItemDTO)).thenReturn(orderItem);
        doNothing().when(orderItemValidator).validate(order.getOrderCertificates().get(0));
        when(orderDAO.update(order, order.getId())).thenReturn(order);
        when(mapper.mapEntityToDTO(order)).thenReturn(orderDTO);
        OrderDTO actual = orderService.placeOrder(orderDTO);
        assertEquals(orderDTO, actual);
        verify(orderDAO, times(1)).insert(any(Order.class));
        verify(orderDAO, times(1)).update(any(Order.class), anyLong());
    }

    @Test
    void updateCorrectTest() {
        Long id = 1L;
        Order orderInDB = new Order();
        orderInDB.setTotalPrice(BigDecimal.valueOf(20));
        orderInDB.setId(id);
        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setTotalPrice(BigDecimal.valueOf(20));
        expectedDTO.setId(id);
        Order expected = new Order();
        expected.setTotalPrice(BigDecimal.valueOf(20));
        expected.setId(id);

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        when(orderDAO.findOne(expected.getId())).thenReturn(orderInDB);
        when(orderDAO.update(expected, id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        OrderDTO actual = orderService.update(expectedDTO, id);

        assertEquals(expectedDTO, actual);
        verify(orderDAO, times(1)).update(expected, id);
    }

    @Test
    void updateNotCorrectIdTest() {
        Long id = 2L;
        Order orderInDB = new Order();
        orderInDB.setTotalPrice(BigDecimal.valueOf(20));
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(30));

        when(orderDAO.findOne(id)).thenReturn(null);

        assertThrows(ValidationException.class, () -> {
            orderService.update(orderDTO, id);
        });
        verify(orderDAO, never()).update(any(Order.class), anyLong());
    }

    @Test
    void updateNotValidTest() {
        Long id = 1L;
        Order orderInDB = new Order();
        orderInDB.setTotalPrice(BigDecimal.valueOf(20));
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(BigDecimal.valueOf(30));
        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(-30));

        when(orderDAO.findOne(id)).thenReturn(null);
        when(mapper.mapDtoToEntity(orderDTO)).thenReturn(order);
        assertThrows(ValidationException.class, () -> {
            orderService.update(orderDTO, id);
        });
        verify(orderDAO, never()).update(any(Order.class), anyLong());
    }

    @Test
    void deleteCorrectTest() {
        Long id = 1L;
        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(20));

        when(orderDAO.delete(id)).thenReturn(true);
        orderService.delete(id);

        verify(orderDAO, times(1)).delete(id);
    }

    @Test
    void deleteNotCorrectTest() {
        Long id = 2L;

        when(orderDAO.delete(id)).thenReturn(false);

        assertThrows(ValidationException.class, () -> {
            orderService.delete(id);
        });
        verify(orderDAO, times(1)).delete(id);
    }

    @Test
    void findNotCorrectTest() {
        Long id = 1L;

        when(orderDAO.findOne(id)).thenReturn(null);

        assertThrows(ValidationException.class, () -> {
            orderService.find(id);
        });
        verify(orderDAO, times(1)).findOne(id);
    }

    @Test
    void findCorrectTest() {
        Long id = 1L;
        Order expected = new Order();
        expected.setTotalPrice(BigDecimal.valueOf(20));
        expected.setId(id);
        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setTotalPrice(BigDecimal.valueOf(20));
        expectedDTO.setId(id);

        when(orderDAO.findOne(id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        OrderDTO actual = orderService.find(id);

        assertEquals(expectedDTO, actual);
        verify(orderDAO, times(1)).findOne(id);
    }

    @Test
    void findByQueryTest() {
        Long id = 1L;
        Order expected = new Order();
        expected.setTotalPrice(BigDecimal.valueOf(20));
        expected.setId(id);
        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setTotalPrice(BigDecimal.valueOf(20));
        expectedDTO.setId(id);
        PaginationDTO paginationDTO = new PaginationDTO(1L, 10);

        when(orderDAO.findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize())).
                thenReturn(Lists.list(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        List<OrderDTO> actual = orderService.findByQuery(null, null, paginationDTO);

        assertEquals(Lists.list(expectedDTO), actual);
        verify(orderDAO, times(1)).findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize());
    }
}