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
@Table(name="tc_mantic_facturas_grupos")
public class TcManticFacturasGruposDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_factura")
  private Long idFactura;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_factura_grupo")
  private Long idFacturaGrupo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_aplicado")
  private Long idAplicado;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="id_venta_estatus")
  private Long idVentaEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticFacturasGruposDto() {
    this(new Long(-1L));
  }

  public TcManticFacturasGruposDto(Long key) {
    this(null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticFacturasGruposDto(Long idFactura, Long idFacturaGrupo, Long idUsuario, Long idAplicado, Long idVenta, Long idVentaEstatus) {
    setIdFactura(idFactura);
    setIdFacturaGrupo(idFacturaGrupo);
    setIdUsuario(idUsuario);
    setIdAplicado(idAplicado);
    setIdVenta(idVenta);
    setIdVentaEstatus(idVentaEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdFactura(Long idFactura) {
    this.idFactura = idFactura;
  }

  public Long getIdFactura() {
    return idFactura;
  }

  public void setIdFacturaGrupo(Long idFacturaGrupo) {
    this.idFacturaGrupo = idFacturaGrupo;
  }

  public Long getIdFacturaGrupo() {
    return idFacturaGrupo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAplicado(Long idAplicado) {
    this.idAplicado = idAplicado;
  }

  public Long getIdAplicado() {
    return idAplicado;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
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
  	return getIdFacturaGrupo();
  }

  @Override
  public void setKey(Long key) {
  	this.idFacturaGrupo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturaGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAplicado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
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
		regresar.put("idFactura", getIdFactura());
		regresar.put("idFacturaGrupo", getIdFacturaGrupo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAplicado", getIdAplicado());
		regresar.put("idVenta", getIdVenta());
		regresar.put("idVentaEstatus", getIdVentaEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdFactura(), getIdFacturaGrupo(), getIdUsuario(), getIdAplicado(), getIdVenta(), getIdVentaEstatus(), getRegistro()
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
    regresar.append("idFacturaGrupo~");
    regresar.append(getIdFacturaGrupo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFacturaGrupo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFacturasGruposDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFacturaGrupo()!= null && getIdFacturaGrupo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFacturasGruposDto other = (TcManticFacturasGruposDto) obj;
    if (getIdFacturaGrupo() != other.idFacturaGrupo && (getIdFacturaGrupo() == null || !getIdFacturaGrupo().equals(other.idFacturaGrupo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFacturaGrupo() != null ? getIdFacturaGrupo().hashCode() : 0);
    return hash;
  }

}


