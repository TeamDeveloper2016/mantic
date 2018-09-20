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
@Table(name="tc_mantic_listas_precios")
public class TcManticListasPreciosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_lista_precio")
  private Long idListaPrecio;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="nombre")
  private String nombre;
  @Column (name="logotipo")
  private String logotipo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticListasPreciosDto() {
    this(new Long(-1L));
  }

  public TcManticListasPreciosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticListasPreciosDto(Long idListaPrecio, Long idEmpresa, Long idProveedor, String nombre, String logotipo, Long idUsuario, String observaciones) {
    setIdListaPrecio(idListaPrecio);
    setIdEmpresa(idEmpresa);
    setIdProveedor(idProveedor);
    setNombre(nombre);
    setLogotipo(logotipo);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdListaPrecio(Long idListaPrecio) {
    this.idListaPrecio = idListaPrecio;
  }

  public Long getIdListaPrecio() {
    return idListaPrecio;
  }

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa=idEmpresa;
	}

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public String getLogotipo() {
		return logotipo;
	}

	public void setLogotipo(String logotipo) {
		this.logotipo=logotipo;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdListaPrecio();
  }

  @Override
  public void setKey(Long key) {
  	this.idListaPrecio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdListaPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLogotipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
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
		regresar.put("idListaPrecio", getIdListaPrecio());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("nombre", getNombre());
		regresar.put("logotipo", getLogotipo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdListaPrecio(), getIdEmpresa(), getIdProveedor(), getNombre(), getLogotipo(), getIdUsuario(), getObservaciones(), getRegistro()
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
    regresar.append("idListaPrecio~");
    regresar.append(getIdListaPrecio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdListaPrecio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticListasPreciosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdListaPrecio()!= null && getIdListaPrecio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticListasPreciosDto other = (TcManticListasPreciosDto) obj;
    if (getIdListaPrecio() != other.idListaPrecio && (getIdListaPrecio() == null || !getIdListaPrecio().equals(other.idListaPrecio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdListaPrecio() != null ? getIdListaPrecio().hashCode() : 0);
    return hash;
  }

}


