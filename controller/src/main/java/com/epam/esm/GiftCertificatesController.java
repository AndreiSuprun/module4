package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.NotFoundException;
<<<<<<< HEAD
=======
import com.epam.esm.exceptions.UnsupportedPatchOperationException;
>>>>>>> Initial commit
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
<<<<<<< HEAD
import java.util.List;
import java.util.Map;
=======
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
>>>>>>> Initial commit

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
<<<<<<< HEAD
    GiftCertificate one(@PathVariable Long id) {
=======
    GiftCertificate one(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
>>>>>>> Initial commit
        return giftCertificatesService.find(id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
    }

    @DeleteMapping("/gift_certificates/{id}")
<<<<<<< HEAD
    void delete(@PathVariable Long id) {
=======
    void delete(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
>>>>>>> Initial commit
        giftCertificatesService.delete(id);
    }

    @PutMapping("/gift_certificates/{id}")
<<<<<<< HEAD
    GiftCertificate update(@RequestBody Map<String, String> requestMap, @PathVariable Long id){
        if (requestMap.containsKey(CREATE_DATE) || requestMap.containsKey(CREATE))
        giftCertificatesService.update(requestMap, id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
=======
    GiftCertificate update(@RequestBody Map<String, String> requestMap, @PathVariable @Min(value = 1, message = "{id.minvalue}") Long id){
        if (requestMap.containsKey(CREATE_DATE) || requestMap.containsKey(LAST_UPDATE_DATE)){
            Set<String> fields = new HashSet<>();
            fields.add(CREATE_DATE);
            fields.add(LAST_UPDATE_DATE);
            throw new UnsupportedPatchOperationException("certificate.fields.notupdatable", fields);
        }
        return giftCertificatesService.update(requestMap, id).orElseThrow(() -> new NotFoundException("certificate.notfound", id));
>>>>>>> Initial commit
    }
}
