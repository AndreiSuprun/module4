package search;

import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchCriteriaBuilderTest {

    @InjectMocks
    private SearchCriteriaBuilder searchCriteriaBuilder;

    @Test
    void build() {
        String key = "name";
        String value = "Andrei";

        SearchCriteria searchCriteria = new SearchCriteria(key, SearchOperation.EQUALITY, value);
        String searchParameter = "name:Andrei";

       SearchCriteria actual = new SearchCriteriaBuilder(searchParameter).build().get(0);

        assertTrue(actual.equals(searchCriteria));
    }
}