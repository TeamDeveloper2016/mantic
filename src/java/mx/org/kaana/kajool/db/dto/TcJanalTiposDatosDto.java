package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

@Entity
@Table(name="tc_janal_tipos_datos")
public class TcJanalTiposDatosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="CADENA")
  private String cadena;
  @Column (name="HORA")
  private Timestamp hora;
  @Column (name="RELACION_BUSQUEDA")
  private Long relacionBusqueda;
  @Column (name="FECHA")
  private Date fecha;
  @Column (name="ENTERO")
  private Long entero;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idTipoDato_sequence")
  //@SequenceGenerator(name="idTipoDato_sequence",sequenceName="SEQ_TC_JANAL_tipos_datos" , allocationSize=1 )
  @Column (name="ID_TIPO_DATO")
  private Long idTipoDato;
  @Column (name="FLOTANTE")
  private Double flotante;
  @Column (name="TEXTO")
  private String texto;
  @Lob
  @Column (name="ARCHIVO")
  private Blob archivo;
  @Column (name="DTYPE")
  private String dtype;
  @Column (name="E_DATO")
  private String edato;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="RELACION")
  private Long relacion;

  public TcJanalTiposDatosDto() {
    this(new Long(-1L));
  }

  public TcJanalTiposDatosDto(Long key) {
    this(null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalTiposDatosDto(String cadena, Timestamp hora, Long relacionBusqueda, Date fecha, Long entero, Long idTipoDato, Double flotante, String texto, Blob archivo, String dtype, String edato, Long relacion) {
    setCadena(cadena);
    setHora(hora);
    setRelacionBusqueda(relacionBusqueda);
    setFecha(fecha);
    setEntero(entero);
    setIdTipoDato(idTipoDato);
    setFlotante(flotante);
    setTexto(texto);
    setArchivo(archivo);
    setDtype(dtype);
    setEdato(edato);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setRelacion(relacion);
  }
	
  public void setCadena(String cadena) {
    this.cadena = cadena;
  }

  public String getCadena() {
    return cadena;
  }

  public void setHora(Timestamp hora) {
    this.hora = hora;
  }

  public Timestamp getHora() {
    return hora;
  }

  public void setRelacionBusqueda(Long relacionBusqueda) {
    this.relacionBusqueda = relacionBusqueda;
  }

  public Long getRelacionBusqueda() {
    return relacionBusqueda;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setEntero(Long entero) {
    this.entero = entero;
  }

  public Long getEntero() {
    return entero;
  }

  public void setIdTipoDato(Long idTipoDato) {
    this.idTipoDato = idTipoDato;
  }

  public Long getIdTipoDato() {
    return idTipoDato;
  }

  public void setFlotante(Double flotante) {
    this.flotante = flotante;
  }

  public Double getFlotante() {
    return flotante;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }

  public String getTexto() {
    return texto;
  }

  public void setArchivo(Blob archivo) {
    this.archivo = archivo;
  }

  public Blob getArchivo() {
    return archivo;
  }

  public void setDtype(String dtype) {
    this.dtype = dtype;
  }

  public String getDtype() {
    return dtype;
  }

  public void setEdato(String edato) {
    this.edato = edato;
  }

  public String getEdato() {
    return edato;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRelacion(Long relacion) {
    this.relacion = relacion;
  }

  public Long getRelacion() {
    return relacion;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdTipoDato();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoDato = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCadena());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRelacionBusqueda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntero());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDato());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFlotante());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTexto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDtype());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEdato());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRelacion());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("cadena", getCadena());
		regresar.put("hora", getHora());
		regresar.put("relacionBusqueda", getRelacionBusqueda());
		regresar.put("fecha", getFecha());
		regresar.put("entero", getEntero());
		regresar.put("idTipoDato", getIdTipoDato());
		regresar.put("flotante", getFlotante());
		regresar.put("texto", getTexto());
		regresar.put("archivo", getArchivo());
		regresar.put("dtype", getDtype());
		regresar.put("edato", getEdato());
		regresar.put("registro", getRegistro());
		regresar.put("relacion", getRelacion());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCadena(), getHora(), getRelacionBusqueda(), getFecha(), getEntero(), getIdTipoDato(), getFlotante(), getTexto(), getArchivo(), getDtype(), getEdato(), getRegistro(), getRelacion()
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
    regresar.append("idTipoDato~");
    regresar.append(getIdTipoDato());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoDato());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalTiposDatosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoDato()!= null && getIdTipoDato()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalTiposDatosDto other = (TcJanalTiposDatosDto) obj;
    if (getIdTipoDato() != other.idTipoDato && (getIdTipoDato() == null || !getIdTipoDato().equals(other.idTipoDato))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoDato() != null ? getIdTipoDato().hashCode() : 0);
    return hash;
  }

}


