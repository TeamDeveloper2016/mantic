package mx.org.kaana.mantic.taller.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.taller.beans.Servicio;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.taller.reglas.AdminServicios;
import mx.org.kaana.mantic.taller.reglas.Transaccion;


@Named(value= "manticTallerDetalle")
@ViewScoped
public class Detalle extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID= 327393488565639367L;
	private EAccion accion;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	} // getAgregar
	
  public Servicio getServicio() {
    return (Servicio)this.getAdminOrden().getOrden();  
  }
  
  public String getCatalogo() {
    String regresar= (String)this.attrs.get("catalogo");
    if((Boolean)this.attrs.get("isCatalogo")) {
      Integer opcion= (Integer)this.attrs.get("idArticuloTipo");    
      switch (opcion) {
        case 1:
          regresar= "Articulos";
          break;
        case 2:
          regresar= "Refacciones";
          break;
        case 3:
          regresar= "Servicios";
          break;
      } // switch
    } // if  
    return regresar;
  }
  
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.MODIFICAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio")== null? -1L: JsfBase.getFlashAttribute("idServicio"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("idArticuloTipo", 1);
			this.attrs.put("catalogo", "Articulos");
      this.attrs.put("isCatalogo", Boolean.TRUE);
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	
	@Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case MODIFICAR:			
        case CONSULTAR:											
          this.setAdminOrden(new AdminServicios((Servicio)DaoFactory.getInstance().toEntity(Servicio.class, "TcManticServiciosDto", "detalle", this.attrs)));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
  
	@Override
  protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		Articulo temporal= this.getAdminOrden().getArticulos().get(index);
		Map<String, Object> params= new HashMap<>();		
    String proceso   = "VistaTallerServiciosDto";
		try {
			if(articulo.size()> 1) {
 				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
        // ESTE CAMPO CONTROLA SI LA REFACCION O EL SERVICIO YA ESTA EN EL CATALOGO MAESTRO
        temporal.setIdAutomatico(1L);
        switch(articulo.toInteger("isArticulo")) {
          case 1: // articulos
            proceso= "TcManticInventariosDto";
            params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
            params.put("idArticuloTipo", 1L);
            break;
          case 2: // refacciones
            // AQUI FALTA BUSCAR EL ID_ARTICULO BASADO EN EL CODIGO DE LA REFACCION SI YA FUE DADA DE ALTA Y POR LO TANTO YA NO ES RANDOM
    				params.put("idAlmacen", ((Servicio)this.getAdminOrden().getOrden()).getIdAlmacen());
            params.put("idArticuloTipo", 2L);
            params.put("codigo", articulo.toString("propio"));
            params.put("nombre", articulo.toString("nombre"));
            if(articulo.toLong("idArticulo")== 0L) {
              Value existe= (Value)DaoFactory.getInstance().toField("VistaTallerServiciosDto", "existe", params, "idArticulo");
              if(existe== null) {
                articulo.put("idArticulo", new Value("idArticulo", new Random().nextLong()));
                temporal.setIdAutomatico(2L);
              } // if
              else {
                proceso= "TcManticInventariosDto";
                articulo.put("idArticulo", new Value("idArticulo", existe.toLong()));
              } // else  
            } // if  
            break;
          case 3:  // servicios
            // AQUI FALTA BUSCAR EL ID_ARTICULO BASADO EN EL CODIGO DE LA REFACCION SI YA FUE DADA DE ALTA Y POR LO TANTO YA NO ES RANDOM
    				params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
            params.put("idArticuloTipo", 3L);
            params.put("codigo", articulo.toString("propio"));
            params.put("nombre", articulo.toString("nombre"));
            if(articulo.toLong("idArticulo")== 0L) {
              Value existe= (Value)DaoFactory.getInstance().toField("VistaTallerServiciosDto", "existe", params, "idArticulo");
              if(existe== null) {
                articulo.put("idArticulo", new Value("idArticulo", new Random().nextLong()));
                temporal.setIdAutomatico(2L);
              } // if
              else {
                proceso= "TcManticInventariosDto";
                articulo.put("idArticulo", new Value("idArticulo", existe.toLong()));
              } // else  
            } // if  
            break;
        } // switch
  			params.put("idArticulo", articulo.toLong("idArticulo"));
  			temporal.setCodigo(articulo.toString("propio"));
				temporal.setIdComodin(articulo.toLong("isArticulo"));
				temporal.setKey(articulo.toLong("idArticulo"));
				temporal.setIdArticulo(articulo.toLong("idArticulo"));
				temporal.setIdProveedor(-1L);
				temporal.setIdRedondear(articulo.toLong("idRedondear"));
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setValor(articulo.toDouble(this.getPrecio()));
				temporal.setCosto(articulo.toDouble(this.getPrecio()));
        if(articulo.containsKey("costo"))
				  temporal.setPrecio(articulo.toDouble("costo"));
				temporal.setIva(articulo.toDouble("iva"));
				temporal.setSat(articulo.toString("sat"));
				temporal.setDescuento(this.getAdminOrden().getDescuento());
				temporal.setExtras(this.getAdminOrden().getExtras());
				temporal.setCantidad(1D);
				temporal.setUltimo(this.attrs.get("ultimo")!= null);
				temporal.setSolicitado(this.attrs.get("solicitado")!= null);
        // AQUI FALTA BUSCAR EL STOCK DE LA REFACCION EN EL ALMACEN DEL TALLER AUN FALTA DEFINIR COMO QUEDARA EL TALLER
				Value stock= (Value)DaoFactory.getInstance().toField(proceso, "stock", params, "stock");
				temporal.setStock(stock== null? 0D: stock.toDouble());
				if(index== this.getAdminOrden().getArticulos().size()- 1) {
					this.getAdminOrden().getArticulos().add(new Articulo(-1L));
					this.getAdminOrden().toAddUltimo(this.getAdminOrden().getArticulos().size()- 1);
					UIBackingUtilities.execute("jsArticulos.update("+ (this.getAdminOrden().getArticulos().size()- 1)+ ");");
				} // if	
				UIBackingUtilities.execute("jsArticulos.callback('"+ articulo.getKey()+ "');");
				this.getAdminOrden().toCalculate();
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		} // finally
	} // toMoveData
	
	@Override
  public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
			params= new HashMap<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= (((boolean)this.attrs.get("buscaPorCodigo")) && !search.startsWith(".")) || (!((boolean)this.attrs.get("buscaPorCodigo")) && search.startsWith("."));  			
				if(search.startsWith("."))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
      Integer opcion= (Integer)this.attrs.get("idArticuloTipo");
  		params.put("idArticuloTipo", opcion);
      if(buscaPorCodigo)
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaTallerServiciosDto", "porCodigo", params, columns, 20L));
      else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaTallerServiciosDto", "porNombre", params, columns, 20L));
      switch(opcion) {
        case 1: // articulos
          break;
        case 2: // refacciones
          break;
        case 3: // servicios
          break;
      } // switch
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // doUpdateArticulos

	@Override
	public void doUpdateDialogArticulos(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("original", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase());
      Integer opcion= (Integer)this.attrs.get("idArticuloTipo");
  		params.put("idArticuloTipo", opcion);
			if(buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaTallerServiciosDto", "por".concat((String)this.attrs.get("catalogo")).concat("Codigo"), params, columns));
			else
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaTallerServiciosDto", "por".concat((String)this.attrs.get("catalogo")).concat("LikeNombre"), params, columns));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // doUpdateDialogArticulos
	
  public String doAceptar() {  
		String regresar        = null;
		Transaccion transaccion= null;
    try {		
			transaccion= new Transaccion(this.getAdminOrden().getArticulos(), Long.valueOf(this.attrs.get("idServicio").toString()), this.getAdminOrden().getTotales());
			if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
      	JsfBase.setFlashAttribute("idServicio", this.getServicio().getIdServicio());
				regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
				JsfBase.addMessage("Se agregaron las refaccion de forma correcta.", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("Ocurrió un error al agregar las refacciones.", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAceptar

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idServicio", this.attrs.get("idServicio"));
    return (String) this.attrs.get("retorno");
  } // doCancelar	
  
  public void doBuscarCatalogo(String catalogo) {
    this.attrs.put("catalogo", catalogo);
    this.attrs.put("isCatalogo", Boolean.FALSE);
  }

  public void doCloseArticulos() {
    this.attrs.put("catalogo", "Articulos");
    this.attrs.put("isCatalogo", Boolean.TRUE);
  }  
  
}