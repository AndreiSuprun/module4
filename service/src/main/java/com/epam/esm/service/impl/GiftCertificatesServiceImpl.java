package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.QueryMapper;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.QueryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        List<TagDTO> tags = giftCertificateDTO.getTags();
        giftCertificate = giftCertificateDAO.insert(giftCertificate);
        addTags(giftCertificate, tags);
        return find(giftCertificate.getId());
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO certificateDto, Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        GiftCertificate certificateInDB = certificateOptional.get();
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
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        GiftCertificate certificateInDB = certificateOptional.get();
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
        find(id);
        giftCertificateDAO.clearTags(id);
        giftCertificateDAO.delete(id);
    }

    @Override
    public GiftCertificateDTO find(Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        GiftCertificate certificate = certificateOptional.get();
        List<Tag> tags = giftCertificateDAO.getTags(certificate);
        certificate.setTags(tags);
        return mapper.mapEntityToDTO(certificate);
    }

    @Override
    public List<GiftCertificateDTO> findByQuery(QueryDTO queryDTO) {
        Query query = queryMapper.mapDtoToEntity(queryDTO);
        queryValidator.validate(query);
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findByQuery(query);
        if (giftCertificates.isEmpty()) {
            throw new ProjectException(ErrorCode.CERTIFICATES_NOT_FOUND);
        }
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = giftCertificateDAO.getTags(giftCertificate);
            giftCertificate.setTags(tags);
        }
        return giftCertificates.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findAll();
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = giftCertificateDAO.getTags(giftCertificate);
            giftCertificate.setTags(tags);
        }
        return giftCertificates.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    private void addTags(GiftCertificate giftCertificate, List<TagDTO> tags) {
        if (tags != null && giftCertificate != null) {
            for (TagDTO tagDTO : tags) {
                if (!tagService.exist(tagDTO)) {
                    tagService.add(tagDTO);
                }
                giftCertificateDAO.addTag(giftCertificate, tagMapper.mapDtoToEntity(tagService.findByName(tagDTO.getName())));
            }
        }
    }
}
