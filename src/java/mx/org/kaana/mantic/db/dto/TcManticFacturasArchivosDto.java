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
@Table(name="tc_mantic_facturas_archivos")
public class TcManticFacturasArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="ruta")
  private String ruta;
  @Column (name="cancelacion")
  private Timestamp cancelacion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="folio_cancelacion")
  private String folioCancelacion;
  @Column (name="registro")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_factura_archivo")
  private Long idFacturaArchivo;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="alias")
  private String alias;
  @Column (name="mes")
  private Long mes;
  @Column (name="comentarios")
  private String comentarios;

  public TcManticFacturasArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticFacturasArchivosDto(Long key) {
    this(null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, new Long(-1L), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticFacturasArchivosDto(Long idFactura, String ruta, Timestamp cancelacion, String nombre, Long ejercicio, String folioCancelacion, Long idFacturaArchivo, Long tamanio, Long idUsuario, Long idTipoArchivo, Long idPrincipal, String observaciones, String alias, Long mes, String comentarios) {
    setIdFactura(idFactura);
    setRuta(ruta);
    setCancelacion(cancelacion);
    setNombre(nombre);
    setEjercicio(ejercicio);
    setFolioCancelacion(folioCancelacion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdFacturaArchivo(idFacturaArchivo);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setAlias(alias);
    setMes(mes);
    setComentarios(comentarios);
  }
	
  public void setIdFactura(Long idFactura) {
    this.idFactura = idFactura;
  }

  public Long getIdFactura() {
    return idFactura;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setCancelacion(Timestamp cancelacion) {
    this.cancelacion = cancelacion;
  }

  public Timestamp getCancelacion() {
    return cancelacion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setFolioCancelacion(String folioCancelacion) {
    this.folioCancelacion = folioCancelacion;
  }

  public String getFolioCancelacion() {
    return folioCancelacion;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdFacturaArchivo(Long idFacturaArchivo) {
    this.idFacturaArchivo = idFacturaArchivo;
  }

  public Long getIdFacturaArchivo() {
    return idFacturaArchivo;
  }

  public void setTamanio(Long tamanio) {
    this.tamanio = tamanio;
  }

  public Long getTamanio() {
    return tamanio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTipoArchivo(Long idTipoArchivo) {
    this.idTipoArchivo = idTipoArchivo;
  }

  public Long getIdTipoArchivo() {
    return idTipoArchivo;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public void setMes(Long mes) {
    this.mes = mes;
  }

  public Long getMes() {
    return mes;
  }

  public void setComentarios(String comentarios) {
    this.comentarios = comentarios;
  }

  public String getComentarios() {
    return comentarios;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdFacturaArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idFacturaArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCancelacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioCancelacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getComentarios());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idFactura", getIdFactura());
		regresar.put("ruta", getRuta());
		regresar.put("cancelacion", getCancelacion());
		regresar.put("nombre", getNombre());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("folioCancelacion", getFolioCancelacion());
		regresar.put("registro", getRegistro());
		regresar.put("idFacturaArchivo", getIdFacturaArchivo());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("alias", getAlias());
		regresar.put("mes", getMes());
		regresar.put("comentarios", getComentarios());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdFactura(), getRuta(), getCancelacion(), getNombre(), getEjercicio(), getFolioCancelacion(), getRegistro(), getIdFacturaArchivo(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getIdPrincipal(), getObservaciones(), getAlias(), getMes(), getComentarios()
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
    regresar.append("idFacturaArchivo~");
    regresar.append(getIdFacturaArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFacturaArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFacturasArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFacturaArchivo()!= null && getIdFacturaArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFacturasArchivosDto other = (TcManticFacturasArchivosDto) obj;
    if (getIdFacturaArchivo() != other.idFacturaArchivo && (getIdFacturaArchivo() == null || !getIdFacturaArchivo().equals(other.idFacturaArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFacturaArchivo() != null ? getIdFacturaArchivo().hashCode() : 0);
    return hash;
  }

}


