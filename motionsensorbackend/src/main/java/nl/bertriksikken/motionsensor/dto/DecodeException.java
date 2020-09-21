package nl.bertriksikken.motionsensor.dto;

/**
 * Checked exception for indicating a decoding problem.
 */
public final class DecodeException extends Exception {

    private static final long serialVersionUID = 1L;

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(Throwable e) {
        super(e);
    }
    
}
