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
@Table(name="tc_mantic_masivas_archivos")
public class TcManticMasivasArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_masiva_archivo")
  private Long idMasivaArchivo;
  @Column (name="ruta")
  private String ruta;
  @Column (name="id_tipo_masivo")
  private Long idTipoMasivo;
  @Column (name="id_masiva_estatus")
  private Long idMasivaEstatus;
  @Column (name="nombre")
  private String nombre;
  @Column (name="archivo")
  private String archivo;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="tuplas")
  private Long tuplas;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="alias")
  private String alias;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_principal")
  private Long idPrincipal;

  public TcManticMasivasArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticMasivasArchivosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, 2L, null);
    setKey(key);
  }

  public TcManticMasivasArchivosDto(Long idMasivaArchivo, String ruta, Long idTipoMasivo, Long idMasivaEstatus, String nombre, Long tamanio, Long idUsuario, Long idTipoArchivo, Long tuplas, String observaciones, String alias, Long idEmpresa, Long idPrincipal, String archivo) {
    setIdMasivaArchivo(idMasivaArchivo);
    setRuta(ruta);
    setIdTipoMasivo(idTipoMasivo);
    setIdMasivaEstatus(idMasivaEstatus);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setTuplas(tuplas);
    setObservaciones(observaciones);
    setAlias(alias);
    setIdEmpresa(idEmpresa);
		this.idPrincipal= idPrincipal;
		this.archivo= archivo;
  }
	
  public void setIdMasivaArchivo(Long idMasivaArchivo) {
    this.idMasivaArchivo = idMasivaArchivo;
  }

  public Long getIdMasivaArchivo() {
    return idMasivaArchivo;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setIdTipoMasivo(Long idTipoMasivo) {
    this.idTipoMasivo = idTipoMasivo;
  }

  public Long getIdTipoMasivo() {
    return idTipoMasivo;
  }

  public void setIdMasivaEstatus(Long idMasivaEstatus) {
    this.idMasivaEstatus = idMasivaEstatus;
  }

  public Long getIdMasivaEstatus() {
    return idMasivaEstatus;
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

  public void setTuplas(Long tuplas) {
    this.tuplas = tuplas;
  }

  public Long getTuplas() {
    return tuplas;
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

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

	public Long getIdPrincipal() {
		return idPrincipal;
	}

	public void setIdPrincipal(Long idPrincipal) {
		this.idPrincipal=idPrincipal;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdMasivaArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idMasivaArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMasivaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMasivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTuplas());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idMasivaArchivo", getIdMasivaArchivo());
		regresar.put("ruta", getRuta());
		regresar.put("idTipoMasivo", getIdTipoMasivo());
		regresar.put("idMasivaEstatus", getIdMasivaEstatus());
		regresar.put("nombre", getNombre());
		regresar.put("archivo", getArchivo());
		regresar.put("registro", getRegistro());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("tuplas", getTuplas());
		regresar.put("observaciones", getObservaciones());
		regresar.put("alias", getAlias());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idPrincipal", getIdPrincipal());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMasivaArchivo(), getRuta(), getIdTipoMasivo(), getIdMasivaEstatus(), getNombre(), getArchivo(), getRegistro(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getTuplas(), getObservaciones(), getAlias(), getIdEmpresa(), getIdPrincipal()
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
    regresar.append("idMasivaArchivo~");
    regresar.append(getIdMasivaArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMasivaArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMasivasArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMasivaArchivo()!= null && getIdMasivaArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMasivasArchivosDto other = (TcManticMasivasArchivosDto) obj;
    if (getIdMasivaArchivo() != other.idMasivaArchivo && (getIdMasivaArchivo() == null || !getIdMasivaArchivo().equals(other.idMasivaArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMasivaArchivo() != null ? getIdMasivaArchivo().hashCode() : 0);
    return hash;
  }

}


