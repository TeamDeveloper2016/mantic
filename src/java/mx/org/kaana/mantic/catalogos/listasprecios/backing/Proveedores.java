package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@javax.inject.Named("manticCatalogosListaPreciosProveedor")
@ViewScoped
public class Proveedores extends IBaseAttribute implements java.io.Serializable
{
  private static final long serialVersionUID = 862453896325723687L;
  private static final int BUFFER_SIZE      = 6124;
  private List<Entity> lazyModel;

  public List<Entity> getLazyModel() {
    return lazyModel;
  }
  public Proveedores() {}
  
  @PostConstruct
  protected void init(){
    try {
      attrs.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }
  
  public void doLoadProveedores() {
    Map<String, Object> params = null;
    List<UISelectEntity> proveedores = null;
    try {
      params = new HashMap();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores = UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      attrs.put("proveedores", proveedores);
      attrs.put("idProveedor", new UISelectEntity("-1"));
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(params);
    }
  }
  
  public void doLoad(){
    List<Columna> campos = null;
    try {
      campos = new ArrayList();
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      lazyModel = DaoFactory.getInstance().toEntitySet("VistaListasArchivosDto", "lazyProveedores", this.attrs);
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(campos);
    }
  }
  
  public void doCerrar() {
		try {
			String name= (String)this.attrs.get("temporal");
			name= name.substring(0, name.lastIndexOf("?"));
			File file= new File(JsfBase.getRealPath().concat(name));
			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  }

  public void doViewPdfDocument(Entity item) { 
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	}
  
  private void toCopyDocument(String alias, String name) {
		try {
  	  this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(name).concat("?pfdrid_c=true"));
  		File file= new File(JsfBase.getRealPath().concat(Constantes.RUTA_TEMPORALES).concat(name));
	  	FileInputStream input= new FileInputStream(new File(alias));
      this.toWriteFile(file, input);		
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	

 private void toWriteFile(File result, InputStream upload) throws Exception{
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		InputStream inputStream          = upload;
		byte[] buffer                    = new byte[BUFFER_SIZE];
		int bulk;
		try{
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
  
}
