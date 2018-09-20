package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@javax.inject.Named("manticCatalogosListaPreciosCatalogos")
@ViewScoped
public class Catalogos extends IBaseAttribute implements Serializable{
	
  private static final long serialVersionUID= 862453896325723687L;
  private List<Entity> lazyModel;

  public List<Entity> getLazyModel() {
    return lazyModel;
  }
  public Catalogos() {}
  
  @PostConstruct
	@Override
  protected void init(){
    try {
			this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // intit
  
  public void doLoadProveedores() {
    Map<String, Object> params = null;
    List<UISelectEntity> proveedores = null;
    try {
      params = new HashMap();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores = UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      attrs.put("proveedores", proveedores);
      attrs.put("idProveedor", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  } // doLoadProveedores
  
  public void doLoad(){
    Map<String, Object> params = null;
    try {
      params = toPrepare();
      params.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      lazyModel = DaoFactory.getInstance().toEntitySet("VistaListasArchivosDto", "lazyProveedores", params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  } // doLoad
  
  public void doCerrar() {
		try {
//			String name= (String)this.attrs.get("temporal");
//			name= name.substring(0, name.lastIndexOf("?"));
//			File file= new File(JsfBase.getRealPath().concat(name));
//			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // doCerrar

  public void doViewPdfDocument(Entity item) { 
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	}
  
  private void toCopyDocument(String alias, String name) {
		try {
  	  this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(name).concat("?pfdrid_c=true"));
  		File source= new File(JsfBase.getRealPath().concat(Constantes.RUTA_TEMPORALES).concat(name));
			if(!source.exists()) {
  	  	FileInputStream input= new FileInputStream(new File(alias));
        this.toWriteFile(source, input);		
			} // if	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	// toCopyDocument

 private void toWriteFile(File result, InputStream inputStream) throws Exception{
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		byte[] buffer                    = new byte[Constantes.BUFFER_SIZE];
		int bulk;
		try {
      while(true) {
        bulk= inputStream.read(buffer);
        if (bulk < 0) 
          break;  
        fileOutputStream.write(buffer, 0, bulk);
        fileOutputStream.flush();
      } // while
      fileOutputStream.close();
      inputStream.close();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // toWriteFile
 
  private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("clave")))
  		sb.append("(tc_mantic_proveedores.clave like '%").append(this.attrs.get("clave")).append("%' or tc_mantic_listas_precios.nombre like '%").append(this.attrs.get("clave")).append("%')");
		if(!Cadena.isVacio(this.attrs.get("razonSocial")))
  		sb.append((!Cadena.isVacio(this.attrs.get("clave"))?" and ":" ").concat("tc_mantic_proveedores.razon_social like '%")).append(this.attrs.get("razonSocial")).append("%' ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb);
		return regresar;
	} // toPrepare
  
	public String doCancelar(){
		String regresar= null;
		try {
			JsfBase.setFlashAttribute("idVenta", this.attrs.get("idVenta"));
			JsfBase.setFlashAttribute("accion", this.attrs.get("accion"));
			regresar= "accion".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch
		return regresar;
	} // doCancelar	
}
