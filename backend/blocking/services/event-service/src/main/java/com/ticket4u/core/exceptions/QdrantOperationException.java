package com.ticket4u.core.exceptions;

public class QdrantOperationException extends RuntimeException{
    public QdrantOperationException(String message) {
        super(message);
    }

    public QdrantOperationException(String action, String collection, String error) {
        super(String.format("Errol Qdrant [%s] in collection [%s]: %s", action, collection, error));
    }
}
