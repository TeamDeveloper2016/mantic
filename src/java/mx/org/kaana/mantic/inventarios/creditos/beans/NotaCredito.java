package mx.org.kaana.mantic.inventarios.creditos.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class NotaCredito extends TcManticCreditosNotasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452481L;
	
	private UISelectEntity ikDevolucion;

	public NotaCredito() {
		this(null);
	}

	public NotaCredito(Long idDevolucion) {
		this(-1L, idDevolucion);
	}

	public NotaCredito(Long key, Long idDevolucion) {
		this(Calendar.getInstance().get(Calendar.YEAR)+ "00000", 1L, idDevolucion, JsfBase.getIdUsuario(), "", "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 1L, -1L, 0D, new Long(Calendar.getInstance().get(Calendar.YEAR)));
	}

	public NotaCredito(String consecutivo, Long idCreditoEstatus, Long idDevolucion, Long idUsuario, String folio, String observaciones, Long idEmpresa, Long orden, Long idCreditoNota, Double importe, Long ejercicio) {
    super(consecutivo, idCreditoEstatus, idDevolucion, idUsuario, folio, observaciones, idEmpresa, orden, idCreditoNota, importe, ejercicio);
	}

	public UISelectEntity getIkDevolucion() {
		return ikDevolucion;
	}

	public void setIkDevolucion(UISelectEntity ikDevolucion) {
		this.ikDevolucion= ikDevolucion;
		if(this.ikDevolucion!= null)
  	  this.setIdCreditoNota(this.ikDevolucion.getKey());
	}

	@Override
	public Class toHbmClass() {
		return TcManticCreditosNotasDto.class;
	}
	
}
