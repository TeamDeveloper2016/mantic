package mx.org.kaana.kajool.reglas.comun.acciones.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 11, 2012
 *@time 2:01:07 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.reglas.comun.acciones.EEvento;
import mx.org.kaana.kajool.enums.EAccion;

public class Accion implements Serializable {
  private static final long serialVersionUID=6778678641838852562L;
  private EAccion tipoAccion;
  private String nameManagedBean;
  private String value;
  private String update;
  private String icon;
  private String action;
  private String actionListener;
  private String onComplete;
  private String onStart;
  private String process;
  private EEvento evento;
  private boolean enabled;
  private String url;

  public Accion(EAccion tipoAccion, String nameManagedBean, EEvento evento) {
   this(tipoAccion, nameManagedBean, evento, true);
  }

  public Accion(EAccion tipoAccion, String nameManagedBean, EEvento evento, boolean enabled) {
    this(tipoAccion.getName(),null, evento,enabled ,tipoAccion.getIcon());
    this.action= Cadena.nombreAccion(nameManagedBean, tipoAccion.getName());
  }

  public Accion(EAccion tipoAccion, String nameManagedBean, EEvento evento, boolean enabled,String update, String process) {
    this(tipoAccion.getName(),null, evento,enabled ,tipoAccion.getIcon(),update, process);
    this.action= Cadena.nombreAccion(nameManagedBean, tipoAccion.getName());
  }

  public Accion(EAccion tipoAccion, String nameManagedBean, EEvento evento, boolean enabled,String update, String process, String onComplete) {
    this(tipoAccion.getName(),null, evento,enabled ,tipoAccion.getIcon(),update, process, onComplete);
    this.action= Cadena.nombreAccion(nameManagedBean, tipoAccion.getName());
  }


  public Accion(String value, String action, EEvento evento, boolean enabled) {
    this(value, action, evento,enabled, null);
  }

  public Accion(String value, String action, EEvento evento, boolean enabled, String icon) {
    this(value, action, evento, enabled, icon,null);
  }

  public Accion(String value, String action, EEvento evento,boolean enabled, String icon, String update) {
   this(value, action, evento,enabled, icon, update, null);
  }

  public Accion(String value, String action, EEvento evento,boolean enabled, String icon, String update, String onComplete) {
    this(value, action, evento, enabled,icon, update, onComplete, null);
  }

  public Accion(String value, String action, EEvento evento, boolean enabled,String icon, String update, String onComplete, String onStart) {
    this(value, action, evento, enabled,icon, update, onComplete, onStart, null);
  }

  public Accion(String value, String action, EEvento evento,boolean enabled, String icon, String update, String onComplete, String onStart, String process) {
    this(value, action, evento, enabled, icon, update, onComplete, onStart, process, null);
  }

  public Accion(String value, String action, EEvento evento,boolean enabled, String icon, String update, String onComplete, String onStart, String process, String url) {
    this.value=value;
    this.icon=icon;
    this.action=action;
    this.evento=evento;
    this.enabled= enabled;
    this.update= update;
    this.onComplete=onComplete;
    this.onStart=onStart;
    this.process= process;
    this.url = url;
  }

  public Accion(String value, String action,String actionListener,EEvento evento,boolean enabled, String icon, String update, String onComplete, String onStart, String process) {
    this(value, action, actionListener, evento, enabled, icon, update, onComplete, onStart, process, null);
  }

  public Accion(String value, String action,String actionListener,EEvento evento,boolean enabled, String icon, String update, String onComplete, String onStart, String process, String url ) {
    this.value=value;
    this.icon=icon;
    this.action=action;
    this.enabled = enabled;
    this.update= update;
    this.onComplete=onComplete;
    this.onStart=onStart;
    this.process= process;
    this.actionListener= actionListener;
    this.evento=evento;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url=url;
  }



  public EEvento getEvento() {
    return evento;
  }

  public void setEvento(EEvento evento) {
    this.evento=evento;
  }

   public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon=icon;
  }

  public String getOnComplete() {
    return onComplete;
  }

  public void setOnComplete(String onComplete) {
    this.onComplete=onComplete;
  }

  public String getOnStart() {
    return onStart;
  }

  public void setOnStart(String onStart) {
    this.onStart=onStart;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update=update;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value=value;
  }


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action=action;
  }

  public String getActionListener() {
    return actionListener;
  }

  public void setActionListener(String actionListener) {
    this.actionListener=actionListener;
  }

  public String getProcess() {
    return process;
  }

  public void setProcess(String process) {
    this.process=process;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled=enabled;
  }

   public EAccion getTipoAccion() {
    return tipoAccion;
  }

  public void setTipoAccion(EAccion tipoAccion) {
    this.tipoAccion=tipoAccion;
  }

  public String getNameManagedBean() {
    return nameManagedBean;
  }

  public void setNameManagedBean(String nameManagedBean) {
    this.nameManagedBean=nameManagedBean;
  }


}
