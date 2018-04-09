package mx.org.kaana.kajool.enums;

public enum ETiposMensajes {

	GENERAL,
	GRUPO,
	PERFIL,
	OPCIONDEMENU,
  USUARIO,
  PROCESO,
  INDICADOREXTERNO,
  INDICADORGENERAL,
  INDICADORAGRUPO,
  INDICADORAPERFIL,
  INDICADORAUSUARIO;
	
	public Long getKey() {
    return ordinal() + 1L;
  } // getKey
} // ETiposMensajes
