package mx.org.kaana.kalan.catalogos.pacientes.expedientes.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/04/2023
 *@time 07:31:05 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Expediente extends Importado implements Serializable {

  private static final long serialVersionUID = 6785152086280149585L;

  private Long idComodin;
  private String comodin;
  private Long idEstatus;
  private Long idCita;
  private UISelectEntity ikCita;

  public Expediente() {
    this("", "", EFormatos.FREE, 0L, 0L, "", "", "", "", 13L);
  }

  public Expediente(String name, String content, EFormatos format, Long size, Long fileSize, String medicion, String ruta, String observaciones, String original, Long idTipoDocumento) {
    super(name, content, format, size, fileSize, medicion, ruta, observaciones, original, idTipoDocumento);
    this.idComodin= -1L;
    this.comodin  = "";
    this.idEstatus= 1L;
    this.ikCita   = new UISelectEntity(-1L);
  }  
  
  public Long getIdComodin() {
    return idComodin;
  }

  public void setIdComodin(Long idComodin) {
    this.idComodin = idComodin;
  }

  public String getComodin() {
    return comodin;
  }

  public void setComodin(String comodin) {
    this.comodin = comodin;
  }

  public Long getIdEstatus() {
    return idEstatus;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public UISelectEntity getIkCita() {
    return ikCita;
  }

  public void setIkCita(UISelectEntity ikCita) {
    this.ikCita = ikCita;
    if(ikCita!= null)
      this.idCita= ikCita.getKey();
  }

  @Override
  public String toString() {
    return "Expediente{" + "idComodin=" + idComodin + ", comodin=" + comodin + ", idEstatus=" + idEstatus + ", idCita=" + idCita + '}';
  }
	  
}
