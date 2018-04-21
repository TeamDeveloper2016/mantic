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
@Table(name="tr_mantic_articulo_cliente_descuento")
public class TrManticArticuloClienteDescuentoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="vigencia_final")
  private Timestamp vigenciaFinal;
  @Column (name="vigencia_inicial")
  private Timestamp vigenciaInicial;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_cliente_descuentio")
  private Long idArticuloClienteDescuentio;
  @Column (name="porcentaje")
  private Double porcentaje;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloClienteDescuentoDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloClienteDescuentoDto(Long key) {
    this(new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TrManticArticuloClienteDescuentoDto(Timestamp vigenciaFinal, Timestamp vigenciaInicial, Long idCliente, Long idUsuario, String observaciones, Long idArticuloClienteDescuentio, Double porcentaje, Long idArticulo) {
    setVigenciaFinal(vigenciaFinal);
    setVigenciaInicial(vigenciaInicial);
    setIdCliente(idCliente);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdArticuloClienteDescuentio(idArticuloClienteDescuentio);
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

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
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

  public void setIdArticuloClienteDescuentio(Long idArticuloClienteDescuentio) {
    this.idArticuloClienteDescuentio = idArticuloClienteDescuentio;
  }

  public Long getIdArticuloClienteDescuentio() {
    return idArticuloClienteDescuentio;
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
  	return getIdArticuloClienteDescuentio();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloClienteDescuentio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getVigenciaFinal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaInicial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloClienteDescuentio());
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
		regresar.put("idCliente", getIdCliente());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idArticuloClienteDescuentio", getIdArticuloClienteDescuentio());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getVigenciaFinal(), getVigenciaInicial(), getIdCliente(), getIdUsuario(), getObservaciones(), getIdArticuloClienteDescuentio(), getPorcentaje(), getIdArticulo(), getRegistro()
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
    regresar.append("idArticuloClienteDescuentio~");
    regresar.append(getIdArticuloClienteDescuentio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloClienteDescuentio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloClienteDescuentoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloClienteDescuentio()!= null && getIdArticuloClienteDescuentio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloClienteDescuentoDto other = (TrManticArticuloClienteDescuentoDto) obj;
    if (getIdArticuloClienteDescuentio() != other.idArticuloClienteDescuentio && (getIdArticuloClienteDescuentio() == null || !getIdArticuloClienteDescuentio().equals(other.idArticuloClienteDescuentio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloClienteDescuentio() != null ? getIdArticuloClienteDescuentio().hashCode() : 0);
    return hash;
  }

}


