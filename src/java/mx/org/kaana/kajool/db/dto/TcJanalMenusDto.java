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
@Table(name="tc_janal_menus")
public class TcJanalMenusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="CLAVE")
  private String clave;
  @Column (name="NIVEL")
  private Long nivel;
  @Column (name="PUBLICAR")
  private String publicar;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="RUTA")
  private String ruta;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="ID_CFG_CLAVE")
  private Long idCfgClave;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMenu_sequence")
  //@SequenceGenerator(name="idMenu_sequence",sequenceName="SEQ_TC_JANAL_MENUS" , allocationSize=1 )
  @Column (name="ID_MENU")
  private Long idMenu;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="AYUDA")
  private String ayuda;
  @Column (name="ICONO")
  private String icono;
  @Column (name="ULTIMO")
  private Long ultimo;

  public TcJanalMenusDto() {
    this(new Long(-1L));
  }

  public TcJanalMenusDto(Long key) {
    this(null, null, null, null, null, null, null, null,  null, null, null);
    setKey(key);
  }

  public TcJanalMenusDto( String clave, Long nivel, String descripcion, String publicar, Long idCfgClave, String ruta,  Long idMenu, String ayuda, Long ultimo,   String icono, Long idUsuario) {
    setClave(clave);
    setNivel(nivel);
    setDescripcion(descripcion);
    setPublicar(publicar);
		setIdCfgClave(idCfgClave);
    setRuta(ruta);
    setIdMenu(idMenu);
    setAyuda(ayuda);
    setUltimo(ultimo);
		setIcono(icono);
		setIdUsuario(idUsuario);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
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

  public void setPublicar(String publicar) {
    this.publicar = publicar;
  }

  public String getPublicar() {
    return publicar;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
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

  public void setIdCfgClave(Long idCfgClave) {
    this.idCfgClave = idCfgClave;
  }

  public Long getIdCfgClave() {
    return idCfgClave;
  }

  public void setIdMenu(Long idMenu) {
    this.idMenu = idMenu;
  }

  public Long getIdMenu() {
    return idMenu;
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

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public String getIcono() {
    return icono;
  }

  public void setUltimo(Long ultimo) {
    this.ultimo = ultimo;
  }

  public Long getUltimo() {
    return ultimo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMenu();
  }

  @Override
  public void setKey(Long key) {
  	this.idMenu = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPublicar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCfgClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenu());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAyuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIcono());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUltimo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("nivel", getNivel());
		regresar.put("publicar", getPublicar());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("ruta", getRuta());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idCfgClave", getIdCfgClave());
		regresar.put("idMenu", getIdMenu());
		regresar.put("registro", getRegistro());
		regresar.put("ayuda", getAyuda());
		regresar.put("icono", getIcono());
		regresar.put("ultimo", getUltimo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getClave(), getNivel(), getPublicar(), getIdUsuario(), getRuta(), getDescripcion(), getIdCfgClave(), getIdMenu(), getRegistro(), getAyuda(), getIcono(), getUltimo()
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
    regresar.append("idMenu~");
    regresar.append(getIdMenu());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMenu());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalMenusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMenu()!= null && getIdMenu()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalMenusDto other = (TcJanalMenusDto) obj;
    if (getIdMenu() != other.idMenu && (getIdMenu() == null || !getIdMenu().equals(other.idMenu))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenu() != null ? getIdMenu().hashCode() : 0);
    return hash;
  }

}


