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
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.dtos.response.TextResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.services.TextService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/text")
public class TextController {

    TextService textService;

    @PostMapping()
    public ResponseEntity<TextResponse> upload(@RequestBody String value) {
        return new ResponseEntity<>(textService.add(value), HttpStatus.CREATED);
    }

    @GetMapping("next")
    public ResponseEntity<TextResponse> getNext() {
        try {
            return ResponseEntity.ok(textService.getNextToVerify());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<TextResponse> get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(textService.get(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<TextResponse>> getAll() {
        return ResponseEntity.ok(textService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<TextResponse> getVerified(String id) {
        try {
            return ResponseEntity.ok(textService.getVerified(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsReal/{id}")
    public ResponseEntity<TextResponse> setAsReal(@PathVariable String id) {
        try {
            return ResponseEntity.ok(textService.setAsReal(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsFake/{id}")
    public ResponseEntity<TextResponse> setAsFake(@PathVariable String id) {
        try {
            return ResponseEntity.ok(textService.setAsFake(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}