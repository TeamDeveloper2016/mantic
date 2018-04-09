package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/09/2015
 *@time 11:13:07 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
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
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.db.dto.TcJanalMensajesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas.Transaccion;

@ManagedBean(name="kajoolMensajesUsuariosDetalleUsuario")
@ViewScoped
public class DetalleUsuario extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 936883776354731463L;

  @PostConstruct
  @Override
  protected void init() {
    Entity datosUsuario = null;
    try {
      datosUsuario = ((Entity) JsfBase.getFlashAttribute("datosUsuario"));
      this.attrs.put("datosUsuario", datosUsuario);
      this.attrs.put("idUsuario", datosUsuario.toLong("idKey"));
      this.attrs.put("usuario", datosUsuario.toString("usuario"));
      this.attrs.put("dto", new TcJanalMensajesDto());
      this.attrs.put("vigenciaIni", null);
      this.attrs.put("vigenciaFin", null);
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // init

  @Override
  public void doLoad() {
    List<Columna> columnas= new ArrayList();
    try {
      columnas.add(new Columna("vigenciaIni", EFormatoDinamicos.FECHA_CORTA));
      columnas.add(new Columna("vigenciaFin", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatLazyModel("VistaTrJanalMensajesUsuariosDto", "mensajesUsuario", this.attrs, columnas);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLoad

  public String doAgregar() {
    JsfBase.setFlashAttribute("datosUsuario", ((Entity)this.attrs.get("datosUsuario")));
    return "agregar".concat(Constantes.REDIRECIONAR);
  } //doEvento

  public void doActualizaDialogo(){
    this.attrs.put("idMensaje", ((Entity)this.attrs.get("seleccion")).toLong("idKey"));
    this.attrs.put("vigenciaIni", ((Entity)this.attrs.get("seleccion")).toTimestamp("vigenciaIni"));
    this.attrs.put("vigenciaFin", ((Entity)this.attrs.get("seleccion")).toTimestamp("vigenciaFin"));
  }

  public void doMofificar(){
    TcJanalMensajesDto dto    = null;
    Map<String, Object> params= new HashMap();
    Transaccion transaccion   = null;
    try {
      params.put("vigenciaIni", this.attrs.get("vigenciaIni"));
      params.put("vigenciaFin", this.attrs.get("vigenciaFin"));
      params.put("idMensajeUsuario", ((Entity)this.attrs.get("seleccion")).toLong("idMensajeUsuario"));
      dto= new TcJanalMensajesDto(Numero.getLong(this.attrs.get("idMensaje").toString()));
      transaccion= new Transaccion(dto, params);
      if(transaccion.ejecutar(EAccion.MODIFICAR))
        JsfBase.addMessage("La vigencia se modifico correctamente");
      else
        JsfBase.addMessage("La vigencia no se ha podido modificar");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
}
