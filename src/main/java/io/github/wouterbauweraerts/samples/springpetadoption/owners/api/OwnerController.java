package io.github.wouterbauweraerts.samples.springpetadoption.owners.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.OwnerService;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.AddOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.UpdateOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public Page<OwnerResponse> listOwners(Pageable pageable) {
        return ownerService.getOwners(pageable);
    }

    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Integer ownerId) {
        return ResponseEntity.notFound().build();
    }

    public OwnerResponse addOwner(AddOwnerRequest request) {
        return null;
    }

    public void updateOwner(UpdateOwnerRequest request) {
    }
}
