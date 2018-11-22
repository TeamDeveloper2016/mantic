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
@Table(name="tc_mantic_faltantes")
public class TcManticFaltantesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_faltante")
  private Long idFaltante;
	@Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_vigente")
  private Long idVigente;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticFaltantesDto() {
    this(new Long(-1L));
  }

  public TcManticFaltantesDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticFaltantesDto(Long idUsuario, Long idFaltante, String observaciones, Double cantidad, Long idVigente, Long idArticulo, Long idEmpresa) {
    setIdUsuario(idUsuario);
    setIdFaltante(idFaltante);
    setObservaciones(observaciones);
    setCantidad(cantidad);
    setIdVigente(idVigente);
    setIdArticulo(idArticulo);
		this.idEmpresa= idEmpresa;
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdFaltante(Long idFaltante) {
    this.idFaltante = idFaltante;
  }

  public Long getIdFaltante() {
    return idFaltante;
  }

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa=idEmpresa;
	}

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdVigente(Long idVigente) {
    this.idVigente = idVigente;
  }

  public Long getIdVigente() {
    return idVigente;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
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
  	return getIdFaltante();
  }

  @Override
  public void setKey(Long key) {
  	this.idFaltante = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFaltante());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idFaltante", getIdFaltante());
		regresar.put("observaciones", getObservaciones());
		regresar.put("cantidad", getCantidad());
		regresar.put("idVigente", getIdVigente());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdFaltante(), getObservaciones(), getCantidad(), getIdVigente(), getIdArticulo(), getIdEmpresa(), getRegistro()
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
    regresar.append("idFaltante~");
    regresar.append(getIdFaltante());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFaltante());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFaltantesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFaltante()!= null && getIdFaltante()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFaltantesDto other = (TcManticFaltantesDto) obj;
    if (getIdFaltante() != other.idFaltante && (getIdFaltante() == null || !getIdFaltante().equals(other.idFaltante))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFaltante() != null ? getIdFaltante().hashCode() : 0);
    return hash;
  }

}


