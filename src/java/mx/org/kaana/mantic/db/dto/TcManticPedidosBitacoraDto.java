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
@Table(name="tc_mantic_pedidos_bitacora")
public class TcManticPedidosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_pedido_bitacora")
  private Long idPedidoBitacora;
  @Column (name="id_pedido")
  private Long idPedido;
  @Column (name="id_pedido_estatus")
  private Long idPedidoEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticPedidosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticPedidosBitacoraDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticPedidosBitacoraDto(Long idUsuario, String observaciones, Long idPedidoBitacora, Long idPedido, Long idPedidoEstatus) {
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdPedidoBitacora(idPedidoBitacora);
    setIdPedido(idPedido);
    setIdPedidoEstatus(idPedidoEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdPedidoBitacora(Long idPedidoBitacora) {
    this.idPedidoBitacora = idPedidoBitacora;
  }

  public Long getIdPedidoBitacora() {
    return idPedidoBitacora;
  }

  public void setIdPedido(Long idPedido) {
    this.idPedido = idPedido;
  }

  public Long getIdPedido() {
    return idPedido;
  }

  public void setIdPedidoEstatus(Long idPedidoEstatus) {
    this.idPedidoEstatus = idPedidoEstatus;
  }

  public Long getIdPedidoEstatus() {
    return idPedidoEstatus;
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
  	return getIdPedidoBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idPedidoBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedidoBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedidoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idPedidoBitacora", getIdPedidoBitacora());
		regresar.put("idPedido", getIdPedido());
		regresar.put("idPedidoEstatus", getIdPedidoEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getObservaciones(), getIdPedidoBitacora(), getIdPedido(), getIdPedidoEstatus(), getRegistro()
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
    regresar.append("idPedidoBitacora~");
    regresar.append(getIdPedidoBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPedidoBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPedidosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPedidoBitacora()!= null && getIdPedidoBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPedidosBitacoraDto other = (TcManticPedidosBitacoraDto) obj;
    if (getIdPedidoBitacora() != other.idPedidoBitacora && (getIdPedidoBitacora() == null || !getIdPedidoBitacora().equals(other.idPedidoBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPedidoBitacora() != null ? getIdPedidoBitacora().hashCode() : 0);
    return hash;
  }

}


