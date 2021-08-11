package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_articulos_imagenes")
public class TcManticArticulosImagenesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_imagen")
  private Long idArticuloImagen;
  @Column (name="costo")
  private Double costo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="menudeo")
  private Double menudeo;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="modelo")
  private String modelo;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticArticulosImagenesDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosImagenesDto(Long key) {
    this(new Long(-1L), 0D, null, null, 2L, 0D, 1L, null, null, null);
    setKey(key);
  }

  public TcManticArticulosImagenesDto(Long idArticuloImagen, Double costo, Long idUsuario, Long idImagen, Long idPrincipal, Double menudeo, Long orden, Long idArticulo, String nombre, String modelo) {
    setIdArticuloImagen(idArticuloImagen);
    setCosto(costo);
    setIdUsuario(idUsuario);
    setIdImagen(idImagen);
    setIdPrincipal(idPrincipal);
    setMenudeo(menudeo);
    setOrden(orden);
    setIdArticulo(idArticulo);
    setNombre(nombre);
    setModelo(modelo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdArticuloImagen(Long idArticuloImagen) {
    this.idArticuloImagen = idArticuloImagen;
  }

  public Long getIdArticuloImagen() {
    return idArticuloImagen;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setMenudeo(Double menudeo) {
    this.menudeo = menudeo;
  }

  public Double getMenudeo() {
    return menudeo;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getModelo() {
    return modelo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloImagen();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloImagen = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdArticuloImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMenudeo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getModelo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idArticuloImagen", getIdArticuloImagen());
		regresar.put("costo", getCosto());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("menudeo", getMenudeo());
		regresar.put("orden", getOrden());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("modelo", getModelo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdArticuloImagen(), getCosto(), getIdUsuario(), getIdImagen(), getIdPrincipal(), getMenudeo(), getOrden(), getIdArticulo(), getNombre(), getModelo(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idArticuloImagen~");
    regresar.append(getIdArticuloImagen());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloImagen());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosImagenesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloImagen()!= null && getIdArticuloImagen()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosImagenesDto other = (TcManticArticulosImagenesDto) obj;
    if (getIdArticuloImagen() != other.idArticuloImagen && (getIdArticuloImagen() == null || !getIdArticuloImagen().equals(other.idArticuloImagen))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloImagen() != null ? getIdArticuloImagen().hashCode() : 0);
    return hash;
  }

}


