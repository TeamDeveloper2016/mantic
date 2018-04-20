package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EAmbitos;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 24/08/2015
 * @time 06:56:34 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Persona implements Serializable, IBaseDto {

  private static final long serialVersionUID = 3735519653441674362L;

  private Long idUsuario;
  private String cuenta;
  private String contrasenia;
  private Long idPerfil;
  private String descripcionPerfil;
  private String descripcionGrupo;
  private String estilo;
  private Long idMenu;
  private Long idPersona;
  private String nombres;
  private String primerApellido;
  private String segundoApellido;
  private Long idTipoSexo; 
  private Long idGrupo;
  private String claveGrupo;
  private Timestamp ultimoAcceso;

  public Persona() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public Persona(Long idUsuario, String cuenta, String contrasenia, Long idPerfil, String descripcionPerfil, String descripcionGrupo, String estilo, Long idMenu, Long idPersona, String nombres, String primerApellido, String segundoApellido, Long idTipoSexo, Long idGrupo,Timestamp ultimoAcceso) {
    this.idUsuario = idUsuario;
    this.cuenta = cuenta;
    this.contrasenia = contrasenia;
    this.idPerfil = idPerfil;
    this.descripcionPerfil = descripcionPerfil;
    this.descripcionGrupo = descripcionGrupo;
    this.estilo = estilo;
    this.idMenu = idMenu;
    this.idPersona = idPersona;
    this.nombres = nombres;
    this.primerApellido = primerApellido;
    this.segundoApellido = segundoApellido;
    this.idTipoSexo = idTipoSexo;  
    this.idGrupo = idGrupo;  
    this.ultimoAcceso = ultimoAcceso;
  }

  public String getClaveGrupo() {
    return claveGrupo;
  }

  public void setClaveGrupo(String claveGrupo) {
    this.claveGrupo = claveGrupo;
  }
  
  public Timestamp getUltimoAcceso() {
    return ultimoAcceso;
  }

  public void setUltimoAcceso(Timestamp ultimoAcceso) {
    this.ultimoAcceso = ultimoAcceso;
  }  
  
  public String getEstilo() {
    return estilo;
  }

  public void setEstilo(String estilo) {
    this.estilo = estilo;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public String getDescripcionPerfil() {
    return descripcionPerfil;
  }

  public void setDescripcionPerfil(String descripcionPerfil) {
    this.descripcionPerfil = descripcionPerfil;
  }

  public String getDescripcionGrupo() {
    return descripcionGrupo;
  }

  public void setDescripcionGrupo(String descripcionGrupo) {
    this.descripcionGrupo = descripcionGrupo;
  }

  public Long getIdMenu() {
    return idMenu;
  }

  public void setIdMenu(Long idMenu) {
    this.idMenu = idMenu;
  }

  public EAmbitos getAmbitoEnum() {
    return EAmbitos.values()[2];
  }

  public String getNombreCompleto() {
    return this.nombres.concat(" ").concat(this.primerApellido != null ? this.primerApellido : "").concat(" ").concat(this.segundoApellido != null ? this.segundoApellido : "");
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getPrimerApellido() {
    return primerApellido;
  }

  public void setPrimerApellido(String primerApellido) {
    this.primerApellido = primerApellido;
  }

  public String getSegundoApellido() {
    return segundoApellido;
  }

  public void setSegundoApellido(String segundoApellido) {
    this.segundoApellido = segundoApellido;
  }

  public Long getIdTipoSexo() {
    return idTipoSexo;
  }

  public void setIdTipoSexo(Long idTipoSexo) {
    this.idTipoSexo = idTipoSexo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

 

  @Override
  public Long getKey() {
    return getIdUsuario();
  }

  @Override
  public void setKey(Long key) {
    this.idUsuario = key;
  }

  @Override
  public Map<String, Object> toMap() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isValid() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object toValue(String name) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toAllKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Class toHbmClass() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
