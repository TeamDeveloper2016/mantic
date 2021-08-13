package mx.org.kaana.libs.facturama.models.exception;

public class FacturamaException extends Exception {

  private static final long serialVersionUID = 8250882544661608458L;

	private ModelException Model;

	public FacturamaException(String message) {
		super(message);
	}

	public FacturamaException(String message, ModelException model) {
		super(message);

		this.Model = model;
	}

	public ModelException getModel() {
		return this.Model;
	}

}
