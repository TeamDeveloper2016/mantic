package mx.org.kaana.mantic.inventarios.creditos.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.sql.Entity;
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
	private UISelectEntity ikNotaEntrada;
	private UISelectEntity ikProveedor;

	public NotaCredito() {
		this(null);
	}

	public NotaCredito(Long key) {
		this(key, -1L);
	}

	public NotaCredito(Long key, Long idDevolucion) {
		this(-1L, -1L, 0D, new Long(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance().get(Calendar.YEAR)+ "00000", 1L, 1L, idDevolucion, JsfBase.getIdUsuario(), "", "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 1L, key, 0D);
	}

	public NotaCredito(Long idProveedor, Long idNotaEntrada, Double importe, Long ejercicio, String consecutivo, Long idCreditoEstatus, Long idTipoCreditoNota, Long idDevolucion, Long idUsuario, String folio, String observaciones, Long idEmpresa, Long orden, Long idCreditoNota, Double saldo) {
    super(idProveedor, idNotaEntrada, importe, ejercicio, consecutivo, idCreditoEstatus, idTipoCreditoNota, idDevolucion, idUsuario, folio, observaciones, idEmpresa, orden, idCreditoNota, new Date(Calendar.getInstance().getTimeInMillis()), saldo);
		this.ikDevolucion = new UISelectEntity(new Entity(idDevolucion));
		this.ikNotaEntrada= new UISelectEntity(new Entity(idNotaEntrada));
		this.ikProveedor  = new UISelectEntity(new Entity(idProveedor));
	}

	public UISelectEntity getIkDevolucion() {
		return ikDevolucion;
	}

	public void setIkDevolucion(UISelectEntity ikDevolucion) {
		this.ikDevolucion= ikDevolucion;
		if(this.ikDevolucion!= null)
  	  this.setIdDevolucion(this.ikDevolucion.getKey());
	}

	public UISelectEntity getIkNotaEntrada() {
		return ikNotaEntrada;
	}

	public void setIkNotaEntrada(UISelectEntity ikNotaEntrada) {
		this.ikNotaEntrada=ikNotaEntrada;
		if(this.ikNotaEntrada!= null)
  	  this.setIdNotaEntrada(this.ikNotaEntrada.getKey());
	}

	public UISelectEntity getIkProveedor() {
		return ikProveedor;
	}

	public void setIkProveedor(UISelectEntity ikProveedor) {
		this.ikProveedor=ikProveedor;
		if(this.ikProveedor!= null)
  	  this.setIdProveedor(this.ikProveedor.getKey());
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticCreditosNotasDto.class;
	}
	
}
