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
@Table(name="tc_mantic_ventas_estatus")
public class TcManticVentasEstatusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_justificacion")
  private Long idJustificacion;
  @Column (name="estatus_asociados")
  private String estatusAsociados;
  @Column (name="decripcion")
  private String decripcion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_tipo_documento")
  private Long idTipoDocumento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_estatus")
  private Long idVentaEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticVentasEstatusDto() {
    this(new Long(-1L));
  }

  public TcManticVentasEstatusDto(Long key) {
    this(null, null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticVentasEstatusDto(Long idJustificacion, String estatusAsociados, String decripcion, String nombre, Long idTipoDocumento, Long idVentaEstatus) {
    setIdJustificacion(idJustificacion);
    setEstatusAsociados(estatusAsociados);
    setDecripcion(decripcion);
    setNombre(nombre);
    setIdTipoDocumento(idTipoDocumento);
    setIdVentaEstatus(idVentaEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdJustificacion(Long idJustificacion) {
    this.idJustificacion = idJustificacion;
  }

  public Long getIdJustificacion() {
    return idJustificacion;
  }

  public void setEstatusAsociados(String estatusAsociados) {
    this.estatusAsociados = estatusAsociados;
  }

  public String getEstatusAsociados() {
    return estatusAsociados;
  }

  public void setDecripcion(String decripcion) {
    this.decripcion = decripcion;
  }

  public String getDecripcion() {
    return decripcion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdTipoDocumento(Long idTipoDocumento) {
    this.idTipoDocumento = idTipoDocumento;
  }

  public Long getIdTipoDocumento() {
    return idTipoDocumento;
  }

  public void setIdVentaEstatus(Long idVentaEstatus) {
    this.idVentaEstatus = idVentaEstatus;
  }

  public Long getIdVentaEstatus() {
    return idVentaEstatus;
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
  	return getIdVentaEstatus();
  }

  @Override
  public void setKey(Long key) {
  	this.idVentaEstatus = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAsociados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDecripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDocumento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idJustificacion", getIdJustificacion());
		regresar.put("estatusAsociados", getEstatusAsociados());
		regresar.put("decripcion", getDecripcion());
		regresar.put("nombre", getNombre());
		regresar.put("idTipoDocumento", getIdTipoDocumento());
		regresar.put("idVentaEstatus", getIdVentaEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdJustificacion(), getEstatusAsociados(), getDecripcion(), getNombre(), getIdTipoDocumento(), getIdVentaEstatus(), getRegistro()
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
    regresar.append("idVentaEstatus~");
    regresar.append(getIdVentaEstatus());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVentaEstatus());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticVentasEstatusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVentaEstatus()!= null && getIdVentaEstatus()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticVentasEstatusDto other = (TcManticVentasEstatusDto) obj;
    if (getIdVentaEstatus() != other.idVentaEstatus && (getIdVentaEstatus() == null || !getIdVentaEstatus().equals(other.idVentaEstatus))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVentaEstatus() != null ? getIdVentaEstatus().hashCode() : 0);
    return hash;
  }
}