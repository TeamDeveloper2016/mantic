package mx.org.kaana.mantic.ventas.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;

public class SaldoCliente implements Serializable {

	private static final long serialVersionUID = 5429344596542874600L;
	private Long idCliente;
	private Double totalDeuda;
	private Double totalCredito;
	private Double totalVenta;
  private Boolean vencidas;

	public SaldoCliente() {
		this(Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY, 0D, 0D, 0D);
	}

	public SaldoCliente(Long idCliente, Double totalDeuda, Double totalCredito, Double totalVenta) {
		this.idCliente   = idCliente;
		this.totalDeuda  = totalDeuda;
		this.totalCredito= totalCredito;
		this.totalVenta  = totalVenta;
    this.vencidas    = this.toCheckCredito();
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
    if(idCliente!= null)
      this.vencidas= this.toCheckCredito();
	}

	public Double getTotalDeuda() {
		return totalDeuda;
	}

	public void setTotalDeuda(Double totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public Double getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Double getTotalSaldo() {
		return this.totalCredito - (this.totalDeuda + this.totalVenta);
	}	

	public Double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}	
	
	public boolean isDeudor() {
		boolean regresar= false;
		if(this.totalCredito> 0) {
			Double totalSaldo= this.totalCredito- (this.totalDeuda + this.totalVenta);
			regresar= totalSaldo < 0;
		} // if
		return regresar || this.vencidas; 
	} // isDeudor
	
  private Boolean toCheckCredito() {
    Boolean regresar= Boolean.FALSE;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();      
      params.put("idCliente", this.idCliente);
			params.put("sortOrder", "order by dias desc, tc_mantic_ventas.ticket desc");
      params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes_deudas.id_cliente_estatus in (1, 2)");
      Value value= DaoFactory.getInstance().toField("VistaClientesDto", "detalle", params, "dias");
      if(value!= null && value.getData()!= null)
        regresar= value.toLong()> 0L;
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
	public String getMensaje() {
		return this.isDeudor()? "CRÉDITO SUPERADO Y/O PLAZO VENCIDO. CONSULTAR CON CRÉDITO Y COBRANZA" : "";
	}
  
}
