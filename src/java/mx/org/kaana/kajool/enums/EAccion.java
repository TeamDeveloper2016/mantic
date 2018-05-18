package mx.org.kaana.kajool.enums;

public enum EAccion {
  AGREGAR               ("Agregar",      "icono-agregar",      "agreg�"),
  MODIFICAR             ("Modificar",    "icono-modificar",    "modific�"),
  ELIMINAR              ("Eliminar",     "icono-eliminar",     "elimino"),
  DESACTIVAR            ("Desactivar",   "icono-desactivar",   "desactivo"),
  CONSULTAR             ("Consultar",    "icono-consultar",    "consilto"),
  LISTAR                ("Listar",       "icono-listar",       "listo"),
  COMPLEMENTAR          ("Complementar", "icono-complementar", "complemento"),
  JUSTIFICAR            ("Justificar",   "icono-justificar",   "justifico"),
  COPIAR                ("Copiar",       "icono-copiar",       "copio"),
  SUBIR                 ("Subir",        "icono-subir",        "subio"),
  BAJAR                 ("Bajar",        "icono-descargar",    "bajo"),
  REGISTRAR             ("Registrar",    "icono-registrar",    "registr�"),
  ASIGNAR               ("Asignar",      "icono-asignar",      "asign�"),
  ACTIVAR               ("Activar",      "icono-activar",      "activ�"),
  PROCESAR              ("Procesar",     "icono-procesar",     "proces�"),
  COMPLETO              ("Completo",     "icono-modificar",    "complet�"),
  CALCULAR              ("Calcular",     "icono-procesar",     "calcul�"),
  GENERAR               ("Generar",      "icono-procesar",     "gener�"),
  REPROCESAR            ("Reprocesar",   "icono-procesar",     "reproces�"),
  MOVIMIENTOS           ("Movimientos",  "icono-movimientos",  "aplici�n"),
  NO_APLICA             ("No aplica",    "icono-procesar",     "no aplica"),
  DESTRANSFORMACION     ("Eliminar",     "icono-eliminar",     "elimino"),
  DEPURAR               ("Depurar",      "icono-procesar",     "depur�"),
  TRANSFORMACION        ("Completo",     "icono-modificar",    "complet�"),
	RESTAURAR             ("Restaurar",    "icono-procesar",     "restaur�");
	

  private String name;
  private String icon;
  private String title;

  private EAccion(String name, String icon, String title) {
    this.name= name;
    this.icon=icon;
    this.title=title;
  }

  public String getName() {
    return this.name;
  }

  public String getIcon() {
    return this.icon;
  }

	public String getTitle() {
		return title;
	}

}
