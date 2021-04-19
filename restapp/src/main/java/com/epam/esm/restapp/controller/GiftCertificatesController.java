package com.epam.esm.restapp.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.QueryUtil;
import com.epam.esm.service.dto.GiftCertificateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificatesController {

    private GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    @GetMapping
    public List<GiftCertificateDTO> getAll(@RequestParam(required = false) Map<String, String> params) {
        if (params.isEmpty()) {
            return giftCertificatesService.findAll();
        } else {
            return giftCertificatesService.findByQuery(new QueryUtil(params));
        }
    }

    @PostMapping
    public GiftCertificateDTO addGiftCertificate(@RequestBody GiftCertificateDTO newCertificate) {
        return giftCertificatesService.add(newCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificateDTO getOne(@PathVariable Long id) {
        return giftCertificatesService.find(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        giftCertificatesService.delete(id);
    }

    @PatchMapping("/{id}")
    public GiftCertificateDTO update(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id){
        return giftCertificatesService.update(updatedCertificateDTO, id);
    }
}