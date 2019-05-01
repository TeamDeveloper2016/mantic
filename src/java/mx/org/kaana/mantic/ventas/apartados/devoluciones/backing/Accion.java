package mx.org.kaana.mantic.ventas.apartados.devoluciones.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.garantias.reglas.AdminGarantia;

@Named(value= "manticVentasApartadosDevolucionesAccion")
@ViewScoped
public class Accion extends mx.org.kaana.mantic.ventas.garantias.backing.Accion implements Serializable {

  private static final long serialVersionUID= 327393488565639367L;
	
	@PostConstruct
  @Override
  protected void init() {	
    try {
			super.init();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));			
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminGarantia(new TicketVenta(-1L), eaccion));
          break;
        case MODIFICAR:			
        case CONSULTAR:			
          this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "VistaTcManticGarantiasArticulosDto", "garantia", this.attrs), eaccion, Long.valueOf(this.attrs.get("idGarantia").toString())));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
    			this.attrs.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
					loadDatosCliente(((TicketVenta)getAdminOrden().getOrden()).getIdVenta());
          break;
      } // switch
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			loadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad	
	
	@Override
	protected String toCondicionOpenTicket() {
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();																
			regresar.append(" tc_mantic_ventas.ticket like '%");
			regresar.append(this.attrs.get("openTicket"));	
			regresar.append("%' and tc_mantic_ventas.id_venta_estatus=");			
			regresar.append(EEstatusVentas.APARTADOS.getIdEstatusVenta());												
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicionOpenTicket
}