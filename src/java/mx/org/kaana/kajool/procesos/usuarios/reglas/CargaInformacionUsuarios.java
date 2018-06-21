package mx.org.kaana.kajool.procesos.usuarios.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 08:11:30 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;

public class CargaInformacionUsuarios {

  private CriteriosBusqueda criteriosBusqueda;

  public CargaInformacionUsuarios(CriteriosBusqueda criteriosBusqueda) {
    this.criteriosBusqueda = criteriosBusqueda;
  }

  private CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }
  
   public void init(boolean incluyeOptionAll) {
    try {
      cargarPerfiles(incluyeOptionAll);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void init() {
    try {
      cargarPerfiles(true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  /**
   * *
   * Carga las los perfiles de usuario disponibles para seleccionar en el filtro
   *
   * @throws Exception
   */
  private void cargarPerfiles(boolean optionAll) throws Exception {
    Map<String, Object> params= null;
    List<Columna> formatos    = null;
    Entity entityDefault      = null;
    try {
      formatos = new ArrayList<>();
      params = new HashMap<>();
      formatos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      params.put("idPerfil", JsfBase.getAutentifica().getPersona().getIdPerfil());
      entityDefault = new Entity();
      this.criteriosBusqueda.getListaPerfiles().addAll(UIEntity.build("VistaMantenimientoPerfilesDto", "jerarquiaMostrarAsignados", params, formatos));
      entityDefault.put("idKeyPerfil", new Value("idKeyPerfil", -1L, "id_Key_Perfil"));      
      entityDefault.put("descripcion", new Value("descripcion", "TODOS", "descripcion")); 
      if (optionAll)
        this.criteriosBusqueda.getListaPerfiles().add(0, new UISelectEntity(entityDefault));
      if (!getCriteriosBusqueda().getListaPerfiles().isEmpty()) {
        getCriteriosBusqueda().setPerfil((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.criteriosBusqueda.getListaPerfiles()));
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
  }
	
  public void cargarPerfilesDisponible() throws Exception {
    Map<String, Object> params= null;
    List<Columna> formatos    = null;
    try {
      formatos= new ArrayList<>();
      params= new HashMap<>();
      formatos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      params.put("idPerfil", JsfBase.getAutentifica().getPersona().getIdPerfil());
      params.put("idPersona", this.criteriosBusqueda.getPersona().getKey());			
      this.criteriosBusqueda.getListaPerfiles().clear();
      this.criteriosBusqueda.getListaPerfiles().addAll(UIEntity.build("VistaMantenimientoPerfilesDto", "jerarquiaMostrarAsignadosPersona", params, formatos));      
      if (!getCriteriosBusqueda().getListaPerfiles().isEmpty())
        getCriteriosBusqueda().setPerfil((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.criteriosBusqueda.getListaPerfiles()));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
  } // cargarPerfilesDisponible

  private String condicionEmpleado(String alias, String cadena) {
    StringBuilder like = new StringBuilder();
    if (!cadena.equals("")) {
      String[] busqueda = cadena.split(" ");
      int contador = 0;
      StringBuilder elemento = new StringBuilder();
      for (int i = 0; i < busqueda.length; i++) {
        elemento.append(busqueda[i]);
        contador = i;
        while (contador + 1 < busqueda.length && busqueda[i].equals(busqueda[contador + 1])) {
          contador++;
          elemento.append(" ").append(busqueda[contador]);
        } // while contador
        i = contador;
        if (i == busqueda.length - 1) {
          like.append("  ((upper(").append(alias).append("paterno) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("materno) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("nombres) like upper('%").append(elemento.toString()).append("%'))) ");
        } // if
        else {
          like.append("  ((upper(").append(alias).append("paterno) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("materno) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("nombres) like upper('%").append(elemento.toString()).append("%'))) and ");
        } // else
        elemento.delete(0, elemento.length());
      }// for i
      elemento = null;
      busqueda = null;
    }//if cadena
    return like.toString();
  } //buscarEmpleado

  /**
   * *
   * Recupera los id de los perfiles permitidos para la operacion. Estoso perfiles son recuperados de la tabla de
   * configuraciones
   *
   * @return Devuele los id de los perfiles encontrados en una cadena separada por comas
   */
  private String recuperarIdPerfiles() throws Exception {
    StringBuilder regresar = new StringBuilder();
    try {
      for (UISelectEntity perlfilAlta : this.getCriteriosBusqueda().getListaPerfiles()) {
        regresar.append(perlfilAlta.getKey());
        regresar.append(",");
      } // for	
      if (regresar.length() != 0) {
        regresar.deleteCharAt(regresar.length() - 1); 
      } // if
      if (regresar.length() == 0) {
        JsfBase.addMessage("No existen perfiles a los que pueda consultar, favor de veriifcarlo con el administrador del sistema", ETipoMensaje.ERROR);
        throw new RuntimeException("No existen perfiles a los que pueda consultar, favor de veriifcarlo con el administrador del sistema");
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    return regresar.toString();
  }

  /**
   * *** Metodos publicos **
   */
  /**
   * *
   * Construye la condicion y realiza la consulta a la base de datos para los resultados que se mostraran en pantalla;
   * esta busqueda solo aplica cuando se realiza por nombre
   *
   * @return Devuele los resultados en una lista de tipo Entity
   */
  public String busquedaPorNombre() throws Exception {
    StringBuilder regresar = new StringBuilder();
    String idPerfiles = null;
    String condicionemp = null;
    try {
      idPerfiles = recuperarIdPerfiles();
      condicionemp = condicionEmpleado("", getCriteriosBusqueda().getNombre().toUpperCase());
      if (!Cadena.isVacio(condicionemp)) {
        regresar.append(condicionemp);
        regresar.append(" and ");
      } // if			
      regresar.append(" tc_janal_perfiles.id_perfil in (");
      regresar.append(idPerfiles);
      regresar.append(")");
    }// try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar.toString();
  } // busquedaPorNombre

  public String busquedaPorPerfil() throws Exception {
    String regresar = null;
    try {
      regresar = "tc_janal_perfiles.id_perfil=".concat(getCriteriosBusqueda().getPerfil().getKey().toString());
    }// try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }//busquedaPorOficina

  public String busquedaPorPerfilNombre() {
    StringBuilder regresar = new StringBuilder();
    String idPerfiles = "";
    String condicionEmp = "";
    try {
      if (getCriteriosBusqueda().getPerfil().getKey().equals(-1L)) {
        idPerfiles = recuperarIdPerfiles();
        regresar.append(" and tr_perfiles.id_perfil in (");
        regresar.append(idPerfiles);
        regresar.append(")");
      } // if
      else {
        regresar.append(" and tr_perfiles.id_perfil = ");
        regresar.append(getCriteriosBusqueda().getPerfil().getKey().toString());
      } // else
      condicionEmp = condicionEmpleado("", getCriteriosBusqueda().getNombre().toUpperCase());
      if (!Cadena.isVacio(condicionEmp)) {
        regresar.append(" and ");
        regresar.append(condicionEmp);
      } // if
      regresar.append(regresar.toString().length() == 0 ? " ".concat(Constantes.SQL_VERDADERO) : "");
    }// try
    catch (Exception e) {
      Error.mensaje(e);
    } //  catch
    return regresar.toString();
  } // busquedaPorOficinaPerfilNombre 
}
