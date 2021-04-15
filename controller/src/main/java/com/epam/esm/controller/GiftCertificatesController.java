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
        return giftCertificatesService.find(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        giftCertificatesService.delete(id);
    }

    @PatchMapping("/{id}")
    public GiftCertificate update(@RequestBody GiftCertificate updatedCertificate, @PathVariable @Min(value = 1, message = "{id.minvalue}") Long id){
        return giftCertificatesService.update(updatedCertificate, id);
    }
}
