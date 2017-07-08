package ve.com.abicelis.chefbuddy.database.exceptions;

/**
 * Created by abicelis on 7/7/2017.
 */

public class CouldNotUpdateDataException extends Exception {

    private static final String DEFAULT_MESSAGE = "Data could not be updated on the database.";

    public CouldNotUpdateDataException() {
        super(DEFAULT_MESSAGE);
    }
    public CouldNotUpdateDataException(String message) {
        super(message);
    }
    public CouldNotUpdateDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
