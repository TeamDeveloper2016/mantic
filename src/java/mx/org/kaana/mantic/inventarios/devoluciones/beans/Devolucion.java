package mx.org.kaana.mantic.inventarios.devoluciones.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Devolucion extends TcManticDevolucionesDto implements Serializable {

	private static final long serialVersionUID=3088884892456452481L;
	
	private UISelectEntity ikNotaEntrada;

	public Devolucion() {
		this(null);
	}

	public Devolucion(Long idNotaEntrada) {
		this(-1L, idNotaEntrada);
	}

	public Devolucion(Long key, Long idNotaEntrada) {
		this(0D, "", "", idNotaEntrada, new Long(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance().get(Calendar.YEAR)+ "00000", -1L, 0D, -1L, -1L, 0D, 0D, "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 1L);
	}

	public Devolucion(Double descuentos, String descuento, String extras, Long idNotaEntrada, Long ejercicio, String consecutivo, Long idDevolucionEstatus, Double total, Long idDevolucion, Long idUsuario, Double impuestos, Double subTotal, String observaciones, Long idEmpresa, Long orden) {
		super(descuentos, descuento, extras, idNotaEntrada, ejercicio, consecutivo, idDevolucionEstatus, total, idDevolucion, idUsuario, impuestos, subTotal, observaciones, idEmpresa, orden);
	}

	public UISelectEntity getIkNotaEntrada() {
		return ikNotaEntrada;
	}

	public void setIkNotaEntrada(UISelectEntity ikNotaEntrada) {
		this.ikNotaEntrada= ikNotaEntrada;
		if(this.ikNotaEntrada!= null)
  	  this.setIdNotaEntrada(this.ikNotaEntrada.getKey());
	}

	@Override
	public Class toHbmClass() {
		return TcManticDevolucionesDto.class;
	}
	
}
