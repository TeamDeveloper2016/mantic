package mx.org.kaana.mantic.ventas.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;

public class MotorBusqueda implements Serializable{

	private static final long serialVersionUID = -1476191556651225342L;	
	private Long idArticulo;
	private Long idCliente;
	
	public MotorBusqueda(Long idArticulo) {
		this(idArticulo, null);
	}	// MotorBusqueda

	public MotorBusqueda(Long idArticulo, Long idCliente) {
		this.idArticulo= idArticulo;
		this.idCliente = idCliente;
	}	
	
	public TcManticArticulosDto toArticulo() throws Exception {
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toArticulo
	
	public Entity toDescuentoGrupo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", this.idArticulo);
			params.put("idCliente", this.idCliente);
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "descuentoGrupoVigente", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDescuentoGrupo
	
	public Entity toCliente() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes.id_cliente=" + this.idCliente);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "findRazonSocial", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCliente
}
