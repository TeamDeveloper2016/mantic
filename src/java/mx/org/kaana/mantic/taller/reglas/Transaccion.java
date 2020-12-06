package mx.org.kaana.mantic.taller.reglas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusServicios;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import org.hibernate.Session;

public class Transaccion extends TransaccionFactura{

	private static final Long ELABORADA= 1L;
	private Totales totales;
	private IBaseDto dto;
  private RegistroServicio registroServicio;
  private String messageError;
	private List<Articulo> articulos;
	private Long idServicio;	
  private TcManticOrdenesComprasDto ordenCompra;
          
	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	} // Transaccion
	
  public Transaccion(RegistroServicio registroServicio) {
    this.registroServicio = registroServicio;
  } // Transaccion

	public Transaccion(List<Articulo> articulos, Long idServicio, Totales totales) {
		this.articulos = articulos;
		this.idServicio= idServicio;		
		this.totales   = totales;
	} // Transaccion

  public TcManticOrdenesComprasDto getOrdenCompra() {
    return ordenCompra;
  }
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar                     = false;
		TcManticServiciosDto servicio        = null;
		TcManticServiciosBitacoraDto bitacora= null;
    try {
			if(this.registroServicio!= null)
				this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
        case AGREGAR:
          regresar= this.procesarServicio(sesion);
          break;
        case MODIFICAR:
          regresar= this.actualizarServicio(sesion);
          break;
        case ELIMINAR:
          regresar= this.eliminarServicio(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
				case JUSTIFICAR:
					bitacora= (TcManticServiciosBitacoraDto) this.dto;
					bitacora.setConsecutivo("");
					if(DaoFactory.getInstance().insert(sesion, bitacora)>= 1L) {
						servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, bitacora.getIdServicio());
						servicio.setIdServicioEstatus(bitacora.getIdServicioEstatus());
						regresar= DaoFactory.getInstance().update(sesion, servicio)>= 1L;
            // DAR DE ALTA LOS SERVICIOS Y REFACCIONES QUE NO ESTEN DADOS DE ALTA EN EL CATALOGO MAESTRO Y CREAR UNA ORDEN DE COMPRA
            if(Objects.equals(EEstatusServicios.EN_REPARACION.getIdEstatusServicio(), servicio.getIdServicioEstatus())) {
              this.toSaveArticulos(sesion, servicio);
              this.toSaveOrdenCompra(sesion, servicio);
            } // if
					} // if
					break;
				case COMPLEMENTAR:
					if(actualizarTotales(sesion))
						regresar= registrarDetalle(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      } // if
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar	
	
	private boolean procesarServicio(Session sesion) throws Exception {
		boolean regresar     = false;
		Long idCliente       = -1L;		
		Long insertado       = -1L;		
		Siguiente consecutivo= null;
    try {
      this.messageError = "Error al registrar el servicio";
			consecutivo= this.toSiguiente(sesion);
			this.registroServicio.getServicio().setConsecutivo(consecutivo.getConsecutivo());
			this.registroServicio.getServicio().setOrden(consecutivo.getOrden());
			this.registroServicio.getServicio().setEjercicio(new Long(Fecha.getAnioActual()));
			this.registroServicio.getServicio().setIdUsuario(JsfBase.getIdUsuario());
			this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.registroServicio.getServicio().setIdServicioEstatus(ELABORADA);
			this.registroServicio.getServicio().setIdCliente(Objects.equals(MotorBusquedaCatalogos.VENTA, this.registroServicio.getCliente().getClave()) || this.registroServicio.getCliente().getIdCliente().equals(-1L) ? null : this.registroServicio.getCliente().getIdCliente());			
			insertado= DaoFactory.getInstance().insert(sesion, this.registroServicio.getServicio());
			if(insertado>= 1L) {
				if(DaoFactory.getInstance().insert(sesion, this.loadBitacora(insertado, this.registroServicio.getServicio().getObservaciones()))>= 1L) {
					if(this.registroServicio.isRegistrarCliente()) {
						if(this.registroServicio.getCliente().getIdCliente()== null || Objects.equals(MotorBusquedaCatalogos.VENTA, this.registroServicio.getCliente().getClave()) || this.registroServicio.getCliente().getIdCliente().equals(-1L)) {
							idCliente= this.registraCliente(sesion);
							this.registroServicio.getServicio().setIdCliente(idCliente);
							regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;
							sesion.flush();
							this.registraClienteFacturama(sesion, idCliente);
						} // if			
            else {
							this.registraContactos(sesion, this.registroServicio.getServicio().getIdCliente());
							sesion.flush();
							this.actualizarClienteFacturama(sesion, this.registroServicio.getServicio().getIdCliente());
							regresar= true;
						} // else
					} // if
					else
						regresar= true;
				} // if			
			} // if
    } // try
    catch (Exception e) {
			this.messageError= "Ocurrio un error al registrar el cliente";
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	private void registraClienteFacturama(Session sesion, Long idCliente) {		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // registraArticuloFacturama
	
	private TcManticServiciosBitacoraDto loadBitacora(Long idServicio, String observaciones) throws Exception{
		return loadBitacora(ELABORADA, idServicio, observaciones);
	} // loadBitacora
	
	private TcManticServiciosBitacoraDto loadBitacora(Long idServicioEstatus, Long idServicio, String observaciones) throws Exception {
    TcManticServiciosBitacoraDto regresar= new TcManticServiciosBitacoraDto();
    try {
      regresar.setIdServicio(idServicio);
      regresar.setIdServicioEstatus(idServicioEstatus);
      regresar.setIdUsuario(JsfBase.getIdUsuario());
      regresar.setSeguimiento(observaciones);
      regresar.setConsecutivo(this.registroServicio.getServicio().getConsecutivo());
      regresar.setImporte(this.registroServicio.getServicio().getTotal());
    } // try
    catch(Exception e) {
      throw e;
    } // catch
		return regresar;
	} // loadBitacora

	private Long registraCliente(Session sesion) throws Exception {
		this.registroServicio.getCliente().setLimiteCredito(0.0D);
		this.registroServicio.getCliente().setIdTipoVenta(EBooleanos.SI.getIdBooleano());
		this.registroServicio.getCliente().setIdCredito(EBooleanos.NO.getIdBooleano());
		this.registroServicio.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		Long idCliente= DaoFactory.getInstance().insert(sesion, this.registroServicio.getCliente());
		int count     =  0;
		for(TrManticClienteTipoContactoDto contacto: this.loadContactos()) {
			contacto.setIdCliente(idCliente);
			contacto.setIdUsuario(JsfBase.getIdUsuario());
			contacto.setOrden(Long.valueOf(count++));
			DaoFactory.getInstance().insert(sesion, contacto);
		}	//for
		return idCliente;
	} // registraCliente
	
	private List<TrManticClienteTipoContactoDto> loadContactos() {
		List<TrManticClienteTipoContactoDto> regresar= new ArrayList<>();
		TrManticClienteTipoContactoDto pivote= new TrManticClienteTipoContactoDto();
		pivote.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
		pivote.setValor(this.registroServicio.getContactoCliente().getTelefono());
		regresar.add(pivote);
		pivote= new TrManticClienteTipoContactoDto();
		pivote.setIdTipoContacto(ETiposContactos.CORREO.getKey());
		pivote.setValor(this.registroServicio.getContactoCliente().getEmail());
		regresar.add(pivote);
		return regresar;
	} // loadContactos
	
  private boolean actualizarServicio(Session sesion) throws Exception {		
    boolean regresar         = false;
		Long idCliente           = -1L;	
		boolean registrarCliente = false;
		boolean actualizarCliente= false;
    try {
			if(this.registroServicio.isRegistrarCliente()) {
				if(this.registroServicio.getCliente().getIdCliente()== null || this.registroServicio.getCliente().getIdCliente().equals(-1L) || this.registroServicio.getCliente().getIdCliente().equals(this.registroServicio.getIdClienteDefault())) {
					idCliente= registraCliente(sesion);
					this.registroServicio.getServicio().setIdCliente(idCliente);				
					registrarCliente= true;
				} // if
				else{
					idCliente= this.registroServicio.getCliente().getIdCliente();					
					registraContactos(sesion, idCliente);
					this.registroServicio.getServicio().setIdCliente(idCliente);				
					actualizarCliente= true;
				} // else
			} // if
			else
				this.registroServicio.getServicio().setIdCliente(this.registroServicio.getIdClienteDefault());				
			regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;      
			if(registrarCliente) {
				sesion.flush();
				registraClienteFacturama(sesion, idCliente);
			} // if
			if(actualizarCliente) {
				sesion.flush();
				actualizarClienteFacturama(sesion, idCliente);
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

	private void actualizarClienteFacturama(Session sesion, Long idCliente) {		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			if(cliente.getIdFacturama()!= null)
				updateCliente(sesion);
			else
				super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // actualizarArticuloFacturama
	
	private void registraContactos(Session sesion, Long idCliente) throws Exception{
		List<ClienteTipoContacto> contactos= null;
		ClienteTipoContacto pivote         = null;
		int count       = 0;		
		int exist       = 0;
		try {
			contactos= toClientesTipoContacto(sesion, idCliente);
			count= contactos.size();				
			for(TrManticClienteTipoContactoDto contacto: loadContactos()) {
				exist= 0;
				if(!contactos.isEmpty()) {
					for(ClienteTipoContacto tipoContacto: contactos) {
						if(tipoContacto.getIdTipoContacto().equals(contacto.getIdTipoContacto())) {
							exist++;
							pivote= tipoContacto;
							pivote.setValor(contacto.getValor());
							pivote.setIdUsuario(JsfBase.getIdUsuario());
						} // if
					} // for
				} // if					
				if(exist== 0) {
					contacto.setIdCliente(idCliente);
					contacto.setIdUsuario(JsfBase.getIdUsuario());
					contacto.setOrden(Long.valueOf(count++));
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
				else
					DaoFactory.getInstance().update(sesion, (TrManticClienteTipoContactoDto) pivote);				
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
	} // registraContactos
	
  private boolean eliminarServicio(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idServicio", this.registroServicio.getIdServicio());
      if (DaoFactory.getInstance().deleteAll(sesion, TcManticServiciosBitacoraDto.class, params) > -1L) {
        if (DaoFactory.getInstance().deleteAll(sesion, TcManticServiciosDetallesDto.class, params) > -1L) {
          regresar = DaoFactory.getInstance().delete(sesion, TcManticServiciosDto.class, this.registroServicio.getIdServicio()) >= 1L;          
        } // if
      } // if
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarCliente
	
	private List<ClienteTipoContacto> toClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	private boolean actualizarTotales(Session sesion) throws Exception{
		boolean regresar             = false;
		TcManticServiciosDto servicio= null;
		try {
			servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, this.idServicio);
			servicio.setDescuento(this.totales.getDescuento$());
			servicio.setDescuentos(this.totales.getDescuentos());
			servicio.setImpuestos(this.totales.getIva());
			servicio.setSubTotal(this.totales.getSubTotal());
			servicio.setTotal(this.totales.getTotal());
			regresar= DaoFactory.getInstance().update(sesion, servicio)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarTotales
	
	private boolean registrarDetalle(Session sesion) throws Exception{
		boolean regresar    = true;
		List<Articulo> todos= null;
		try {
			todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticServiciosDetallesDto", "detalle", new TcManticServiciosDto(this.idServicio).toMap());
			for (Articulo item: todos) 
				if(this.articulos.indexOf(item)< 0)
					DaoFactory.getInstance().delete(sesion, item);
			for (Articulo articulo: this.articulos) {
				if(articulo.isValid()) {
					TcManticServiciosDetallesDto item= articulo.toServicioDetalle();
					item.setIdServicio(this.idServicio);					
					item.setIdUsuario(JsfBase.getIdUsuario());
          TcManticServiciosDetallesDto existe= (TcManticServiciosDetallesDto)DaoFactory.getInstance().findIdentically(sesion, TcManticServiciosDetallesDto.class, item.toMap());
					if(existe== null) 
						DaoFactory.getInstance().insert(sesion, item);
          else {
            item.setIdServicioDetalle(existe.getIdServicioDetalle());
            DaoFactory.getInstance().update(sesion, item);
          } // else
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;	
		} // catch		
		return regresar;
	} // registrarDetalle
	
	private Siguiente toSiguiente(Session sesion, Long idServicio) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", idServicio);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosBitacoraDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(next.toLong());
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente

	private Long toFindUnidadMedida(Session sesion, String codigo) {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaCargasMasivasDto", "empaque", params, "idEmpaqueUnidadMedida");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFindUnidadMedida
  
  private void toSaveArticulos(Session sesion, TcManticServiciosDto servicio) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", servicio.getIdServicio());
      List<TcManticServiciosDetallesDto> items= (List<TcManticServiciosDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticServiciosDetallesDto.class, "TcManticServiciosDetallesDto", "catalogo", params);
      for(TcManticServiciosDetallesDto item: items) {
        params.put("idArticuloTipo",item.getIdArticuloTipo());
        params.put("codigo", item.getCodigo());
        params.put("nombre", item.getConcepto());
        TcManticArticulosDto articulo= null;
        Value existe= (Value)DaoFactory.getInstance().toField("VistaTallerServiciosDto", "existe", params, "idArticulo");
        if(existe!= null && existe.getData()!= null)
          articulo= new TcManticArticulosDto(existe.toLong());
        else {
          articulo= new TcManticArticulosDto(
            item.getConcepto(), // String descripcion, 
            "0", // String descuentos, 
            null, // Long idImagen, 
            null, // Long idCategoria, 
            "0", // String extras, 
            null, // String metaTag, 
            item.getConcepto(), // String nombre, 
            item.getCosto(), // Double precio, 
            item.getIva(), // Double iva, 
            item.getCosto(), // Double mayoreo, 
            2D, // Double desperdicio, 
            null, // String metaTagDescipcion, 
            1L, // Long idVigente, 
            -1L, // Long idArticulo, 
            0D, // Double stock, 
            item.getCosto(), // Double medioMayoreo, 
            0D, // Double pesoEstimado, 
            this.toFindUnidadMedida(sesion, "PIEZA"), // Long idEmpaqueUnidadMedida, 
            2L, // Long idRedondear, 
            item.getCosto(), // Double menudeo, 
            null, // String metaTagTeclado, 
            new Timestamp(Calendar.getInstance().getTimeInMillis()), // Timestamp fecha, 
            JsfBase.getIdUsuario(), //  Long idUsuario, 
            servicio.getIdEmpresa(), // Long idEmpresa, 
            0D, // Double cantidad, 
            10D, // Double minimo, 
            20D, // Double maximo, 
            20D, // Double limiteMedioMayoreo, 
            50D, // Double limiteMayoreo, 
            item.getSat(), // String sat, 
            item.getIdArticuloTipo(), // Long idArticuloTipo, 
            2L, // Long idBarras, 
            "0", // String descuento, 
            "0", // String extra, 
            null, // String idFacturama
            2L, // String idDescontinuado
            null // String fabricante
          );
          DaoFactory.getInstance().insert(sesion, articulo);
          // INSERTAR EL CODIGO DE LA REFACCION 
          TcManticArticulosCodigosDto codigos= new TcManticArticulosCodigosDto(
            item.getCodigo(), // String codigo, 
            null, // Long idProveedor, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            1L, // Long idPrincipal, 
            null, // String observaciones, 
            -1L, // Long idArticuloCodigo, 
            1L, // Long orden, 
            articulo.getIdArticulo() // Long idArticulo
          );
          DaoFactory.getInstance().insert(sesion, codigos);
        } // if  
        // ACTUALIZAR EL DETALLE DE LA ORDEN CON EL ID_ARTICULO
        item.setIdArticulo(articulo.getIdArticulo());
        item.setIdValido(3L);
        DaoFactory.getInstance().update(sesion, item);
      } // for
		} // try
		catch (Exception e) {
			throw new RuntimeException("El articulo ya existe en el catalogo maestro !");
		} // catch
		finally {
			Methods.clean(params);
		} // finally
  }

  private void toSaveOrdenCompra(Session sesion, TcManticServiciosDto servicio) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", servicio.getIdServicio());
			params.put("idProveedor", servicio.getIdProveedor());
      // params.put("clave", "B&D");
      // params.put("razonSocial", "BLACK Y DECKER S.A. DE C.V.");
      Entity proveedor= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaTallerServiciosDto", "proveedor", params);
      if(proveedor!= null && !proveedor.isEmpty()) {
        List<TcManticServiciosDetallesDto> items= (List<TcManticServiciosDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticServiciosDetallesDto.class, "TcManticServiciosDetallesDto", "detalle", params);
        Totales importes= new Totales();
        for(TcManticServiciosDetallesDto item: items) {
          importes.addServicio(item);
        } // for
        Siguiente consecutivo= this.toSiguiente(sesion, servicio);
        this.ordenCompra= new TcManticOrdenesComprasDto(
          proveedor.toLong("idProveedorPago"), // Long idProveedorPago, 
          Numero.toRedondearSat(servicio.getDescuentos()), // Double descuentos, 
          proveedor.toLong("idProveedor"), // Long idProveedor, 
          null, // Long idCliente, 
          servicio.getDescuento(), // String descuento, 
          -1L, // Long idOrdenCompra, 
          "0", // String extras, 
          servicio.getEjercicio(), // Long ejercicio, 
          consecutivo.getConsecutivo(), // String consecutivo, 
          2L, // Long idGasto, 
          Numero.toRedondearSat(importes.getTotal()), // Double total, 
          1L, // Long idOrdenEstatus, 
          this.toCalculateFechaEstimada(Calendar.getInstance(), proveedor.toInteger("idTipoDia"), proveedor.toInteger("dias")), // Date entregaEstimada, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          servicio.getIdAlmacen(), // Long idAlmacen, 
          Numero.toRedondearSat(importes.getImporte()), // Double impuestos, 
          Numero.toRedondearSat(importes.getSubTotal()), // Double subTotal, 
          1D, // Double tipoDeCambio, 
          2L, // Long idSinIva, 
          "ORDEN DE SERVICIO [".concat(servicio.getConsecutivo()).concat("]"), // String observaciones, 
          servicio.getIdEmpresa(), // Long idEmpresa, 
          consecutivo.getOrden(), // Long orden, 
          0D // Double excedentes
        );        
        DaoFactory.getInstance().insert(sesion, this.ordenCompra);
        for(TcManticServiciosDetallesDto item: items) {
          TcManticOrdenesDetallesDto detalle= new TcManticOrdenesDetallesDto(
            item.getImporte(), // Double importes, 
            0D, // Double descuentos, 
            item.getCodigo(), //  String codigo, 
            item.getCosto(), // Double costo, 
            item.getDescuento(), // String descuento, 
            this.ordenCompra.getIdOrdenCompra(), // Long idOrdenCompra, 
            "0", // String extras, 
            item.getConcepto(), // String nombre, 
            item.getImporte(), // Double importe, 
            item.getCosto(), // Double precios, 
            item.getCodigo(), // String propio, 
            item.getCantidad(), // Double cantidades, 
            item.getIva(), // Double iva, 
            item.getImpuestos(), // Double impuestos, 
            item.getSubTotal(), // Double subTotal, 
            item.getCantidad(), // Double cantidad, 
            -1L, // Long idOrdenDetalle, 
            item.getIdArticulo(), // Long idArticulo, 
            0D, // Double excedentes, 
            item.getCosto(), // Double costoReal, 
            item.getCosto() // Double costoCalculado
          );    
          DaoFactory.getInstance().insert(sesion, detalle);
        } // for
      } // if  
		} // try
		catch (Exception e) {
			throw new RuntimeException("No se pudo insertar la orden de compra !");
		} // catch
		finally {
			Methods.clean(params);
		} // finally
  }
 
	private Date toCalculateFechaEstimada(Calendar fechaEstimada, int tipoDia, int dias) {
		fechaEstimada.set(Calendar.DATE, fechaEstimada.get(Calendar.DATE)+ dias);
		if(tipoDia== 2) {
			fechaEstimada.add(Calendar.DATE, ((int)(dias/5)* 2));
			int dia= fechaEstimada.get(Calendar.DAY_OF_WEEK);
			dias= dia== Calendar.SUNDAY? 1: dia== Calendar.SATURDAY? 2: 0;
			fechaEstimada.add(Calendar.DATE, dias);
		} // if
		return new Date(fechaEstimada.getTimeInMillis());
	}
  
	private Siguiente toSiguiente(Session sesion, TcManticServiciosDto servicio) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", servicio.getIdEmpresa());
		  params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticOrdenesComprasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
  
}