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
@Table(name="tc_mantic_codigos_postales")
public class TcManticCodigosPostalesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="municipio")
  private String municipio;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_codigo_postal")
  private Long idCodigoPostal;
  @Column (name="localidad")
  private String localidad;
  @Column (name="id_entidad")
  private Long idEntidad;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCodigosPostalesDto() {
    this(new Long(-1L));
  }

  public TcManticCodigosPostalesDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticCodigosPostalesDto(String codigo, String municipio, Long idCodigoPostal, String localidad, Long idEntidad) {
    setCodigo(codigo);
    setMunicipio(municipio);
    setIdCodigoPostal(idCodigoPostal);
    setLocalidad(localidad);
    setIdEntidad(idEntidad);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setIdCodigoPostal(Long idCodigoPostal) {
    this.idCodigoPostal = idCodigoPostal;
  }

  public Long getIdCodigoPostal() {
    return idCodigoPostal;
  }

  public void setLocalidad(String localidad) {
    this.localidad = localidad;
  }

  public String getLocalidad() {
    return localidad;
  }

  public void setIdEntidad(Long idEntidad) {
    this.idEntidad = idEntidad;
  }

  public Long getIdEntidad() {
    return idEntidad;
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
  	return getIdCodigoPostal();
  }

  @Override
  public void setKey(Long key) {
  	this.idCodigoPostal = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMunicipio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCodigoPostal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLocalidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEntidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("municipio", getMunicipio());
		regresar.put("idCodigoPostal", getIdCodigoPostal());
		regresar.put("localidad", getLocalidad());
		regresar.put("idEntidad", getIdEntidad());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getMunicipio(), getIdCodigoPostal(), getLocalidad(), getIdEntidad(), getRegistro()
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
    regresar.append("idCodigoPostal~");
    regresar.append(getIdCodigoPostal());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCodigoPostal());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCodigosPostalesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCodigoPostal()!= null && getIdCodigoPostal()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCodigosPostalesDto other = (TcManticCodigosPostalesDto) obj;
    if (getIdCodigoPostal() != other.idCodigoPostal && (getIdCodigoPostal() == null || !getIdCodigoPostal().equals(other.idCodigoPostal))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCodigoPostal() != null ? getIdCodigoPostal().hashCode() : 0);
    return hash;
  }

}


