package mx.org.kaana.mantic.catalogos.conteos.backing;

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
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;

@Named(value = "manticCatalogosConteosUmbrales")
@ViewScoped
public class Umbrales extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private Entity conteo;
  private List<Umbral> fuentes;
  
	public Entity getConteo() {
		return conteo;
	}

  public List<Umbral> getFuentes() {
    return fuentes;
  }

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("idConteo", JsfBase.getFlashAttribute("idConteo"));
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
      params.put("sortOrder", "order by tc_mantic_conteos.registro");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      params.put(Constantes.SQL_CONDICION, "tc_mantic_conteos.id_conteo= "+ this.attrs.get("idConteo"));
      this.conteo= (Entity)DaoFactory.getInstance().toEntity("VistaConteosDto", "lazy", params);
  		if(!Objects.equals(this.conteo, null)) {
        params.put("idConteo", this.attrs.get("idConteo"));
        params.put("idAlmacen", this.conteo.toLong("idAlmacen"));
        this.fuentes= (List<Umbral>)DaoFactory.getInstance().toEntitySet(Umbral.class, "VistaConteosDto", "umbrales", params);
      } // if  
      else {
        this.fuentes= new ArrayList<>();
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
        transaccion= new Transaccion(this.fuentes);
        if(transaccion.ejecutar(EAccion.REPROCESAR)) {
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
		JsfBase.setFlashAttribute("idConteoProcess", this.attrs.get("idConteo"));
		return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
	} 

  private Boolean toCheck() {
    Boolean regresar= Boolean.FALSE;
    for (Umbral item: this.fuentes) {
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
