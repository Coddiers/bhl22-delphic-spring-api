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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.dtos.response.VideoResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.services.VideoService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/video")
public class VideoController {

    VideoService videoService;

    @PostMapping()
    public ResponseEntity<VideoResponse> upload(@RequestParam MultipartFile file) throws IOException {
        try {
            return new ResponseEntity<>(videoService.add(file), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("next")
    public ResponseEntity<VideoResponse> getNext() {
        try {
            return ResponseEntity.ok(videoService.getNextToVerify());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<VideoResponse> get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(videoService.get(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<VideoResponse>> getAll() {
            return ResponseEntity.ok(videoService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<VideoResponse> getVerified(String id) {
        try {
            return ResponseEntity.ok(videoService.getVerified(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsReal/{id}")
    public ResponseEntity<VideoResponse> setAsReal(@PathVariable String id) {
        try {
            return ResponseEntity.ok(videoService.setAsReal(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("setAsFake/{id}")
    public ResponseEntity<VideoResponse> setAsFake(@PathVariable String id, @RequestParam MultipartFile file) {
        try {
            return ResponseEntity.ok(videoService.setAsFake(id, List.of(file)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}