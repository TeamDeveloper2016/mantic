package mx.org.kaana.kajool.procesos.usuarios.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 13/10/2016
 * @time 10:40:04 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

@Named(value = "kajoolUsuariosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 5319332808932704073L;
  private CriteriosBusqueda criteriosBusqueda;
	private EAccion accion;

  public CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.criteriosBusqueda = new CriteriosBusqueda();
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idUsuario", JsfBase.getFlashAttribute("idUsuario")!= null? (Long)JsfBase.getFlashAttribute("idUsuario"): -1L);
			this.attrs.put("nuevo", JsfBase.getFlashAttribute("idUsuario")!= null);
      loadPerfiles();
      loadPersonas();      
      loadUsuario();
      this.attrs.put("titulo", this.accion.equals(EAccion.MODIFICAR) ? "Modificar usuario cuenta [".concat(((TcManticPersonasDto) this.attrs.get("tcManticPersonasDto")).getCuenta()).concat("]") : "Agregar usuario [...]");
      buscar();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }  

  private void loadPersonas() {
    List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
      columns= new ArrayList<>();
      params = new HashMap<>();
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona in (1,2,6)");
      this.criteriosBusqueda.getListaPersonas().addAll(UIEntity.build("TcManticPersonasDto", "row", params, columns));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally 
  }

  public String doAceptar() {
    Transaccion transaccion    = null;
    String regresar            = null;
    TcManticPersonasDto persona= null;
    TcJanalUsuariosDto usuario = null;
    try {
      persona= (TcManticPersonasDto)this.attrs.get("tcManticPersonasDto");
      usuario= (TcJanalUsuariosDto)this.attrs.get("tcJanalUsuariosDto");
      usuario.setIdPerfil(this.criteriosBusqueda.getPerfil().getKey());
      persona.setEstilo("sentinel");
      persona.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      transaccion = new Transaccion(usuario, persona);
      if (transaccion.ejecutar(this.accion)) {
        regresar = "filtro";
        JsfBase.addMessage(this.accion.equals(EAccion.AGREGAR) ? "Se agregó el usuario con éxito." : "Se modificó el usuario con éxito.");
      } // if
      else {
        String perfil= this.criteriosBusqueda.getListaPerfiles().get(this.criteriosBusqueda.getListaPerfiles().indexOf(new UISelectEntity(this.criteriosBusqueda.getPerfil().getKey().toString()))).toString("descripcion");
        JsfBase.addMessage("El usuario ".concat(persona.getNombres()).concat(" ").concat(persona.getPaterno()).concat(" ya existe con perfil de ").concat(perfil));
      } // else
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private void loadUsuario() {
		Long idUsuario= (Long)this.attrs.get("idUsuario");
    try {
      if (idUsuario> 0) {
        this.attrs.put("tcJanalUsuariosDto", (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, idUsuario));
        this.attrs.put("tcManticPersonasDto", (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuariosDto")).getIdPersona()));
      } // if
			else {
        this.attrs.put("tcJanalUsuariosDto", new TcJanalUsuariosDto(-1L, JsfBase.getIdUsuario(), -1L, 1L, -1L));
        this.attrs.put("tcManticPersonasDto", new TcManticPersonasDto());
      } // else
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  }

  public void doBuscar() {
		CargaInformacionUsuarios ciu= null;
		try {
			ciu= new CargaInformacionUsuarios(this.criteriosBusqueda);
			ciu.cargarPerfilesDisponible();	
			this.attrs.put("tcManticPersonasDto", (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, this.criteriosBusqueda.getPersona().getKey()));			
			buscar();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doBuscar
	
  public void buscar() {
    TcJanalUsuariosDto usuario= null;
    int index                 = -1;
    try {
			TcManticPersonasDto persona= (TcManticPersonasDto)this.attrs.get("tcManticPersonasDto");			
      if (persona.isValid()) {
				persona.setContrasenia(BouncyEncryption.decrypt(persona.getContrasenia()));
        index = this.getCriteriosBusqueda().getListaPersonas().indexOf(new UISelectEntity(new Entity(persona.getIdPersona())));
				if(index>= 0)
          this.criteriosBusqueda.setPersona(this.getCriteriosBusqueda().getListaPersonas().get(index));
			} // if
      usuario = (TcJanalUsuariosDto)this.attrs.get("tcJanalUsuariosDto");
      if (usuario.isValid()) {
        index = this.getCriteriosBusqueda().getListaPerfiles().indexOf(new UISelectEntity(new Entity(usuario.getIdPerfil())));
				if(index>= 0)
          this.criteriosBusqueda.setPerfil(this.getCriteriosBusqueda().getListaPerfiles().get(index));
      } // if
			this.attrs.put("confirmar", persona.getContrasenia());
    }  // try
		catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }
  
	public String doCancelar() {
		return "filtro";
	} 

  protected void loadPerfiles() {
    CargaInformacionUsuarios cargarInformaUsuario= null;
    try {
      cargarInformaUsuario = new CargaInformacionUsuarios(this.criteriosBusqueda);
      cargarInformaUsuario.init(false);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } 

  public boolean isLockField() {
    return !((TcManticPersonasDto)this.attrs.get("tcManticPersonasDto")).isValid();
  }

}
