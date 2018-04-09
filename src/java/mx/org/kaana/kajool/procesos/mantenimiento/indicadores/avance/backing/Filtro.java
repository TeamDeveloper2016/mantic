package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 28, 2014
 *@time 10:19:10 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;

@ManagedBean(name="kajoolMantenimientoIndicadoresAvanceFiltro")
@ViewScoped

public class Filtro extends IBaseFilter implements Serializable {
	
	private static final long serialVersionUID=2148529028543463455L;
	private Date fecha;

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha=fecha;
	}

	public String getFechaFormato() {
			return Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, this.fecha);
	}
	
  @PostConstruct
	@Override
	protected void init() {
		this.fecha= new Date(Fecha.getFechaCalendar(Fecha.getHoy()).getTimeInMillis());
		//this.attrs.put("fechaInicio", TcConfiguraciones.getInstance().getPropiedad(JsfBase.getAutentifica().getEncuesta().getNombre().concat(".indicadores.fechaInicio")));
		//this.attrs.put("fechaFin", TcConfiguraciones.getInstance().getPropiedad(JsfBase.getAutentifica().getEncuesta().getNombre().concat(".indicadores.fechaFin")));
		this.attrs.put("fechaInicio", "27/07/2015");
		this.attrs.put("fechaFin", "31/10/2015");
		this.attrs.put("sortOrder", " order by	orden");
		doArmarParams();
	} // init
	
	@Override
	public void doLoad() {
		List<Columna> columnas= null;
		try {
			columnas = new ArrayList<>();
			columnas.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
			columnas.add(new Columna("fin", EFormatoDinamicos.FECHA_HORA_ANTERIOR));
			this.lazyModel= new FormatCustomLazy("VistaBitacorasAvances","row", this.attrs, columnas);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(columnas);
		} // finally
	}
	
	public void doArmarParams(){
		try {
			this.attrs.put("idGrupo", JsfBase.seekParameter("idGrupo"));
			this.attrs.put("activo", 1L);
			this.attrs.put("estatus", "1,0");
			this.attrs.put("fecha", Fecha.formatear(Fecha.FECHA_CORTA, this.fecha));
			doLoad();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
		} // catch
	}
	
	public String doAvance(){
		JsfBase.setFlashAttribute("seleccionado", (Entity)this.attrs.get("seleccionado"));
	  return "avance".concat(Constantes.REDIRECIONAR);
	} // doAvance
}
