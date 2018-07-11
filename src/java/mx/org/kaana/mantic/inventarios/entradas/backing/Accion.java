package mx.org.kaana.mantic.inventarios.entradas.backing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.inventarios.entradas.reglas.AdminNotas;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticNotasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.beans.Concepto;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosEntradasAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID= 327393488565639367L;
	private static final int BUFFER_SIZE      = 6124;
	
	
	private EAccion accion;	
	private EOrdenes tipoOrden;
	private boolean aplicar;
	private Importado importado;

	public String getValidacion() {
		return this.tipoOrden.equals(EOrdenes.NORMAL)? "libre": "requerido";
	}
	
	public String getTitulo() {
		return this.tipoOrden.equals(EOrdenes.NORMAL)? "(DIRECTA)": "";
	}

	public Boolean getIsDirecta() {
		return this.tipoOrden.equals(EOrdenes.NORMAL);
	}

	public Boolean getIsAplicar() {
		Boolean regresar= true;
		try {
			regresar= JsfBase.isAdminEncuestaOrAdmin();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	}
	
	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}

	public Importado getImportado() {
		return importado;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.aplicar  =  false;
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null || JsfBase.getFlashAttribute("idOrdenCompra")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra")== null? -1L: JsfBase.getFlashAttribute("idOrdenCompra"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("formatos", "/(\\.|\\/)(xml|txt)$/");
			this.importado= null;
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminNotas(new NotaEntrada(-1L, (Long)this.attrs.get("idOrdenCompra")), this.tipoOrden));
          TcManticOrdenesComprasDto ordenCompra= this.attrs.get("idOrdenCompra").equals(-1L)? new TcManticOrdenesComprasDto(): (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(TcManticOrdenesComprasDto.class, (Long)this.attrs.get("idOrdenCompra"));
					if(this.tipoOrden.equals(EOrdenes.NORMAL)) {
						((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(new UISelectEntity(new Entity(-1L)));
						((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(new UISelectEntity(new Entity(-1L)));
					} // if
					else {
						((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(new UISelectEntity(new Entity(ordenCompra.getIdAlmacen())));
						((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(new UISelectEntity(new Entity(ordenCompra.getIdProveedor())));
					} // else	
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.setAdminOrden(new AdminNotas((NotaEntrada)DaoFactory.getInstance().toEntity(NotaEntrada.class, "TcManticNotasEntradasDto", "detalle", this.attrs), this.tipoOrden));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			this.toLoadCatalog();
			this.doFilterRows();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAplicar() {  
		this.aplicar= true;
		return this.doAceptar();
	}
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			((NotaEntrada)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuento());
			((NotaEntrada)this.getAdminOrden().getOrden()).setExcedentes(this.getAdminOrden().getTotales().getExtra());
			((NotaEntrada)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((NotaEntrada)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((NotaEntrada)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
			this.getAdminOrden().toAdjustArticulos();
			transaccion = new Transaccion(((NotaEntrada)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.aplicar);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR) || this.aplicar) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
					if(this.accion.equals(EAccion.AGREGAR))
    			  RequestContext.getCurrentInstance().execute("jsArticulos.back('generó la nota de entrada', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					else
   			    RequestContext.getCurrentInstance().execute("jsArticulos.back('aplicó la nota de entrada', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
     	    this.toUpdateDeleteXml();	
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la nota de entrada."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la nota de entrada.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idOrdenCompra", this.attrs.get("idOrdenCompra"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty() && this.accion.equals(EAccion.AGREGAR)) 
				((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			int index= 0;
			if(!proveedores.isEmpty()) {
				if(this.tipoOrden.equals(EOrdenes.NORMAL))
			   ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(proveedores.get(0));
				else {
				  index= proveedores.indexOf(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor());
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(proveedores.get(index));
				} // else
		    this.attrs.put("proveedor", proveedores.get(index));
			} // if	
			if(this.attrs.get("idOrdenCompra")!= null) {
        columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
        this.attrs.put("ordenes", UIEntity.build("VistaNotasEntradasDto", "ordenes", params, columns));
			  List<UISelectEntity> ordenes= (List<UISelectEntity>)this.attrs.get("ordenes");
			  if(!ordenes.isEmpty()) 
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkOrdenCompra(ordenes.get(0));
			} // if	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

	public void doUpdateProveedor() {
		try {
			if(this.tipoOrden.equals(EOrdenes.PROVEEDOR)) {
				this.getAdminOrden().getArticulos().clear();
				this.getAdminOrden().toCalculate();
			} // if	
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			UISelectEntity proveedor= ((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor();
			this.attrs.put("proveedor", proveedores.get(proveedores.indexOf(proveedor)));
		}	
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 

	public void doUpdateAlmacen() {
		if(this.tipoOrden.equals(EOrdenes.ALMACEN)) {
  		this.getAdminOrden().getArticulos().clear();
			this.getAdminOrden().toCalculate();
		} // if	
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Faltantes")) 
      this.toLoadFaltantes();
	}
	
	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
    File result       = null;		
		Long fileSize     = 0L;
		try {
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("facturas"));
      path.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
      path.append(File.separator);
      path.append(Calendar.getInstance().get(Calendar.YEAR));
      path.append(File.separator);
      path.append(Cadena.letraCapital(Fecha.getNombreMes(Calendar.getInstance().get(Calendar.MONTH))));
      path.append(File.separator);
      path.append(((UISelectEntity)this.attrs.get("proveedor")).toString("prefijo"));
      path.append(File.separator);
			result= new File(path.toString());		
			if (!result.exists())
				result.mkdirs();
      path.append(event.getFile().getFileName());
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			this.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			this.importado= new Importado(event.getFile().getFileName(), event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", result.getCanonicalPath());      
			this.toReadFactura(result);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
		} // catch
	} // doFileUpload	
	
	private void toWriteFile(File result, InputStream upload) throws Exception {
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		InputStream inputStream          = upload;
		byte[] buffer                    = new byte[BUFFER_SIZE];
		int bulk;
		while(true) {
			bulk= inputStream.read(buffer);
			if (bulk < 0) 
				break;        
			fileOutputStream.write(buffer, 0, bulk);
			fileOutputStream.flush();
		} // while
		fileOutputStream.close();
		inputStream.close();
	} 

	private void toUpdateDeleteXml() throws Exception {
		//		this(idNotaArchivo, ruta, tamanio, idUsuario, idTipoArchivo, alias, mes, idNotaEntrada, nombre, observacion, ejercicio);
		if(this.importado!= null && ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada()!= -1L) {
			TcManticNotasArchivosDto xml= new TcManticNotasArchivosDto(
				-1L,
				this.importado.getRuta(),
				this.importado.getFileSize(),
				JsfBase.getIdUsuario(),
				1L,
				this.importado.getRuta().concat(File.separator).concat(this.importado.getName()),
				new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
				((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada(),
				this.importado.getName(),
				"",
				new Long(Calendar.getInstance().get(Calendar.YEAR))
			);
			TcManticNotasArchivosDto exists= (TcManticNotasArchivosDto)DaoFactory.getInstance().toEntity(TcManticNotasArchivosDto.class, "TcManticNotasArchivosDto", "identically", xml.toMap());
			if(exists== null) 
				DaoFactory.getInstance().insert(xml);
		} // if	
	}

	private void toReadFactura(File file) throws Exception {
    Reader reader            = null;
		ComprobanteFiscal factura= null;
		List<Articulo> faltantes = null;
		try {
			faltantes= new ArrayList<>();
			reader = new Reader(file.getAbsolutePath());
			factura= reader.execute();
			for (Concepto concepto: factura.getConceptos()) {
		    //this(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida);
		    faltantes.add(new Articulo(
				  (boolean)this.attrs.get("sinIva"),
					this.getAdminOrden().getTipoDeCambio(),
					concepto.getDescripcion(),
					concepto.getNoIdentificacion(),
					Double.parseDouble(concepto.getValorUnitario()),
					"",
					-1L,
					"",
					0D,
					"",
					Double.parseDouble(concepto.getTraslado().getTasaCuota())* 100,
					0D,
					Double.parseDouble(concepto.getImporte()),
					Double.parseDouble(concepto.getCantidad()),
					-1L,
					new Random().nextLong(),
					0D,
					this.getAdminOrden().getIdProveedor(),
					false,
					false,
					0D,
					0D,
					concepto.getClaveProdServ(),
					concepto.getUnidad()
				));
			} // for
			this.attrs.put("faltantes", faltantes);
			this.toPrepareDisponibles();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}
	
	private void toPrepareDisponibles() {
		List<Articulo> disponibles= new ArrayList<>();
		for (Articulo disponible : this.getAdminOrden().getArticulos()) {
			if(disponible.isDisponible() && disponible.getIdArticulo()> -1L)
				disponibles.add(disponible);
		} // for
		this.attrs.put("disponibles", disponibles);
	}
		
	@Override
	public void doFaltanteArticulo() {
		try {
			Long idFaltante  = new Long((String)this.attrs.get("faltante"));
		  List<Articulo> faltantes= (List<Articulo>)this.attrs.get("faltantes");
			Articulo faltante= faltantes.get(faltantes.indexOf(new Articulo(idFaltante)));
			faltantes.remove(faltante);
			
			Long idDisponible = new Long((String)this.attrs.get("disponible"));
		  List<Articulo> disponibles= (List<Articulo>)this.attrs.get("disponibles");
			Articulo disponible= disponibles.get(disponibles.indexOf(new Articulo(idDisponible)));
			disponibles.remove(disponible);
			
   		disponible.setSat(faltante.getSat());
   		disponible.setCodigo(faltante.getCodigo());
   		disponible.setCosto(faltante.getCosto());
   		disponible.setCantidad(faltante.getCantidad());
   		disponible.setIva(faltante.getIva());
   		disponible.setUnidadMedida(faltante.getUnidadMedida());
			disponible.setDisponible(false);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doCheckFolio() {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("factura", ((NotaEntrada)this.getAdminOrden().getOrden()).getFactura());
			params.put("idProveedor", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdProveedor());
			int month= Calendar.getInstance().get(Calendar.MONTH);
			if(month<= 5) {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0101");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "0630");
			} // if
			else {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0701");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "1231");
			} // else
			Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticNotasEntradasDto", "folio", params);
			if(entity!= null && entity.size()> 0) 
				RequestContext.getCurrentInstance().execute("$('#contenedorGrupos\\\\:factura').val('');janal.show([{summary: 'Error:', detail: 'El folio ["+ ((NotaEntrada)this.getAdminOrden().getOrden()).getFactura()+ "] se registró en la nota de entrada "+ entity.toString("consecutivo")+ ", el dia "+ Global.format(EFormatoDinamicos.FECHA_HORA, entity.toTimestamp("registro"))+ ".'}]);");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
}