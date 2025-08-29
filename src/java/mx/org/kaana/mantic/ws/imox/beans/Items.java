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
 * @date 1/03/2024
 * @time 20:06:20 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Items implements Serializable {

  private static final long serialVersionUID = -8330660760447754833L;
  private static final Log LOG = LogFactory.getLog(Items.class);

  private Long idConteo;
  private Long idEmpresa;
  private Long idAlmacen;
  private Long idUsuario;
  private String nombre;
  protected List<Item> productos;
  private String semilla;
  private String version;
  private String registro;

  public Items() {
    this.productos= new ArrayList<>();
  }

  public Items(Long idConteo, Long idUsuario, String nombre, String registro, Long idEmpresa, Long idAlmacen, String semilla, String version) {
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

  public List<Item> getProductos() {
    return productos;
  }

  public void setProductos(List<Item> productos) {
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
    final Items other = (Items) obj;
    if (!Objects.equals(this.idConteo, other.idConteo)) 
      return false;
    if (!Objects.equals(this.idUsuario, other.idUsuario)) 
      return false;
    return true;
  }

  public void load() {
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idTransferencia", this.getIdConteo());  
      this.productos= (List<Item>)DaoFactory.getInstance().toEntitySet(Item.class, "VistaPlanetasDto", "sirio", params, -1L);
      if(Objects.equals(this.productos, null))
        this.productos= new ArrayList<>();
    } // try
    catch (Exception e) {
      LOG.error(e);
    } // catch	
  }  
  
  @Override
  public String toString() {
    return "Conteo{" + "idConteo=" + idConteo + ", idEmpresa=" + idEmpresa + ", idAlmacen=" + idAlmacen + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", productos=" + productos + ", semilla=" + semilla + ", version=" + version + ", registro=" + registro + '}';
  }
  
}
