package ch.uzh.ifi.hase.soprafs21.exceptions;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message){
        super(message);
    }
}
