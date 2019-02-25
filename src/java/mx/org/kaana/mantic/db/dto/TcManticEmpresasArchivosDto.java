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
@Table(name="tc_mantic_empresas_archivos")
public class TcManticEmpresasArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ruta")
  private String ruta;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa_archivo")
  private Long idEmpresaArchivo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="archivo")
  private String archivo;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
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
  @Column (name="id_empresa_pago")
  private Long idEmpresaPago;
  @Column (name="alias")
  private String alias;
  @Column (name="mes")
  private Long mes;

  public TcManticEmpresasArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticEmpresasArchivosDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticEmpresasArchivosDto(String ruta, Long idEmpresaArchivo, String nombre, Long ejercicio, Long tamanio, Long idUsuario, Long idTipoArchivo, Long idPrincipal, String observaciones, Long idEmpresaPago, String alias, Long mes, String archivo) {
    setRuta(ruta);
    setIdEmpresaArchivo(idEmpresaArchivo);
    setNombre(nombre);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setIdEmpresaPago(idEmpresaPago);
    setAlias(alias);
    setMes(mes);
		this.archivo= archivo;
  }
	
  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setIdEmpresaArchivo(Long idEmpresaArchivo) {
    this.idEmpresaArchivo = idEmpresaArchivo;
  }

  public Long getIdEmpresaArchivo() {
    return idEmpresaArchivo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo=archivo;
	}

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
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

  public void setIdEmpresaPago(Long idEmpresaPago) {
    this.idEmpresaPago = idEmpresaPago;
  }

  public Long getIdEmpresaPago() {
    return idEmpresaPago;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdEmpresaArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresaArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
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
		regresar.append(getIdEmpresaPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMes());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("ruta", getRuta());
		regresar.put("idEmpresaArchivo", getIdEmpresaArchivo());
		regresar.put("nombre", getNombre());
		regresar.put("archivo", getArchivo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresaPago", getIdEmpresaPago());
		regresar.put("alias", getAlias());
		regresar.put("mes", getMes());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getRuta(), getIdEmpresaArchivo(), getNombre(), getArchivo(), getEjercicio(), getRegistro(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getIdPrincipal(), getObservaciones(), getIdEmpresaPago(), getAlias(), getMes()
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
    regresar.append("idEmpresaArchivo~");
    regresar.append(getIdEmpresaArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresaArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEmpresasArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresaArchivo()!= null && getIdEmpresaArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEmpresasArchivosDto other = (TcManticEmpresasArchivosDto) obj;
    if (getIdEmpresaArchivo() != other.idEmpresaArchivo && (getIdEmpresaArchivo() == null || !getIdEmpresaArchivo().equals(other.idEmpresaArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresaArchivo() != null ? getIdEmpresaArchivo().hashCode() : 0);
    return hash;
  }

}


