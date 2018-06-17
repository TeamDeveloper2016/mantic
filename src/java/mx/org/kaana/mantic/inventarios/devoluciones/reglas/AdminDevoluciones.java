package mx.org.kaana.mantic.inventarios.devoluciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.inventarios.devoluciones.beans.Devolucion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminDevoluciones extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID=-5550539230224591509L;
	private static final Log LOG=LogFactory.getLog(AdminDevoluciones.class);

	private Devolucion orden;

	public AdminDevoluciones(Devolucion orden) throws Exception {
		this.orden= orden;
		if(this.orden.isValid()) 
 	    this.setArticulos(this.toLoadOrdenDetalle());
		else {
		  this.setArticulos(this.toDefaultOrdenDetalle());
			this.orden.setConsecutivo(this.toConsecutivo("0"));
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		} // else	
		for (Articulo articulo : this.getArticulos()) {
			if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()<= 0)
				articulo.setIdOrdenDetalle(null);
		} // for
		this.toCalculate();
	}

	@Override
	public Long getIdAlmacen() {
		return -1L;
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
		this.orden= (Devolucion)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return 1D;
	}
	
	@Override
	public String getDescuento() {
		return "";
	}
	
	@Override
	public String getExtras() {
		return "";
	}
	
	@Override
	public Long getIdSinIva() {
		return 1L;
	}
	
	@Override
	public void setIdSinIva(Long idSinIva) {
	}

	private ArrayList<Articulo> toLoadOrdenDetalle() throws Exception {
		ArrayList<Articulo> regresar= new ArrayList<>((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "TcManticNotasDetallesDto", "detalle", this.orden.toMap()));
		ArrayList<Articulo> loaded  = new ArrayList<>((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaNotasEntradasDto", "diferencia", this.orden.toMap()));
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			for (Articulo item: loaded) {
  			params.put("idArticulo", item.getIdArticulo());
        Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", this.orden.toMap(), "stock");
        int index= regresar.indexOf(item);
				item.setStock(stock== null? 0L: stock.toLong());
				if(index< 0) 
					regresar.add(item);
				else {
					((Articulo)regresar.get(index)).setValor(item.getCosto());
					((Articulo)regresar.get(index)).setStock(stock== null? 0L: stock.toLong());
				} // else	
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(loaded);
		} // finally
		return regresar;
	}
	private ArrayList<Articulo> toDefaultOrdenDetalle() throws Exception {
		ArrayList<Articulo> regresar= new ArrayList<>((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaNotasEntradasDto", "diferencia", this.orden.toMap()));
		Map<String, Object> params  = null;
		try {
			params=new HashMap<>();
			for (Articulo item: regresar) {
  			params.put("idArticulo", item.getIdArticulo());
        Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", this.orden.toMap(), "stock");
				item.setStock(stock== null? 0L: stock.toLong());
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	@Override
	public void setDescuento(String descuento) {
		this.orden.setDescuento(descuento);
	}
	
}
