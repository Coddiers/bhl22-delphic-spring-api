package pl.edu.wat.repo.api.services;

import java.io.IOException;
import pl.edu.wat.repo.api.dtos.response.SiteResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface SiteService {
    SiteResponse add(String url) throws IOException, EntityNotFoundException;

    SiteResponse get(String id) throws EntityNotFoundException;
}
