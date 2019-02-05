package mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/01/2019
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminConfronta extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID=-5550539234221541510L;
	private static final Log LOG=LogFactory.getLog(AdminConfronta.class);

	private Confronta orden;

	public AdminConfronta(Confronta orden) throws Exception {
		this.orden= orden;
		if(this.orden.isValid()) {
  	  this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaConfrontasDto", "detalle", orden.toMap()));
			this.orden.init();
		}	// if
		else {
  	  this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaConfrontasDto", "diferencia", orden.toMap()));
			this.orden.setConsecutivo(this.toConsecutivo("0"));
		} // else	
		this.toLoadStockArticulos();
		this.orden.setIkEmpresa(new UISelectEntity(new Entity(this.orden.getTransferencia().getIdAlmacen())));
		this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getTransferencia().getIdAlmacen())));
		this.orden.setIkDestino(new UISelectEntity(new Entity(this.orden.getTransferencia().getIdDestino())));
		if(this.orden.getTransferencia().getIdSolicito()!= null)
	  	this.orden.setIkSolicito(new UISelectEntity(new Entity(this.orden.getTransferencia().getIdSolicito())));
		this.getArticulos().add(new Articulo(-1L));
		this.toCalculate();
	}

	private void toLoadStockArticulos() throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			for (Articulo item: this.getArticulos()) {
  			params.put("idAlmacen", this.orden.getTransferencia().getIdAlmacen());
  			params.put("idArticulo", item.getIdArticulo());
				Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				item.setStock(stock== null? 0D: stock.toDouble());
			  // el almacen origen no tiene conteo 
			  item.setSolicitado(stock== null);
  			params.put("idAlmacen", this.orden.getTransferencia().getIdDestino());
				// recuperar el stock de articulos en el almacen destino
				stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				item.setValor(stock== null? 0D: stock.toDouble());
				// el almacen destino no tiene conteo
				item.setCostoLibre(stock== null);
				stock= (Value)DaoFactory.getInstance().toField("TcManticAlmacenesArticulosDto", "umbral", params, "maximo");
				// recuperar el maximo del catalogo de articulos
				if(stock== null) {
					TcManticArticulosDto articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, item.getIdArticulo());
					item.setCosto(articulo.getMaximo());
				} // if
				else	
					item.setCosto(stock.toDouble());
				item.setUltimo(item.getValor()> item.getCosto());
				item.setModificado(true);				
			} // for
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
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
