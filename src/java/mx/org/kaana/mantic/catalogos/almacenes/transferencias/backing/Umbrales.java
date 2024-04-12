package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Umbral;

@Named(value = "manticCatalogosAlmacenesTransferenciasUmbrales")
@ViewScoped
public class Umbrales extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private Entity transferencia;
  private List<Umbral> fuentes;
  private List<Umbral> destinos;
  
	public Entity getTransferencia() {
		return transferencia;
	}

  public List<Umbral> getFuentes() {
    return fuentes;
  }

  public List<Umbral> getDestinos() {
    return destinos;
  }
  
	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
  public void doLoad() {
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("sortOrder", "order by tc_mantic_transferencias.registro");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      params.put(Constantes.SQL_CONDICION, "tc_mantic_transferencias.id_transferencia= "+ this.attrs.get("idTransferencia"));
      this.transferencia= (Entity)DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "lazy", params);
  		if(this.transferencia!= null) {
        params.put("idTransferencia", this.transferencia.toLong("idTransferencia"));
        params.put("idAlmacen", this.transferencia.toLong("idAlmacen"));
        this.fuentes = (List<Umbral>)DaoFactory.getInstance().toEntitySet(Umbral.class, "VistaTransferenciasMultiplesDto", "umbrales", params);
        params.put("idAlmacen", this.transferencia.toLong("idDestino"));
        this.destinos= (List<Umbral>)DaoFactory.getInstance().toEntitySet(Umbral.class, "VistaTransferenciasMultiplesDto", "umbrales", params);
      } // if  
      else {
        this.fuentes = new ArrayList<>();
        this.destinos= new ArrayList<>();
      } // else
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		finally {
      Methods.clean(params);
    } // finally
  } 

	public String doAceptar() {
		Transaccion transaccion = null;
    String regresar         = null;
		try {
      if(this.toCheck()) {
        transaccion= new Transaccion(this.fuentes, this.destinos);
        if(transaccion.ejecutar(EAccion.PROCESAR)) {
          regresar= this.doCancelar();
          JsfBase.addMessage("Se registró de forma correcta la información", ETipoMensaje.INFORMACION);
        } // if
        else
          JsfBase.addMessage("Ocurrió un error al registrar la información", ETipoMensaje.ERROR);
      } // if
      else 
        JsfBase.addMessage("No existe ninguna partida por actualizar", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
	} 
  
  public String doCancelar() {
		JsfBase.setFlashAttribute("idTransferencia", this.attrs.get("idTransferencia"));
		return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
	} 

  private Boolean toCheck() {
    Boolean regresar= Boolean.FALSE;
    for (Umbral item: this.fuentes) {
      if(!regresar)
        regresar= Objects.equals(item.getAction(), ESql.UPDATE);
    } // for
    for (Umbral item: this.destinos) {
      if(!regresar)
        regresar= Objects.equals(item.getAction(), ESql.UPDATE);
    } // for
    return regresar;
  }
  
  public void doChangeMinimo(Umbral row) {
    row.setAction(ESql.UPDATE);
    row.setIdVerificado(1L);
  }
  
  public void doChangeMaximo(Umbral row) {
    row.setAction(ESql.UPDATE);
    row.setIdVerificado(1L);
  }

  public String toColor(Umbral row) {
    return Objects.equals(row.getAction(), ESql.UPDATE)? "janal-tr-yellow": "";
  }
  
}
