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
@Table(name="tc_mantic_ordenes_bitacora")
public class TcManticOrdenesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_bitacora")
  private Long idOrdenBitacora;
  @Column (name="id_compras_estatus")
  private Long idComprasEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticOrdenesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesBitacoraDto(Long key) {
    this(null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticOrdenesBitacoraDto(String justificacion, Long idOrdenCompra, Long idOrdenBitacora, Long idComprasEstatus) {
    setJustificacion(justificacion);
    setIdOrdenCompra(idOrdenCompra);
    setIdOrdenBitacora(idOrdenBitacora);
    setIdComprasEstatus(idComprasEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setIdOrdenBitacora(Long idOrdenBitacora) {
    this.idOrdenBitacora = idOrdenBitacora;
  }

  public Long getIdOrdenBitacora() {
    return idOrdenBitacora;
  }

  public void setIdComprasEstatus(Long idComprasEstatus) {
    this.idComprasEstatus = idComprasEstatus;
  }

  public Long getIdComprasEstatus() {
    return idComprasEstatus;
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
  	return getIdOrdenBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idOrdenBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdComprasEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("idOrdenBitacora", getIdOrdenBitacora());
		regresar.put("idComprasEstatus", getIdComprasEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdOrdenCompra(), getIdOrdenBitacora(), getIdComprasEstatus(), getRegistro()
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
    regresar.append("idOrdenBitacora~");
    regresar.append(getIdOrdenBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdOrdenBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticOrdenesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdOrdenBitacora()!= null && getIdOrdenBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticOrdenesBitacoraDto other = (TcManticOrdenesBitacoraDto) obj;
    if (getIdOrdenBitacora() != other.idOrdenBitacora && (getIdOrdenBitacora() == null || !getIdOrdenBitacora().equals(other.idOrdenBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdOrdenBitacora() != null ? getIdOrdenBitacora().hashCode() : 0);
    return hash;
  }

}


