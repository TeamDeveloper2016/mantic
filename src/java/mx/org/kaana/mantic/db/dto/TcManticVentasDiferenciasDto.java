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
@Table(name="tc_mantic_ventas_diferencias")
public class TcManticVentasDiferenciasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_diferencia")
  private Long idVentaDiferencia;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="importe_detalle")
  private Double importeDetalle;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="importe")
  private Double importe;
  @Column (name="diferencia")
  private Double diferencia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticVentasDiferenciasDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDiferenciasDto(Long key) {
    this(new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticVentasDiferenciasDto(Long idVentaDiferencia, Long idUsuario, Double importeDetalle, Long idVenta, Double importe, Double diferencia) {
    setIdVentaDiferencia(idVentaDiferencia);
    setIdUsuario(idUsuario);
    setImporteDetalle(importeDetalle);
    setIdVenta(idVenta);
    setImporte(importe);
    setDiferencia(diferencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdVentaDiferencia(Long idVentaDiferencia) {
    this.idVentaDiferencia = idVentaDiferencia;
  }

  public Long getIdVentaDiferencia() {
    return idVentaDiferencia;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setImporteDetalle(Double importeDetalle) {
    this.importeDetalle = importeDetalle;
  }

  public Double getImporteDetalle() {
    return importeDetalle;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setDiferencia(Double diferencia) {
    this.diferencia = diferencia;
  }

  public Double getDiferencia() {
    return diferencia;
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
  	return getIdVentaDiferencia();
  }

  @Override
  public void setKey(Long key) {
  	this.idVentaDiferencia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdVentaDiferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporteDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDiferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idVentaDiferencia", getIdVentaDiferencia());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("importeDetalle", getImporteDetalle());
		regresar.put("idVenta", getIdVenta());
		regresar.put("importe", getImporte());
		regresar.put("diferencia", getDiferencia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdVentaDiferencia(), getIdUsuario(), getImporteDetalle(), getIdVenta(), getImporte(), getDiferencia(), getRegistro()
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
    regresar.append("idVentaDiferencia~");
    regresar.append(getIdVentaDiferencia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVentaDiferencia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticVentasDiferenciasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVentaDiferencia()!= null && getIdVentaDiferencia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticVentasDiferenciasDto other = (TcManticVentasDiferenciasDto) obj;
    if (getIdVentaDiferencia() != other.idVentaDiferencia && (getIdVentaDiferencia() == null || !getIdVentaDiferencia().equals(other.idVentaDiferencia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVentaDiferencia() != null ? getIdVentaDiferencia().hashCode() : 0);
    return hash;
  }
}