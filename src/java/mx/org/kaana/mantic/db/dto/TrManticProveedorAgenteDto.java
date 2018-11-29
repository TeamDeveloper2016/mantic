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
@Table(name="tr_mantic_proveedor_agente")
public class TrManticProveedorAgenteDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_proveedor_agente")
  private Long idProveedorAgente;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_agente")
  private Long idAgente;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticProveedorAgenteDto() {
    this(new Long(-1L));
  }

  public TrManticProveedorAgenteDto(Long key) {
    this(null, new Long(-1L), null, 2L, null, null);
    setKey(key);
  }

  public TrManticProveedorAgenteDto(Long idProveedor, Long idProveedorAgente, Long idUsuario, Long idPrincipal, String observaciones, Long idAgente) {
    setIdProveedor(idProveedor);
    setIdProveedorAgente(idProveedorAgente);
    setIdUsuario(idUsuario);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setIdAgente(idAgente);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdProveedorAgente(Long idProveedorAgente) {
    this.idProveedorAgente = idProveedorAgente;
  }

  public Long getIdProveedorAgente() {
    return idProveedorAgente;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdAgente(Long idAgente) {
    this.idAgente = idAgente;
  }

  public Long getIdAgente() {
    return idAgente;
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
  	return getIdProveedorAgente();
  }

  @Override
  public void setKey(Long key) {
  	this.idProveedorAgente = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedorAgente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAgente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idProveedorAgente", getIdProveedorAgente());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idAgente", getIdAgente());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getIdProveedorAgente(), getIdUsuario(), getIdPrincipal(), getObservaciones(), getIdAgente(), getRegistro()
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
    regresar.append("idProveedorAgente~");
    regresar.append(getIdProveedorAgente());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProveedorAgente());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticProveedorAgenteDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProveedorAgente()!= null && getIdProveedorAgente()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticProveedorAgenteDto other = (TrManticProveedorAgenteDto) obj;
    if (getIdProveedorAgente() != other.idProveedorAgente && (getIdProveedorAgente() == null || !getIdProveedorAgente().equals(other.idProveedorAgente))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProveedorAgente() != null ? getIdProveedorAgente().hashCode() : 0);
    return hash;
  }

}


