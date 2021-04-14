package com.epam.esm.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.controller.exception.NotFoundException;

import com.epam.esm.controller.exception.UnsupportedPatchOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@Validated
@RequestMapping("/gift_certificates")
public class GiftCertificatesController {

    private GiftCertificatesService giftCertificatesService;
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    @GetMapping
    public List<GiftCertificate> getAll() {
        return giftCertificatesService.findAll();
    }

    @PostMapping
    public GiftCertificate addGiftCertificate(@Valid @RequestBody GiftCertificate newCertificate) {
        return giftCertificatesService.add(newCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificate getOne(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        return giftCertificatesService.find(id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        giftCertificatesService.delete(id);
    }

    @PutMapping("/{id}")
    public GiftCertificate update(@RequestBody Map<String, String> requestMap, @PathVariable @Min(value = 1, message = "{id.minvalue}") Long id){
        if (requestMap.containsKey(CREATE_DATE) || requestMap.containsKey(LAST_UPDATE_DATE)){
            Set<String> fields = new HashSet<>();
            fields.add(CREATE_DATE);
            fields.add(LAST_UPDATE_DATE);
            throw new UnsupportedPatchOperationException("certificate.fields.notupdatable", fields);
        }
        return giftCertificatesService.update(requestMap, id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
    }
}
