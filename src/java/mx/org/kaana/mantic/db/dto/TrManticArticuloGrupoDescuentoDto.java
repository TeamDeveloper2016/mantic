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
@Table(name="tr_mantic_articulo_grupo_descuento")
public class TrManticArticuloGrupoDescuentoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="vigencia_final")
  private Timestamp vigenciaFinal;
  @Column (name="vigencia_inicial")
  private Timestamp vigenciaInicial;
  @Column (name="id_grupo")
  private Long idGrupo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_grupo_descuento")
  private Long idArticuloGrupoDescuento;
  @Column (name="porcentaje")
  private Double porcentaje;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloGrupoDescuentoDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloGrupoDescuentoDto(Long key) {
    this(new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TrManticArticuloGrupoDescuentoDto(Timestamp vigenciaFinal, Timestamp vigenciaInicial, Long idGrupo, Long idUsuario, String observaciones, Long idArticuloGrupoDescuento, Double porcentaje, Long idArticulo) {
    setVigenciaFinal(vigenciaFinal);
    setVigenciaInicial(vigenciaInicial);
    setIdGrupo(idGrupo);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdArticuloGrupoDescuento(idArticuloGrupoDescuento);
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

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
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

  public void setIdArticuloGrupoDescuento(Long idArticuloGrupoDescuento) {
    this.idArticuloGrupoDescuento = idArticuloGrupoDescuento;
  }

  public Long getIdArticuloGrupoDescuento() {
    return idArticuloGrupoDescuento;
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
  	return getIdArticuloGrupoDescuento();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloGrupoDescuento = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getVigenciaFinal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaInicial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloGrupoDescuento());
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
		regresar.put("idGrupo", getIdGrupo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idArticuloGrupoDescuento", getIdArticuloGrupoDescuento());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getVigenciaFinal(), getVigenciaInicial(), getIdGrupo(), getIdUsuario(), getObservaciones(), getIdArticuloGrupoDescuento(), getPorcentaje(), getIdArticulo(), getRegistro()
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
    regresar.append("idArticuloGrupoDescuento~");
    regresar.append(getIdArticuloGrupoDescuento());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloGrupoDescuento());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloGrupoDescuentoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloGrupoDescuento()!= null && getIdArticuloGrupoDescuento()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloGrupoDescuentoDto other = (TrManticArticuloGrupoDescuentoDto) obj;
    if (getIdArticuloGrupoDescuento() != other.idArticuloGrupoDescuento && (getIdArticuloGrupoDescuento() == null || !getIdArticuloGrupoDescuento().equals(other.idArticuloGrupoDescuento))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloGrupoDescuento() != null ? getIdArticuloGrupoDescuento().hashCode() : 0);
    return hash;
  }
}