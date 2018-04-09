package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
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

@Entity
@Table(name="tc_janal_campos")
public class TcJanalCamposDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="NOMBRE")
  private String nombre;
  @Column (name="MOSTRAR")
  private Long mostrar;
  @Column (name="TIPO")
  private String tipo;
  @Column (name="ID_CATALOGO")
  private Long idCatalogo;
  @Column (name="VER_MAS")
  private Long verMas;
  @Column (name="FORMATO")
  private String formato;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="MASCARA")
  private String mascara;
  @Column (name="ORDEN")
  private Long orden;
  @Column (name="SELECCIONAR")
  private Long seleccionar;
  @Column (name="MODIFICAR")
  private Long modificar;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idCampo_sequence")
  //@SequenceGenerator(name="idCampo_sequence",sequenceName="SEQ_TC_JANAL_CAMPOS" , allocationSize=1 )
  @Column (name="ID_CAMPO")
  private Long idCampo;
  @Column (name="VALIDACION")
  private String validacion;
  @Column (name="DEPENDENCIA")
  private Long dependencia;
  @Column (name="DUPLICADOS")
  private String duplicados;
  @Column (name="ERROR")
  private String error;
  @Column (name="CAPTURAR")
  private Long capturar;
  @Column (name="BUSCAR")
  private Long buscar;
  @Column (name="ENCRIPTADO")
  private Long encriptado;
  @Column (name="LONGITUD")
  private Long longitud;
  @Column (name="FORANEO")
  private String foraneo;

  public TcJanalCamposDto() {
    this(new Long(-1L));
  }

  public TcJanalCamposDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalCamposDto(String nombre, Long mostrar, String tipo, Long idCatalogo, Long verMas, String formato, String descripcion, String mascara, Long orden, Long seleccionar, Long modificar, Long idCampo, String validacion, Long dependencia, String duplicados, String error, Long capturar, Long buscar, Long encriptado, Long longitud, String foraneo) {
    setNombre(nombre);
    setMostrar(mostrar);
    setTipo(tipo);
    setIdCatalogo(idCatalogo);
    setVerMas(verMas);
    setFormato(formato);
    setDescripcion(descripcion);
    setMascara(mascara);
    setOrden(orden);
    setSeleccionar(seleccionar);
    setModificar(modificar);
    setIdCampo(idCampo);
    setValidacion(validacion);
    setDependencia(dependencia);
    setDuplicados(duplicados);
    setError(error);
    setCapturar(capturar);
    setBuscar(buscar);
    setEncriptado(encriptado);
    setLongitud(longitud);
    setForaneo(foraneo);
  }
	
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setMostrar(Long mostrar) {
    this.mostrar = mostrar;
  }

  public Long getMostrar() {
    return mostrar;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setIdCatalogo(Long idCatalogo) {
    this.idCatalogo = idCatalogo;
  }

  public Long getIdCatalogo() {
    return idCatalogo;
  }

  public void setVerMas(Long verMas) {
    this.verMas = verMas;
  }

  public Long getVerMas() {
    return verMas;
  }

  public void setFormato(String formato) {
    this.formato = formato;
  }

  public String getFormato() {
    return formato;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setMascara(String mascara) {
    this.mascara = mascara;
  }

  public String getMascara() {
    return mascara;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setSeleccionar(Long seleccionar) {
    this.seleccionar = seleccionar;
  }

  public Long getSeleccionar() {
    return seleccionar;
  }

  public void setModificar(Long modificar) {
    this.modificar = modificar;
  }

  public Long getModificar() {
    return modificar;
  }

  public void setIdCampo(Long idCampo) {
    this.idCampo = idCampo;
  }

  public Long getIdCampo() {
    return idCampo;
  }

  public void setValidacion(String validacion) {
    this.validacion = validacion;
  }

  public String getValidacion() {
    return validacion;
  }

  public void setDependencia(Long dependencia) {
    this.dependencia = dependencia;
  }

  public Long getDependencia() {
    return dependencia;
  }

  public void setDuplicados(String duplicados) {
    this.duplicados = duplicados;
  }

  public String getDuplicados() {
    return duplicados;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setCapturar(Long capturar) {
    this.capturar = capturar;
  }

  public Long getCapturar() {
    return capturar;
  }

  public void setBuscar(Long buscar) {
    this.buscar = buscar;
  }

  public Long getBuscar() {
    return buscar;
  }

  public void setEncriptado(Long encriptado) {
    this.encriptado = encriptado;
  }

  public Long getEncriptado() {
    return encriptado;
  }

  public void setLongitud(Long longitud) {
    this.longitud = longitud;
  }

  public Long getLongitud() {
    return longitud;
  }

  public void setForaneo(String foraneo) {
    this.foraneo = foraneo;
  }

  public String getForaneo() {
    return foraneo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdCampo();
  }

  @Override
  public void setKey(Long key) {
  	this.idCampo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMostrar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCatalogo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVerMas());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFormato());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMascara());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSeleccionar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getModificar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCampo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getValidacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDependencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDuplicados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getError());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCapturar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getBuscar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEncriptado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLongitud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getForaneo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("nombre", getNombre());
		regresar.put("mostrar", getMostrar());
		regresar.put("tipo", getTipo());
		regresar.put("idCatalogo", getIdCatalogo());
		regresar.put("verMas", getVerMas());
		regresar.put("formato", getFormato());
		regresar.put("descripcion", getDescripcion());
		regresar.put("mascara", getMascara());
		regresar.put("orden", getOrden());
		regresar.put("seleccionar", getSeleccionar());
		regresar.put("modificar", getModificar());
		regresar.put("idCampo", getIdCampo());
		regresar.put("validacion", getValidacion());
		regresar.put("dependencia", getDependencia());
		regresar.put("duplicados", getDuplicados());
		regresar.put("error", getError());
		regresar.put("capturar", getCapturar());
		regresar.put("buscar", getBuscar());
		regresar.put("encriptado", getEncriptado());
		regresar.put("longitud", getLongitud());
		regresar.put("foraneo", getForaneo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getNombre(), getMostrar(), getTipo(), getIdCatalogo(), getVerMas(), getFormato(), getDescripcion(), getMascara(), getOrden(), getSeleccionar(), getModificar(), getIdCampo(), getValidacion(), getDependencia(), getDuplicados(), getError(), getCapturar(), getBuscar(), getEncriptado(), getLongitud(), getForaneo()
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
    regresar.append("idCampo~");
    regresar.append(getIdCampo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCampo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalCamposDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCampo()!= null && getIdCampo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalCamposDto other = (TcJanalCamposDto) obj;
    if (getIdCampo() != other.idCampo && (getIdCampo() == null || !getIdCampo().equals(other.idCampo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCampo() != null ? getIdCampo().hashCode() : 0);
    return hash;
  }

}


