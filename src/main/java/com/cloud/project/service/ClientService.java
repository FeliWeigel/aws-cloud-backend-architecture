package com.cloud.project.service;

import ch.qos.logback.core.net.server.Client;
import com.cloud.project.entity.ClientEntity;
import com.cloud.project.exception.ClientNotFoundException;
import com.cloud.project.exception.InvalidFileTypeException;
import com.cloud.project.repository.ClientJpaRepository;
import com.cloud.project.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientJpaRepository repository;
    private final S3Service s3Service;
    public ClientEntity createClient(ClientEntity clientData){
        return repository.save(clientData);
    }

    public ResponseEntity<Object> getClientById(Long clientId){
        Optional<ClientEntity> clientSaved = repository.findById(clientId);
        if(clientSaved.isPresent()){
            return ResponseEntity.ok(clientSaved);
        }
        return new ResponseEntity<>(new ClientNotFoundException("Client not found with id: " + clientId), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ClientEntity>> getAllClients(){
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Object> deleteClient(Long clientId){
        Optional<ClientEntity> clientSaved = repository.findById(clientId);
        if(!clientSaved.isPresent()){
            return new ResponseEntity<>(new ClientNotFoundException("Client not found with id: " + clientId), HttpStatus.NOT_FOUND);
        }
        repository.deleteById(clientId);
        return ResponseEntity.ok("Client deleted successfully!");
    }

    public ResponseEntity<Object> updateClient(ClientEntity newVersionClient){
        ClientEntity clientSaved = repository.findById(newVersionClient.getId())
                .orElse(null);

        if(clientSaved != null){
            clientSaved.setEmail(newVersionClient.getEmail());
            clientSaved.setFirstname(newVersionClient.getFirstname());
            clientSaved.setLastName(newVersionClient.getLastName());

            return ResponseEntity.ok(repository.save(clientSaved));
        }

        return new ResponseEntity<>(new ClientNotFoundException("Client not found with id: " + newVersionClient.getId()), HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<Object> uploadProfileImage(Long clientId, MultipartFile image) throws FileUploadException {
        ClientEntity client = repository.findById(clientId).orElse(null);

        String contentType = image.getContentType();
        if (!isContentTypeSupported(contentType)) {
            return new ResponseEntity<>(new InvalidFileTypeException("Unsupported file type: " + contentType), HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = "clients/" + client.getId() + "/profile_image/" + image.getOriginalFilename();
            s3Service.uploadFile(fileName, image.getInputStream(), image.getSize());
            String imageURL = s3Service.generatePresignedUrl(fileName);
            client.setProfileImageURL(imageURL);
            repository.save(client);
            return ResponseEntity.ok("Profile photo modified correctly!");
        }catch (IOException e) {
            return new ResponseEntity<>(new FileUploadException("Error uploading file", e), HttpStatus.NOT_FOUND);
        }
    }

    private Boolean isContentTypeSupported(String contentType){
        return contentType != null && (contentType.startsWith("image"));
    }
}
