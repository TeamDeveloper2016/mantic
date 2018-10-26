package mx.org.kaana.libs.facturama.models.request;

import java.util.List;

public class CfdiRelation {

	private String Type;
	private List<CfdiRelations> Cfdis;

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public List<CfdiRelations> getCfdis() {
		return Cfdis;
	}

	public void setCfdis(List<CfdiRelations> Cfdis) {
		this.Cfdis = Cfdis;
	}

}
