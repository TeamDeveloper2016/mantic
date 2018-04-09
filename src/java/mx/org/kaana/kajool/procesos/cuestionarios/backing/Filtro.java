package mx.org.kaana.kajool.procesos.cuestionarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/11/2016
 *@time 10:51:10 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.cuestionarios.beans.JuntarReporte;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolCuestionariosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -3565375733676226695L;
  private List<UISelectItem> capturistas;

  public List<UISelectItem> getCapturistas() {
    return capturistas;
  }
  
  @PostConstruct
  @Override
	protected void init() {
    try {			
      loadEntidades();
      this.attrs.put("control", "");
      this.attrs.put("folio", "");
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());      
      this.attrs.put("idEstatus", EEstatus.DISPONIBLE.getKey());
      doActualizaCapturistas();
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna>campos= null;
    String perfil      = null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      perfil= JsfBase.getAutentifica().getEmpleado().getDescripcionPerfil();
      this.attrs.put("condicionUsuario",  "and tr_janal_movimientos.id_usuario=" + (perfil.equals(EPerfiles.CAPTURISTA.name()) ? JsfBase.getIdUsuario() : this.attrs.get("capturista").toString()));
      this.lazyModel= new FormatLazyModel("VistaCuestionariosDto", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
  } // doLoad

  
  public void doActualizaCapturistas(){
    try {
      this.attrs.put("idCapturista", TcConfiguraciones.getInstance().getPropiedad(CONFIG_CAPTURISTA));
      this.attrs.put("condicionUsuario", JsfBase.getAutentifica().getEmpleado().getDescripcionPerfil().equals(EPerfiles.CAPTURISTA.name()) ? "and tc_janal_usuarios.id_usuario=" + JsfBase.getIdUsuario() : "");
      this.capturistas= UISelect.build("VistaCargasTrabajoDto", "capturistasCuestionarios", this.attrs, "nombreCompleto", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("capturista", UIBackingUtilities.toFirstKeySelectItem(this.capturistas));
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doActualizaCapturistas
  
  public String doReportes(){
    return doReportes(EReporte.CUESTIONARIO);
  }
  
  public String doReportes(EReporte reporte){
    String regresar               = null;
    List<Entity> seleccionados    = null;
    List<Definicion> definiciones = null;
    Map<String, Object> params		= null;
		Map<String, Object> parametros= null;
    Long integrantes              = 0L;
    String inicio                 = null;
    String fin                    = null;
    try {
      definiciones= new ArrayList<>();
      parametros= new HashMap<>();
      params= new HashMap<>();
      seleccionados= DaoFactory.getInstance().toEntitySet("VistaCuestionariosDto", this.attrs);
      for(Entity folio: seleccionados){
        parametros.put("REPORTE_TITULO", reporte.getNombre());
        params.put("folio", folio.get("folio"));
        params.put("idMuestra", folio.getKey());
        integrantes= DaoFactory.getInstance().toField("Cuestionarios", "cuentaIntegrantes", params, "integrantes").toLong();
        for(int indice=1; indice<= integrantes; indice++ ){
          params.put("cuest",  "1");
          parametros.put("REPORTE_SUBREPORTE_SQL_UNO",   Dml.getInstance().getSelect(EReporte.INTEGRANTES.getProceso(), EReporte.INTEGRANTES.getIdXml(), params));
          parametros.put("~INTEGRANTES_SUBREPORTE_1",  EReporte.INTEGRANTES.getJasper());
          definiciones.add(new Definicion((Map<String, Object>) ((HashMap)params).clone(), (Map<String, Object>) ((HashMap)parametros).clone(), reporte.getProceso(), reporte.getIdXml(), reporte.getJasper()));
          inicio= String.valueOf(indice);
          fin= String.valueOf(integrantes);
        } // for bloques
        parametros.put("HOJA_INICIO", inicio);
        parametros.put("HOJA_FIN", fin);
      } // for
      JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new JuntarReporte(definiciones,reporte)); 
      regresar= "/Paginas/Reportes/juntar";
    } // try 
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // doReportes
  
  public String doImprimir(){
    return doImprimir(EReporte.CUESTIONARIO);
  }
  
  public String doImprimir(EReporte reporte){
    String regresar               = null;
    Entity seleccionado           = null;
    List<Definicion> definiciones = null;
    Map<String, Object> params		= null;
		Map<String, Object> parametros= null;
    Long integrantes              = 0L;
    String inicio                 = null;
    String fin                    = null;
    try {
      seleccionado= (Entity) this.attrs.get("selected");
      definiciones= new ArrayList<>();
      parametros= new HashMap<>();
      params= new HashMap<>();
      parametros.put("REPORTE_TITULO", reporte.getTitulo());
      params.put("folio", seleccionado.get("folio"));
      params.put("idMuestra", seleccionado.getKey());
      integrantes= DaoFactory.getInstance().toField("Cuestionarios", "cuentaIntegrantes", params, "integrantes").toLong();
      for(int indice=1; indice<= integrantes; indice++ ){
        params.put("cuest",  "1");
        parametros.put("REPORTE_SUBREPORTE_SQL_UNO",   Dml.getInstance().getSelect(EReporte.INTEGRANTES.getProceso(), EReporte.INTEGRANTES.getIdXml(), params));
        parametros.put("~INTEGRANTES_SUBREPORTE_1",  EReporte.INTEGRANTES.getJasper());
        definiciones.add(new Definicion((Map<String, Object>) ((HashMap)params).clone(), (Map<String, Object>) ((HashMap)parametros).clone(), reporte.getProceso(), reporte.getIdXml(), reporte.getJasper()));
        inicio= String.valueOf(indice);
        fin= String.valueOf(integrantes);
      } // for bloques
      parametros.put("HOJA_INICIO", inicio);
      parametros.put("HOJA_FIN", fin);
      JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new JuntarReporte(definiciones,reporte)); 
      regresar= "/Paginas/Reportes/juntar";
    } // try 
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // doImprimir
}
