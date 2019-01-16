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
@Table(name="tc_mantic_confrontas_bitacora")
public class TcManticConfrontasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_confronta")
  private Long idConfronta;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_confronta_estatus")
  private Long idConfrontaEstatus;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_confronta_bitacora")
  private Long idConfrontaBitacora;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConfrontasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticConfrontasBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticConfrontasBitacoraDto(String justificacion, Long idConfronta, Long idUsuario, Long idConfrontaEstatus, Long idConfrontaBitacora) {
    setJustificacion(justificacion);
    setIdConfronta(idConfronta);
    setIdUsuario(idUsuario);
    setIdConfrontaEstatus(idConfrontaEstatus);
    setIdConfrontaBitacora(idConfrontaBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdConfronta(Long idConfronta) {
    this.idConfronta = idConfronta;
  }

  public Long getIdConfronta() {
    return idConfronta;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdConfrontaEstatus(Long idConfrontaEstatus) {
    this.idConfrontaEstatus = idConfrontaEstatus;
  }

  public Long getIdConfrontaEstatus() {
    return idConfrontaEstatus;
  }

  public void setIdConfrontaBitacora(Long idConfrontaBitacora) {
    this.idConfrontaBitacora = idConfrontaBitacora;
  }

  public Long getIdConfrontaBitacora() {
    return idConfrontaBitacora;
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
  	return getIdConfrontaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idConfrontaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfronta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfrontaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfrontaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idConfronta", getIdConfronta());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idConfrontaEstatus", getIdConfrontaEstatus());
		regresar.put("idConfrontaBitacora", getIdConfrontaBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdConfronta(), getIdUsuario(), getIdConfrontaEstatus(), getIdConfrontaBitacora(), getRegistro()
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
    regresar.append("idConfrontaBitacora~");
    regresar.append(getIdConfrontaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConfrontaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConfrontasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConfrontaBitacora()!= null && getIdConfrontaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConfrontasBitacoraDto other = (TcManticConfrontasBitacoraDto) obj;
    if (getIdConfrontaBitacora() != other.idConfrontaBitacora && (getIdConfrontaBitacora() == null || !getIdConfrontaBitacora().equals(other.idConfrontaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConfrontaBitacora() != null ? getIdConfrontaBitacora().hashCode() : 0);
    return hash;
  }

}


