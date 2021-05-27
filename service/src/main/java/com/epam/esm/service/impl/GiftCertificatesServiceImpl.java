package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.GiftCertificateDTOValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private final static String AUDIT = "audit";
    private final static String TAGS = "tags";

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final GiftCertificateMapper mapper;
    private final GiftCertificateValidator validator;
    private final GiftCertificateDTOValidator certificateDTOValidator;
    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificatesServiceImpl(CertificateRepository certificateRepository, TagService tagService,
                                       GiftCertificateMapper mapper, GiftCertificateValidator validator,
                                       GiftCertificateDTOValidator certificateDTOValidator, TagMapper tagMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.mapper = mapper;
        this.validator = validator;
        this.certificateDTOValidator = certificateDTOValidator;
        this.tagMapper = tagMapper;
    }

    @Transactional
    @Override
    public GiftCertificateDTO add(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = mapper.mapDtoToEntity(giftCertificateDTO);
        validator.validate(giftCertificate);
        if (certificateRepository.findByName(giftCertificate.getName()).isPresent()){
            throw new ValidationException(ErrorCode.CERTIFICATE_ALREADY_IN_DB, giftCertificate.getName());
        }
        giftCertificate = certificateRepository.save(giftCertificate);
        return mapper.mapEntityToDTO(giftCertificate);
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO certificateDto, Long id) {
        GiftCertificate certificateInDB = certificateDTOValidator.validate(certificateDto, id);
        GiftCertificate certificateInRequest = mapper.mapDtoToEntity(certificateDto);
        validator.validate(certificateInRequest);
        BeanUtils.copyProperties(certificateInRequest, certificateInDB, AUDIT, TAGS);
        certificateInDB.setTags(certificateInRequest.getTags());
        certificateRepository.addTags(certificateInDB);
        certificateInDB = certificateRepository.save(certificateInDB);
        return mapper.mapEntityToDTO(certificateInDB);
    }

    @Transactional
    @Override
    public GiftCertificateDTO patch(GiftCertificateDTO certificateDto, Long id) {
        GiftCertificate certificateInDB = certificateDTOValidator.validate(certificateDto, id);
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
        if (certificateDto.getTags() != null) {
            List<Tag> tags = certificateDto.getTags().stream().
                    map(tagMapper::mapDtoToEntity).
                    collect(Collectors.toList());
            certificateInDB.setTags(tags);
        }
        validator.validate(certificateInDB);
        certificateRepository.addTags(certificateInDB);
        certificateInDB = certificateRepository.save(certificateInDB);
        return mapper.mapEntityToDTO(certificateInDB);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if(!certificateRepository.existsById(id)){
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        if (!certificateRepository.findOrdersForCertificates(id).isEmpty()){
            throw new ValidationException(ErrorCode.CERTIFICATE_CANNOT_BE_DELETED, id);
        }
        certificateRepository.deleteById(id);
    }

    @Override
    public GiftCertificateDTO find(Long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new ValidationException(ErrorCode.CERTIFICATE_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(certificateOptional.get());
    }

    @Override
    public Page<GiftCertificateDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, Pageable pageable) {
        Page<GiftCertificate> certificates = certificateRepository.findByQuery(searchParams, orderParams, pageable);
        return certificates.map(mapper::mapEntityToDTO);
    }
}
