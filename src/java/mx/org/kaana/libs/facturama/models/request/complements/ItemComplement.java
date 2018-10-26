package mx.org.kaana.libs.facturama.models.request.complements;

import mx.org.kaana.libs.facturama.models.request.complements.iedu.EducationalInstitution;
import mx.org.kaana.libs.facturama.models.request.complements.terceros.ThirdPartyAccount;

public class ItemComplement {

	private EducationalInstitution EducationalInstitution;
	private ThirdPartyAccount ThirdPartyAccount;

	public EducationalInstitution getEducationalInstitution() {
		return EducationalInstitution;
	}

	public void setEducationalInstitution(EducationalInstitution EducationalInstitution) {
		this.EducationalInstitution = EducationalInstitution;
	}

	public ThirdPartyAccount getThirdPartyAccount() {
		return ThirdPartyAccount;
	}

	public void setThirdPartyAccount(ThirdPartyAccount ThirdPartyAccount) {
		this.ThirdPartyAccount = ThirdPartyAccount;
	}
}
