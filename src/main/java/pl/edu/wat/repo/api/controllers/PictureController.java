package pl.edu.wat.repo.api.controllers;

import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.services.PictureService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/picture")
public class PictureController {

    PictureService pictureService;

    @PostMapping()
    public ResponseEntity<PictureResponse> upload(@RequestBody MultipartFile file) {
        try {
            return new ResponseEntity<>(pictureService.add(file), HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("next")
    public ResponseEntity<PictureResponse> getNext() {
        try {
            return ResponseEntity.ok(pictureService.getNextToVerify());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PictureResponse> get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(pictureService.get(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<PictureResponse>> getAll() {
        return ResponseEntity.ok(pictureService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<PictureResponse> getVerified(String id) {
        try {
            return ResponseEntity.ok(pictureService.getVerified(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsReal/{id}")
    public ResponseEntity<PictureResponse> setAsReal(@PathVariable String id) {
        try {
            return ResponseEntity.ok(pictureService.setAsReal(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsFake/{id}")
    public ResponseEntity<PictureResponse> setAsFake(@PathVariable String id, @RequestParam MultipartFile file) {
        try {
            return ResponseEntity.ok(pictureService.setAsFake(id, List.of(file)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}