package mx.org.kaana.libs.facturama.models.request.complements.iedu;

public class EducationalInstitution {

	private String StudentsName;
	private String Curp;
	private String EducationLevel;
	private String AutRvoe;
	private String PaymentRfc;

	public String getStudentsName() {
		return StudentsName;
	}

	public void setStudentsName(String StudentsName) {
		this.StudentsName = StudentsName;
	}

	public String getCurp() {
		return Curp;
	}

	public void setCurp(String Curp) {
		this.Curp = Curp;
	}

	public String getEducationLevel() {
		return EducationLevel;
	}

	public void setEducationLevel(String EducationLevel) {
		this.EducationLevel = EducationLevel;
	}

	public String getAutRvoe() {
		return AutRvoe;
	}

	public void setAutRvoe(String AutRvoe) {
		this.AutRvoe = AutRvoe;
	}

	public String getPaymentRfc() {
		return PaymentRfc;
	}

	public void setPaymentRfc(String PaymentRfc) {
		this.PaymentRfc = PaymentRfc;
	}

}
