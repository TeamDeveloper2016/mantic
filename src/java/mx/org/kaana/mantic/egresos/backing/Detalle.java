package mx.org.kaana.mantic.egresos.backing;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;

@Named(value = "manticEgresosDetalle")
@ViewScoped
public class Detalle extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
	private List<Entity> egresosNotasEntradas;
	private List<Entity> egresosCreditosNotas;
	private List<Entity> egresosEmpresasPagos;
	private List<Entity> egresosNotas;		

	public List<Entity> getEgresosNotasEntradas() {
		return egresosNotasEntradas;
	}

	public List<Entity> getEgresosCreditosNotas() {
		return egresosCreditosNotas;
	}

	public List<Entity> getEgresosEmpresasPagos() {
		return egresosEmpresasPagos;
	}

	public List<Entity> getEgresosNotas() {
		return egresosNotas;
	}	
	
  @PostConstruct
  @Override
  protected void init() {		
		Long idEgreso = -1L;
		Long idEstatus= 1L;
    try {    	            
			idEgreso= (Long) JsfBase.getFlashAttribute("idEgreso");			
      this.attrs.put("egreso", DaoFactory.getInstance().findById(TcManticEgresosDto.class, idEgreso));  
			idEstatus= ((TcManticEgresosDto)this.attrs.get("egreso")).getIdEgresoEstatus();
			this.attrs.put("estatus", idEstatus.equals(1L) ? "REGISTRADO" : (idEstatus.equals(2L) ? "INCOMPLETO" : "COMPLETO"));
      this.attrs.put("idEgreso", idEgreso);  
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {    		
    try {      
			this.egresosNotasEntradas= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "notasEntradas", this.attrs, Constantes.SQL_TODOS_REGISTROS);
			this.egresosCreditosNotas= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "creditosNotas", this.attrs, Constantes.SQL_TODOS_REGISTROS);
			this.egresosEmpresasPagos= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "empresasPagos", this.attrs, Constantes.SQL_TODOS_REGISTROS);
			this.egresosNotas= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "notas", this.attrs, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
    } // finally		
  } // doLoad	

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idEgreso", this.attrs.get("idEgreso"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doEliminarNotaEntrada(){
		Transaccion transaccion= null;
		Entity notaEntrada     = null;
		try {			
			notaEntrada= (Entity)this.attrs.get("notaEntrada");
			if(this.egresosNotasEntradas.remove(notaEntrada)){
				transaccion= new Transaccion(notaEntrada.getKey(), EAccion.ACTIVAR);
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarNotaEntrada

	public void doEliminarCreditoNota(){
		Transaccion transaccion= null;
		Entity creditoNota     = null;
		try {			
			creditoNota= (Entity)this.attrs.get("creditoNota");
			if(this.egresosCreditosNotas.remove(creditoNota)){
				transaccion= new Transaccion(creditoNota.getKey(), EAccion.AGREGAR);
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarCreditoNota
	
	public void doEliminarEmpresaPago(){
		Transaccion transaccion= null;
		Entity empresaPago     = null;
		try {			
			empresaPago= (Entity)this.attrs.get("empresaPago");
			if(this.egresosEmpresasPagos.remove(empresaPago)){
				transaccion= new Transaccion(empresaPago.getKey(), EAccion.ASIGNAR);
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
	
	public void doEliminarNota(){
		Transaccion transaccion= null;
		Entity nota            = null;
		try {			
			nota= (Entity)this.attrs.get("nota");
			if(this.egresosNotas.remove(nota)){
				transaccion= new Transaccion(nota.getKey(), EAccion.BAJAR);
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
}