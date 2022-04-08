package pl.edu.wat.repo.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.repo.api.services.SiteService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/site")
public class SiteController {

    SiteService siteService;

//    @PostMapping()
//    public ResponseEntity<SiteResponse> uploadVideo(@RequestParam String url) {
//        try {
//            return new ResponseEntity<>(siteService.add(url), HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
