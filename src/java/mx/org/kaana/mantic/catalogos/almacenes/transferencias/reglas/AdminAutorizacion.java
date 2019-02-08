package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/01/2019
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminAutorizacion extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID=-5550539234661541510L;
	private static final Log LOG=LogFactory.getLog(AdminAutorizacion.class);

	private Confronta orden;

	public AdminAutorizacion(Confronta orden) throws Exception {
		this.orden= orden;
 	  this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaConfrontasDto", "autorizar", orden.toMap()));
		if(this.getArticulos()!= null)
		  this.getTotales().setArticulos(this.getArticulos().size());
	}

	@Override
	public Long getIdAlmacen() {
		return this.orden.getTransferencia().getIdAlmacen();
	}

	@Override
	public Long getIdProveedor() {
		return -1L;
	}

	@Override
	public IBaseDto getOrden() {
		return orden;
	}

	@Override
	public void setOrden(IBaseDto orden) {
		this.orden= (Confronta)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return 1D;
	}
	
	@Override
	public String getDescuento() {
		return "0";
	}
	
	@Override
	public String getExtras() {
		return "0";
	}
	
	@Override
	public Long getIdSinIva() {
		return 1L;
	}
	
	@Override
	public void setIdSinIva(Long idSinIva) {
		LOG.warn("Este metodo no se utiliza para este proceso "+ idSinIva);
	}

	@Override
	public void setDescuento(String descuento) {
		LOG.warn("Este metodo no se utiliza para este proceso "+ descuento);
	}	
	
}
