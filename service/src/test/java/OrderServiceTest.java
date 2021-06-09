import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.mapper.impl.OrderItemMapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.validator.OrderDTOValidator;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private OrderRepository orderDAO;
    @Mock
    private OrderMapper mapper;
    @Mock
    private OrderItemValidator orderItemValidator;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderDTOValidator orderDTOValidator;

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

        doNothing().when(orderDTOValidator).validate(orderDTO);
        when(userService.find(orderDTO.getUser().getId())).thenReturn(userDTO);
        when(giftCertificatesService.find(orderDTO.getCertificates().get(0).getGiftCertificateDTO().getId())).
                thenReturn(certificateDTO);
        when(mapper.mapDtoToEntity(orderDTO)).thenReturn(order);
        when(orderDAO.save(order)).thenReturn(order);
        when(orderItemMapper.mapDtoToEntity(orderItemDTO)).thenReturn(orderItem);
        doThrow(ValidationException.class).when(orderItemValidator).validate(order.getOrderCertificates().get(0));

        assertThrows(ValidationException.class, () -> {
            orderService.createOrder(orderDTO);
        });
        verify(orderDAO, times(1)).save(any(Order.class));
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

        doNothing().when(orderDTOValidator).validate(orderDTO);
        when(userService.find(orderDTO.getUser().getId())).thenReturn(userDTO);
        when(giftCertificatesService.find(orderDTO.getCertificates().get(0).getGiftCertificateDTO().getId())).
                thenReturn(certificateDTO);
        when(mapper.mapDtoToEntity(orderDTO)).thenReturn(order);
        when(orderDAO.save(order)).thenReturn(order);
        when(orderItemMapper.mapDtoToEntity(orderItemDTO)).thenReturn(orderItem);
        doNothing().when(orderItemValidator).validate(order.getOrderCertificates().get(0));
        when(orderDAO.save(order)).thenReturn(order);
        when(mapper.mapEntityToDTO(order)).thenReturn(orderDTO);
        OrderDTO actual = orderService.createOrder(orderDTO);
        assertEquals(orderDTO, actual);
        verify(orderDAO, times(2)).save(any(Order.class));
    }

    @Test
    void deleteCorrectTest() {
        Long id = 1L;
        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(20));

        doNothing().when(orderDAO).delete(order);
        orderService.delete(id);

        verify(orderDAO, times(1)).delete(order);
    }

    @Test
    void deleteNotCorrectTest() {
        Order order = new Order();

        doNothing().when(orderDAO).delete(order);

        assertThrows(ValidationException.class, () -> {
            orderService.delete(order.getId());
        });
        verify(orderDAO, times(1)).delete(order);
    }

    @Test
    void findNotCorrectTest() {
        Long id = 1L;

        when(orderDAO.findById(id)).thenReturn(null);

        assertThrows(ValidationException.class, () -> {
            orderService.find(id);
        });
        verify(orderDAO, times(1)).findById(id);
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

        when(orderDAO.findById(id)).thenReturn(Optional.of(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        OrderDTO actual = orderService.find(id);

        assertEquals(expectedDTO, actual);
        verify(orderDAO, times(1)).findById(id);
    }

    @Test
    void findByQueryTest() {
        Long id = 1L;
        Order expected = new Order();
        expected.setTotalPrice(BigDecimal.valueOf(20));
        expected.setId(id);
        Page<Order> pageable = new PageImpl<>(Lists.list(expected));
        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setTotalPrice(BigDecimal.valueOf(20));
        expectedDTO.setId(id);
        Page<OrderDTO> pageableDTO = new PageImpl<>(Lists.list(expectedDTO));

        when(orderDAO.findByQuery(null, null, Pageable.unpaged())).
                thenReturn(pageable);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        Page<OrderDTO> actual = orderService.findByQuery(null, null, Pageable.unpaged());

        assertEquals(pageableDTO, actual);
        verify(orderDAO, times(1)).findByQuery(null, null, Pageable.unpaged());
    }

    @Test
    void findByUserTest() {
        Long id = 1L;
        SearchCriteria searchCriteria = new SearchCriteria("user_id", SearchOperation.EQUALITY, id);
        searchCriteria.setNestedProperty(true);
        List<SearchCriteria> searchParams = Stream.of(searchCriteria).collect(Collectors.toList());
        Order expected = new Order();
        expected.setTotalPrice(BigDecimal.valueOf(20));
        expected.setId(id);
        Page<Order> pageable = new PageImpl<>(Lists.list(expected));
        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setTotalPrice(BigDecimal.valueOf(20));
        expectedDTO.setId(id);
        Page<OrderDTO> pageableDTO = new PageImpl<>(Lists.list(expectedDTO));

        when(userService.find(id)).thenReturn(new UserDTO());
        when(orderDAO.findByQuery(searchParams, null, Pageable.unpaged())).
                thenReturn(pageable);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        Page<OrderDTO> actual = orderService.findByUser(id, Pageable.unpaged());

        assertEquals(pageableDTO, actual);
        verify(orderDAO, times(1)).findByQuery(searchParams, null, Pageable.unpaged());
    }
}