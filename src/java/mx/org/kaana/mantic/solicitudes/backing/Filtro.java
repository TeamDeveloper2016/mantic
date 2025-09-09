package mx.org.kaana.mantic.solicitudes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;

@Named(value = "manticSolicitudesFiltro")
@ViewScoped
public class Filtro extends mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing.Filtro implements Serializable {

  private static final long serialVersionUID = 8793661741599428879L;
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      super.init();
      this.attrs.put("idTransferenciaParche", Boolean.FALSE);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
  @Override
	protected void toLoadTransferenciasEstatus() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put(Constantes.SQL_CONDICION, "id_transferencia_estatus in (1,2,3,4,5,8,10)");
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("catalogo", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasEstatusDto", "row", params, columns));
			this.attrs.put("idTransferenciasEstatus", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  
  @Override
	protected void toLoadTransferenciasTipos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put(Constantes.SQL_CONDICION, "id_transferencia_tipo= 4");
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("tipos", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasTiposDto", "row", params, columns));
			this.attrs.put("idTransferenciaTipo", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

  public void doWhatsapp() {
    Entity seleccionado      = (Entity) this.attrs.get("seleccionado");
    Map<String, Object>params= new HashMap<>();
    Transaccion transaccion  = null;
    List<Articulo> articulos = null;
    TcManticTransferenciasDto transferencia= null;
    try {
      transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey());
  	  articulos    = (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", transferencia.toMap(), Constantes.SQL_TOPE_REGISTROS);
      transaccion    = new Transaccion(transferencia, articulos);
      if(transaccion.ejecutar(EAccion.NOTIFICAR)) 
				JsfBase.addMessage("Whatsapp", "Se enviaron los mensajes", ETipoMensaje.INFORMACION);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
}
