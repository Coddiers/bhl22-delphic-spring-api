package pl.edu.wat.repo.api.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class<?> claz) {
        super(claz.getName() + " not found");
    }
}
