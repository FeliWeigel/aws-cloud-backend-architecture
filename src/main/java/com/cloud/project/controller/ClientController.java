package com.cloud.project.controller;

import com.cloud.project.entity.ClientEntity;
import com.cloud.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientEntity>> listAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping("/new")
    public ClientEntity registerNewClient(@RequestBody ClientEntity clientEntity) {
        return clientService.createClient(clientEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable("id") Long id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable("id") Long id) {
        return clientService.deleteClient(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateClient(
            @PathVariable("id") Long id,
            @RequestBody ClientEntity clientUpdated
    ) {
        clientUpdated.setId(id);
        return clientService.updateClient(clientUpdated);
    }

    @PostMapping(
            value = "/{id}/profile-image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Object> uploadClientProfileImage(
            @PathVariable("id") Long clientId,
            @RequestParam(value = "image", required = true) MultipartFile image
    ) throws FileUploadException {
        return clientService.uploadProfileImage(clientId, image);
    }

}
