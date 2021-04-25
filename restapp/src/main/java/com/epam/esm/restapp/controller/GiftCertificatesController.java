package com.epam.esm.restapp.controller;

import com.epam.esm.service.GiftCertificatesService;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests for gift certificates.
 *
 * @author Andrei Suprun
 */
@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificateService) {
        this.giftCertificatesService = giftCertificateService;
    }

    /**
     * Retrieves gift certificates from repository according to provided request parameters.
     *
     * @param tag (optional) request parameter for search by tag
     * @param contains (optional) request parameter for search by phrase contained in name or description of gift
     *                 certificate
     * @param order (optional) request parameter for sorting by name or date, ascending or descending
     * @throws ProjectException if provided query is not valid or gift certificates according to provided query
     * are not present in repository
     * @return List<GiftCertificate> list of gift certificates from repository according to provided query
     */
    @GetMapping
    public List<GiftCertificateDTO> getByQuery(@RequestParam(required = false) String tag,
                                               @RequestParam(required = false) String contains,
                                               @RequestParam(required = false) String order) {
            return giftCertificatesService.findByQuery(new QueryDTO(tag, contains, order));
    }

    /**
     * Adds gift certificate to repository according to provided dto object.
     *
     * @param newCertificate GiftCertificateDTO object on basis of which is created new gift certificate
     *                           in repository
     * @throws ProjectException if fields in provided GiftCertificateDTO object is not valid
     * @return GiftCertificateDTO gift certificate dto of created in repository gift certificate
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO addGiftCertificate(@RequestBody GiftCertificateDTO newCertificate) {
        return giftCertificatesService.add(newCertificate);
    }

    /**
     * Returns GiftCertificateDTO object for gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to find
     * @throws ProjectException if gift certificate with provided id is not present in repository
     * @return GiftCertificateDTO object og gift certificate with provided id in repository
     */
    @GetMapping("/{id}")
    public GiftCertificateDTO getOne(@PathVariable Long id) {
        return giftCertificatesService.find(id);
    }

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ProjectException if gift certificate with provided id is not present in repository
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        giftCertificatesService.delete(id);
    }

    /**
     * Updates gift certificate according to request body.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object according to which is necessary to update gift certificate
     *                              in repository
     * @param id id of updated gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     * id is not present in repository
     * @return GiftCertificateDTO gift certificate dto of updated gift certificate in repository
     */
    @PutMapping("/{id}")
    public GiftCertificateDTO update(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id){
        return giftCertificatesService.update(updatedCertificateDTO, id);
    }

    /**
     * Updates gift certificate fields according to provided in request body fields.
     *
     * @param updatedCertificateDTO GiftCertificateDTO object which consist fields to update
     * @param id id of updated gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     * id is not present in repository
     * @return GiftCertificateDTO gift certificate dto of updated certificate
     */
    @PatchMapping("/{id}")
    public GiftCertificateDTO patch(@RequestBody GiftCertificateDTO updatedCertificateDTO, @PathVariable Long id){
        return giftCertificatesService.patch(updatedCertificateDTO, id);
    }
}
