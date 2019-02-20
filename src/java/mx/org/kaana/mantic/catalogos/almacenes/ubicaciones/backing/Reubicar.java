package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ENivelUbicacion;

@Named(value = "manticCatalogosAlmacenesUbicacionesReubicar")
@ViewScoped
public class Reubicar extends Comun implements Serializable {

	private static final long serialVersionUID= -3317240095835919217L;

  @PostConstruct
  @Override
  protected void init() {
    try {			
			if(JsfBase.getFlashAttribute("idArticulo")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");      
      this.attrs.put("idArticulo", JsfBase.getFlashAttribute("idArticulo")); 			
      this.attrs.put("sucursales", JsfBase.getFlashAttribute("idEmpresa")); 			
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa")); 			
      this.attrs.put("idAlmacen", JsfBase.getFlashAttribute("idAlmacen")); 
			this.attrs.put("idAlmacenUbicacion", JsfBase.getFlashAttribute("idAlmacenUbicacion"));			
			doLoad();
			loadNiveles();
			doLoadUbicaciones();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init		
	
	private void loadNiveles(){
		List<UISelectItem> niveles= null;
		try {
			niveles= new ArrayList<>();
			for(ENivelUbicacion nivel: ENivelUbicacion.values()){
				if(nivel.getNivel()>= 1L)
					niveles.add(new UISelectItem(nivel.getNivel(), nivel.name()));
			} // for
			this.attrs.put("niveles", niveles);
			this.attrs.put("nivel", UIBackingUtilities.toFirstKeySelectItem(niveles));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadNiveles
	
	public void doLoadUbicaciones(){
		List<UISelectEntity> ubicaciones= null;
		try {			
			ubicaciones= UIEntity.build("VistaUbicacionesDto", "ubicacionesNivel", this.attrs);
			this.attrs.put("ubicaciones", ubicaciones);
			this.attrs.put("ubicacion", UIBackingUtilities.toFirstKeySelectEntity(ubicaciones));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadUbicaciones
	
  @Override
  public void doLoad() {
	  Map<String, Object> params= null;	
		Entity articuloUbicacion  = null;
    try {  	  
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_articulos.id_articulo=" + this.attrs.get("idArticulo"));			
			params.put("sortOrder", "order by tc_mantic_almacenes_ubicaciones.id_almacen, tc_mantic_almacenes_ubicaciones.piso, tc_mantic_almacenes_ubicaciones.cuarto, tc_mantic_almacenes_ubicaciones.anaquel, tc_mantic_almacenes_ubicaciones.charola, tc_mantic_articulos.nombre");
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias()); 
			articuloUbicacion= (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesUbicacionesDto", "lazyArticulo", params);
			this.attrs.put("articulo", articuloUbicacion);			
      UIBackingUtilities.resetDataTable();		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally		
  } // doLoad

	public String doRegresar() {
		String regresar= null;		
		JsfBase.setFlashAttribute("idArticulo", this.attrs.get("idArticulo"));		
		JsfBase.setFlashAttribute("idAlmacen", this.attrs.get("idAlmacen"));		
		JsfBase.setFlashAttribute("idEmpresa", this.attrs.get("idEmpresa"));		
		JsfBase.setFlashAttribute("empresaOrganigram", this.attrs.get("idEmpresa"));
		JsfBase.setFlashAttribute("idAlmacenUbicacion", this.attrs.get("idAlmacenUbicacion"));
		regresar=	"articulos".concat(Constantes.REDIRECIONAR);
		return regresar;
	} // doRegresar					
	
	public void doAceptar(){
		Entity articulo         = null;
		EAccion accion          = null;
		Transaccion transaccion = null;
		UISelectEntity ubicacion= null;
		try {
			ubicacion= ((List<UISelectEntity>)this.attrs.get("ubicaciones")).get(((List<UISelectEntity>)this.attrs.get("ubicaciones")).indexOf((UISelectEntity) this.attrs.get("ubicacion")));
			articulo= (Entity) this.attrs.get("articulo");			
			articulo.put("idAlmacen", ubicacion.get("idAlmacen"));			
			articulo.put("idAlmacenUbicacion", new Value("idAlmacenUbicacion", ubicacion.getKey()));			
			accion= articulo.get("idAlmacenArticulo").getData()!= null ? EAccion.MODIFICAR : EAccion.ASIGNAR;
			transaccion= new Transaccion(articulo);
			if(transaccion.ejecutar(accion)){
				JsfBase.addMessage("Asignación de ubicación", "Se asigno la ubicación de forma correcta.", ETipoMensaje.INFORMACION);
				doLoad();
			} // if
			else
				JsfBase.addMessage("Asignación de ubicación", "Ocurrio un error al asignar la ubicación.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAceptar
}