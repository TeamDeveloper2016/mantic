package mx.org.kaana.mantic.catalogos.clientes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.bean.RegistroCliente;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;


@Named(value = "manticCatalogosClientesAccion")
@ViewScoped  
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID = -7668104942302148046L;
	private RegistroCliente registroCliente;

	public RegistroCliente getRegistroCliente() {
		return registroCliente;
	}

	public void setRegistroCliente(RegistroCliente registroCliente) {
		this.registroCliente = registroCliente;
	}
		
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));      
			this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
      doLoad();    
			loadRepresentantes();
			loadTiposContactos();
			loadTiposDomicilios();
			loadEntidades();
			loadMunicipios();
			loadLocalidades();
			loadDomicilios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    Long idCliente = -1L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
					this.registroCliente= new RegistroCliente();
					break;
        case MODIFICAR:
					idCliente= Long.valueOf(this.attrs.get("idCliente").toString());
					this.registroCliente= new RegistroCliente(idCliente);
					break;
			} // switch      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {
      transaccion = new Transaccion(this.registroCliente);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro";
        JsfBase.addMessage("Se registro el cliente de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el cliente", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return "filtro";
  } // doAccion
	
	private void loadRepresentantes() {
    List<UISelectItem> representantes= null;
    Map<String, Object> params       = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona=" + ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());
      representantes = UISelect.build("TcManticPersonasDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("representantes", representantes);      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadRepresentantes
	
	private void loadTiposContactos() {
    List<UISelectItem> tiposContactos= null;
    try {
			tiposContactos= new ArrayList<>();
			for(ETiposContactos tipoContacto: ETiposContactos.values())
				tiposContactos.add(new UISelectItem(tipoContacto.getKey(), Cadena.reemplazarCaracter(tipoContacto.name(), '_', ' ')));
      this.attrs.put("tiposContactos", tiposContactos);      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		    
	}
	
	private void loadTiposDomicilios() {
    List<UISelectItem> tiposDomicilios= null;
    try {
			tiposDomicilios= new ArrayList<>();
			for(ETiposDomicilios tipoDomicilio: ETiposDomicilios.values())
				tiposDomicilios.add(new UISelectItem(tipoDomicilio.getKey(), Cadena.reemplazarCaracter(tipoDomicilio.name(), '_', ' ')));
      this.attrs.put("tiposDomicilios", tiposDomicilios);      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		    
  } // loadTiposDomicilios
	
	private void loadEntidades(){
		List<UISelectItem> entidades= null;
    Map<String, Object> params  = null;
    try {
      params = new HashMap<>();
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      entidades = UISelect.build("TcJanalEntidadesDto", "comboEntidades", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);
      this.attrs.put("entidad", UIBackingUtilities.toFirstKeySelectItem(entidades));			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} // loadEntidades
	
	private void loadMunicipios(){
		List<UISelectItem> municipios= null;
    Map<String, Object> params   = null;
    try {
      params = new HashMap<>();
      params.put("idEntidad", this.attrs.get("entidad"));
      municipios = UISelect.build("TcJanalMunicipiosDto", "comboMunicipios", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("municipios", municipios);
      this.attrs.put("municipio", UIBackingUtilities.toFirstKeySelectItem(municipios));			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} // loadMunicipios
	
	private void loadLocalidades(){
		List<UISelectItem> localidades= null;
    Map<String, Object> params    = null;
    try {
      params = new HashMap<>();
      params.put("idMunicipio", this.attrs.get("municipio"));
      localidades = UISelect.build("TcJanalLocalidadesDto", "comboLocalidades", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("localidades", localidades);
      this.attrs.put("localidad", UIBackingUtilities.toFirstKeySelectItem(localidades));			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} // loadLocalidades
	
	private void loadDomicilios(){
		
	} // loadDomicilios
}