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
@Table(name="tc_mantic_facturas_bitacora")
public class TcManticFacturasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_factura_estatus")
  private Long idFacturaEstatus;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="registro")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_factura_bitacora")
  private Long idFacturaBitacora;

  public TcManticFacturasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticFacturasBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticFacturasBitacoraDto(Long idFacturaEstatus, Long idFactura, String justificacion, Long idUsuario, Long idFacturaBitacora) {
    setIdFacturaEstatus(idFacturaEstatus);
    setIdFactura(idFactura);
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdFacturaBitacora(idFacturaBitacora);
  }
	
  public void setIdFacturaEstatus(Long idFacturaEstatus) {
    this.idFacturaEstatus = idFacturaEstatus;
  }

  public Long getIdFacturaEstatus() {
    return idFacturaEstatus;
  }

  public void setIdFactura(Long idFactura) {
    this.idFactura = idFactura;
  }

  public Long getIdFactura() {
    return idFactura;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdFacturaBitacora(Long idFacturaBitacora) {
    this.idFacturaBitacora = idFacturaBitacora;
  }

  public Long getIdFacturaBitacora() {
    return idFacturaBitacora;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdFacturaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idFacturaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdFacturaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturaBitacora());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idFacturaEstatus", getIdFacturaEstatus());
		regresar.put("idFactura", getIdFactura());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("registro", getRegistro());
		regresar.put("idFacturaBitacora", getIdFacturaBitacora());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdFacturaEstatus(), getIdFactura(), getJustificacion(), getIdUsuario(), getRegistro(), getIdFacturaBitacora()
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
    regresar.append("idFacturaBitacora~");
    regresar.append(getIdFacturaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFacturaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFacturasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFacturaBitacora()!= null && getIdFacturaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFacturasBitacoraDto other = (TcManticFacturasBitacoraDto) obj;
    if (getIdFacturaBitacora() != other.idFacturaBitacora && (getIdFacturaBitacora() == null || !getIdFacturaBitacora().equals(other.idFacturaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFacturaBitacora() != null ? getIdFacturaBitacora().hashCode() : 0);
    return hash;
  }
}