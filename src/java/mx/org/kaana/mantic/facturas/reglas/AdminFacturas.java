package mx.org.kaana.mantic.facturas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.facturas.beans.FacturaFicticia;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminFacturas extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID= 8594649943986572245L;
	private static final Log LOG              = LogFactory.getLog(AdminFacturas.class);

	private FacturaFicticia orden;

	public AdminFacturas(FacturaFicticia orden) throws Exception {
		this(orden, true);
	}
	
	public AdminFacturas(FacturaFicticia orden, boolean loadDefault) throws Exception {
		List<ArticuloVenta> arts= null;
		this.orden= orden;		
		if(this.orden.isValid()) {
			arts= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaFicticiasDto", "detalle", orden.toMap());
  	  this.setArticulos(arts);      
		}	// if
		else	{
		  arts= new ArrayList<>();
		  this.setArticulos(arts);			
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		} // else	
		if(loadDefault)
			this.getArticulos().add(new ArticuloVenta(-1L));
		setIdSinIva(1L);
		this.toCalculate();
	}
	
	@Override
	public IBaseDto getOrden() {
		return orden;
	}

	@Override
	public void setOrden(IBaseDto orden) {
		this.orden= (FacturaFicticia)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return this.orden.getTipoDeCambio();
	}
	
	@Override
	public String getDescuento() {
		return this.orden.getDescuento();		
	}
	
	@Override
	public String getExtras() {
		return this.orden.getExtras();
	}
	
	@Override
	public Long getIdSinIva() {
		return this.orden.getIdSinIva();
	}
	
	@Override
	public void setIdSinIva(Long idSinIva) {
		this.orden.setIdSinIva(idSinIva);
	}

	@Override
	public Long getIdProveedor() {
		return -1L;
	}

	@Override
	public void setDescuento(String descuento){
		this.orden.setDescuento(descuento);
	}

	@Override
	public Long getIdAlmacen() {
		return -1L;
	}
}
