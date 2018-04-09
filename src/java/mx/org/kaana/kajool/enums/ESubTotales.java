package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 22, 2012
 *@time 8:26:45 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ESubTotales {
	
	NINGUNO("Ninguno"),
	SUMA("Suma"),
	PROMEDIO("Promedio"),
	MINIMO("Mínimo"),
	MAXIMO("Máximo"),
	PORCENTAJE("Porcentaje"),
	VARIANZA("Varianza"),
	DESVIACION_ESTANDAR("Desviacion estandar"),
	CONTAR("Contar"),
	DIFERENTES("Diferentes"),
	AGREGACION("Agregación"),
	PRIMERO("Primero");
	
	private String name;

	private ESubTotales(String name) {
	  this.name   = name;	
	}
	
	public String getName() {
		return name;
	}

	public boolean isFunction(ETipoDato type) {
		boolean regresar= true;
		switch (type) {
			case DOUBLE:
			case LONG  :
				regresar= this.equals(NINGUNO) || this.equals(SUMA) || this.equals(PROMEDIO) || this.equals(MINIMO) || this.equals(MAXIMO) || this.equals(PORCENTAJE) || this.equals(VARIANZA) || this.equals(DESVIACION_ESTANDAR) || this.equals(CONTAR) || this.equals(DIFERENTES);
				break;
			case DATE:
			case TIME:
			case TIMESTAMP:
				regresar= this.equals(NINGUNO) || this.equals(MINIMO) || this.equals(MAXIMO) || this.equals(CONTAR) || this.equals(DIFERENTES);
				break;
			case TEXT:
				regresar= this.equals(NINGUNO) || this.equals(CONTAR) || this.equals(DIFERENTES);
				break;
			case BLOB:
				regresar= false;
				break;
		} // switch
		return regresar;
	}
	
}
