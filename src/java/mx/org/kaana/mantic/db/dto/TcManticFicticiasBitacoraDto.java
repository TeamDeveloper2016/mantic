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
@Table(name="tc_mantic_ventas_bitacora")
public class TcManticFicticiasBitacoraDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=-1495517599059966946L;
		
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_venta_estatus")
  private Long idFicticiaEstatus;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_venta")
  private Long idFicticia;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_bitacora")
  private Long idFicticiaBitacora;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticFicticiasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticFicticiasBitacoraDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticFicticiasBitacoraDto(String consecutivo, String justificacion, Long idFicticiaEstatus, Long idUsuario, Long idFicticia, Long idFicticiaBitacora, Double importe) {
    setConsecutivo(consecutivo);
    setJustificacion(justificacion);
    setIdFicticiaEstatus(idFicticiaEstatus);
    setIdUsuario(idUsuario);
    setIdFicticia(idFicticia);
    setIdFicticiaBitacora(idFicticiaBitacora);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdFicticiaEstatus(Long idFicticiaEstatus) {
    this.idFicticiaEstatus = idFicticiaEstatus;
  }

  public Long getIdFicticiaEstatus() {
    return idFicticiaEstatus;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdFicticia(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdFicticia() {
    return idFicticia;
  }

  public void setIdVenta(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdVenta() {
    return idFicticia;
  }

  public void setIdFicticiaBitacora(Long idFicticiaBitacora) {
    this.idFicticiaBitacora = idFicticiaBitacora;
  }

  public Long getIdFicticiaBitacora() {
    return idFicticiaBitacora;
  }

  public void setIdVentaBitacora(Long idFicticiaBitacora) {
    this.idFicticiaBitacora = idFicticiaBitacora;
  }

  public Long getIdVentaBitacora() {
    return idFicticiaBitacora;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdFicticiaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idFicticiaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticiaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticiaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idFicticiaEstatus", getIdFicticiaEstatus());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idFicticia", getIdFicticia());
		regresar.put("idFicticiaBitacora", getIdFicticiaBitacora());
		regresar.put("importe", getImporte());
		regresar.put("idVenta", getIdVenta());
		regresar.put("idVentaBitacora", getIdVentaBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getJustificacion(), getIdFicticiaEstatus(), getIdUsuario(), getIdFicticia(), getIdFicticiaBitacora(), getImporte(), getRegistro()
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
    regresar.append("idFicticiaBitacora~");
    regresar.append(getIdFicticiaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFicticiaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFicticiasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFicticiaBitacora()!= null && getIdFicticiaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFicticiasBitacoraDto other = (TcManticFicticiasBitacoraDto) obj;
    if (getIdFicticiaBitacora() != other.idFicticiaBitacora && (getIdFicticiaBitacora() == null || !getIdFicticiaBitacora().equals(other.idFicticiaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFicticiaBitacora() != null ? getIdFicticiaBitacora().hashCode() : 0);
    return hash;
  }

}


