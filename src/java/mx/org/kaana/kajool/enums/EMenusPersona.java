package mx.org.kaana.kajool.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/09/2015
 *@time 12:20:21 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EMenusPersona {

	PAIS         ("PAIS", "País"),
	ENTIDAD      ("ENTIDAD", "Entidad"),
	MUNICIPIO    ("MUNICIPIO", "Municipio"),
	RESIDENCIA   ("RESIDENCIA", "Residencia"),	
	CODIGO_POSTAL("CODIGOPOSTAL", "Codigo postal"),
	SEXO         ("SEXO", "Sexo");
	
	private static final Map<String, EMenusPersona> lookUp= new HashMap<>();
	private String identificador;
	private String descripcion;

	private EMenusPersona(String identificador, String descripcion) {		
		this.identificador= identificador;
		this.descripcion  = descripcion;
	}

	static{
		for(EMenusPersona menu: EMenusPersona.values())
			lookUp.put(menu.getIdentificador(), menu);		
	}
	
	public Long getKey(){
		return new Long(this.ordinal() + 1);
	}
	
	public String getIdentificador() {
		return identificador;
	}

	public String getDescripcion() {
		return descripcion;
	}	
	
	public static EMenusPersona getMenuPersona(String codigo){
		return lookUp.get(codigo);
	}
}
