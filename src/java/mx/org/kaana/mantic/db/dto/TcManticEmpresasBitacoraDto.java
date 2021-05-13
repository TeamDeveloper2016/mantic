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
@Table(name="tc_mantic_empresas_bitacora")
public class TcManticEmpresasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_empresa_estatus")
  private Long idEmpresaEstatus;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_empresa_deuda")
  private Long idEmpresaDeuda;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa_bitacora")
  private Long idEmpresaBitacora;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEmpresasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticEmpresasBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticEmpresasBitacoraDto(String justificacion, Long idEmpresaEstatus, Long idUsuario, Long idEmpresaDeuda, Long idEmpresaBitacora) {
    setJustificacion(justificacion);
    setIdEmpresaEstatus(idEmpresaEstatus);
    setIdUsuario(idUsuario);
    setIdEmpresaDeuda(idEmpresaDeuda);
    setIdEmpresaBitacora(idEmpresaBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdEmpresaEstatus(Long idEmpresaEstatus) {
    this.idEmpresaEstatus = idEmpresaEstatus;
  }

  public Long getIdEmpresaEstatus() {
    return idEmpresaEstatus;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEmpresaDeuda(Long idEmpresaDeuda) {
    this.idEmpresaDeuda = idEmpresaDeuda;
  }

  public Long getIdEmpresaDeuda() {
    return idEmpresaDeuda;
  }

  public void setIdEmpresaBitacora(Long idEmpresaBitacora) {
    this.idEmpresaBitacora = idEmpresaBitacora;
  }

  public Long getIdEmpresaBitacora() {
    return idEmpresaBitacora;
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
  	return getIdEmpresaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaDeuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idEmpresaEstatus", getIdEmpresaEstatus());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpresaDeuda", getIdEmpresaDeuda());
		regresar.put("idEmpresaBitacora", getIdEmpresaBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdEmpresaEstatus(), getIdUsuario(), getIdEmpresaDeuda(), getIdEmpresaBitacora(), getRegistro()
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
    regresar.append("idEmpresaBitacora~");
    regresar.append(getIdEmpresaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEmpresasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresaBitacora()!= null && getIdEmpresaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEmpresasBitacoraDto other = (TcManticEmpresasBitacoraDto) obj;
    if (getIdEmpresaBitacora() != other.idEmpresaBitacora && (getIdEmpresaBitacora() == null || !getIdEmpresaBitacora().equals(other.idEmpresaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresaBitacora() != null ? getIdEmpresaBitacora().hashCode() : 0);
    return hash;
  }

}


