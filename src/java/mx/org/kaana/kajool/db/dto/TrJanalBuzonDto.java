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
@Table(name="tr_janal_buzon")
public class TrJanalBuzonDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_PROBLEMATICA")
  private Long idProblematica;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="ID_MODULO")
  private Long idModulo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idBuzon_sequence")
  //@SequenceGenerator(name="idBuzon_sequence",sequenceName="SEQ_TR_JANAL_BUZON" , allocationSize=1 )
  @Column (name="ID_BUZON")
  private Long idBuzon;
  @Column (name="PAGINA")
  private String pagina;
  @Column (name="REGISTRO")
  private Timestamp registro;

  public TrJanalBuzonDto() {
    this(new Long(-1L));
  }

  public TrJanalBuzonDto(Long key) {
    this(null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TrJanalBuzonDto(Long idProblematica, Long idUsuario, String descripcion, Long idModulo, Long idBuzon, String pagina) {
    setIdProblematica(idProblematica);
    setIdUsuario(idUsuario);
    setDescripcion(descripcion);
    setIdModulo(idModulo);
    setIdBuzon(idBuzon);
    setPagina(pagina);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProblematica(Long idProblematica) {
    this.idProblematica = idProblematica;
  }

  public Long getIdProblematica() {
    return idProblematica;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdModulo(Long idModulo) {
    this.idModulo = idModulo;
  }

  public Long getIdModulo() {
    return idModulo;
  }

  public void setIdBuzon(Long idBuzon) {
    this.idBuzon = idBuzon;
  }

  public Long getIdBuzon() {
    return idBuzon;
  }

  public void setPagina(String pagina) {
    this.pagina = pagina;
  }

  public String getPagina() {
    return pagina;
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
  	return getIdBuzon();
  }

  @Override
  public void setKey(Long key) {
  	this.idBuzon = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProblematica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdModulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBuzon());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPagina());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProblematica", getIdProblematica());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idModulo", getIdModulo());
		regresar.put("idBuzon", getIdBuzon());
		regresar.put("pagina", getPagina());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProblematica(), getIdUsuario(), getDescripcion(), getIdModulo(), getIdBuzon(), getPagina(), getRegistro()
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
    regresar.append("idBuzon~");
    regresar.append(getIdBuzon());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdBuzon());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalBuzonDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdBuzon()!= null && getIdBuzon()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalBuzonDto other = (TrJanalBuzonDto) obj;
    if (getIdBuzon() != other.idBuzon && (getIdBuzon() == null || !getIdBuzon().equals(other.idBuzon))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdBuzon() != null ? getIdBuzon().hashCode() : 0);
    return hash;
  }

}


