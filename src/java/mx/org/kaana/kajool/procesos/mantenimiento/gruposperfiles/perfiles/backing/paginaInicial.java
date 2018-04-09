package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/02/2015
 *@time 04:40:09 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas.Transaccion;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolMantenimientoGruposperfilesPerfilesPaginaInicial")
@ViewScoped
public class paginaInicial extends IBaseFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(paginaInicial.class);

  @PostConstruct
	@Override
	protected void init() {
    Entity paginaActual = null;
		try {
			this.attrs.put("idGrupo",Numero.getLong(JsfBase.getFlashAttribute("idGrupo").toString()));
			this.attrs.put("idPerfil",Numero.getLong(JsfBase.getFlashAttribute("idPerfil").toString()));
      this.attrs.put(Constantes.SQL_CONDICION, "id_perfil = ".concat(JsfBase.getFlashAttribute("idPerfil").toString()));
			this.attrs.put("nombrePerfil",DaoFactory.getInstance().toEntity("TcJanalPerfilesDto",this.attrs).toValue("descripcion"));
			paginaActual = (Entity)DaoFactory.getInstance().toEntity("VistaPerfilesMenusDto","paginaInicialAsignada",this.attrs);
			this.attrs.put("paginaActual",paginaActual!=null?paginaActual.toValue("descripcion"):"No asignada");
			this.attrs.put("rutaActual",paginaActual!=null?paginaActual.toValue("ruta"):"No asignada");
			LOG.debug(JsfBase.getFacesContext().getCurrentPhaseId());
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		try {
			this.lazyModel = new FormatLazyModel("VistaPerfilesMenusDto","paginaInicial",this.attrs,Collections.EMPTY_LIST);
			UIBackingUtilities.resetDataTable();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // doLoad

	public void doAsignar(){
		Transaccion transaccion= null;
		TcJanalPerfilesDto dto      = null;
		try {
			dto = new TcJanalPerfilesDto();
			dto.setIdPerfil(Numero.getLong(this.attrs.get("idPerfil").toString()));
			dto.setIdMenu(((Entity)this.attrs.get("seleccionado")).toLong("idKey"));
			transaccion =  new Transaccion(dto);
			if(transaccion.ejecutar(EAccion.PROCESAR)){
				this.attrs.put("paginaActual",((Entity)this.attrs.get("seleccionado")).toString("descripcion"));
				this.attrs.put("rutaActual",((Entity)this.attrs.get("seleccionado")).toString("ruta"));
				JsfBase.addMessage("Página inicial asignada correctamente");
			} // if
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // doAsignar
	
	public String doRegresar(){
    String regresar = null;
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		regresar = "/Paginas/Mantenimiento/GruposPerfiles/Perfiles/filtro";
    return regresar;
  } // doRegresar
	
	public void doLimpiar(){
   Transaccion transaccion= null;
		TcJanalPerfilesDto dto      = null;
		try {
			dto = new TcJanalPerfilesDto();
			dto.setIdPerfil(Numero.getLong(this.attrs.get("idPerfil").toString()));
			dto.setIdMenu(null);
			transaccion =  new Transaccion(dto);
			if(transaccion.ejecutar(EAccion.PROCESAR)){
				this.attrs.put("paginaActual","No asignada");
				this.attrs.put("rutaActual","No asignada");
				JsfBase.addMessage("Página inicial eliminada correctamente");
			} // if
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLimpiar
}
