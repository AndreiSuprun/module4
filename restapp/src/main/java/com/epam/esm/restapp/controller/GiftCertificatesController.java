package com.epam.esm.restapp.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.GiftCertificateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    @GetMapping
    public List<GiftCertificateDTO> getByQuery(@RequestParam(required = false) String tag,
                                               @RequestParam(required = false) String contains,
                                               @RequestParam(required = false) String order) {
            return giftCertificatesService.findByQuery(new QueryDTO(tag, contains, order));
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

    @PutMapping("/{id}")
    public GiftCertificateDTO update(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id){
        return giftCertificatesService.update(updatedCertificateDTO, id);
    }

    @PatchMapping("/{id}")
    public GiftCertificateDTO patch(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id){
        return giftCertificatesService.patch(updatedCertificateDTO, id);
    }
}
