package mx.org.kaana.mantic.egresos.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.egresos.beans.IEgresos;
import mx.org.kaana.mantic.egresos.beans.Nota;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Importados;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticEgresosAsociar") 
@ViewScoped
public class Asociar extends IBaseFilter implements Serializable {

	private static final Log LOG=LogFactory.getLog(Asociar.class);
	private static final long serialVersionUID=-6770709196941718368L;

	private Long idEgreso;
	private int control;
	private Entity orden;
	private List<Nota> articulos;
  
	public Entity getOrden() {
		return orden;
	}

	public List<Nota> getArticulos() {
		return articulos;
	}

	@Override
	@PostConstruct
	protected void init() {
    List<Columna> columns     = null;    
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      columns= new ArrayList<>();
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));
      if(JsfBase.getFlashAttribute("idNotaEntrada")== null)
      	UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("codigo", "");
      this.idEgreso= JsfBase.getFlashAttribute("idEgreso")== null? -1L: (Long)JsfBase.getFlashAttribute("idEgreso");
      params.put("idEgreso", this.idEgreso);      
      this.orden= (Entity)DaoFactory.getInstance().toEntity("TcManticEgresosDto", "detalle", params);
      if(this.orden!= null) 
        UIBackingUtilities.toFormatEntity(this.orden, columns);
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Egresos/filtro": JsfBase.getFlashAttribute("retorno"));
      this.toLoadPartidas();
      this.control= 0;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	}
	
  @Override
	public void doLoad() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorFecha    = false;
    try {
      this.attrs.put("existe", null);
			columns= new ArrayList<>();
      columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("dia", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
			String search= new String((String)this.attrs.get("codigo")); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorFecha= search.startsWith(".");
				if(buscaPorFecha)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
      params.put("sortOrder", "order by tc_mantic_notas_entradas.registro desc");
      StringBuilder sb= new StringBuilder();
      switch(this.control) {
        case 0: // consecutivo
         sb.append("tc_mantic_notas_entradas.consecutivo regexp '.*").append(search).append(".*'");
         break;
        case 1: // proveedor
          if (!Cadena.isVacio(this.attrs.get("proveedor")))
            sb.append("tc_mantic_proveedores.id_proveedor= ").append(this.attrs.get("proveedor"));
         break;
        case 2: // factura
         sb.append("tc_mantic_notas_entradas.factura regexp '.*").append(search).append(".*'");
         break;
        case 3: // Fecha
          if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
            sb.append("date_format(tc_mantic_notas_entradas.fecha_factura, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("'");
          if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
            sb.append("and date_format(tc_mantic_notas_entradas.fecha_factura, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("'");
         break;
        case 4: // Importe
          if(!Cadena.isVacio(this.attrs.get("importeInicio")))
            sb.append("tc_mantic_notas_entradas.total>= ").append((Double)this.attrs.get("importeInicio"));
          if(!Cadena.isVacio(this.attrs.get("importeTermino")))
            sb.append("and tc_mantic_notas_entradas.total<= ").append((Double)this.attrs.get("importeTermino"));
         break;
      } // switch
      params.put(Constantes.SQL_CONDICION, sb.toString());
      this.lazyModel= new FormatCustomLazy("VistaEgresosDto", "porComodin", params, columns);
      UIBackingUtilities.resetDataTable("encontrados");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally    
	}
	
  private void toLoadPartidas() {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("sortOrder", "order by tc_mantic_egresos_notas.registro desc");      
      params.put("idEgreso", this.idEgreso);      
      this.articulos= (List<Nota>)DaoFactory.getInstance().toEntitySet(Nota.class, "VistaEgresosDto", "egresos", params);
      if(this.articulos== null)
        this.articulos= new ArrayList<>();
      else
        for (Nota item: this.articulos) {
          item.setAccion(ESql.SELECT);
        } // for
      this.attrs.put("total", this.articulos.size());
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
	private void updateArticulo(Entity articulo) throws Exception {
    Map<String, Object> params= new HashMap<>();
		try {
      int index= this.articulos.indexOf(new Nota(articulo.toLong("idNotaEntrada")));
      if(index< 0) {
        Nota item= new Nota(
          articulo.toLong("idNotaEntrada"), 
          this.orden.toLong("idEgreso"), 
          articulo.toString("consecutivo"),
          articulo.toString("factura"),
          articulo.toDate("fechaFactura"), 
          articulo.toDouble("total"),
          articulo.toString("proveedor")
        );
        this.articulos.add(item);
      } // if
      else {
        if(Objects.equals(this.articulos.get(index).getAccion(), ESql.SELECT) || Objects.equals(this.articulos.get(index).getAccion(), ESql.INSERT))
          this.attrs.put("existe", "<span class='janal-color-orange'>LA CUENTA x PAGAR YA ESTA EN LA LISTA DE CUENTAS</span>");
        if(Objects.equals(this.articulos.get(index).getAccion(), ESql.DELETE))
          this.articulos.get(index).setAccion(ESql.SELECT);
      }	// else 
			this.attrs.put("total", this.articulos.size());
	  } // try // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	}
	
	public String doAceptar() {
		String regresar       = null;
		Importados transaccion= null;
    List<IEgresos> notas  = new ArrayList<>();
    try {		
      for (Nota item: this.articulos) 
        notas.add((IEgresos)item);
			transaccion= new Importados(notas);
			if(transaccion.ejecutar(EAccion.PROCESAR)) {
			  JsfBase.addMessage("Se agregaron las partidas de forma correcta !", ETipoMensaje.INFORMACION);
  			this.attrs.put("total", this.articulos.size());
			} // if	
			else 
				JsfBase.addMessage("Ocurrió un error al agregar las partidas", ETipoMensaje.ALERTA);
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(notas);
    } // finally
		return regresar;
	}
	
	public void doAgregar(Entity row) {
    try {
      this.attrs.put("existe", null);
      this.updateArticulo(row);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doEliminar(Nota row) {
    try {
      this.attrs.put("existe", null);
      if(Objects.equals(row.getAccion(), ESql.INSERT)) 
        this.articulos.remove(row);
      else
        row.setAccion(ESql.DELETE);
      this.attrs.put("total", this.articulos.size());
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

  public String toColor(Nota row) {
    return Objects.equals(row.getAccion(), ESql.DELETE)? "janal-display-none": "";
  }

	public void doTabChange(TabChangeEvent event) {
    TabView tab= (TabView)event.getTab().getParent();
    this.control= tab.getActiveIndex();
  } 

  	public List<UISelectEntity> doCompleteProveedor(String codigo) {
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("proveedores");
	}		
    
}
