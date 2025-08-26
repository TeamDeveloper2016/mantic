package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2025
 * @time 12:32:20 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Contador implements Serializable {

  private static final long serialVersionUID = -1330660760447754833L;
  private static final Log LOG = LogFactory.getLog(Contador.class);

  private Long idConteo;
  private Long idEmpresa;
  private Long idAlmacen;
  private Long idTrabaja;
  private Long idUsuario;
  private String consecutivo;
  private String nombre;
  private Long idEstatus;
  private List<Cantidad> productos;
  private String semilla;
  private String version;
  private String registro;

  public Contador() {
    this.productos= new ArrayList<>();
  }

  public Contador(Long idConteo, Long idUsuario, String nombre, String registro, Long idEmpresa, Long idAlmacen, String semilla, String version) {
    this.idConteo = idConteo;
    this.idConteo = idEmpresa;
    this.idConteo = idAlmacen;
    this.idUsuario= idUsuario;
    this.nombre   = nombre;
    this.productos= new ArrayList<>();
    this.semilla  = semilla;
    this.version  = version;
    this.registro = registro;
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
    if(!Objects.equals(idConteo, null))
      this.load();
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Cantidad> getProductos() {
    return productos;
  }

  public void setProductos(List<Cantidad> productos) {
    this.productos = productos;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public String getSemilla() {
    return semilla;
  }

  public void setSemilla(String semilla) {
    this.semilla = semilla;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  public Long getIdTrabaja() {
    return idTrabaja;
  }

  public void setIdTrabaja(Long idTrabaja) {
    this.idTrabaja = idTrabaja;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public Long getIdEstatus() {
    return idEstatus;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public void load() {
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idContador", this.getIdConteo());  
      this.productos= (List<Cantidad>)DaoFactory.getInstance().toEntitySet(Cantidad.class, "VistaPlanetasDto", "rigel", params, -1L);
      if(Objects.equals(this.productos, null))
        this.productos= new ArrayList<>();
    } // try
    catch (Exception e) {
      LOG.error(e);
    } // catch	
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.idConteo);
    hash = 71 * hash + Objects.hashCode(this.idUsuario);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    final Contador other = (Contador) obj;
    if (!Objects.equals(this.idConteo, other.idConteo)) 
      return false;
    if (!Objects.equals(this.idUsuario, other.idUsuario)) 
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Conteo{" + "idConteo=" + idConteo + ", idEmpresa=" + idEmpresa + ", idAlmacen=" + idAlmacen + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", productos=" + productos + ", semilla=" + semilla + ", version=" + version + ", registro=" + registro + '}';
  }
  
}
