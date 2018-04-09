package mx.org.kaana.kajool.enums;

public enum EAccion {
  AGREGAR               ("Agregar",      "icono-agregar",      "agregá"),
  MODIFICAR             ("Modificar",    "icono-modificar",    "modificó"),
  ELIMINAR              ("Eliminar",     "icono-eliminar",     "elimino"),
  DESACTIVAR            ("Desactivar",   "icono-desactivar",   "desactivo"),
  CONSULTAR             ("Consultar",    "icono-consultar",    "consilto"),
  LISTAR                ("Listar",       "icono-listar",       "listo"),
  COMPLEMENTAR          ("Complementar", "icono-complementar", "complemento"),
  JUSTIFICAR            ("Justificar",   "icono-justificar",   "justifico"),
  COPIAR                ("Copiar",       "icono-copiar",       "copio"),
  SUBIR                 ("Subir",        "icono-subir",        "subio"),
  BAJAR                 ("Bajar",        "icono-descargar",    "bajo"),
  REGISTRAR             ("Registrar",    "icono-registrar",    "registrá"),
  ASIGNAR               ("Asignar",      "icono-asignar",      "asignó"),
  ACTIVAR               ("Activar",      "icono-activar",      "activó"),
  PROCESAR              ("Procesar",     "icono-procesar",     "procesó"),
  COMPLETO              ("Completo",     "icono-modificar",    "completá"),
  CALCULAR              ("Calcular",     "icono-procesar",     "calculá"),
  GENERAR               ("Generar",      "icono-procesar",     "generá"),
  REPROCESAR            ("Reprocesar",   "icono-procesar",     "reprocesó"),
  MOVIMIENTOS           ("Movimientos",  "icono-movimientos",  "aplición"),
  NO_APLICA             ("No aplica",    "icono-procesar",     "no aplica"),
  ELIMINAR_CAPTURA      ("Eliminar",     "icono-eliminar",     "elimino"),
  ELIMINAR_VERIFICACION ("Eliminar",     "icono-eliminar",     "elimino"),
  ELIMINAR_DEPURADOR    ("Eliminar",     "icono-eliminar",     "elimino"),
  CARGAR_VERIFICACION   ("Agregar",      "icono-agregar",      "agregó"),
  CARGAR_DEPURADOR      ("Agregar",      "icono-agregar",      "agregó"),
  DESTRANSFORMACION     ("Eliminar",     "icono-eliminar",     "elimino"),
  DEPURAR               ("Depurar",      "icono-procesar",     "depuró"),
  TRANSFORMACION        ("Completo",     "icono-modificar",    "completó"),
	RESTAURAR             ("Restaurar",    "icono-procesar",     "restauró");
	

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
