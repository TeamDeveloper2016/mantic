package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
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
@Table(name="tc_mantic_empresas")
public class TcManticEmpresasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="clave")
  private String clave;
  @Column (name="id_usuarios")
  private Long idUsuarios;
  @Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_empresa_depende")
  private Long idEmpresaDepende;
  @Column (name="titulo")
  private String titulo;
  @Column (name="id_responsable")
  private Long idResponsable;
  @Column (name="carpeta_trabajo")
  private String carpetaTrabajo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="iva")
  private Double iva;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_tipo_empresa")
  private Long idTipoEmpresa;

  public TcManticEmpresasDto() {
    this(new Long(-1L));
  }

  public TcManticEmpresasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticEmpresasDto(String clave, Long idUsuarios, Long idImagen, Long idEmpresaDepende, String titulo, Long idResponsable, String carpetaTrabajo, String nombre, Double iva, String observaciones, Long idEmpresa, Long idTipoEmpresa) {
    setClave(clave);
    setIdUsuarios(idUsuarios);
    setIdImagen(idImagen);
    setIdEmpresaDepende(idEmpresaDepende);
    setTitulo(titulo);
    setIdResponsable(idResponsable);
    setCarpetaTrabajo(carpetaTrabajo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIva(iva);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setIdTipoEmpresa(idTipoEmpresa);
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdUsuarios(Long idUsuarios) {
    this.idUsuarios = idUsuarios;
  }

  public Long getIdUsuarios() {
    return idUsuarios;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdEmpresaDepende(Long idEmpresaDepende) {
    this.idEmpresaDepende = idEmpresaDepende;
  }

  public Long getIdEmpresaDepende() {
    return idEmpresaDepende;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setIdResponsable(Long idResponsable) {
    this.idResponsable = idResponsable;
  }

  public Long getIdResponsable() {
    return idResponsable;
  }

  public void setCarpetaTrabajo(String carpetaTrabajo) {
    this.carpetaTrabajo = carpetaTrabajo;
  }

  public String getCarpetaTrabajo() {
    return carpetaTrabajo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdTipoEmpresa(Long idTipoEmpresa) {
    this.idTipoEmpresa = idTipoEmpresa;
  }

  public Long getIdTipoEmpresa() {
    return idTipoEmpresa;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdEmpresa();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresa = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarios());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaDepende());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTitulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdResponsable());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCarpetaTrabajo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoEmpresa());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("idUsuarios", getIdUsuarios());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idEmpresaDepende", getIdEmpresaDepende());
		regresar.put("titulo", getTitulo());
		regresar.put("idResponsable", getIdResponsable());
		regresar.put("carpetaTrabajo", getCarpetaTrabajo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
		regresar.put("iva", getIva());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idTipoEmpresa", getIdTipoEmpresa());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getIdUsuarios(), getIdImagen(), getIdEmpresaDepende(), getTitulo(), getIdResponsable(), getCarpetaTrabajo(), getNombre(), getRegistro(), getIva(), getObservaciones(), getIdEmpresa(), getIdTipoEmpresa()
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
    regresar.append("idEmpresa~");
    regresar.append(getIdEmpresa());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresa());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEmpresasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresa()!= null && getIdEmpresa()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEmpresasDto other = (TcManticEmpresasDto) obj;
    if (getIdEmpresa() != other.idEmpresa && (getIdEmpresa() == null || !getIdEmpresa().equals(other.idEmpresa))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresa() != null ? getIdEmpresa().hashCode() : 0);
    return hash;
  }

}


