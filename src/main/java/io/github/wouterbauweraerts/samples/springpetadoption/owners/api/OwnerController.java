package io.github.wouterbauweraerts.samples.springpetadoption.owners.api;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.OwnerService;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.AddOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.UpdateOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import jakarta.validation.Valid;

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

    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Integer ownerId) {
        return ResponseEntity.of(ownerService.getOwnerById(ownerId));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public OwnerResponse addOwner(@RequestBody @Valid AddOwnerRequest request) {
        return ownerService.addOwner(request);
    }

    public void updateOwner(UpdateOwnerRequest request) {
    }
}
