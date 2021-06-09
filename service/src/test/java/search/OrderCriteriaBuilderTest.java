package search;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderCriteriaBuilderTest {

    @InjectMocks
    private OrderCriteriaBuilder orderCriteriaBuilder;

    @Test
    void build() {
        String orderParameter = "name,desc";
        String key = "name";
        String direction = "desc";
        OrderCriteria orderCriteria = new OrderCriteria("name", "desc");

        OrderCriteria actual = new OrderCriteriaBuilder(orderParameter).build().get(0);

        assertTrue(actual.equals(orderCriteria));
    }
}