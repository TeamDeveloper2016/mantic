package mx.org.kaana.mantic.inventarios.entradas.reglas;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Inventarios;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends Inventarios implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-6069204157451117549L;
 
	protected NotaEntrada orden;	
	private List<Articulo> articulos;
	private boolean aplicar;
	protected Importado xml;
	private Importado pdf;
	private Importado jpg;
	private String messageError;
	private TcManticNotasBitacoraDto bitacora;

	public Transaccion(NotaEntrada orden, TcManticNotasBitacoraDto bitacora) {
		this(orden);
		this.xml= null;
		this.pdf= null;
		this.bitacora= bitacora;
	}
	
	public Transaccion(NotaEntrada orden) {
		this(orden, new ArrayList<Articulo>(), false, null, null);
	}

	public Transaccion(NotaEntrada orden, List<Articulo> articulos, boolean aplicar, Importado xml, Importado pdf) {
    this(orden, articulos, aplicar, xml, pdf, null);
  }
  
	public Transaccion(NotaEntrada orden, List<Articulo> articulos, boolean aplicar, Importado xml, Importado pdf, Importado jpg) {
		super(orden.getIdAlmacen(), orden.getIdProveedor());
		this.orden    = orden;		
		this.articulos= articulos;
		this.aplicar  = aplicar;
		this.xml      = xml;
		this.pdf      = pdf;
    this.jpg      = jpg;
	} // Transaccion

	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                     = false;
		TcManticNotasBitacoraDto bitacoraNota= null;
		Map<String, Object> params           = null;
		Siguiente consecutivo                = null;
		try {
			if(this.orden!= null) {
				params= new HashMap<>();
				params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
				params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
			} // if
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la nota de entrada.");
			switch(accion) {
				case MOVIMIENTOS:
					if(this.orden.isValid()) {
  					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
  					this.toRemoveOrdenDetalle(sesion);
					} // if
					else {
					  consecutivo= this.toSiguiente(sesion);
					  this.orden.setConsecutivo(consecutivo.getConsecutivo());
					  this.orden.setOrden(consecutivo.getOrden());
					  this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					  if(this.orden.getIdNotaTipo().equals(1L))
						  this.orden.setIdOrdenCompra(null);
					  regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					  bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus(), this.orden.getConsecutivo(), this.orden.getTotal());
					  regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
       	    this.toUpdateDeleteXml(sesion);	
					} // else	
					this.toFillArticulos(sesion);
      		for (Articulo articulo: this.articulos) 
						articulo.setModificado(false);
					break;
				case COMPLETO:
					consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo.getConsecutivo());
					this.orden.setOrden(consecutivo.getOrden());
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					this.orden.setIdOrdenCompra(null);					
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus(), this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
  				if(this.aplicar) 
						this.toApplyNotaEntrada(sesion);
	   	    this.toUpdateDeleteXml(sesion);	
					break;
				case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo.getConsecutivo());
					this.orden.setOrden(consecutivo.getOrden());
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					if(this.orden.getIdNotaTipo().equals(1L))
						this.orden.setIdOrdenCompra(null);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus(), this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
     	    this.toUpdateDeleteXml(sesion);	
					break;
				case COMPLEMENTAR:
          this.checkConsecutivo(sesion);
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
  				if(this.aplicar) 
						this.toApplyNotaEntrada(sesion);
	   	    this.toUpdateDeleteXml(sesion);	
					break;				
				case MODIFICAR:
  				if(this.aplicar) {
						this.orden.setIdNotaEstatus(3L);
  					bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus(), this.orden.getConsecutivo(), this.orden.getTotal());
	  				regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					} // if	
          this.checkConsecutivo(sesion);
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					this.toRemoveOrdenDetalle(sesion);
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
     	    this.toUpdateDeleteXml(sesion);	
					break;				
				case ELIMINAR:
					regresar= this.toNotExistsArticulosBitacora(sesion);
					if(regresar) {
						this.toRemoveOrdenDetalle(sesion);
						regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticNotasArchivosDto.class, params)>= 1L;
						regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticNotasDetallesDto.class, params)>= 1L;
						regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
            this.orden.setIdNotaEstatus(2L);
						bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), 2L, this.orden.getConsecutivo(), this.orden.getTotal());
						regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
						this.toCheckOrden(sesion);
            this.toCheckDeleteFile(sesion);
       	    // this.toDeleteXmlPdf();	
					} // if
					else
       			this.messageError= "No se puede eliminar la nota de entrada porque ya fue aplicada en los precios de los articulos.";
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden.setIdNotaEstatus(this.bitacora.getIdNotaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdNotaEstatus().equals(2L)) {
							this.toRemoveOrdenDetalle(sesion);
							regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticNotasDetallesDto.class, params)>= 1L;
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
							this.orden.setIdNotaEstatus(2L);
							bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), 2L, this.orden.getConsecutivo(), this.orden.getTotal());
							regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
              this.toCheckOrden(sesion);
						} // if	
					} // if
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		LOG.info("Se genero de forma correcta la nota de entrada: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
	  StringBuilder error = new StringBuilder();
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaNotasEntradasDto", "detalle", this.orden.toMap());
		Map<String, Object> params= new HashMap<>();
		try {
			for (Articulo item: todos) 
				if(this.articulos.indexOf(item)< 0) {
					this.toAffectOrdenDetalle(sesion, item);
					DaoFactory.getInstance().delete(sesion, TcManticNotasDetallesDto.class, item.getIdComodin());
				} // if
			for (Articulo articulo: this.articulos) {
				TcManticNotasDetallesDto item= articulo.toNotaDetalle();
				item.setIdNotaEntrada(this.orden.getIdNotaEntrada());
				if(item.getDiferencia()!= 0)
					error.append("[").append(item.getNombre()!= null && item.getNombre().length()> 20? item.getNombre().substring(0, 20): item.getNombre()).append(" - ").append(item.getDiferencia()).append("]</br> ");
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticNotasDetallesDto.class, item.toMap())== null && (articulo.getCantidad()> 0D || articulo.getCosto()> 0D)) {
					this.toAffectOrdenDetalle(sesion, articulo);
					if(!item.isValid()) 
						DaoFactory.getInstance().insert(sesion, item);
				  else
						if(articulo.isModificado())
							DaoFactory.getInstance().update(sesion, item);
					articulo.setObservacion("ARTICULO SURTIDO EN LA NOTA DE ENTRADA ".concat(this.orden.getConsecutivo()).concat(" EL DIA ").concat(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, this.orden.getRegistro())));
					// QUITAR DE LAS VENTAS PERDIDAS LOS ARTICULOS QUE FUERON YA SURTIDOS EN EL ALMACEN
					params.put("idArticulo", articulo.getIdArticulo());
					params.put("idEmpresa", this.orden.getIdEmpresa());
					params.put("observaciones", "ESTE ARTICULO FUE SURTIDO CON NO. NOTA DE ENTRADA "+ this.orden.getConsecutivo()+ " EL DIA "+ Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, this.orden.getRegistro()));
					DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, params);
				} // if
			} // for
		} // try
		finally {
			Methods.clean(todos);
			Methods.clean(params);
		} // finally
	}

	private void toRemoveOrdenDetalle(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaNotasEntradasDto", "detalle", this.orden.toMap());
		for (Articulo articulo: todos) {
			if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
				TcManticOrdenesDetallesDto detalle= (TcManticOrdenesDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesDetallesDto.class, articulo.getIdOrdenDetalle());
				detalle.setCantidades(detalle.getCantidades()+ articulo.getCantidad());
				detalle.setImportes(Numero.toRedondearSat(detalle.getImportes()+ articulo.getImporte()));
				detalle.setPrecios(Numero.toRedondearSat(detalle.getCosto()- articulo.getCosto()));
				DaoFactory.getInstance().update(sesion, detalle);
			} // if
		} // for
	}
	
	private void toAffectOrdenDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticOrdenesDetallesDto detalle= (TcManticOrdenesDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()- articulo.getCantidad());
      detalle.setImportes(Numero.toRedondearSat(detalle.getImportes()- articulo.getImporte()));
      detalle.setPrecios(Numero.toRedondearSat(articulo.getValor()- articulo.getCosto()));
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params=new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticNotasEntradasDto", "siguiente", params, "siguiente");
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
	
	private void toCheckOrden(Session sesion) throws Exception {
		try {
			sesion.flush();
			if(this.orden.getIdNotaTipo().equals(2L)) {
        TcManticOrdenesComprasDto ordenCompra= (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesComprasDto.class, this.orden.getIdOrdenCompra());
  		  Value errors= DaoFactory.getInstance().toField(sesion, "VistaNotasEntradasDto", "errores", this.orden.toMap(), "total");
			  if(errors.toLong()!= null && errors.toLong()== 0) {
				  ordenCompra.setIdOrdenEstatus(6L); // TERMINADA
					this.toApplyNotaEntrada(sesion);
				} // if	
				else {
					ordenCompra.setIdOrdenEstatus(5L); // INCOMPLETA
   				if(this.aplicar)
  					this.toApplyNotaEntrada(sesion);
				} // else	
				DaoFactory.getInstance().update(sesion, ordenCompra);
				TcManticOrdenesBitacoraDto estatus= new TcManticOrdenesBitacoraDto(ordenCompra.getIdOrdenEstatus(), "", JsfBase.getIdUsuario(), ordenCompra.getIdOrdenCompra(), -1L, ordenCompra.getConsecutivo(), this.orden.getTotal());
				DaoFactory.getInstance().insert(sesion, estatus);
			} // if
			else
			  if(this.orden.getIdNotaTipo().equals(1L) && this.aplicar) 
					this.toApplyNotaEntrada(sesion);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} 
	
	private boolean toNotExistsArticulosBitacora(Session sesion) throws Exception {
		boolean regresar= true;
		Value total= DaoFactory.getInstance().toField(sesion, "TcManticArticulosBitacoraDto", "existe", this.orden.toMap(), "total");
		if(total.getData()!= null)
		  regresar= total.toLong()<= 0;
		return regresar;
	}
	
	private void toApplyNotaEntrada(Session sesion) throws Exception {
		Map<String, Object> params= new HashMap<>();
		try {
			for (Articulo articulo: this.articulos) {
				TcManticNotasDetallesDto item= articulo.toNotaDetalle();
				item.setIdNotaEntrada(this.orden.getIdNotaEntrada());
				// Si la cantidad es mayor a cero realizar todo el proceso para el articulos, si no ignorarlo porque el articulo no se surtio
				if(articulo.getCantidad()> 0L)
					this.toAffectAlmacenes(sesion, this.orden.getConsecutivo(), this.orden, item, articulo);
			} // for
			this.orden.setIdNotaEstatus(3L);
			DaoFactory.getInstance().update(sesion, this.orden);

			// Una vez que la nota de entrada es cambiada a terminar se registra la cuenta por cobrar
			TcManticEmpresasDeudasDto deuda= null;
			if(this.orden.getDiasPlazo()> 1) 
				deuda= new TcManticEmpresasDeudasDto(1L, JsfBase.getIdUsuario(), -1L, "", this.orden.getIdEmpresa(), this.orden.getDeuda()- this.orden.getExcedentes(), this.orden.getIdNotaEntrada(), this.orden.getFechaPago(), this.orden.getDeuda(), this.orden.getDeuda()- this.orden.getExcedentes(), 2L, Cadena.isVacio(this.orden.getFactura())? 1L: 2L, null, null, null);
			else
				deuda= new TcManticEmpresasDeudasDto(4L, JsfBase.getIdUsuario(), -1L, "ESTE DEUDA FUE LIQUIDADA EN EFECTIVO", this.orden.getIdEmpresa(), 0D, this.orden.getIdNotaEntrada(), this.orden.getFechaPago(), this.orden.getDeuda(), this.orden.getDeuda()- this.orden.getExcedentes(), 2L, Cadena.isVacio(this.orden.getFactura())? 1L: 2L, new Date(Calendar.getInstance().getTimeInMillis()), JsfBase.getIdUsuario(), null);
			DaoFactory.getInstance().insert(sesion, deuda);
			params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
			params.put("idNotaEstatus", this.orden.getIdNotaEstatus());
			TcManticNotasBitacoraDto registro= (TcManticNotasBitacoraDto)DaoFactory.getInstance().findFirst(TcManticNotasBitacoraDto.class, "igual", params);
			if(registro== null) {
				registro= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus(), this.orden.getConsecutivo(), this.orden.getTotal());
				DaoFactory.getInstance().insert(sesion, registro);
			} // if
			if(!this.orden.getIdNotaTipo().equals(3L))
				this.toCommonNotaEntrada(sesion, this.orden.getIdNotaEntrada(), this.orden.toMap());
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0)
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
          LOG.warn("Nota cr�dito: "+ this.orden.getConsecutivo()+ " delete file: ".concat(matched));
				  // File file= new File(matched);
				  // file.delete();
				} // if
      } // for
	}
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticNotasArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
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
	
	protected void toUpdateDeleteXml(Session sesion) throws Exception {
		TcManticNotasArchivosDto tmp= null;
		if(this.orden.getIdNotaEntrada()!= -1L) {
			if(this.xml!= null) {
				tmp= new TcManticNotasArchivosDto(
					-1L,
					this.xml.getRuta(),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),
					1L,
					Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
					this.orden.getIdNotaEntrada(),
					this.xml.getName(),
					this.xml.getObservaciones(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					1L,
					this.xml.getOriginal(),
          this.xml.getIdTipoDocumento(),
          2L      
				);
				TcManticNotasArchivosDto exists= (TcManticNotasArchivosDto)DaoFactory.getInstance().toEntity(TcManticNotasArchivosDto.class, "TcManticNotasArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticNotasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
        this.toCheckDeleteFile(sesion, this.xml.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticNotasArchivosDto(
					-1L,
					this.pdf.getRuta(),
					this.pdf.getFileSize(),
					JsfBase.getIdUsuario(),
					2L,
					Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.pdf.getRuta()).concat(this.pdf.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
					this.orden.getIdNotaEntrada(),
					this.pdf.getName(),
					this.pdf.getObservaciones(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					1L,
					this.pdf.getOriginal(),
          this.pdf.getIdTipoDocumento(),
          2L
				);
				TcManticNotasArchivosDto exists= (TcManticNotasArchivosDto)DaoFactory.getInstance().toEntity(TcManticNotasArchivosDto.class, "TcManticNotasArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticNotasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
        this.toCheckDeleteFile(sesion, this.pdf.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
			if(this.jpg!= null) {
				tmp= new TcManticNotasArchivosDto(
					-1L,
					this.jpg.getRuta(),
					this.jpg.getFileSize(),
					JsfBase.getIdUsuario(),
					17L,
					Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.jpg.getRuta()).concat(this.jpg.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
					this.orden.getIdNotaEntrada(),
					this.jpg.getName(),
					this.jpg.getObservaciones(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					1L,
					this.jpg.getOriginal(),
          this.jpg.getIdTipoDocumento(),
          2L
				);
				TcManticNotasArchivosDto exists= (TcManticNotasArchivosDto)DaoFactory.getInstance().toEntity(TcManticNotasArchivosDto.class, "TcManticNotasArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticNotasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
        this.toCheckDeleteFile(sesion, this.jpg.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.jpg.getRuta()), ".".concat(this.jpg.getFormat().name()), this.toListFile(sesion, this.jpg, 17L));
			} // if	
  	} // if	
	}

	public void toDeleteXmlPdf() throws Exception {
		List<TcManticNotasArchivosDto> list= (List<TcManticNotasArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticNotasArchivosDto.class, this.orden.toMap(), "all");
		if(list!= null)
			for (TcManticNotasArchivosDto item: list) {
				LOG.info("Nota entrada: "+ this.orden.getConsecutivo()+ " delete file: "+ item.getAlias());
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}	

  private void checkConsecutivo(Session sesion) throws Exception {
    try {      
      if(!Objects.equals(this.orden.getIdEmpresa(), this.orden.getIdEmpresaBack())) {
        Siguiente consecutivo= this.toSiguiente(sesion);
        this.orden.setConsecutivo(consecutivo.getConsecutivo());
        this.orden.setOrden(consecutivo.getOrden());
        this.orden.setEjercicio(new Long(Fecha.getAnioActual()));      }
    } // try
    catch (Exception e) {
      throw e;      
    } // catch	
  }
  
} 