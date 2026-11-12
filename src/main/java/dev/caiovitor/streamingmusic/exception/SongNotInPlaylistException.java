package dev.caiovitor.streamingmusic.exception;

public class SongNotInPlaylistException extends RuntimeException {
    public SongNotInPlaylistException(String message) {
        super(message);
    }
}
