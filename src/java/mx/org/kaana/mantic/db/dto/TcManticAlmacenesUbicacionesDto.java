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
@Table(name="tc_mantic_almacenes_ubicaciones")
public class TcManticAlmacenesUbicacionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="cuarto")
  private String cuarto;
  @Column (name="piso")
  private String piso;
  @Column (name="anaquel")
  private String anaquel;
  @Column (name="charola")
  private String charola;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_almacen_ubicacion")
  private Long idAlmacenUbicacion;
  @Column (name="registro")
  private Timestamp registro;
	@Column (name="nivel")
  private Long nivel;

  public TcManticAlmacenesUbicacionesDto() {
    this(new Long(-1L));
  }

  public TcManticAlmacenesUbicacionesDto(Long key) {
    this(null, null, null, null, null, null, null, new Long(-1L), -1L);
    setKey(key);
  }

  public TcManticAlmacenesUbicacionesDto(String descripcion, String cuarto, String piso, String anaquel, String charola, Long idUsuario, Long idAlmacen, Long idAlmacenUbicacion) {
		this(descripcion, cuarto, piso, anaquel, charola, idUsuario, idAlmacen, idAlmacenUbicacion, 1L);
	}
	
  public TcManticAlmacenesUbicacionesDto(String descripcion, String cuarto, String piso, String anaquel, String charola, Long idUsuario, Long idAlmacen, Long idAlmacenUbicacion, Long nivel) {
    setDescripcion(descripcion);
    setCuarto(cuarto);
    setPiso(piso);
    setAnaquel(anaquel);
    setCharola(charola);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setIdAlmacenUbicacion(idAlmacenUbicacion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setNivel(nivel);
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setCuarto(String cuarto) {
    this.cuarto = cuarto;
  }

  public String getCuarto() {
    return cuarto;
  }

  public void setPiso(String piso) {
    this.piso = piso;
  }

  public String getPiso() {
    return piso;
  }

  public void setAnaquel(String anaquel) {
    this.anaquel = anaquel;
  }

  public String getAnaquel() {
    return anaquel;
  }

  public void setCharola(String charola) {
    this.charola = charola;
  }

  public String getCharola() {
    return charola;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacenUbicacion(Long idAlmacenUbicacion) {
    this.idAlmacenUbicacion = idAlmacenUbicacion;
  }

  public Long getIdAlmacenUbicacion() {
    return idAlmacenUbicacion;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Long getNivel() {
		return nivel;
	}

	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdAlmacenUbicacion();
  }

  @Override
  public void setKey(Long key) {
  	this.idAlmacenUbicacion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuarto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPiso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAnaquel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCharola());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacenUbicacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("cuarto", getCuarto());
		regresar.put("piso", getPiso());
		regresar.put("anaquel", getAnaquel());
		regresar.put("charola", getCharola());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("idAlmacenUbicacion", getIdAlmacenUbicacion());
		regresar.put("registro", getRegistro());
		regresar.put("nivel", getNivel());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getCuarto(), getPiso(), getAnaquel(), getCharola(), getIdUsuario(), getIdAlmacen(), getIdAlmacenUbicacion(), getRegistro(), getNivel()
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
    regresar.append("idAlmacenUbicacion~");
    regresar.append(getIdAlmacenUbicacion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAlmacenUbicacion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticAlmacenesUbicacionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAlmacenUbicacion()!= null && getIdAlmacenUbicacion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticAlmacenesUbicacionesDto other = (TcManticAlmacenesUbicacionesDto) obj;
    if (getIdAlmacenUbicacion() != other.idAlmacenUbicacion && (getIdAlmacenUbicacion() == null || !getIdAlmacenUbicacion().equals(other.idAlmacenUbicacion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAlmacenUbicacion() != null ? getIdAlmacenUbicacion().hashCode() : 0);
    return hash;
  }

}


