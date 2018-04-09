package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/09/2015
 * @time 09:17:57 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.reglas.Transaccion;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;

@ManagedBean(name="kajoolMensajesPerfilesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 936883776354731463L;

  @PostConstruct
  @Override
  protected void init() {
    this.attrs.put("idGrupo", new Long(-1L));
    this.attrs.put("descripcion", "");
    this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    this.attrs.put("grupos", UISelect.build("TcJanalGruposDto", this.attrs, "descripcion"));
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns = null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcionPerfil", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcionMensaje", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("vigenciaIni", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("vigenciaFin", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaMensajesPerfiles", "row", this.attrs, columns);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(columns);
    }//finally
  } // doLoad

  public String doEvento(EAccion accion) {
    switch (accion) {
      case MODIFICAR:
        JsfBase.setFlashAttribute("idMensaje", ((Entity) this.attrs.get("seleccion")).getKey());
      case AGREGAR:
        JsfBase.setFlashAttribute("accion", accion);
        break;
      case ELIMINAR:
        doEliminar();
        break;
    } // switch accion
    return "agregar".concat(Constantes.REDIRECIONAR);
  } //doEvento

  public void doPerfiles() {
    this.attrs.put("idPerfil", new Long(-1L));
    this.attrs.put("perfiles", UISelect.build("TcJanalPerfilesDto", "porGrupo", this.attrs, "descripcion"));
  }

  public void doEliminar() {
    Map params = new HashMap();
    Long idMensajePerfil = -1L;
    try {
      params.put(Constantes.SQL_CONDICION, "id_mensaje=" + ((Entity) this.attrs.get("seleccion")).getKey());
      this.attrs.put("id", DaoFactory.getInstance().toEntity("TrJanalMensajesPerfilesDto", "idMensajePerfil", params));
      idMensajePerfil = ((Entity) this.attrs.get("id")).getKey();
      Transaccion transaccion = new Transaccion(new TrJanalMensajesPerfilesDto(idMensajePerfil));
      transaccion.ejecutar(EAccion.ELIMINAR);
    } //try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } //catch
    finally {
      Methods.clean(params);
    } //finally
  }
}
