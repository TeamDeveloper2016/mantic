package mx.org.kaana.kajool.db.dto;

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
@Table(name="tc_mantic_articulos")
public class TcManticArticulosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_tmp_articulo")
  private Long idTmpArticulo;
  @Column (name="peso_estimado")
  private Double pesoEstimado;
  @Column (name="id_empaque_unidad_medida")
  private Long idEmpaqueUnidadMedida;
  @Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_redondear")
  private Long idRedondear;
  @Column (name="id_categoria")
  private Long idCategoria;
  @Column (name="ultimo_precio")
  private Double ultimoPrecio;
  @Column (name="menudeo")
  private Double menudeo;
  @Column (name="meta_tag")
  private String metaTag;
  @Column (name="nombre")
  private String nombre;
  @Column (name="meta_tag_teclado")
  private String metaTagTeclado;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="iva")
  private Double iva;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="mayoreo")
  private Double mayoreo;
  @Column (name="desperdicio")
  private Double desperdicio;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="meta_tag_descipcion")
  private String metaTagDescipcion;
  @Column (name="id_vigente")
  private Long idVigente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="stock")
  private Long stock;
  @Column (name="medio_mayoreo")
  private Double medioMayoreo;

  public TcManticArticulosDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticArticulosDto(String descripcion, Long idTmpArticulo, Double pesoEstimado, Long idEmpaqueUnidadMedida, Long idImagen, Long idRedondear, Long idCategoria, Double ultimoPrecio, Double menudeo, String metaTag, String nombre, String metaTagTeclado, Double iva, Long idUsuario, Double mayoreo, Double desperdicio, Long idEmpresa, String metaTagDescipcion, Long idVigente, Long idArticulo, Long stock, Double medioMayoreo) {
    setDescripcion(descripcion);
    setIdTmpArticulo(idTmpArticulo);
    setPesoEstimado(pesoEstimado);
    setIdEmpaqueUnidadMedida(idEmpaqueUnidadMedida);
    setIdImagen(idImagen);
    setIdRedondear(idRedondear);
    setIdCategoria(idCategoria);
    setUltimoPrecio(ultimoPrecio);
    setMenudeo(menudeo);
    setMetaTag(metaTag);
    setNombre(nombre);
    setMetaTagTeclado(metaTagTeclado);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIva(iva);
    setIdUsuario(idUsuario);
    setMayoreo(mayoreo);
    setDesperdicio(desperdicio);
    setIdEmpresa(idEmpresa);
    setMetaTagDescipcion(metaTagDescipcion);
    setIdVigente(idVigente);
    setIdArticulo(idArticulo);
    setStock(stock);
    setMedioMayoreo(medioMayoreo);
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTmpArticulo(Long idTmpArticulo) {
    this.idTmpArticulo = idTmpArticulo;
  }

  public Long getIdTmpArticulo() {
    return idTmpArticulo;
  }

  public void setPesoEstimado(Double pesoEstimado) {
    this.pesoEstimado = pesoEstimado;
  }

  public Double getPesoEstimado() {
    return pesoEstimado;
  }

  public void setIdEmpaqueUnidadMedida(Long idEmpaqueUnidadMedida) {
    this.idEmpaqueUnidadMedida = idEmpaqueUnidadMedida;
  }

  public Long getIdEmpaqueUnidadMedida() {
    return idEmpaqueUnidadMedida;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdRedondear(Long idRedondear) {
    this.idRedondear = idRedondear;
  }

  public Long getIdRedondear() {
    return idRedondear;
  }

  public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
  }

  public Long getIdCategoria() {
    return idCategoria;
  }

  public void setUltimoPrecio(Double ultimoPrecio) {
    this.ultimoPrecio = ultimoPrecio;
  }

  public Double getUltimoPrecio() {
    return ultimoPrecio;
  }

  public void setMenudeo(Double menudeo) {
    this.menudeo = menudeo;
  }

  public Double getMenudeo() {
    return menudeo;
  }

  public void setMetaTag(String metaTag) {
    this.metaTag = metaTag;
  }

  public String getMetaTag() {
    return metaTag;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setMetaTagTeclado(String metaTagTeclado) {
    this.metaTagTeclado = metaTagTeclado;
  }

  public String getMetaTagTeclado() {
    return metaTagTeclado;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setMayoreo(Double mayoreo) {
    this.mayoreo = mayoreo;
  }

  public Double getMayoreo() {
    return mayoreo;
  }

  public void setDesperdicio(Double desperdicio) {
    this.desperdicio = desperdicio;
  }

  public Double getDesperdicio() {
    return desperdicio;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setMetaTagDescipcion(String metaTagDescipcion) {
    this.metaTagDescipcion = metaTagDescipcion;
  }

  public String getMetaTagDescipcion() {
    return metaTagDescipcion;
  }

  public void setIdVigente(Long idVigente) {
    this.idVigente = idVigente;
  }

  public Long getIdVigente() {
    return idVigente;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setStock(Long stock) {
    this.stock = stock;
  }

  public Long getStock() {
    return stock;
  }

  public void setMedioMayoreo(Double medioMayoreo) {
    this.medioMayoreo = medioMayoreo;
  }

  public Double getMedioMayoreo() {
    return medioMayoreo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTmpArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPesoEstimado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpaqueUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRedondear());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCategoria());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUltimoPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMenudeo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTag());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTagTeclado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDesperdicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTagDescipcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStock());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMedioMayoreo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTmpArticulo", getIdTmpArticulo());
		regresar.put("pesoEstimado", getPesoEstimado());
		regresar.put("idEmpaqueUnidadMedida", getIdEmpaqueUnidadMedida());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idRedondear", getIdRedondear());
		regresar.put("idCategoria", getIdCategoria());
		regresar.put("ultimoPrecio", getUltimoPrecio());
		regresar.put("menudeo", getMenudeo());
		regresar.put("metaTag", getMetaTag());
		regresar.put("nombre", getNombre());
		regresar.put("metaTagTeclado", getMetaTagTeclado());
		regresar.put("registro", getRegistro());
		regresar.put("iva", getIva());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("mayoreo", getMayoreo());
		regresar.put("desperdicio", getDesperdicio());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("metaTagDescipcion", getMetaTagDescipcion());
		regresar.put("idVigente", getIdVigente());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("stock", getStock());
		regresar.put("medioMayoreo", getMedioMayoreo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdTmpArticulo(), getPesoEstimado(), getIdEmpaqueUnidadMedida(), getIdImagen(), getIdRedondear(), getIdCategoria(), getUltimoPrecio(), getMenudeo(), getMetaTag(), getNombre(), getMetaTagTeclado(), getRegistro(), getIva(), getIdUsuario(), getMayoreo(), getDesperdicio(), getIdEmpresa(), getMetaTagDescipcion(), getIdVigente(), getIdArticulo(), getStock(), getMedioMayoreo()
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
    regresar.append("idArticulo~");
    regresar.append(getIdArticulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticulo()!= null && getIdArticulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosDto other = (TcManticArticulosDto) obj;
    if (getIdArticulo() != other.idArticulo && (getIdArticulo() == null || !getIdArticulo().equals(other.idArticulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticulo() != null ? getIdArticulo().hashCode() : 0);
    return hash;
  }

}


