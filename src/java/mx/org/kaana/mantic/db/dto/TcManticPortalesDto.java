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
@Table(name="tc_mantic_portales")
public class TcManticPortalesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="marca")
  private String marca;
  @Column (name="herramienta")
  private String herramienta;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_portal")
  private Long idPortal;
  @Column (name="modelo")
  private String modelo;
  @Column (name="url")
  private String url;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticPortalesDto() {
    this(new Long(-1L));
  }

  public TcManticPortalesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticPortalesDto(Long idEmpresa, String marca, String herramienta, Long idUsuario, Long idPortal, String modelo, String url) {
    this.idEmpresa= idEmpresa;
    setMarca(marca);
    setHerramienta(herramienta);
    setIdUsuario(idUsuario);
    setIdPortal(idPortal);
    setModelo(modelo);
    setUrl(url);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }
	
  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getMarca() {
    return marca;
  }

  public void setHerramienta(String herramienta) {
    this.herramienta = herramienta;
  }

  public String getHerramienta() {
    return herramienta;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdPortal(Long idPortal) {
    this.idPortal = idPortal;
  }

  public Long getIdPortal() {
    return idPortal;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getModelo() {
    return modelo;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
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
  	return getIdPortal();
  }

  @Override
  public void setKey(Long key) {
  	this.idPortal = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMarca());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHerramienta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPortal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getModelo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUrl());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("marca", getMarca());
		regresar.put("herramienta", getHerramienta());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idPortal", getIdPortal());
		regresar.put("modelo", getModelo());
		regresar.put("url", getUrl());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdEmpresa(), getMarca(), getHerramienta(), getIdUsuario(), getIdPortal(), getModelo(), getUrl(), getRegistro()
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
    regresar.append("idPortal~");
    regresar.append(getIdPortal());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPortal());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPortalesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPortal()!= null && getIdPortal()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPortalesDto other = (TcManticPortalesDto) obj;
    if (getIdPortal() != other.idPortal && (getIdPortal() == null || !getIdPortal().equals(other.idPortal))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPortal() != null ? getIdPortal().hashCode() : 0);
    return hash;
  }

}


