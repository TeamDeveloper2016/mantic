package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.facturas.beans.FacturaFicticia;
import mx.org.kaana.mantic.facturas.reglas.Transaccion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolControlGenerar")
public class Generar extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Generar.class);
  private static final long serialVersionUID = 5323749709626263801L;
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    this.toLoadCfdis();
  }
  
  @Override
  public void doLoad() {
  }
  
  public void doProcessTicket() {
		Map<String, Object> params= new HashMap<>();
    Encriptar encriptar       = new Encriptar();
		try {
      String codigo= encriptar.desencriptar((String)this.attrs.get("seguridad"));
      params.put("codigo", codigo);
      params.put("rfc", this.attrs.get("rfc"));
      params.put("folio", this.attrs.get("folio"));
      FacturaFicticia venta= (FacturaFicticia)DaoFactory.getInstance().toEntity(FacturaFicticia.class, "TcManticVentasDto", "codigo", params);
      if(venta!= null) {
        if(Cadena.isVacio(venta.getIdFactura()) && this.toCheckDate(venta)) {
          TcManticClientesDto cliente= (TcManticClientesDto)DaoFactory.getInstance().toEntity(TcManticClientesDto.class, "TcManticClientesDto", "rfc", params);
          if(cliente!= null && !Cadena.isVacio(cliente.getIdFacturama())) {
            if(Objects.equals(Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY, venta.getIdCliente()) || Objects.equals(cliente.getIdCliente(), venta.getIdCliente())) {
              if(Objects.equals(venta.getIdFicticiaEstatus(), EEstatusVentas.PAGADA.getIdEstatusVenta())) {
                if(venta.getTotal()>= 50D) {
                  venta.setIdUsoCfdi(((UISelectEntity)this.attrs.get("cfdi")).getKey());
                  venta.setIdCliente(cliente.getIdCliente());
                  Transaccion transaccion = new Transaccion(venta, "ESTA FACTURA SE GENERO EN LINEA");
                  if(transaccion.ejecutar(EAccion.TRANSFORMACION))
                    this.toLoadDocumentos();
                  else
                    JsfBase.addAlert("Error", "Ocurrio un error al intentar facturar !", ETipoMensaje.ERROR);
                } // if
                else 
                 JsfBase.addAlert("Error", "Este ticket no puede ser facturado, el importe es menor a 50MX !", ETipoMensaje.ERROR);
              } // if
              else 
               JsfBase.addAlert("Error", "Este ticket no puede ser facturado !", ETipoMensaje.ERROR);
            } // if
            else 
             JsfBase.addAlert("Error", "Este ticket corresponde a otro cliente !", ETipoMensaje.ERROR);
          } // if
          else 
            JsfBase.addAlert("Error", "Este cliente no puede facturar, consulte al negocio !", ETipoMensaje.ERROR);
        } // if
        else 
          if(!Cadena.isVacio(venta.getIdFactura()) && venta.getIdFactura()> 0L) {
            JsfBase.addAlert("Error", "Este ticket ya fue facturado !", ETipoMensaje.ERROR);
            this.toLoadDocumentos();
          } // if
          else
            JsfBase.addAlert("Error", "Este ticket ya expiró, solo se facturan tickets del mismo mes !", ETipoMensaje.ERROR);
      } // if
      else 
        JsfBase.addAlert("Error", "Los datos capturados son incorrectos !", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addAlert("Error", "No se puedo factura el ticket, consulte al gerente !", ETipoMensaje.ERROR);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		this.attrs.put("seguridad", "");
  }
  
  private Boolean toCheckDate(FacturaFicticia venta) {
    Calendar calendar= Calendar.getInstance();
    calendar.setTimeInMillis(venta.getRegistro().getTime());
    return Objects.equals(calendar.get(Calendar.YEAR), Fecha.getAnioActual()) && Objects.equals(calendar.get(Calendar.MONTH), Fecha.getMesActual()- 1);
  }
 
	private void toLoadCfdis() {
		List<UISelectEntity> cfdis= null;
		List<Columna> columns     = null;
		Map<String, Object>params = null;
		try {
			params = new HashMap<>();
			columns= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			cfdis= UIEntity.build("TcManticUsosCfdiDto", "row", params, columns, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("cfdis", cfdis);
      if(cfdis!= null && !cfdis.isEmpty())
        for(Entity item: cfdis) {
          Value nombre= item.get("nombre");
          if(nombre!= null && nombre.getData()!= null && ((String)nombre.getData()).length()> 30) 
            nombre.setData(((String)nombre.getData()).substring(0, 30).concat(" ..."));
          if(Objects.equals(item.getKey(), 3L))
            this.attrs.put("cfdi", item);
        } // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadCfdis
  
}