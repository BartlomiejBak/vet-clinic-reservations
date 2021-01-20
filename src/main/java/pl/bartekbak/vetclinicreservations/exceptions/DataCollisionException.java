package pl.bartekbak.vetclinicreservations.exceptions;

public class DataCollisionException extends RuntimeException {

    public DataCollisionException(String message) {
        super(message);
    }
}
