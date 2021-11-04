package mx.org.kaana.mantic.catalogos.empresas.saldar.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Importados;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosEmpresasSaldarImportar")
@ViewScoped
public class Importar extends mx.org.kaana.mantic.inventarios.entradas.backing.Importar implements Serializable {

  private static final long serialVersionUID= 7327134019658182127L;
  private static final Log LOG              = LogFactory.getLog(Importar.class);
  private String path;

  public String getPath() {
    return path;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    super.init();
    this.toLoadDocumentos();
    this.attrs.put("idTipoDocumento", -1L);
    this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_DOCUMENTOS);
    this.attrs.put("jpg", ""); 
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/documentos/");
  } // init
  
  private void toLoadDocumentos() {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("agrupador", "5");
      List<UISelectItem> documentos= UISelect.seleccione("TcManticTiposArchivosDto", "grupo", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "idKey");
      this.attrs.put("documentos", documentos);
      this.attrs.put("idTipoDocumento", UIBackingUtilities.toFirstKeySelectItem(documentos));
     } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
  @Override
  public void doTabChange(TabChangeEvent event) {  
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      if(event.getTab().getTitle().equals("Archivos")) {
        params.put("idNotaEntrada", this.orden.getIdNotaEntrada());      
        params.put("idTipoDocumento", -1L);      
        this.doLoadImportados("VistaNotasEntradasDto", "importados", params);
      } // if  
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  @Override
	public String doAceptar() {
		String regresar      = null;
		Importados importados= null;
		try {
      if(this.getXml()!= null || this.getPdf()!= null || this.getJpg()!= null) {
        importados= new Importados(this.orden, this.getXml(), this.getPdf(), this.getJpg());
        if(importados.ejecutar(EAccion.AGREGAR)) {
          UIBackingUtilities.execute("janal.alert('Se actualizó y se importaron los catalogos de forma correcta !');");
          this.attrs.put("idTipoDocumento", -1L);
          this.reset();
          // regresar= this.doCancelar();
        } // if
      } // if
      else 
        JsfBase.addMessage("Archivo", "Se tiene que subir un archivo en formato XML, PDF o JPG", ETipoMensaje.ALERTA);	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // doAceptar		
 
  public void doValueUpload() {
    LOG.info(this.attrs.get("idTipoDocumento")); 
    if(this.attrs.get("observaciones")!= null)
      this.attrs.put("observaciones", ((String)this.attrs.get("observaciones")).toUpperCase());
  }
 
	public String toColor(Entity row) {
		return "";
	} 
 
  public void doDelete(Entity row) {
 		Importados importados= null;
		try {
      importados= new Importados(row);
      if(importados.ejecutar(EAccion.MOVIMIENTOS)) 
        JsfBase.addMessage("Alerta", "Se "+ (Objects.equals(row.toLong("idEliminado"), 1L)? "eliminó": "recuperó")+ " de forma correcta el documento");      
      else 
        JsfBase.addMessage("Error", "No se pudo ser "+ (Objects.equals(row.toLong("idEliminado"), 2L)? "eliminado": "recuperado")+ " el documento", ETipoMensaje.ALERTA);	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  }
  
}