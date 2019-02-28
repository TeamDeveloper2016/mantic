package mx.org.kaana.mantic.inventarios.creditos.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticInventariosCreditosFacturas")
@ViewScoped
public class Facturas extends IBaseFilter implements Serializable {

	private static final long serialVersionUID=1368701967796774746L;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("mes", -1L);
      this.attrs.put("idProveedor", -1L);
			this.toLoadEjercicios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= toPrepare();
    try {
      params.put("sortOrder", "order by tc_mantic_creditos_notas.registro desc");
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaCreditosNotasDto", "facturas", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch      
    finally {
			Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

  public StreamedContent doAccion(String accion) {
		StreamedContent regresar= null;
    EAccion eaccion= EAccion.valueOf(accion.toUpperCase());
		try {
			switch (eaccion) {
				case COMPLETO:
					regresar= this.toAllFile();
					break;
				case PROCESAR:
					regresar= this.toSingleFile();
					break;
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } 
	
	private StreamedContent toZipFile(String[] files) {
		String zipName    = null;
		String temporal   = Archivo.toFormatNameFile("NOTAS_CREDITOS.").concat(EFormatos.ZIP.name().toLowerCase());
		InputStream stream= null;
		try {
			Zip zip= new Zip();
			zipName= "/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.ZIP.name()).concat("/").concat(temporal));
			zip.setDebug(true);
			zip.setEliminar(false);
			zip.compactar(JsfBase.getRealPath(zipName), Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").length(), files);
  	  stream = new FileInputStream(new File(JsfBase.getRealPath(zipName)));
		} // try // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
    return new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), temporal);		
	}
	
	private StreamedContent toAllFile() {
		StreamedContent regresar  = null;
		Map<String, Object> params= toPrepare();
		try {
			List<Nombres> list= (List<Nombres>)DaoFactory.getInstance().toEntitySet(Nombres.class, "VistaCreditosNotasDto", "exportar", params);
			String[] files= new String[list.size()];
			int count= 0;
			for (Nombres nombre: list) {
				files[count++]= nombre.getNombre();
			} // for
			regresar= this.toZipFile(files);
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
    return regresar;		
	}
	
	private StreamedContent toSingleFile() {
		StreamedContent regresar  = null;
		Map<String, Object> params= null;
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, "tc_mantic_creditos_archivos.id_credito_nota="+ seleccionado.getKey());
			List<Nombres> list= (List<Nombres>)DaoFactory.getInstance().toEntitySet(Nombres.class, "VistaCreditosNotasDto", "exportar", params);
			String[] files= new String[list.size()];
			int count= 0;
			for (Nombres nombre: list) {
				files[count++]= nombre.getNombre();
			} // for
			regresar= this.toZipFile(files);
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
    return regresar;		
	}
		
	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("ejercicio")) && !this.attrs.get("ejercicio").toString().equals("-1"))
  		sb.append("(tc_mantic_creditos_archivos.ejercicio=").append(this.attrs.get("ejercicio")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("mes")) && !this.attrs.get("mes").toString().equals("-1"))
  		sb.append("(tc_mantic_creditos_archivos.mes= ").append(this.attrs.get("mes")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idTipo")) && !this.attrs.get("idTipo").toString().equals("-1"))
  		sb.append("(tc_mantic_tipos_creditos_notas.id_tipo_credito_nota= ").append(this.attrs.get("idTipo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;
	}
	
	private void toLoadEjercicios() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));
			columns.clear();
      this.attrs.put("tipos", (List<UISelectEntity>) UIEntity.build("TcManticTiposCreditosNotasDto", "todos", params, columns));
			this.attrs.put("idTipo", new UISelectEntity("-1"));
      this.attrs.put("ejercicios", (List<UISelectEntity>) UIEntity.build("TcManticCreditosArchivosDto", "ejercicios", params, columns));
			this.attrs.put("ejercicio", "");
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doLoadMeses() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			params =new HashMap<>();
			params.put("ejercicio", this.attrs.get("ejercicio"));
      this.attrs.put("meses", (List<UISelectEntity>) UIEntity.build("VistaCreditosNotasDto", "meses", params, columns));
			this.attrs.put("mes", new UISelectEntity("-1"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
  
	public String doCancelar() {
		return "filtro".concat(Constantes.REDIRECIONAR);
	}
	
	public String doNotaCredito() {
		JsfBase.setFlashAttribute("idCreditoNota", this.attrs.get("idCreditoNota"));
		return "/Paginas/Mantic/Inventarios/Creditos/filtro".concat(Constantes.REDIRECIONAR);
	}
	
}
