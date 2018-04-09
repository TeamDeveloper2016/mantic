package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 01/09/2015
 * @time 02:10:42 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class CentroTrabajo implements Serializable, IBaseDto {

  private static final long serialVersionUID = -6551230589979107218L;

	private String clave;
	private String nombres;
  private Long idEscuela;
  private Long idServicioEducativo;

  public CentroTrabajo() {
    this("","",-1L,-1L);
  }

  public CentroTrabajo(String clave, String nombres, Long idEscuela, Long idServicioEducativo) {
    this.clave = clave;
    this.nombres = nombres;
    this.idEscuela = idEscuela;
    this.idServicioEducativo = idServicioEducativo;
  }

  public Long getIdServicioEducativo() {
    return idServicioEducativo;
  }

  public void setIdServicioEducativo(Long idServicioEducativo) {
    this.idServicioEducativo = idServicioEducativo;
  }

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public Long getIdEscuela() {
    return idEscuela;
  }

  public void setIdEscuela(Long idEscuela) {
    this.idEscuela = idEscuela;
  }

  @Override
  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("[");
    regresar.append(clave);
    regresar.append(",");
    regresar.append(nombres);
    regresar.append(",");
    regresar.append(idEscuela);
    regresar.append(",");
    regresar.append(idServicioEducativo);
    regresar.append("]");
    return regresar.toString();
  }

  @Override
  public Long getKey() {
    return idEscuela;
  }

  @Override
  public void setKey(Long key) {
    idEscuela=key;
  }

  @Override
  public Map<String, Object> toMap() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isValid() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object toValue(String name) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toAllKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Class toHbmClass() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


}
