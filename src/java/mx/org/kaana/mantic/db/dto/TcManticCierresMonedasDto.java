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
@Table(name="tc_mantic_cierres_monedas")
public class TcManticCierresMonedasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cierre")
  private Long idCierre;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre_moneda")
  private Long idCierreMoneda;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_moneda")
  private Long idMoneda;
  @Column (name="id_efectivo")
  private Long idEfectivo;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCierresMonedasDto() {
    this(new Long(-1L));
  }

  public TcManticCierresMonedasDto(Long key) {
    this(null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticCierresMonedasDto(Long idCierre, Long idCierreMoneda, Long cantidad, Long idMoneda, Double importe, Long idEfectivo) {
    setIdCierre(idCierre);
    setIdCierreMoneda(idCierreMoneda);
    setCantidad(cantidad);
    setIdMoneda(idMoneda);
    setIdEfectivo(idEfectivo);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setIdCierreMoneda(Long idCierreMoneda) {
    this.idCierreMoneda = idCierreMoneda;
  }

  public Long getIdCierreMoneda() {
    return idCierreMoneda;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setIdMoneda(Long idMoneda) {
    this.idMoneda = idMoneda;
  }

  public Long getIdMoneda() {
    return idMoneda;
  }

	public Long getIdEfectivo() {
		return idEfectivo;
	}

	public void setIdEfectivo(Long idEfectivo) {
		this.idEfectivo=idEfectivo;
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
  	return getIdCierreMoneda();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierreMoneda = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreMoneda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMoneda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEfectivo());
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
		regresar.put("idCierreMoneda", getIdCierreMoneda());
		regresar.put("cantidad", getCantidad());
		regresar.put("idMoneda", getIdMoneda());
		regresar.put("idEfectivo", getIdEfectivo());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdCierre(), getIdCierreMoneda(), getCantidad(), getIdMoneda(), getImporte(), getRegistro(), getIdEfectivo()
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
    regresar.append("idCierreMoneda~");
    regresar.append(getIdCierreMoneda());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierreMoneda());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresMonedasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierreMoneda()!= null && getIdCierreMoneda()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresMonedasDto other = (TcManticCierresMonedasDto) obj;
    if (getIdCierreMoneda() != other.idCierreMoneda && (getIdCierreMoneda() == null || !getIdCierreMoneda().equals(other.idCierreMoneda))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierreMoneda() != null ? getIdCierreMoneda().hashCode() : 0);
    return hash;
  }

}


