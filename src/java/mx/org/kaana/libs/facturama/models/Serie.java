package mx.org.kaana.libs.facturama.models;

public class Serie {

	private String IdBranchOffice;
	private String Name;
	private String Description;
	private Long Folio;

	public String getIdBranchOffice() {
		return IdBranchOffice;
	}

	public void setIdBranchOffice(String IdBranchOffice) {
		this.IdBranchOffice = IdBranchOffice;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Long getFolio() {
		return Folio;
	}

	public void setFolio(Long Folio) {
		this.Folio = Folio;
	}

}
