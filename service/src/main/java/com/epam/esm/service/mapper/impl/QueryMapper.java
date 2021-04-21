package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryMapper implements Mapper<Query, QueryDTO> {

    public Query mapDtoToEntity(QueryDTO queryDTO) {
        Query query = new Query();
        BeanUtils.copyProperties(queryDTO, query);
        return query;
    }

    public QueryDTO mapEntityToDTO(Query query) {
        QueryDTO queryDTO = new QueryDTO();
        BeanUtils.copyProperties(query, queryDTO);
        return queryDTO;
    }
}

