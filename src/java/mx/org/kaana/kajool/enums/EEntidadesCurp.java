package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/04/2015
 *@time 12:30:56 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EEntidadesCurp {
	AGUASCALIENTES    		 ("Aguascalientes", "AS"),
	BAJA_CALIFORNIA        ("Baja California","BC"),
	BAJA_CALIFORNIA_SUR    ("Baja California Sur","BS"),
	CAMPECHE          		 ("Campeche","CC"),
	COAHUILA      				 ("Coahuila","CL"),
	COAHUILA_DE_ZARAGOZA	 ("Coahuila de Zaragoza","CL"),
	COLIMA                 ("Colima","CM"),
	CHIAPAS                ("Chiapas","CS"),
	CHIHUAHUA              ("Chihuahua","CH"),
	DISTRITO_FEDERAL    	 ("Distrito Federal","DF"),
	DURANGO         			 ("Durango","DG"),
	GUANAJUATO    				 ("Guanajuato","GT"),
	GUERRERO               ("Guerrero","GR"),
	HIDALGO           		 ("Hidalgo","HG"),
	JALISCO       				 ("Jalisco","JC"),
	MEXICO    						 ("Mexico","MC"),
	MICHOACAN       			 ("Michoacan","MN"),
	MICHOACAN_DE_OCAMPO		 ("Michoacan de Ocampo","MN"),
	MORELOS     					 ("Morelos","MS"),
	NAYARIT           		 ("Nayarit","NT"),
	NUEVO_LEON             ("Nuevo Leon","NL"),
	OAXACA            		 ("Oaxaca","OC"),
	PUEBLA        				 ("Puebla","PL"),
	QUERETARO           	 ("Queretaro","QT"),
	QUINTANA_ROO    			 ("Quintana Roo","QR"),
	SAN_LUIS_POTOSI      	 ("San Luis Potosi","SP"),
	SINALOA   						 ("Sinaloa","SL"),
	SONORA                 ("Sonora","SR"),
	TABASCO           		 ("Tabasco","TC"),
	TAMAULIPAS    				 ("Tamaulipas","TS"),
	TLAXCALA          		 ("Tlaxcala","TL"),
	VERACRUZ      				 ("Veracruz","VZ"),
	YUCATAN   						 ("Yucatan","YN"),
	ZACATECAS              ("Zacatecas","ZS"),
	NACIDO_EN_EL_EXTRANJERO("Nacido en el extranjero","NE"),
	EXTRANJERO             ("Extranjero","NE"),
	NO_SABE                ("NO SABE","NI"),
	;
	
	String clave;
	String nombre;

	private EEntidadesCurp(String nombre, String clave) {
		this.clave=clave;
		this.nombre=nombre;
	}

	public String getClave() {
		return clave;
	}

	public String getNombre() {
		return nombre;
	}
}
