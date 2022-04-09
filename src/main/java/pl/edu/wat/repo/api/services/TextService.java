package pl.edu.wat.repo.api.services;

import java.io.IOException;
import pl.edu.wat.repo.api.dtos.response.TextResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface TextService {
    TextResponse add(String value);

    TextResponse getNextToVerify() throws EntityNotFoundException;

    TextResponse setAsReal(String id) throws EntityNotFoundException;

    TextResponse setAsFake(String id) throws EntityNotFoundException;

    TextResponse get(String id) throws EntityNotFoundException;
}
