package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticArticuloGrupoDescuentoDto;

public class DescuentoEspecial extends TrManticArticuloGrupoDescuentoDto implements Serializable{
	
	private static final long serialVersionUID = 8334628425056719016L;
	private ESql sqlAccion;
	private Boolean nuevo;
	private Date vigenciaIni;
	private Date vigenciaFin;
	
	public DescuentoEspecial() {
		this(-1L);
	}

	public DescuentoEspecial(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public DescuentoEspecial(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public DescuentoEspecial(Long key, ESql sqlAccion, Boolean nuevo) {
		super(key);
		this.sqlAccion= sqlAccion;
		this.nuevo    = nuevo;
		this.vigenciaIni= new Date(Calendar.getInstance().getTimeInMillis());
		this.vigenciaFin= new Date(Calendar.getInstance().getTimeInMillis());
	}

	public ESql getSqlAccion() {
		return sqlAccion;
	}

	public void setSqlAccion(ESql sqlAccion) {
		this.sqlAccion = sqlAccion;
	}

	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Date getVigenciaIni() {
		return vigenciaIni;
	}

	public void setVigenciaIni(Date vigenciaIni) {
		this.vigenciaIni = vigenciaIni;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}
}