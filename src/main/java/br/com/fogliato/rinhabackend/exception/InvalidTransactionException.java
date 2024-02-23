package br.com.fogliato.rinhabackend.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException() {
        super("Invalid transaction. Balance cannot be higher/lower than the limit");
    }

    public InvalidTransactionException(String message) {
        super(message);
    }
}
