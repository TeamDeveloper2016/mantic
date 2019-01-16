package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Transferencia;
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

public final class AdminTransferencias extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID=-5550539230224591510L;
	private static final Log LOG=LogFactory.getLog(AdminTransferencias.class);

	private Transferencia orden;

	public AdminTransferencias(Transferencia orden) throws Exception {
		this.orden= orden;
		if(this.orden.isValid()) {
  	  this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", orden.toMap()));
      this.orden.setIkEmpresa(new UISelectEntity(new Entity(this.orden.getIdEmpresa())));
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkDestino(new UISelectEntity(new Entity(this.orden.getIdDestino())));
      this.orden.setIkSolicito(new UISelectEntity(new Entity(this.orden.getIdSolicito())));
		}	// if
		else {
		  this.setArticulos(new ArrayList<>());
			this.orden.setConsecutivo(this.toConsecutivo("0"));
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      this.orden.setIkEmpresa(new UISelectEntity(new Entity(this.orden.getIdEmpresa())));
		} // else	
		this.getArticulos().add(new Articulo(-1L));
		this.toCalculate();
	}

	@Override
	public Long getIdAlmacen() {
		return this.orden.getIdAlmacen();
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
		this.orden= (Transferencia)orden;
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
