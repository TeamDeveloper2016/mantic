package mx.org.kaana.libs.facturama.models.exception;

import java.util.HashMap;
import java.util.Map;

public class ModelException {

	private String Message;
	private Map<String, String[]> ModelState = new HashMap<>();

	public String getMessage() {
		return Message;
	}

	public void putMessage(String message) {
		this.Message = message;
	}

	public Map<String, String[]> getDetails() {
		return ModelState;
	}

	public void putDetails(Map<String, String[]> details) {
		this.ModelState = details;
	}

}
