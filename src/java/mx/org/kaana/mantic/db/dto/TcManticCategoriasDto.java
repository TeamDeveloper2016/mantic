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
@Table(name="tc_mantic_categorias")
public class TcManticCategoriasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="clave")
  private String clave;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_categoria")
  private Long idCategoria;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="nivel")
  private Long nivel;
  @Column (name="nombre")
  private String nombre;
  @Column (name="traza")
  private String traza;
  @Column (name="id_cfg_claves")
  private Long idCfgClaves;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCategoriasDto() {
    this(new Long(-1L));
  }

  public TcManticCategoriasDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticCategoriasDto(String descripcion, String clave, Long idUsuario, Long idCategoria, Long idEmpresa, Long nivel, String nombre, String traza, Long idCfgClaves) {
    setDescripcion(descripcion);
    setClave(clave);
    setIdUsuario(idUsuario);
    setIdCategoria(idCategoria);
    setIdEmpresa(idEmpresa);
    setNivel(nivel);
    setNombre(nombre);
    setTraza(traza);
    setIdCfgClaves(idCfgClaves);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
  }

  public Long getIdCategoria() {
    return idCategoria;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setNivel(Long nivel) {
    this.nivel = nivel;
  }

  public Long getNivel() {
    return nivel;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setTraza(String traza) {
    this.traza = traza;
  }

  public String getTraza() {
    return traza;
  }

  public void setIdCfgClaves(Long idCfgClaves) {
    this.idCfgClaves = idCfgClaves;
  }

  public Long getIdCfgClaves() {
    return idCfgClaves;
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
  	return getIdCategoria();
  }

  @Override
  public void setKey(Long key) {
  	this.idCategoria = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCategoria());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTraza());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCfgClaves());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("clave", getClave());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCategoria", getIdCategoria());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("nivel", getNivel());
		regresar.put("nombre", getNombre());
		regresar.put("traza", getTraza());
		regresar.put("idCfgClaves", getIdCfgClaves());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getClave(), getIdUsuario(), getIdCategoria(), getIdEmpresa(), getNivel(), getNombre(), getTraza(), getIdCfgClaves(), getRegistro()
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
    regresar.append("idCategoria~");
    regresar.append(getIdCategoria());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCategoria());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCategoriasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCategoria()!= null && getIdCategoria()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCategoriasDto other = (TcManticCategoriasDto) obj;
    if (getIdCategoria() != other.idCategoria && (getIdCategoria() == null || !getIdCategoria().equals(other.idCategoria))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCategoria() != null ? getIdCategoria().hashCode() : 0);
    return hash;
  }

}


