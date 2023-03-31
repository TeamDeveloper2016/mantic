package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_expedientes")
public class TcKalanExpedientesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="archivo")
  private String archivo;
  @Column (name="ruta")
  private String ruta;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_expediente")
  private Long idExpediente;
  @Column (name="id_cita")
  private Long idCita;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="id_expediente_estatus")
  private Long idExpedienteEstatus;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="alias")
  private String alias;

  public TcKalanExpedientesDto() {
    this(new Long(-1L));
  }

  public TcKalanExpedientesDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcKalanExpedientesDto(Long idCliente, String archivo, String ruta, Long idExpediente, Long idCita, String nombre, Long tamanio, Long idUsuario, Long idTipoArchivo, Long idExpedienteEstatus, String observaciones, String alias) {
    setIdCliente(idCliente);
    setArchivo(archivo);
    setRuta(ruta);
    setIdExpediente(idExpediente);
    setIdCita(idCita);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setIdExpedienteEstatus(idExpedienteEstatus);
    setObservaciones(observaciones);
    setAlias(alias);
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setIdExpediente(Long idExpediente) {
    this.idExpediente = idExpediente;
  }

  public Long getIdExpediente() {
    return idExpediente;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
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

  public void setIdExpedienteEstatus(Long idExpedienteEstatus) {
    this.idExpedienteEstatus = idExpedienteEstatus;
  }

  public Long getIdExpedienteEstatus() {
    return idExpedienteEstatus;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdExpediente();
  }

  @Override
  public void setKey(Long key) {
  	this.idExpediente = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdExpediente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdExpedienteEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCliente", getIdCliente());
		regresar.put("archivo", getArchivo());
		regresar.put("ruta", getRuta());
		regresar.put("idExpediente", getIdExpediente());
		regresar.put("idCita", getIdCita());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idExpedienteEstatus", getIdExpedienteEstatus());
		regresar.put("observaciones", getObservaciones());
		regresar.put("alias", getAlias());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCliente(), getArchivo(), getRuta(), getIdExpediente(), getIdCita(), getNombre(), getRegistro(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getIdExpedienteEstatus(), getObservaciones(), getAlias()
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
    regresar.append("idExpediente~");
    regresar.append(getIdExpediente());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdExpediente());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanExpedientesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdExpediente()!= null && getIdExpediente()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanExpedientesDto other = (TcKalanExpedientesDto) obj;
    if (getIdExpediente() != other.idExpediente && (getIdExpediente() == null || !getIdExpediente().equals(other.idExpediente))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdExpediente() != null ? getIdExpediente().hashCode() : 0);
    return hash;
  }

}


