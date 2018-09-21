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
@Table(name="tc_mantic_articulos_dimenciones")
public class TcManticArticulosDimencionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ancho")
  private Long ancho;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_dimension")
  private Long idArticuloDimension;
  @Column (name="largo")
  private Long largo;
  @Column (name="alto")
  private Long alto;
  @Column (name="id_articulo")
  private Long idArticulo;

  public TcManticArticulosDimencionesDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosDimencionesDto(Long key) {
    this(0L, new Long(-1L), 0L, 0L, null);
    setKey(key);
  }

  public TcManticArticulosDimencionesDto(Long ancho, Long idArticuloDimension, Long largo, Long alto, Long idArticulo) {
    setAncho(ancho);
    setIdArticuloDimension(idArticuloDimension);
    setLargo(largo);
    setAlto(alto);
    setIdArticulo(idArticulo);
  }
	
  public void setAncho(Long ancho) {
    this.ancho = ancho;
  }

  public Long getAncho() {
    return ancho;
  }

  public void setIdArticuloDimension(Long idArticuloDimension) {
    this.idArticuloDimension = idArticuloDimension;
  }

  public Long getIdArticuloDimension() {
    return idArticuloDimension;
  }

  public void setLargo(Long largo) {
    this.largo = largo;
  }

  public Long getLargo() {
    return largo;
  }

  public void setAlto(Long alto) {
    this.alto = alto;
  }

  public Long getAlto() {
    return alto;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloDimension();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloDimension = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getAncho());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloDimension());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLargo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("ancho", getAncho());
		regresar.put("idArticuloDimension", getIdArticuloDimension());
		regresar.put("largo", getLargo());
		regresar.put("alto", getAlto());
		regresar.put("idArticulo", getIdArticulo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getAncho(), getIdArticuloDimension(), getLargo(), getAlto(), getIdArticulo()
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
    regresar.append("idArticuloDimension~");
    regresar.append(getIdArticuloDimension());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloDimension());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosDimencionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloDimension()!= null && getIdArticuloDimension()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosDimencionesDto other = (TcManticArticulosDimencionesDto) obj;
    if (getIdArticuloDimension() != other.idArticuloDimension && (getIdArticuloDimension() == null || !getIdArticuloDimension().equals(other.idArticuloDimension))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloDimension() != null ? getIdArticuloDimension().hashCode() : 0);
    return hash;
  }

}


