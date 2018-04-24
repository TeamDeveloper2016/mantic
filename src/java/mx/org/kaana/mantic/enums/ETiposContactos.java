package mx.org.kaana.mantic.enums;

public enum ETiposContactos {

	TELEFONO,
	TELEFONO_CASA,
	TELEFONO_NEGOCIO,
	TELEFONO_PERSONAL,
	TELEFONO_TRABAJO,
	CELULAR,
	CELULAR_PERSONAL,
	CELULAR_NEGOCIO,
	CORREO,
	CORREO_PERSONAL,
	CORREO_NEGOCIO,
	PAGINA,
	PAGINA_PERSONAL,
	PAGINA_NEGOCIO,
	CORREO_1,
	CORREO_2,
	CORREO_3,
	CORREO_4;

	public Long getKey(){
		return this.ordinal() + 1L;
	}
}