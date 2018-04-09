package mx.org.kaana.kajool.beans;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class RespuestaMovil implements Serializable{
  
  private static final long serialVersionUID = 8844820976242160086L;
  private String indicador;
  private String descripcion;
  private Persona usuario;
  private List<Perfil> perfiles;
  private String numEmpleado;
  private String fechaSistema;

  public RespuestaMovil(String indicador, String descripcion) {
    this(indicador, descripcion, null, null, null, Cadena.rellenar(Fecha.formatear(Fecha.FECHA_HORA_LARGA), 17, '0', false));
  } // RespuestaMovil
  
  public RespuestaMovil(String indicador, String descripcion, Persona usuario, List<Perfil> perfiles, String numEmpleado, String fechaSistema) {
    this.indicador   = indicador;
    this.descripcion = descripcion;
    this.usuario     = usuario;
    this.perfiles    = perfiles;
    this.numEmpleado = numEmpleado;
    this.fechaSistema= fechaSistema;
  } // RespuestaMovil

  public String getIndicador() {
    return indicador;
  }

  public void setIndicador(String indicador) {
    this.indicador= indicador;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion= descripcion;
  }

  public Persona getUsuario() {
    return usuario;
  }

  public void setUsuario(Persona usuario) {
    this.usuario= usuario;
  }

  public List<Perfil> getPerfiles() {
    return perfiles;
  }

  public void setPerfiles(List<Perfil> perfiles) {
    this.perfiles= perfiles;
  }

  public String getFechaSistema() {
    return fechaSistema;
  }

  public void setFechaSistema(String fechaSistema) {
    this.fechaSistema= fechaSistema;
  }  

  public String getNumEmpleado() {
    return numEmpleado;
  }

  public void setNumEmpleado(String numEmpleado) {
    this.numEmpleado= numEmpleado;
  }
}
