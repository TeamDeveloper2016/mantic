package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 01:13:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;

@ManagedBean(name="kajoolMensajesUsuariosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 936883776354731463L;

  @PostConstruct
  @Override
  protected void init() {
    this.attrs.put("grupo", " ");
    this.attrs.put("curp", "");
    this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    this.attrs.put("grupos", UISelect.build("TcJanalGruposDto", this.attrs, "descripcion"));
    doPerfiles();
  } // init

  @Override
  public void doLoad() {
    try {
      this.attrs.put("idGrupo", this.attrs.get("grupo").toString().equals(" ")? obtenerIdsGrupos(): this.attrs.get("grupo").toString());
      this.attrs.put("idPerfil", this.attrs.get("perfil").toString().equals(" ")? obtenerIdsPerfiles(): this.attrs.get("perfil").toString());
      this.lazyModel = new FormatLazyModel("VistaTcJanalUsuariosDto", "mensajes", this.attrs, Collections.EMPTY_LIST);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLoad

  public void doPerfiles() {
    this.attrs.put("perfil", " ");
    this.attrs.put("idGrupo", this.attrs.get("grupo").toString().equals(" ")?obtenerIdsGrupos():this.attrs.get("grupo").toString());
    this.attrs.put("perfiles", UISelect.build("TcJanalPerfilesDto", this.attrs.get("grupo").toString().equals(" ")?"row":"porGrupo", this.attrs, "descripcion"));
  }

  private String obtenerIdsGrupos(){
    StringBuilder regresar= new StringBuilder();
    List<Entity> grupos= null;
    try {
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      grupos= DaoFactory.getInstance().toEntitySet("TcJanalGruposDto", this.attrs);
      for(Entity grupo: grupos){
        regresar.append(grupo.toLong("idKey"));
        regresar.append(",");
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString().substring(0,regresar.toString().length()-1);
  }

  private String obtenerIdsPerfiles(){
    StringBuilder regresar= new StringBuilder();
    List<Entity> perfiles= null;
    try {
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      perfiles= DaoFactory.getInstance().toEntitySet("TcJanalPerfilesDto", this.attrs);
      for(Entity grupo: perfiles){
        regresar.append(grupo.toLong("idKey"));
        regresar.append(",");
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString().substring(0,regresar.toString().length()-1);
  }

  public String doVerDetalle(){
    JsfBase.setFlashAttribute("datosUsuario", ((Entity)this.attrs.get("seleccion")));
    return "detalleUsuario".concat(Constantes.REDIRECIONAR);
  } // doVerDetalle
}
