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
@Table(name="tc_mantic_articulos_descuentos")
public class TcManticArticulosDescuentosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="vigencia_final")
  private Timestamp vigenciaFinal;
  @Column (name="vigencia_inicial")
  private Timestamp vigenciaInicial;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_descuento")
  private Long idArticuloDescuento;
  @Column (name="porcentaje")
  private Double porcentaje;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticArticulosDescuentosDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosDescuentosDto(Long key) {
    this(new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticArticulosDescuentosDto(Timestamp vigenciaFinal, Timestamp vigenciaInicial, Long idUsuario, String observaciones, Long idArticuloDescuento, Double porcentaje, Long idArticulo) {
    setVigenciaFinal(vigenciaFinal);
    setVigenciaInicial(vigenciaInicial);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdArticuloDescuento(idArticuloDescuento);
    setPorcentaje(porcentaje);
    setIdArticulo(idArticulo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setVigenciaFinal(Timestamp vigenciaFinal) {
    this.vigenciaFinal = vigenciaFinal;
  }

  public Timestamp getVigenciaFinal() {
    return vigenciaFinal;
  }

  public void setVigenciaInicial(Timestamp vigenciaInicial) {
    this.vigenciaInicial = vigenciaInicial;
  }

  public Timestamp getVigenciaInicial() {
    return vigenciaInicial;
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

  public void setIdArticuloDescuento(Long idArticuloDescuento) {
    this.idArticuloDescuento = idArticuloDescuento;
  }

  public Long getIdArticuloDescuento() {
    return idArticuloDescuento;
  }

  public void setPorcentaje(Double porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Double getPorcentaje() {
    return porcentaje;
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
  	return getIdArticuloDescuento();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloDescuento = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getVigenciaFinal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaInicial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("vigenciaFinal", getVigenciaFinal());
		regresar.put("vigenciaInicial", getVigenciaInicial());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idArticuloDescuento", getIdArticuloDescuento());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getVigenciaFinal(), getVigenciaInicial(), getIdUsuario(), getObservaciones(), getIdArticuloDescuento(), getPorcentaje(), getIdArticulo(), getRegistro()
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
    regresar.append("idArticuloDescuento~");
    regresar.append(getIdArticuloDescuento());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloDescuento());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosDescuentosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloDescuento()!= null && getIdArticuloDescuento()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosDescuentosDto other = (TcManticArticulosDescuentosDto) obj;
    if (getIdArticuloDescuento() != other.idArticuloDescuento && (getIdArticuloDescuento() == null || !getIdArticuloDescuento().equals(other.idArticuloDescuento))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloDescuento() != null ? getIdArticuloDescuento().hashCode() : 0);
    return hash;
  }

}


