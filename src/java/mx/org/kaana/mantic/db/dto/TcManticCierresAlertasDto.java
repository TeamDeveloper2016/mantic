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
@Table(name="tc_mantic_cierres_alertas")
public class TcManticCierresAlertasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cierre")
  private Long idCierre;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_notifica")
  private Long idNotifica;
  @Column (name="mensaje")
  private String mensaje;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre_alerta")
  private Long idCierreAlerta;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCierresAlertasDto() {
    this(new Long(-1L));
  }

  public TcManticCierresAlertasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticCierresAlertasDto(Long idCierre, Long idUsuario, Long idNotifica, String mensaje, Long idCierreAlerta, Double importe) {
    setIdCierre(idCierre);
    setIdUsuario(idUsuario);
    setIdNotifica(idNotifica);
    setMensaje(mensaje);
    setIdCierreAlerta(idCierreAlerta);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdNotifica(Long idNotifica) {
    this.idNotifica = idNotifica;
  }

  public Long getIdNotifica() {
    return idNotifica;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setIdCierreAlerta(Long idCierreAlerta) {
    this.idCierreAlerta = idCierreAlerta;
  }

  public Long getIdCierreAlerta() {
    return idCierreAlerta;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdCierreAlerta();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierreAlerta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotifica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreAlerta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCierre", getIdCierre());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idNotifica", getIdNotifica());
		regresar.put("mensaje", getMensaje());
		regresar.put("idCierreAlerta", getIdCierreAlerta());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCierre(), getIdUsuario(), getIdNotifica(), getMensaje(), getIdCierreAlerta(), getImporte(), getRegistro()
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
    regresar.append("idCierreAlerta~");
    regresar.append(getIdCierreAlerta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierreAlerta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresAlertasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierreAlerta()!= null && getIdCierreAlerta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresAlertasDto other = (TcManticCierresAlertasDto) obj;
    if (getIdCierreAlerta() != other.idCierreAlerta && (getIdCierreAlerta() == null || !getIdCierreAlerta().equals(other.idCierreAlerta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierreAlerta() != null ? getIdCierreAlerta().hashCode() : 0);
    return hash;
  }

}


