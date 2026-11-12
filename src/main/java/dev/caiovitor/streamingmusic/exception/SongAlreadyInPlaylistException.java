package dev.caiovitor.streamingmusic.exception;

public class SongAlreadyInPlaylistException extends RuntimeException {
    public SongAlreadyInPlaylistException(String message) {
        super(message);
    }
}
