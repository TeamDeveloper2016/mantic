package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import org.primefaces.model.DualListModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 20, 2015
 *@time 1:51:55 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BeanIndicadorAvance implements Serializable {
	
	private static final long serialVersionUID=4687693591686604554L;
	private Boolean disableOrden;
	private List<UISelectItem> estatusIndicadores;
	private List<UISelectItem> perfiles;
	private List<SelectionItem> targetTemporal;
	private List<SelectionItem> sourceTemporal;
	private DualListModel<SelectionItem> model;
	private Long idEstatusIndicador;
	private String[] perfilesSeleccionados;
	
	public BeanIndicadorAvance(){
		this.estatusIndicadores= new ArrayList<>();
		this.perfiles					 = new ArrayList<>();
		this.targetTemporal		 = new ArrayList<>();
		this.sourceTemporal		 = new ArrayList<>();
		this.model						 = new DualListModel<>();
		this.disableOrden			 = false;
		this.idEstatusIndicador= -1L;
	}

	public List<UISelectItem> getEstatusIndicadores() {
		return estatusIndicadores;
	}

	public void setEstatusIndicadores(List<UISelectItem> estatusIndicadores) {
		this.estatusIndicadores=estatusIndicadores;
	}
	
	public List<UISelectItem> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<UISelectItem> perfiles) {
		this.perfiles=perfiles;
	}

	public Boolean getDisableOrden() {
		return disableOrden;
	}

	public void setDisableOrden(Boolean disableOrden) {
		this.disableOrden=disableOrden;
	}

	public List<SelectionItem> getTargetTemporal() {
		return targetTemporal;
	}

	public void setTargetTemporal(List<SelectionItem> targetTemporal) {
		this.targetTemporal=targetTemporal;
	}

	public List<SelectionItem> getSourceTemporal() {
		return sourceTemporal;
	}

	public void setSourceTemporal(List<SelectionItem> sourceTemporal) {
		this.sourceTemporal=sourceTemporal;
	}

	public DualListModel<SelectionItem> getModel() {
		return model;
	}

	public void setModel(DualListModel<SelectionItem> model) {
		this.model=model;
	}

	public Long getIdEstatusIndicador() {
		return idEstatusIndicador;
	}

	public void setIdEstatusIndicador(Long idEstatusIndicador) {
		this.idEstatusIndicador=idEstatusIndicador;
	}

	public String[] getPerfilesSeleccionados() {
		return perfilesSeleccionados;
	}

	public void setPerfilesSeleccionados(String[] perfilesSeleccionados) {
		this.perfilesSeleccionados=perfilesSeleccionados;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.estatusIndicadores);
		Methods.clean(this.perfiles);
		Methods.clean(this.targetTemporal);
		Methods.clean(this.sourceTemporal);
		Methods.clean(this.model);
	}

}
