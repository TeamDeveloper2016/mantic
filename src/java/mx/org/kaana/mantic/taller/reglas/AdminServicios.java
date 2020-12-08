package mx.org.kaana.mantic.taller.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.taller.beans.Servicio;
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

public final class AdminServicios extends IAdminArticulos  implements Serializable {

	private static final long serialVersionUID=-5550539230224561510L;
	private static final Log LOG              = LogFactory.getLog(AdminServicios.class);

	private Servicio orden;

	public AdminServicios(Servicio orden) throws Exception {
		this.orden                = orden;
		Map<String, Object> params= new HashMap<>();		
		try {
      if(this.orden.isValid()) {
        this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaServiciosDetallesDto", "detalle", orden.toMap()));
        // AQUI FALTA BUSCAR EL STOCK DE LA REFACCION EN EL ALMACEN DEL TALLER AUN FALTA DEFINIR COMO QUEDARA EL TALLER
        // ID_AUTOMATICO ALMACENA EL VALOR DE ID_VALIDO DONDE 1 EXISTE, 3 EXISTE PERO NO EXISTIA, 2 NO EXISTE, ID_COMODIN ALMACEN EL VALOR DE ID_ARTICULO_TIPO 
        if(this.getArticulos()!= null && !this.getArticulos().isEmpty())
          for (Articulo articulo: this.getArticulos()) {
            if(Objects.equals(articulo.getIdAutomatico(), 1L) || Objects.equals(articulo.getIdAutomatico(), 3L)) {
              params.put("idAlmacen", Objects.equals(articulo.getIdComodin(), 1L)? JsfBase.getAutentifica().getEmpresa().getIdAlmacen(): orden.getIdAlmacen());
              params.put("idArticulo", articulo.getIdArticulo());
              Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
              articulo.setStock(stock== null? 0D: stock.toDouble());
            } // if  
          } // for
      } // if  
      else	{
        this.setArticulos(new ArrayList<>());
        this.orden.setConsecutivo(this.toConsecutivo("0"));
        this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
        this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      } // else	
      this.getArticulos().add(new Articulo(-1L));
      this.toCalculate();
		} // try
		finally {
			Methods.clean(params);
		} // finally
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
		this.orden= (Servicio)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return 1D;
	}
	
	@Override
	public String getDescuento() {
		return this.orden.getDescuento();
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

	@Override
	public void setDescuento(String descuento) {
		this.orden.setDescuento(descuento);
	}	
	
}
