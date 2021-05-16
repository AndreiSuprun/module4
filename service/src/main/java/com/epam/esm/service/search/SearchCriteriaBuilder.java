package com.epam.esm.service.search;

import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.SearchOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class responsible for processing search parameter in request
 *
 * @author Andrei Suprun
 */

public class SearchCriteriaBuilder {

    private static final String DELIMITER = "|";
    private static final String COMA = ",";
    private List<SearchCriteria> params;
    private final String searchParameters;

    public SearchCriteriaBuilder(String searchParameters) {
        this.searchParameters = searchParameters;
    }

    /**
     * Adds SearchCriteria object to list of SearchCriteria for search request parameter.
     *
     * @param orPredicate boolean value that indicates to
     * @param key key key for searching
     * @param operation operation that is applied to search by key
     * @param value value for search by key using specified operation
     * @param prefix prefix for value for search by key
     * @param suffix suffix for value for search by key
     * @return SearchCriteriaBuilder this object for SearchCriteriaBuilder
     */
    public SearchCriteriaBuilder with(String orPredicate, String key, String operation, Object value,
                                            String prefix, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    /**
     * Parses search request parameter to SearchCriteria object and adds them to list of SearchCriteria for search request
     * parameter.
     *
     * @return List of SearchCriteria fot search request parameter
     */
    public List<SearchCriteria> build() {
        params = new ArrayList<>();
        String operationSet = String.join(DELIMITER, SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\p{Punct}?)([\\w_]+?)(" + operationSet + ")([*]?)([\\p{L}\\p{Digit}@.\\s]+?)([*]?),");
        Matcher matcher = pattern.matcher(searchParameters + COMA);
        while (matcher.find()) {
            with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), matcher.group(6));
        }
        return params;
    }

    /**
     * Adds existing SearchCriteria object to list of SearchCriteria for search request parameter.
     *
     * @param criteria SearchCriteria for serach request parameter to add to list of SearchCriteria
     * @return SearchCriteriaBuilder this object for SearchCriteriaBuilder
     */
    public SearchCriteriaBuilder with(SearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
