package dev.caiovitor.streamingmusic.exception;

public class ArtistNotFoundException extends RuntimeException {
  public ArtistNotFoundException(String message) {
    super(message);
  }
}
