package mx.org.kaana.kajool.db.dto;

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

@Entity
@Table(name="tc_janal_menus_encabezado")
public class TcJanalMenusEncabezadoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMenuEncabezado_sequence")
  //@SequenceGenerator(name="idMenuEncabezado_sequence",sequenceName="SEQ_TC_JANAL_MENUS_ENCABEZADO" , allocationSize=1 )
  @Column (name="ID_MENU_ENCABEZADO")
  private Long idMenuEncabezado;
  @Column (name="CLAVE")
  private String clave;
  @Column (name="NIVEL")
  private Long nivel;
  @Column (name="PUBLICAR")
  private Long publicar;
  @Column (name="RUTA")
  private String ruta;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="AYUDA")
  private String ayuda;
  @Column (name="ULTIMO")
  private Long ultimo;
  @Column (name="ICONO")
  private String icono;

  public TcJanalMenusEncabezadoDto() {
    this(new Long(-1L));
  }

  public TcJanalMenusEncabezadoDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalMenusEncabezadoDto(Long idMenuEncabezado, String clave, Long nivel, Long publicar, String ruta, String descripcion, String ayuda, Long ultimo, String icono) {
    setIdMenuEncabezado(idMenuEncabezado);
    setClave(clave);
    setNivel(nivel);
    setPublicar(publicar);
    setRuta(ruta);
    setDescripcion(descripcion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setAyuda(ayuda);
    setUltimo(ultimo);
    setIcono(icono);
  }
	
  public void setIdMenuEncabezado(Long idMenuEncabezado) {
    this.idMenuEncabezado = idMenuEncabezado;
  }

  public Long getIdMenuEncabezado() {
    return idMenuEncabezado;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setNivel(Long nivel) {
    this.nivel = nivel;
  }

  public Long getNivel() {
    return nivel;
  }

  public void setPublicar(Long publicar) {
    this.publicar = publicar;
  }

  public Long getPublicar() {
    return publicar;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setAyuda(String ayuda) {
    this.ayuda = ayuda;
  }

  public String getAyuda() {
    return ayuda;
  }

  public void setUltimo(Long ultimo) {
    this.ultimo = ultimo;
  }

  public Long getUltimo() {
    return ultimo;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public String getIcono() {
    return icono;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMenuEncabezado();
  }

  @Override
  public void setKey(Long key) {
  	this.idMenuEncabezado = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMenuEncabezado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPublicar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAyuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUltimo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIcono());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idMenuEncabezado", getIdMenuEncabezado());
		regresar.put("clave", getClave());
		regresar.put("nivel", getNivel());
		regresar.put("publicar", getPublicar());
		regresar.put("ruta", getRuta());
		regresar.put("descripcion", getDescripcion());
		regresar.put("registro", getRegistro());
		regresar.put("ayuda", getAyuda());
		regresar.put("ultimo", getUltimo());
		regresar.put("icono", getIcono());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMenuEncabezado(), getClave(), getNivel(), getPublicar(), getRuta(), getDescripcion(), getRegistro(), getAyuda(), getUltimo(), getIcono()
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
    regresar.append("idMenuEncabezado~");
    regresar.append(getIdMenuEncabezado());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMenuEncabezado());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalMenusEncabezadoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMenuEncabezado()!= null && getIdMenuEncabezado()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalMenusEncabezadoDto other = (TcJanalMenusEncabezadoDto) obj;
    if (getIdMenuEncabezado() != other.idMenuEncabezado && (getIdMenuEncabezado() == null || !getIdMenuEncabezado().equals(other.idMenuEncabezado))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenuEncabezado() != null ? getIdMenuEncabezado().hashCode() : 0);
    return hash;
  }

}


