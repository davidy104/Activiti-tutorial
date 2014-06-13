package nz.co.activiti.tutorial;

@SuppressWarnings("serial")
public class GenericActivitiRestException extends Exception {

	public GenericActivitiRestException() {
		super();
	}

	public GenericActivitiRestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GenericActivitiRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenericActivitiRestException(String message) {
		super(message);
	}

	public GenericActivitiRestException(Throwable cause) {
		super(cause);
	}

}
