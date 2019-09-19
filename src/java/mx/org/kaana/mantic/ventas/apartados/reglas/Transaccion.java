package mx.org.kaana.mantic.ventas.apartados.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private String messageError;
	private TcManticApartadosPagosDto pago;
	private Long idCaja;
	private Long idEmpresa;
	private Long idCierreActivo;
	private Long idBanco;
	private Long idVenta;
	private Long idTipoMedioPago;
	private Double devuelto;
	private String referencia;
  private TcManticApartadosBitacoraDto bitacora;

	public Transaccion(TcManticApartadosBitacoraDto bitacora, Long idVenta) {
		this.bitacora= bitacora;
    this.idVenta = idVenta;
	} // Transaccion

  public Transaccion(Double devuelto,Long idCaja,  Long idBanco, Long idTipoMedioPago, Long idVenta, String referencia, TcManticApartadosBitacoraDto bitacora, Long idEmpresa) {
    this.devuelto       = devuelto;
    this.idCaja         = idCaja;
    this.idBanco        = idBanco;
    this.idTipoMedioPago= idTipoMedioPago;
    this.idVenta        = idVenta;
    this.referencia     = referencia;
    this.bitacora       = bitacora;
    this.idEmpresa      = idEmpresa;
  }
  
  public Transaccion(TcManticApartadosPagosDto pago, Long idCaja, Long idEmpresa, Long idBanco, String referencia) {
		this(pago, idCaja, -1L, idEmpresa, idBanco, referencia);
	} // Transaccion
	
	public Transaccion(TcManticApartadosPagosDto pago, Long idCaja, Long idVenta, Long idEmpresa, Long idBanco, String referencia) {
		this.pago      = pago;
		this.idVenta   = idVenta;
		this.idCaja    = idCaja;
		this.idEmpresa = idEmpresa;
		this.idBanco   = idBanco;
		this.referencia= referencia;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:					
					regresar = procesarPago(sesion);
          break;        
        case JUSTIFICAR:
					regresar = insertarBitacora(sesion);
					if(this.devuelto!=null)
						regresar = registrarPago(sesion, this.bitacora.getIdApartado(), (0D-this.devuelto));
					break;		
      } // switch
      if (!regresar) 
        throw new Exception("");      
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar
	
	private boolean procesarPago(Session sesion) throws Exception{
		boolean regresar               = false;
		TcManticApartadosDto apartado  = null;
		Double saldo                   = 0D;
		Double abonado                 = 0D;
    Map<String, Object>params      = null;
		try {
			if(toCierreCaja(sesion, this.pago.getPago())){
				this.pago.setIdCierre(this.idCierreActivo);				
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					this.pago.setReferencia(this.referencia);
					this.pago.setIdBanco(this.idBanco);
				} // if
				if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
					apartado= (TcManticApartadosDto) DaoFactory.getInstance().findById(sesion, TcManticApartadosDto.class, this.pago.getIdApartado());
					saldo= apartado.getSaldo() - this.pago.getPago();
					abonado= apartado.getAbonado() + this.pago.getPago();
					apartado.setSaldo(saldo);
					apartado.setAbonado(abonado);
					apartado.setIdApartadoEstatus(saldo.equals(0L) ? 3L : 2L);
					regresar= DaoFactory.getInstance().update(sesion, apartado)>= 1L;
          params= new HashMap<>();
					params.put(Constantes.SQL_CONDICION, "id_apartado = ".concat(apartado.getIdApartado().toString()));
          this.bitacora = (TcManticApartadosBitacoraDto)DaoFactory.getInstance().findFirst(TcManticApartadosBitacoraDto.class, params);
          //preguntar si al iniciar inserta bitacora, si no cambiar a crear instancia en lugar de consultar
          if(this.bitacora!= null){
            this.bitacora.setIdApartadoEstatus(apartado.getIdApartadoEstatus());
            this.bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            this.bitacora.setIdUsuario(JsfBase.getIdUsuario());
            regresar = insertarBitacora(sesion);
          } // if 
				} // if
			} // if
		} // try		
		finally{
			Methods.clean(params);
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	private boolean registrarPago(Session sesion, Long idApartado, Double pagoParcial) throws Exception{
		TcManticApartadosPagosDto registroPago= null;
		boolean regresar                     = false;		
		if(toCierreCaja(sesion, (pagoParcial))){
			registroPago= new TcManticApartadosPagosDto();
			registroPago.setIdApartado(idApartado);
			registroPago.setIdUsuario(JsfBase.getIdUsuario());
			registroPago.setObservaciones(this.pago!= null?"Pago aplicado los apartados generales del cliente. ".concat(this.pago.getObservaciones()):"Monto devuelto al cliente");
			registroPago.setPago(pagoParcial);
			registroPago.setIdTipoMedioPago(this.pago!= null? this.pago.getIdTipoMedioPago():this.idTipoMedioPago);
			registroPago.setIdCierre(this.idCierreActivo);
			if(!(this.pago!= null?this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago()):this.idTipoMedioPago.equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago()))){
				registroPago.setIdBanco(this.idBanco);
				registroPago.setReferencia(this.referencia);
			} // if
			regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
		} // if		
		return regresar;
	} // registrarPago
	
	private List<Entity> toApartados(Session sesion) throws Exception {
		List<Entity> regresar    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.idEmpresa);
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			params.put("sortOrder", "order by registro desc");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaTcManticApartadosDto", "apartados", params);			
		} // try
		finally {			
			Methods.clean(params);
		} // catch		
		return regresar;
	} // toApartados	
	
	private boolean toCierreCaja(Session sesion, Double pago) throws Exception{
		mx.org.kaana.mantic.ventas.caja.reglas.Transaccion cierre= null;
		VentaFinalizada datosCierre= null;
		boolean regresar= false;		
		datosCierre= new VentaFinalizada();
		datosCierre.getTicketVenta().setIdEmpresa(this.idEmpresa);
		datosCierre.setIdCaja(this.idCaja);
		datosCierre.getTotales().setEfectivo(pago);
		cierre= new mx.org.kaana.mantic.ventas.caja.reglas.Transaccion(datosCierre);
		if(cierre.verificarCierreCaja(sesion)){
			this.idCierreActivo= cierre.getIdCierreVigente();
			regresar= cierre.alterarCierreCaja(sesion,this.pago!=null? this.pago.getIdTipoMedioPago(): this.idTipoMedioPago);
		} // if		
		return regresar;
	} // toCierreCaja	

  private boolean insertarBitacora(Session sesion) throws Exception {
    boolean regresar               = false;
    TcManticApartadosDto apartado  = null;		
		if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
			apartado = (TcManticApartadosDto) DaoFactory.getInstance().findById(sesion, TcManticApartadosDto.class, this.bitacora.getIdApartado());
			apartado.setIdApartadoEstatus(this.bitacora.getIdApartadoEstatus());
			if(apartado.getIdApartadoEstatus().equals(6L)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(apartado.getVencimiento());
				calendar.add(Calendar.DAY_OF_YEAR, 30);
				apartado.setVencimiento(new java.sql.Date(calendar.getTime().getTime()));
			}
			regresar= DaoFactory.getInstance().update(sesion, apartado)>= 1L;
			if(apartado.getIdApartadoEstatus().equals(4L))
				regresar = alterarStockArticulos(sesion);
		} // if		
		return regresar;
  } // insertarBitacora

  private boolean alterarStockArticulos(Session sesion) throws Exception{
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		TcManticArticulosDto articuloVenta           = null;		
		Map<String, Object>params                    = null;
		boolean regresar                             = false;
		int count                                    = 0; 
		try {			
      params= new HashMap<>();
			params.put("idVenta", this.idVenta);
      IAdminArticulos adminOrden = new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false);
			for(Articulo articulo: adminOrden.getArticulos()){
				params.put(Constantes.SQL_CONDICION, "id_articulo="+ articulo.getIdArticulo()+ " and id_almacen= "+ adminOrden.getIdAlmacen());
				almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
				if(almacenArticulo!= null){
					almacenArticulo.setStock(almacenArticulo.getStock() + articulo.getCantidad());
					regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
				} // if
				if(regresar){
					articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
					articuloVenta.setStock(articuloVenta.getStock() + articulo.getCantidad());
					if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
						regresar= actualizaInventario(sesion, articulo.getIdArticulo(), articulo.getCantidad(), adminOrden.getIdAlmacen());
				} // if
				if(regresar)
					count++;
			} // for		
			regresar= count== adminOrden.getArticulos().size();			
		} // try		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // alterarStockArticulos
  
  private boolean actualizaInventario(Session sesion, Long idArticulo, Double cantidad, Long idAlmacen) throws Exception{
		boolean regresar                 = false;
		TcManticInventariosDto inventario= null;
		Map<String, Object>params        = null;
		try {
			params= new HashMap<>();
			params.put("idAlmacen", idAlmacen);//el almacen es por artículo o por venta.?
			params.put("idArticulo", idArticulo);
			inventario= (TcManticInventariosDto) DaoFactory.getInstance().toEntity(sesion, TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario!= null){
				inventario.setSalidas(inventario.getSalidas() - cantidad);
				inventario.setStock(inventario.getStock() + cantidad);
				regresar= DaoFactory.getInstance().update(sesion, inventario)>= 1L;
			} // if				
		} // try
		finally {			
			Methods.clean(params);
		} // catch		
		return regresar;
	} // actualizaInventario
}
