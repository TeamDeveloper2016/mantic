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
@Table(name="tr_mantic_proveedor_persona")
public class TrManticProveedorPersonaDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="id_persona")
  private Long idPersona;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_proveedor_persona")
  private Long idProveedorPersona;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_responsable")
  private Long idResponsable;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticProveedorPersonaDto() {
    this(new Long(-1L));
  }

  public TrManticProveedorPersonaDto(Long key) {
    this(null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TrManticProveedorPersonaDto(Long idProveedor, Long idPersona, Long idProveedorPersona, Long idUsuario, String observaciones, Long idResponsable) {
    setIdProveedor(idProveedor);
    setIdPersona(idPersona);
    setIdProveedorPersona(idProveedorPersona);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdResponsable(idResponsable);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdProveedorPersona(Long idProveedorPersona) {
    this.idProveedorPersona = idProveedorPersona;
  }

  public Long getIdProveedorPersona() {
    return idProveedorPersona;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdResponsable(Long idResponsable) {
    this.idResponsable = idResponsable;
  }

  public Long getIdResponsable() {
    return idResponsable;
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
  	return getIdProveedorPersona();
  }

  @Override
  public void setKey(Long key) {
  	this.idProveedorPersona = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedorPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdResponsable());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idPersona", getIdPersona());
		regresar.put("idProveedorPersona", getIdProveedorPersona());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idResponsable", getIdResponsable());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getIdPersona(), getIdProveedorPersona(), getIdUsuario(), getObservaciones(), getIdResponsable(), getRegistro()
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
    regresar.append("idProveedorPersona~");
    regresar.append(getIdProveedorPersona());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProveedorPersona());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticProveedorPersonaDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProveedorPersona()!= null && getIdProveedorPersona()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticProveedorPersonaDto other = (TrManticProveedorPersonaDto) obj;
    if (getIdProveedorPersona() != other.idProveedorPersona && (getIdProveedorPersona() == null || !getIdProveedorPersona().equals(other.idProveedorPersona))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProveedorPersona() != null ? getIdProveedorPersona().hashCode() : 0);
    return hash;
  }

}


