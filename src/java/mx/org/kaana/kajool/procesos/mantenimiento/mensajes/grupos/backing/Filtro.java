package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.grupos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesGruposDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.grupos.reglas.Transaccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date @Date
 *@time @Time
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name="kajoolMantenimientoMensajesGruposFiltro")
@ViewScoped
public class Filtro  extends IBaseFilter implements Serializable{

  private static final long serialVersionUID = 4234326591453522346L;

  @Override
  public void doLoad() {
    List<Columna> columns = null;
    try {
      columns= new ArrayList<>();
      columns.add(new Columna("ini", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("fin", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaTrJanalMensajesGruposDto", "row", this.attrs, columns);
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
    } //catch
    finally {
      Methods.clean(columns);
    } //finally
  } //doLoad

  @PostConstruct
  @Override
  protected void init() {
    this.attrs.put("descripcion", "");
    this.attrs.put("sortOrder", " order by tc_janal_mensajes.descripcion");
    this.attrs.put("idGrupo", new Long(-1));
    try {
      this.attrs.put("grupos", UISelect.build("TcJanalGruposDto", "selectGrupos", this.attrs, "clave|descripcion", " ", EFormatoDinamicos.MAYUSCULAS));
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
    } //catch
  } //init

  public void doEvento(String proceso) {
    EAccion accion = EAccion.valueOf(proceso);
    try {
      switch (accion) {
        case CONSULTAR:
        case MODIFICAR:
          JsfBase.setFlashAttribute("idMensaje", ((Entity) this.attrs.get("selected")).getKey());
          break;
        case ELIMINAR:
          doEliminar(accion);
      } //switch
      JsfBase.setFlashAttribute("accion", accion);
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
    } //catch
  } //doEvento

  public void doEliminar(EAccion accion) {
    Map params = new HashMap();
    Long idMensajeGrupo = -1L;
    try {
      params.put(Constantes.SQL_CONDICION, "id_mensaje=" + ((Entity) this.attrs.get("selected")).getKey());
      this.attrs.put("id", DaoFactory.getInstance().toEntity("TrJanalMensajesGruposDto", "idMensajeGrupo", params));
      idMensajeGrupo = ((Entity) this.attrs.get("id")).getKey();
      Transaccion transaccion = new Transaccion(new TrJanalMensajesGruposDto(idMensajeGrupo));
      transaccion.ejecutar(accion);
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
    } //catch
    finally {
      Methods.clean(params);
    } //finally
  } //doEliminar

}

