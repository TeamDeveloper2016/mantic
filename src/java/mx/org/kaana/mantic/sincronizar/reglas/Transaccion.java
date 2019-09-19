package mx.org.kaana.mantic.sincronizar.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.sincronizar.beans.Ventas;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/05/2019
 *@time 11:50:41 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

	private static final long serialVersionUID= -5564624690812780724L;
	private static final Log LOG              = LogFactory.getLog(Transaccion.class);

	private ISincronizar documento;
	private String messageError;
	
	public Transaccion(ISincronizar documento) {
	  this.documento= documento;	
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar          = true;
		Map<String, Object> params= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para el proceso de sincronizar la información.");
			switch(this.documento.getDocumento()) {
				case VENTAS: 
					regresar= this.toVentas(sesion, (Ventas)this.documento);
					break;				
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally 
		return regresar;
	}

	private boolean toVentas(Session sesion, Ventas documento) throws Exception {
		boolean regresar= true;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			// INSERTAR LA VENTA PERO ANTES DE ESO SE TIENE QUE LIMPIAR EL ID_VENTA PARA QUE GENERE EL CORRESPONDIENTE A LA VENTA
			documento.getVenta().setIdVenta(-1L);
			regresar= DaoFactory.getInstance().insert(sesion, documento.getVenta()).intValue()>= 1;
			if(documento.getVdetalle()!= null && !documento.getVdetalle().isEmpty())
				for (TcManticVentasDetallesDto item : documento.getVdetalle()) {
					item.setIdVentaDetalle(-1L);
					item.setIdVenta(documento.getVenta().getIdVenta());
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					// FALTA AFECTAR EL INVENTARIO Y LOS ALMACENES CORRESPONDIENTES Y SI ESTA MARCADA PARA FACTURAR ENTONCES LANZAR EL TIMBRADO DE LA VENTA
				} // for
			if(documento.getVbitacora()!= null && !documento.getVbitacora().isEmpty())
				for (TcManticVentasBitacoraDto item : documento.getVbitacora()) {
					item.setIdVentaBitacora(-1L);
					item.setIdVenta(documento.getVenta().getIdVenta());
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
				} // for
			if(documento.getVmedios()!= null && !documento.getVmedios().isEmpty())
				for (TrManticVentaMedioPagoDto item : documento.getVmedios()) {
					item.setIdVentaMedioPago(-1L);
					item.setIdVenta(documento.getVenta().getIdVenta());
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					// FALTA AFECTAR LOS IMPORTES DE CAJA DE ACUERDO A LOS TIPOS DE PAGO REALIZADOS
				} // for
			Long idClienteDeuda= -1L;
			if(documento.getVdeudas()!= null && !documento.getVdeudas().isEmpty())
				for (TcManticClientesDeudasDto item : documento.getVdeudas()) {
					item.setIdClienteDeuda(-1L);
					item.setIdVenta(documento.getVenta().getIdVenta());
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					idClienteDeuda= item.getIdClienteDeuda();
					// FALTA AFECTAR EL CATALOGO DE CLIENTES PARA MODIFICAR SU SALDO CORRESPONDIENTE
				} // for
			if(documento.getVpagos()!= null && !documento.getVpagos().isEmpty())
				for (TcManticClientesPagosDto item : documento.getVpagos()) {
					item.setIdClientePago(-1L);
					item.setIdClienteDeuda(idClienteDeuda);
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					// FALTA AFECTAR LOS IMPORTES DE CAJA DE ACUERDO A AL TIPO DE PAGO REALIZADOS
				} // for
			Long idApartado= -1L;
			if(documento.getApartados()!= null && !documento.getApartados().isEmpty())
				for (TcManticApartadosDto item : documento.getApartados()) {
					item.setIdApartado(-1L);
					item.setIdVenta(documento.getVenta().getIdVenta());
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					idApartado= item.getIdApartado();
				} // for
			if(documento.getAbitacora()!= null && !documento.getAbitacora().isEmpty())
				for (TcManticApartadosBitacoraDto item : documento.getAbitacora()) {
					item.setIdApartadoBitacora(-1L);
					item.setIdApartado(idApartado);
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
				} // for
			if(documento.getApagos()!= null && !documento.getApagos().isEmpty())
				for (TcManticApartadosPagosDto item : documento.getApagos()) {
					item.setIdApartadoPago(-1L);
					item.setIdApartado(idApartado);
					regresar= DaoFactory.getInstance().insert(sesion, item).intValue()>= 1;
					// FALTA AFECTAR LOS IMPORTES DE CAJA DE ACUERDO A AL TIPO DE PAGO REALIZADOS
				} // for
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

}
