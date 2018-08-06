package mx.org.kaana.mantic.compras.requisiciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.requisiciones.beans.TicketRequisicion;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminTickets extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID= 8594649943986572245L;
	private static final Log LOG              = LogFactory.getLog(AdminTickets.class);

	private TicketRequisicion orden;

	public AdminTickets(TicketRequisicion orden) throws Exception {
		this(orden, true);
	}
	
	public AdminTickets(TicketRequisicion orden, boolean loadDefault) throws Exception {
		this.orden  = orden;
		if(this.orden.isValid()) {
  	  this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "TcManticRequisicionesDetallesDto", "detalle", orden.toMap()));      
		}	// if
		else	{
		  this.setArticulos(new ArrayList<>());
			this.orden.setConsecutivo("1");
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());			
		} // else	
		if(loadDefault)
			this.getArticulos().add(new Articulo(-1L));
		this.toCalculate();
	}

	@Override
	public Long getIdAlmacen() {
		return -1L;
	}

	public Long getIdCliente() {
		return -1L;
	}
	
	@Override
	public IBaseDto getOrden() {
		return orden;
	}

	@Override
	public void setOrden(IBaseDto orden) {
		this.orden= (TicketRequisicion)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return -1D;
	}
	
	@Override
	public String getDescuento() {
		return null;
	}
	
	@Override
	public String getExtras() {
		return null;
	}
	
	@Override
	public Long getIdSinIva() {
		return -1L;
	}
	
	@Override
	public void setIdSinIva(Long idSinIva) {
		//this.orden.setIdSinIva(idSinIva);
	}

	@Override
	public Long getIdProveedor() {
		return -1L;
	}

	@Override
	public void setDescuento(String descuento){
		//this.orden.setDescuento(descuento);
	}
}
