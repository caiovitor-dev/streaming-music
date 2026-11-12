package dev.caiovitor.streamingmusic.exception;

public class SongAlreadyExists extends RuntimeException {
    public SongAlreadyExists(String message) {
        super(message);
    }
}
