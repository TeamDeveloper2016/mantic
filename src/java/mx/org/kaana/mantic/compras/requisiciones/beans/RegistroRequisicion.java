package mx.org.kaana.mantic.compras.requisiciones.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.compras.requisiciones.reglas.MotorBusqueda;

public class RegistroRequisicion implements Serializable {

	private static final long serialVersionUID=-6413309480495985622L;
	
	private Requisicion requisicion;
	private List<RequisicionProveedor> proveedores;
	private RequisicionProveedor proveedorSeleccion;
	private Long idRequisicion;
	private ContadoresListas contadores;
	private Long countIndice;
	private List<IBaseDto> deleteList;
	private Integer totalProveedores;
	
	public RegistroRequisicion() {
		this(new Requisicion(), new ArrayList<RequisicionProveedor>());
	}
	
	public RegistroRequisicion(Requisicion requisicion, List<RequisicionProveedor> proveedores) {
		this.requisicion= requisicion;
		this.proveedores= proveedores;
		this.contadores = new ContadoresListas();
		this.deleteList = new ArrayList<>();
		this.deleteList = new ArrayList<>();
		this.countIndice= 0L;
	}

	public RegistroRequisicion(Long idRequisicion) {
		this.idRequisicion= idRequisicion;
		this.contadores   = new ContadoresListas();
		this.countIndice  = 0L;
		this.deleteList   = new ArrayList<>();
		this.deleteList   = new ArrayList<>();
		init();		
	}
	
	public Requisicion getRequisicion() {
		return requisicion;
	}

	public void setRequisicion(Requisicion requisicion) {
		this.requisicion = requisicion;
	}

	public List<RequisicionProveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<RequisicionProveedor> proveedores) {
		this.proveedores = proveedores;
	}	

	public RequisicionProveedor getProveedorSeleccion() {
		return proveedorSeleccion;
	}

	public void setProveedorSeleccion(RequisicionProveedor proveedorSeleccion) {
		this.proveedorSeleccion = proveedorSeleccion;
	}
	
	public List<IBaseDto> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<IBaseDto> deleteList) {
		this.deleteList = deleteList;
	}

	public void setTotalProveedores(Integer totalProveedores) {
		this.totalProveedores=totalProveedores;
	}

	public Integer getTotalProveedores() {
		return totalProveedores;
	}

	private void init(){
		MotorBusqueda motorBusqueda= null;		
		try {
			motorBusqueda= new MotorBusqueda(this.idRequisicion);
			this.requisicion= motorBusqueda.toRequisicion();
			this.proveedores= motorBusqueda.toArticulosProveedor();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // init
	
	public void doAgregarRequisicionProveedor(){
		RequisicionProveedor requisicionProveedor= null;
		try {					
			requisicionProveedor= new RequisicionProveedor(this.contadores.getTotalRequisicionProveedor() + this.countIndice, ESql.INSERT, true);				
			this.proveedores.add(requisicionProveedor);			
			this.totalProveedores= this.proveedores.size();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarRequisicionProveedor(){
		try {			
			if(this.proveedores.remove(this.proveedorSeleccion)){
				if(!this.proveedorSeleccion.getNuevo())
					addDeleteList(this.proveedorSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el proveedor", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el proveedor", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion	
	
	public boolean validateDuplicateProveedor(){
		boolean regresar  = false;
		int countDuplicate= 0;
		try {
			for(RequisicionProveedor prov: this.proveedores){
				for(RequisicionProveedor provPivote: this.proveedores){
					if(prov.getIdProveedor().equals(provPivote.getIdProveedor()))
						countDuplicate++;
				} // for				
			} // for
			regresar= !(countDuplicate > this.proveedores.size());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // validateDuplicateProveedor
	
	private void addDeleteList(IBaseDto dto){
		try {
			this.deleteList.add(dto);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
}
