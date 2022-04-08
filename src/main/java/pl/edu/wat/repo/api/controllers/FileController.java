package pl.edu.wat.repo.api.controllers;

import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.services.FileService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/file")
public class FileController {

    FileService fileService;

    @PostMapping()
    public ResponseEntity<FileResponse> uploadVideo(@RequestParam String title, @RequestParam MultipartFile file) throws IOException {
        try {
            return new ResponseEntity<>(fileService.saveVideo(file), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ByteArrayResource> get(@PathVariable String id) {
        try {
            FileResponse response = fileService.getFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getTitle() + "\"")
                    .body(new ByteArrayResource(response.getFile()));

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}