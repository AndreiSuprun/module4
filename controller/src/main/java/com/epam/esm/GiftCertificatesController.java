package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;
    private final String CREATE_DATE = "create_date";
    private final String LAST_UPDATE_DATE = "last_update_date";

    public GiftCertificatesController(@Autowired GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    @GetMapping("/gift_certificates")
    List<GiftCertificate> all() {
        return giftCertificatesService.findAll();
    }

    @PostMapping("/gift_certificates")
    GiftCertificate newGiftCertificate(@Valid @RequestBody GiftCertificate newCertificate) {
        return giftCertificatesService.add(newCertificate);
    }

    @GetMapping("/gift_certificates/{id}")
    GiftCertificate one(@PathVariable Long id) {
        return giftCertificatesService.find(id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
    }

    @DeleteMapping("/gift_certificates/{id}")
    void delete(@PathVariable Long id) {
        giftCertificatesService.delete(id);
    }

    @PutMapping("/gift_certificates/{id}")
    GiftCertificate update(@RequestBody Map<String, String> requestMap, @PathVariable Long id){
        if (requestMap.containsKey(CREATE_DATE) || requestMap.containsKey(CREATE))
        giftCertificatesService.update(requestMap, id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
    }
}
