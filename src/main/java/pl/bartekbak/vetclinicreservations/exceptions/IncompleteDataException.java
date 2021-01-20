package pl.bartekbak.vetclinicreservations.exceptions;

public class IncompleteDataException extends RuntimeException {

    public IncompleteDataException(String message) {
        super(message);
    }
}
