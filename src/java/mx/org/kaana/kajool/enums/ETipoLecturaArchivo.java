package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/04/2015
 *@time 04:49:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETipoLecturaArchivo {
	XLS ("Xls"),
	TXT ("Txt"),
	CSV ("Csv")  ;
	
	private static final String pathBase= "mx.org.kaana.kajool.procesos.insumos.carga.reglas.modulos.";
	private String nameClass;

	private ETipoLecturaArchivo(String nameClass) {
		this.nameClass=nameClass;
	}

	public String getNameClass() {
		return pathBase.concat(nameClass);
	}
}
