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
@Table(name="tr_mantic_cliente_representante")
public class TrManticClienteRepresentanteDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_representante")
  private Long idClienteRepresentante;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_representante")
  private Long idRepresentante;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticClienteRepresentanteDto() {
    this(new Long(-1L));
  }

  public TrManticClienteRepresentanteDto(Long key) {
    this(null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TrManticClienteRepresentanteDto(Long idCliente, Long idClienteRepresentante, Long idUsuario, Long idRepresentante, Long idPrincipal, String observaciones) {
    setIdCliente(idCliente);
    setIdClienteRepresentante(idClienteRepresentante);
    setIdUsuario(idUsuario);
    setIdRepresentante(idRepresentante);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdClienteRepresentante(Long idClienteRepresentante) {
    this.idClienteRepresentante = idClienteRepresentante;
  }

  public Long getIdClienteRepresentante() {
    return idClienteRepresentante;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdRepresentante(Long idRepresentante) {
    this.idRepresentante = idRepresentante;
  }

  public Long getIdRepresentante() {
    return idRepresentante;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdClienteRepresentante();
  }

  @Override
  public void setKey(Long key) {
  	this.idClienteRepresentante = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteRepresentante());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRepresentante());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCliente", getIdCliente());
		regresar.put("idClienteRepresentante", getIdClienteRepresentante());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idRepresentante", getIdRepresentante());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCliente(), getIdClienteRepresentante(), getIdUsuario(), getIdRepresentante(), getIdPrincipal(), getObservaciones(), getRegistro()
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
    regresar.append("idClienteRepresentante~");
    regresar.append(getIdClienteRepresentante());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdClienteRepresentante());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticClienteRepresentanteDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdClienteRepresentante()!= null && getIdClienteRepresentante()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticClienteRepresentanteDto other = (TrManticClienteRepresentanteDto) obj;
    if (getIdClienteRepresentante() != other.idClienteRepresentante && (getIdClienteRepresentante() == null || !getIdClienteRepresentante().equals(other.idClienteRepresentante))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdClienteRepresentante() != null ? getIdClienteRepresentante().hashCode() : 0);
    return hash;
  }

}


