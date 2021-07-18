package cse41321.containers;
// Extends RuntimeException instead of Exception since that's the
// convention set by NoSuchElementException.
class DuplicateElementException extends RuntimeException {
    public DuplicateElementException() {
    }

    public DuplicateElementException(String message) {
        super(message);
    }

    public DuplicateElementException(Throwable cause) {
        super(cause);
    }

    public DuplicateElementException(String message, Throwable cause) {
        super(message, cause);
    }
}

