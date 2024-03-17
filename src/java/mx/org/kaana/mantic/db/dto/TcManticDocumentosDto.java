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
@Table(name="tc_mantic_documentos")
public class TcManticDocumentosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_documento")
  private Long idDocumento;
  @Column (name="fecha")
  private String fecha;
  @Column (name="total")
  private String total;
  @Column (name="tipo")
  private String tipo;
  @Column (name="receptor")
  private String receptor;
  @Column (name="semilla")
  private String semilla;
  @Column (name="archivo")
  private String archivo;
  @Column (name="folio")
  private String folio;
  @Column (name="emisor")
  private String emisor;
  @Column (name="uuid")
  private String uuid;
  @Column (name="rfc")
  private String rfc;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticDocumentosDto() {
    this(new Long(-1L));
  }

  public TcManticDocumentosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticDocumentosDto(Long idDocumento, String fecha, String total, String tipo, String receptor, String semilla, String archivo, String folio, String emisor, String uuid, String rfc) {
    setIdDocumento(idDocumento);
    setFecha(fecha);
    setTotal(total);
    setTipo(tipo);
    setReceptor(receptor);
    setSemilla(semilla);
    setArchivo(archivo);
    setFolio(folio);
    setEmisor(emisor);
    setUuid(uuid);
    setRfc(rfc);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdDocumento(Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  public Long getIdDocumento() {
    return idDocumento;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getTotal() {
    return total;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setReceptor(String receptor) {
    this.receptor = receptor;
  }

  public String getReceptor() {
    return receptor;
  }

  public void setSemilla(String semilla) {
    this.semilla = semilla;
  }

  public String getSemilla() {
    return semilla;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getFolio() {
    return folio;
  }

  public void setEmisor(String emisor) {
    this.emisor = emisor;
  }

  public String getEmisor() {
    return emisor;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public String getRfc() {
    return rfc;
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
  	return getIdDocumento();
  }

  @Override
  public void setKey(Long key) {
  	this.idDocumento = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDocumento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReceptor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSemilla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEmisor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUuid());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRfc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDocumento", getIdDocumento());
		regresar.put("fecha", getFecha());
		regresar.put("total", getTotal());
		regresar.put("tipo", getTipo());
		regresar.put("receptor", getReceptor());
		regresar.put("semilla", getSemilla());
		regresar.put("archivo", getArchivo());
		regresar.put("folio", getFolio());
		regresar.put("emisor", getEmisor());
		regresar.put("uuid", getUuid());
		regresar.put("rfc", getRfc());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdDocumento(), getFecha(), getTotal(), getTipo(), getReceptor(), getSemilla(), getArchivo(), getFolio(), getEmisor(), getUuid(), getRfc(), getRegistro()
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
    regresar.append("idDocumento~");
    regresar.append(getIdDocumento());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDocumento());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDocumentosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDocumento()!= null && getIdDocumento()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDocumentosDto other = (TcManticDocumentosDto) obj;
    if (getIdDocumento() != other.idDocumento && (getIdDocumento() == null || !getIdDocumento().equals(other.idDocumento))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDocumento() != null ? getIdDocumento().hashCode() : 0);
    return hash;
  }

}


