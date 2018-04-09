package mx.org.kaana.kajool.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Perfil implements Serializable{
  
  private static final long serialVersionUID= 4651776065290339826L;
  private String idPerfil;
  private String idUsuario;
  private String perfil;
  private String ultimoAcceso;
  private String estilo;
  private String registro;
  private String entidad;
  private String abreviaturaEntidad;
  private String unidadEjecutora;
  private String abreviaturaUniEjecutora;
  private String claveEntidad;
  private String claveUnidadEjecutora;
  private String idAmbito;
  private String ambitoDescripcion;
        
  public Perfil(){
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }
  
  public Perfil(String idPerfil, String idUsuario, String perfil, String ultimoAcceso, String estilo, String registro, String entidad, String abreviaturaEntidad, String unidadEjecutora, String abreviaturaUniEjecutora, String claveEntidad, String claveUnidadEjecutora, String idAmbito, String ambitoDescripcion ) {
    this.idPerfil               = idPerfil;
    this.idUsuario              = idUsuario;
    this.perfil                 = perfil;
    this.ultimoAcceso           = ultimoAcceso;
    this.estilo                 = estilo;
    this.registro               = registro;
    this.entidad                = entidad;
    this.abreviaturaEntidad     = abreviaturaEntidad;
    this.unidadEjecutora        = unidadEjecutora;
    this.abreviaturaUniEjecutora= abreviaturaUniEjecutora;
    this.claveEntidad           = claveEntidad;
    this.claveUnidadEjecutora   = claveUnidadEjecutora;
    this.idAmbito               = idAmbito;
    this.ambitoDescripcion      = ambitoDescripcion;
  }

  public String getIdPerfil() {
    return idPerfil;
  }

  public void setIdPerfil(String idPerfil) {
    this.idPerfil= idPerfil;
  }

  public String getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(String idUsuario) {
    this.idUsuario= idUsuario;
  }

  public String getPerfil() {
    return perfil;
  }

  public void setPerfil(String perfil) {
    this.perfil= perfil;
  }

  public String getUltimoAcceso() {
    return ultimoAcceso;
  }

  public void setUltimoAcceso(String ultimoAcceso) {
    this.ultimoAcceso= ultimoAcceso;
  }

  public String getEstilo() {
    return estilo;
  }

  public void setEstilo(String estilo) {
    this.estilo= estilo;
  }    

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getAbreviaturaEntidad() {
    return abreviaturaEntidad;
  }

  public void setAbreviaturaEntidad(String abreviaturaEntidad) {
    this.abreviaturaEntidad = abreviaturaEntidad;
  }

  public String getUnidadEjecutora() {
    return unidadEjecutora;
  }

  public void setUnidadEjecutora(String unidadEjecutora) {
    this.unidadEjecutora = unidadEjecutora;
  }

  public String getAbreviaturaUniEjecutora() {
    return abreviaturaUniEjecutora;
  }

  public void setAbreviaturaUniEjecutora(String abreviaturaUniEjecutora) {
    this.abreviaturaUniEjecutora = abreviaturaUniEjecutora;
  }

  public String getClaveEntidad() {
    return claveEntidad;
  }

  public void setClaveEntidad(String claveEntidad) {
    this.claveEntidad = claveEntidad;
  }

  public String getClaveUnidadEjecutora() {
    return claveUnidadEjecutora;
  }

  public void setClaveUnidadEjecutora(String claveUnidadEjecutora) {
    this.claveUnidadEjecutora = claveUnidadEjecutora;
  }

  public String getIdAmbito() {
    return idAmbito;
  }

  public void setIdAmbito(String idAmbito) {
    this.idAmbito = idAmbito;
  }

  public String getAmbitoDescripcion() {
    return ambitoDescripcion;
  }

  public void setAmbitoDescripcion(String ambitoDescripcion) {
    this.ambitoDescripcion = ambitoDescripcion;
  }
 
}
