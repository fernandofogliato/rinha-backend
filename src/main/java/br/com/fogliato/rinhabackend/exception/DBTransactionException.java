package br.com.fogliato.rinhabackend.exception;

public class DBTransactionException extends RuntimeException {
    public DBTransactionException() {
        super("Row is locked.");
    }

    public DBTransactionException(String message) {
        super(message);
    }
}
