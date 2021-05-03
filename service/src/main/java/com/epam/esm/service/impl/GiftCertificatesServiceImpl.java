package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.QueryMapper;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.search.OrderCriteria;
import com.epam.esm.service.search.SearchCriteria;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.QueryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagService tagService;
    private final GiftCertificateMapper mapper;
    private final GiftCertificateValidator validator;
    private final TagMapper tagMapper;
    private final QueryMapper queryMapper;
    private final QueryValidator queryValidator;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificateDAO giftCertificateDAO, TagService tagService,
                                       GiftCertificateMapper mapper, GiftCertificateValidator validator,
                                       TagMapper tagMapper, QueryMapper queryMapper, QueryValidator queryValidator) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagService = tagService;
        this.mapper = mapper;
        this.validator = validator;
        this.tagMapper = tagMapper;
        this.queryMapper = queryMapper;
        this.queryValidator = queryValidator;
    }

    @Transactional
    @Override
    public GiftCertificateDTO add(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = mapper.mapDtoToEntity(giftCertificateDTO);
        validator.validate(giftCertificate);
        if (giftCertificateDAO.findByName(giftCertificate.getName()).isPresent()){
            throw new ProjectException(ErrorCode.CERTIFICATE_ALREADY_IN_DB, giftCertificate.getName());
        }
        List<TagDTO> tags = giftCertificateDTO.getTags();
        giftCertificate = giftCertificateDAO.insert(giftCertificate);
        addTags(giftCertificate, tags);
        return find(giftCertificate.getId());
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO certificateDto, Long id) {
        GiftCertificate certificateInDB = checkCertificateAlreadyInDb(certificateDto, id);
        if (certificateDto.getTags() != null) {
            giftCertificateDAO.clearTags(certificateInDB.getId());
        }
        GiftCertificate certificateInRequest = mapper.mapDtoToEntity(certificateDto);
        validator.validate(certificateInRequest);
        certificateInDB = giftCertificateDAO.update(certificateInRequest, id);
        if (certificateDto.getTags() != null) {
            giftCertificateDAO.clearTags(certificateInDB.getId());
            addTags(certificateInDB, certificateDto.getTags());
        }
        return find(id);
    }

    @Transactional
    @Override
    public GiftCertificateDTO patch(GiftCertificateDTO certificateDto, Long id) {
        GiftCertificate certificateInDB = checkCertificateAlreadyInDb(certificateDto, id);
        if (certificateDto.getName() != null) {
            certificateInDB.setName(certificateDto.getName());
        }
        if (certificateDto.getDescription() != null) {
            certificateInDB.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice() != null) {
            certificateInDB.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration() != null) {
            certificateInDB.setDuration(certificateDto.getDuration());
        }
        validator.validate(certificateInDB);
        certificateInDB = giftCertificateDAO.update(certificateInDB, id);
        if (certificateDto.getTags() != null) {
            giftCertificateDAO.clearTags(certificateInDB.getId());
            addTags(certificateInDB, certificateDto.getTags());
        }
        return find(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        giftCertificateDAO.delete(id);
    }

    @Override
    public GiftCertificateDTO find(Long id) {
        GiftCertificate certificate = giftCertificateDAO.findOne(id);
        if (certificate == null) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(certificate);
    }

    @Override
    public List<GiftCertificateDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, PaginationDTO paginationDTO) {
        if (paginationDTO.getPage() == null || paginationDTO.getPage() <= 0){
            paginationDTO.setPage(PaginationDTO.FIRST_PAGE);
        }
        if (paginationDTO.getSize() == null || paginationDTO.getSize() <= 0){
            paginationDTO.setPage(PaginationDTO.DEFAULT_RECORDS_PER_PAGE);
        }
        List<GiftCertificate> users;
        Long count = giftCertificateDAO.count(searchParams);
        if(((long) (paginationDTO.getPage() - 1) * paginationDTO.getSize()) < count) {
            users = giftCertificateDAO.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        } else {
            throw new ProjectException(ErrorCode.BAD_REQUEST);
        }
        return users.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDTO> findAll(PaginationDTO paginationDTO) {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findAll(paginationDTO.getPage(), paginationDTO.getSize());
        return giftCertificates.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }
}
