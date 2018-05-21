package mx.org.kaana.mantic.compras.entradas.reglas;

import java.io.Serializable;
import java.util.Collections;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
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

public final class AdminNotas extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID=-5550539230224591510L;
	private static final Log LOG=LogFactory.getLog(AdminNotas.class);

	private NotaEntrada orden;

	public AdminNotas(NotaEntrada orden) throws Exception {
		super(!orden.isValid(), orden.toMap());
		this.orden  = orden;
		if(this.orden.isValid()) {
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdProveedor())));
		}	// if
		else	{
			this.orden.setConsecutivo(this.toConsecutivo("0"));
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		} // else	
	}

	@Override
	public IBaseDto getOrden() {
		return orden;
	}

	@Override
	public void setOrden(IBaseDto orden) {
		this.orden= (NotaEntrada)orden;
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
	
	public static void main(String ... args) throws Exception {
		AdminNotas notas= new AdminNotas(new NotaEntrada());
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 1.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 10L, -1L, 10L, 0D, -1L));
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 2.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 5L, -1L, 1L, 0D, -1L));
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 3.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 4L, -1L, 2L, 0D, -1L));
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 4.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 8L, -1L, 1L, 0D, -1L));
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 5.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 4L, -1L, 2L, 0D, -1L));
		notas.getArticulos().add(new Articulo(true, 1.0, "", "", 6.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 100L, -1L, 10L, 0D, -1L));
		Collections.sort(notas.getArticulos());
		for (Articulo item : notas.getArticulos()) {
  		LOG.info(item.getIdArticulo()+ "->"+ item.getCosto()+ "->"+ item.getCantidad());
		} // 
		LOG.info("-----------------------------------");
		notas.toAdjustArticulos();
		for (Articulo item : notas.getArticulos()) {
  		LOG.info(item.getIdArticulo()+ "->"+ item.getCosto()+ "->"+ item.getCantidad());
		} // 
	}	

}
