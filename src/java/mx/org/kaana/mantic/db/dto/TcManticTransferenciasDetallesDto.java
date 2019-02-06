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
@Table(name="tc_mantic_transferencias_detalles")
public class TcManticTransferenciasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="cantidades")
  private Double cantidades;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia_detalle")
  private Long idTransferenciaDetalle;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_transferencia")
  private Long idTransferencia;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasDetallesDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticTransferenciasDetallesDto(String codigo, Double cantidades, Long idTransferenciaDetalle, Double cantidad, Long idArticulo, Long idTransferencia, String nombre) {
    setCodigo(codigo);
    setCantidades(cantidades);
    setIdTransferenciaDetalle(idTransferenciaDetalle);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdTransferencia(idTransferencia);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCantidades(Double cantidades) {
    this.cantidades = cantidades;
  }

  public Double getCantidades() {
    return cantidades;
  }

  public void setIdTransferenciaDetalle(Long idTransferenciaDetalle) {
    this.idTransferenciaDetalle = idTransferenciaDetalle;
  }

  public Long getIdTransferenciaDetalle() {
    return idTransferenciaDetalle;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdTransferencia() {
    return idTransferencia;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdTransferenciaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferenciaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidades());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("cantidades", getCantidades());
		regresar.put("idTransferenciaDetalle", getIdTransferenciaDetalle());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idTransferencia", getIdTransferencia());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getCantidades(), getIdTransferenciaDetalle(), getCantidad(), getIdArticulo(), getIdTransferencia(), getNombre(), getRegistro()
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
    regresar.append("idTransferenciaDetalle~");
    regresar.append(getIdTransferenciaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaDetalle()!= null && getIdTransferenciaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasDetallesDto other = (TcManticTransferenciasDetallesDto) obj;
    if (getIdTransferenciaDetalle() != other.idTransferenciaDetalle && (getIdTransferenciaDetalle() == null || !getIdTransferenciaDetalle().equals(other.idTransferenciaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaDetalle() != null ? getIdTransferenciaDetalle().hashCode() : 0);
    return hash;
  }

}


