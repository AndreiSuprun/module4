package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryUtil;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
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

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificateDAO giftCertificateDAO, TagService tagService,
                                       GiftCertificateMapper mapper, GiftCertificateValidator validator) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagService = tagService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Transactional
    @Override
    public GiftCertificateDTO add(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = mapper.mapDtoToEntity(giftCertificateDTO);
        validator.validate(giftCertificate);
        List<TagDTO> tags = giftCertificateDTO.getTags();
        giftCertificate = giftCertificateDAO.insert(giftCertificate);
        for (TagDTO tagDTO : tags) {
            TagDTO tagInDB = tagService.findByName(tagDTO.getName());
            if (tagInDB == null) {
                tagInDB = tagService.add(tagDTO);
            }
            giftCertificateDAO.addTag(giftCertificate, mapper.mapTagDTOToEntity(tagInDB));
        }
        return mapper.mapEntityToDTO(giftCertificate);
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO certificateDto, Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDAO.findOne(id);
        if (!certificateOptional.isPresent()) {
            throw new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        GiftCertificate certificateInDB = certificateOptional.get();

        if (certificateDto.getName()!=null) {
            certificateInDB.setName(certificateDto.getName());
        }
        if (certificateDto.getDescription()!=null) {
            certificateInDB.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice()!=null) {
            certificateInDB.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration()!=null) {
            certificateInDB.setDuration(certificateDto.getDuration());
        }
        if (!certificateDto.getTags().isEmpty()) {
            certificateInDB.getTags().clear();
            for (TagDTO tagDTO : certificateDto.getTags()) {
                certificateInDB.addTag(mapper.mapTagDTOToEntity(tagDTO));
            }
        }
        validator.validate(certificateInDB);
        if (!certificateDto.getTags().isEmpty()) {
            giftCertificateDAO.clearTags(certificateInDB.getId());
        }
        giftCertificateDAO.update(certificateInDB);
        return mapper.mapEntityToDTO(certificateInDB);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (giftCertificateDAO.delete(id)){
            giftCertificateDAO.clearTags(id);
        }
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
    public List<GiftCertificateDTO> findByQuery(QueryUtil query) {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findByQuery(query.buildSQLQuery(), query.getQueryParams());
        if (!giftCertificates.isEmpty()) {
            return giftCertificates.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findAll();
        for (GiftCertificate giftCertificate : giftCertificates){
            List<Tag> tags = giftCertificateDAO.getTags(giftCertificate);
            giftCertificate.setTags(tags);
        }
        return giftCertificates.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }
}
