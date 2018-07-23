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
 *@date 21/07/2018
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_transferencias")
public class TcManticTransferenciasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia")
  private Long idTransferencia;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="id_destino")
  private Long idDestino;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_solicito")
  private Long idSolicito;
  @Column (name="id_usuario")
  private Long idUsusrio;
  @Column (name="idTransferenciaEstatus")
  private Long idTransferenciaEstatus;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasDto(Long key) {
    this(new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),null,null);
    setKey(key);
  }

  public TcManticTransferenciasDto(Long idTransferencia, Long idAlmacen, Long idDestino, Long idArticulo, Long idSolicito, Long idUsusrio, Long idTransferenciaEstatus, Long cantidad, String observaciones, Timestamp registro) {
    setIdTransferencia(idTransferencia);
    setIdAlmacen(idAlmacen);
    setIdDestino(idDestino);
    setIdArticulo(idArticulo);
    setIdSolicito(idSolicito);
    setIdUsusrio(idUsusrio);
    setIdTransferenciaEstatus(idTransferenciaEstatus);
    setCantidad(cantidad);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));;
  }
  
  public Long getIdTransferencia() {
    return idTransferencia;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdDestino() {
    return idDestino;
  }

  public void setIdDestino(Long idDestino) {
    this.idDestino = idDestino;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdSolicito() {
    return idSolicito;
  }

  public void setIdSolicito(Long idSolicito) {
    this.idSolicito = idSolicito;
  }

  public Long getIdUsusrio() {
    return idUsusrio;
  }

  public void setIdUsusrio(Long idUsusrio) {
    this.idUsusrio = idUsusrio;
  }

  public Long getIdTransferenciaEstatus() {
    return idTransferenciaEstatus;
  }

  public void setIdTransferenciaEstatus(Long idTransferenciaEstatus) {
    this.idTransferenciaEstatus = idTransferenciaEstatus;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdTransferencia();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferencia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDestino());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSolicito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsusrio());
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
		regresar.put("cantidad", getCantidad());
    regresar.put("idAlmacen", getIdAlmacen());
    regresar.put("idArticulo", getIdArticulo());
    regresar.put("idDestino", getIdDestino());
    regresar.put("idSolicito", getIdSolicito());
    regresar.put("idTransferencia", getIdTransferencia());
    regresar.put("idTransferenciaEstatus", getIdTransferenciaEstatus());
    regresar.put("idUsusrio", getIdUsusrio());
    regresar.put("observaciones", getObservaciones());
    regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getCantidad(),
      getIdAlmacen(),
      getIdArticulo(),
      getIdDestino(),
      getIdSolicito(),
      getIdTransferencia(),
      getIdTransferenciaEstatus(),
      getIdUsusrio(),
      getObservaciones(),
      getRegistro()
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
    regresar.append("idTrabajo~");
    regresar.append(getIdTransferencia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferencia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferencia()!= null && getIdTransferencia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasDto other = (TcManticTransferenciasDto) obj;
    if (getIdTransferencia() != other.idTransferencia && (getIdTransferencia() == null || !getIdTransferencia().equals(other.idTransferencia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferencia()!= null ? getIdTransferencia().hashCode() : 0);
    return hash;
  }

}


