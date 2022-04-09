package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.repo.api.dtos.response.TextResponse;
import pl.edu.wat.repo.api.entities.Text;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.TextRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TextServiceImpl implements TextService {

    TextRepository textRepository;

    @Override
    public TextResponse add(String value) {
        return TextResponse.from(
                textRepository.save(
                        Text.builder()
                                .value(value)
                                .build()));
    }

    @Override
    public TextResponse getNextToVerify() throws EntityNotFoundException {
        return textRepository.findFirstByVerifiedOrderByCreateDate(false)
                .map(TextResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Text.class));
    }

    @Override
    public TextResponse setAsReal(String id) throws EntityNotFoundException {
        Text text = textRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Text.class));

        text.setVerifiedDate(Instant.now());
        text.setVerified(true);
        text.setFake(false);
        return TextResponse.from(textRepository.save(text));
    }

    @Override
    public TextResponse setAsFake(String id) throws EntityNotFoundException {
        Text video = textRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Text.class));

        video.setVerifiedDate(Instant.now());
        video.setVerified(true);
        video.setFake(true);

        return TextResponse.from(textRepository.save(video));
    }

    @Override
    public TextResponse get(String id) throws EntityNotFoundException {
        return textRepository.findById(id)
                .map(TextResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Text.class));
    }

}
