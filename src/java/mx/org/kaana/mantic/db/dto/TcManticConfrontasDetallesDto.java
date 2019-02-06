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
@Table(name="tc_mantic_confrontas_detalles")
public class TcManticConfrontasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_confronta_detalle")
  private Long idConfrontaDetalle;
  @Column (name="cantidades")
  private Double cantidades;
  @Column (name="id_confronta")
  private Long idConfronta;
  @Column (name="id_transferencia_detalle")
  private Long idTransferenciaDetalle;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_aplicar")
  private Long idAplicar;
  @Column (name="nombre")
  private String nombre;
  @Column (name="declarados")
  private Double declarados;
  @Column (name="diferencia")
  private Double diferencia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConfrontasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticConfrontasDetallesDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticConfrontasDetallesDto(String codigo, Long idConfrontaDetalle, Double cantidades, Long idConfronta, Long idTransferenciaDetalle, Double cantidad, Long idArticulo, Long idAplicar, String nombre, Double declarados, Double diferencia) {
    setCodigo(codigo);
    setIdConfrontaDetalle(idConfrontaDetalle);
    setCantidades(cantidades);
    setIdConfronta(idConfronta);
    setIdTransferenciaDetalle(idTransferenciaDetalle);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdAplicar(idAplicar);
    setNombre(nombre);
    setDeclarados(declarados);
    setDiferencia(diferencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setIdConfrontaDetalle(Long idConfrontaDetalle) {
    this.idConfrontaDetalle = idConfrontaDetalle;
  }

  public Long getIdConfrontaDetalle() {
    return idConfrontaDetalle;
  }

  public void setCantidades(Double cantidades) {
    this.cantidades = cantidades;
  }

  public Double getCantidades() {
    return cantidades;
  }

  public void setIdConfronta(Long idConfronta) {
    this.idConfronta = idConfronta;
  }

  public Long getIdConfronta() {
    return idConfronta;
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

  public void setIdAplicar(Long idAplicar) {
    this.idAplicar = idAplicar;
  }

  public Long getIdAplicar() {
    return idAplicar;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setDeclarados(Double declarados) {
    this.declarados = declarados;
  }

  public Double getDeclarados() {
    return declarados;
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
  	return getIdConfrontaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idConfrontaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfrontaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidades());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfronta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAplicar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDeclarados());
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
		regresar.put("codigo", getCodigo());
		regresar.put("idConfrontaDetalle", getIdConfrontaDetalle());
		regresar.put("cantidades", getCantidades());
		regresar.put("idConfronta", getIdConfronta());
		regresar.put("idTransferenciaDetalle", getIdTransferenciaDetalle());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idAplicar", getIdAplicar());
		regresar.put("nombre", getNombre());
		regresar.put("declarados", getDeclarados());
		regresar.put("diferencia", getDiferencia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getIdConfrontaDetalle(), getCantidades(), getIdConfronta(), getIdTransferenciaDetalle(), getCantidad(), getIdArticulo(), getIdAplicar(), getNombre(), getDeclarados(), getDiferencia(), getRegistro()
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
    regresar.append("idConfrontaDetalle~");
    regresar.append(getIdConfrontaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConfrontaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConfrontasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConfrontaDetalle()!= null && getIdConfrontaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConfrontasDetallesDto other = (TcManticConfrontasDetallesDto) obj;
    if (getIdConfrontaDetalle() != other.idConfrontaDetalle && (getIdConfrontaDetalle() == null || !getIdConfrontaDetalle().equals(other.idConfrontaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConfrontaDetalle() != null ? getIdConfrontaDetalle().hashCode() : 0);
    return hash;
  }

}


