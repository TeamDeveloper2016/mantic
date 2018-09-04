package mx.org.kaana.mantic.consultas.enums;

import java.util.Calendar;
import java.util.Date;
import mx.org.kaana.libs.formato.Fecha;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 29/08/2018
 *@time 10:38:09 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EPeriodos {
  
	HOY("HOY", 1), ULTIMOS_7DIAS("ULTIMOS 7 DIAS", -6), ULTIMA_SEMANA("ULTIMA SEMANA", -6), ULTIMOS_15DIAS("ULTIMOS 15 DIAS", -14), ULTIMA_QUINCENA("ULTIMA QUINCENA", -14),
	ULTIMO_30DIAS("ULTIMOS 30 DIAS", -30), ULTIMO_MES("ULTIMO MES", -30), MES_ANTERIOR("MES ANTERIOR", -30), TRIMESTRE_ACTUAL("TRIMESTRE ACTUAL", -90), ULTIMO_TRIMESTRE("ULTIMO TRIMESTRE", -90),
	SEMESTRE_ACTUAL("SEMESTRE ACTUAL", -180), ULTIMO_SEMESTRE("ULTIMO SEMESTRE", -180), ANIO_ACTUAL("AÑO ACTUAL", -365), ANIO_ANTERIOR("AÑO ANTERIOR", -365), COMPLETO("COMPLETO", -999999), LIBRE("LIBRE", 0);
	
	private final String title;
	private final int days;
	
	private EPeriodos(String title, int days) {
		this.title= title;
		this.days = days;
	}

	public String getTitle() {
		return title;
	}

	public int getDays() {
		return days;
	}
	
	public Date getStart() {
		Calendar regresar= Calendar.getInstance();
		regresar.set(Calendar.DATE, 25);
		int dia= 0;
		switch(this) {
			case HOY:
 			  break;
			case ULTIMOS_7DIAS:
				regresar.add(Calendar.DATE, this.getDays());
 			  break;
			case ULTIMA_SEMANA:
				regresar.add(Calendar.DATE, this.getDays());
				dia= regresar.get(Calendar.DAY_OF_WEEK);
				if(dia== Calendar.SATURDAY)
					dia= this.getDays()+ 1;
				else
					dia-= 1;
				regresar.add(Calendar.DATE, dia* -1);
 			  break;
			case ULTIMOS_15DIAS:
				regresar.add(Calendar.DATE, this.getDays());
				break;
			case ULTIMA_QUINCENA:
				dia= regresar.get(Calendar.DAY_OF_MONTH);
				if(dia> 15)
					dia= 16;
				else
					dia= 1;
				regresar.set(Calendar.DATE, dia);
				break;
			case ULTIMO_30DIAS:
				regresar.add(Calendar.DATE, this.getDays());
				break;
			case ULTIMO_MES:
				regresar.set(Calendar.DATE, 1);
				break;
			case MES_ANTERIOR:
				regresar.add(Calendar.MONTH, -1);
				regresar.set(Calendar.DATE, 1);
				break;
			case TRIMESTRE_ACTUAL:
				break;
			case ULTIMO_TRIMESTRE:
				regresar.add(Calendar.DATE, this.getDays());
				break;
			case SEMESTRE_ACTUAL:
				break;
			case ULTIMO_SEMESTRE:
				regresar.add(Calendar.DATE, this.getDays());
				break;
			case ANIO_ACTUAL:
				break;
			case ANIO_ANTERIOR:
				regresar.add(Calendar.DATE, this.getDays());
				break;
			case COMPLETO:
				break;
			case LIBRE:
				break;
		} // switch
		return new Date(regresar.getTimeInMillis());
	}
	
	public Date getEnd() {
		Calendar regresar= Calendar.getInstance();
		regresar.set(Calendar.DATE, 25);
		int dia= 0;
		switch(this) {
			case HOY:
 			  break;
			case ULTIMOS_7DIAS:
 			  break;
			case ULTIMA_SEMANA:
				regresar.add(Calendar.DATE, this.getDays());
				dia= regresar.get(Calendar.DAY_OF_WEEK);
				if(dia!= Calendar.SUNDAY) {
					dia= this.getDays()+ dia+ 2;
				  regresar.add(Calendar.DATE, dia); 			  
				} // if	
				break;
			case ULTIMOS_15DIAS:
				break;
			case ULTIMA_QUINCENA:
				break;
			case ULTIMO_30DIAS:
				break;
			case ULTIMO_MES:
				break;
			case MES_ANTERIOR:
				regresar.set(Calendar.DATE, 1);
				regresar.add(Calendar.DATE, -1);
				break;
			case TRIMESTRE_ACTUAL:
				break;
			case ULTIMO_TRIMESTRE:
				break;
			case SEMESTRE_ACTUAL:
				break;
			case ULTIMO_SEMESTRE:
				break;
			case ANIO_ACTUAL:
				break;
			case ANIO_ANTERIOR:
				break;
			case COMPLETO:
				break;
			case LIBRE:
				break;
		} // switch
		return new Date(regresar.getTimeInMillis());
	}

	@Override
	public String toString() {
		return "EPeriodos{title="+ title+ ", days="+days +", start="+ Fecha.formatear(Fecha.FECHA_CORTA, this.getStart())+ ", end="+ Fecha.formatear(Fecha.FECHA_CORTA, this.getEnd())+'}';
	}
	
}
