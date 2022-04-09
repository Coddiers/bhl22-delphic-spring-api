package pl.edu.wat.repo.api.services;

import java.util.List;
import pl.edu.wat.repo.api.dtos.response.TextResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface TextService {
    TextResponse add(String value);

    TextResponse getNextToVerify() throws EntityNotFoundException;

    TextResponse setAsReal(String id) throws EntityNotFoundException;

    TextResponse setAsFake(String id) throws EntityNotFoundException;

    TextResponse get(String id) throws EntityNotFoundException;

    List<TextResponse> getAll();

    TextResponse getVerified(String id) throws EntityNotFoundException;
}
