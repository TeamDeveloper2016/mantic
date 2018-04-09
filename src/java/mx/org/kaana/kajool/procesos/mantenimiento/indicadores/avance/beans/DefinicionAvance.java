package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 28, 2014
 *@time 11:00:34 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DefinicionAvance implements Serializable {

  private static final long serialVersionUID=705921068189211347L;

  private Long idSepDefinicionAvance;
	private Long idBitacoraAvance;
  private String tabla;
  private String campo;
  private String indicador;
  private String descripcion;
  private String estatusAplican;
	private String corte;
  private String registroFin;

  public DefinicionAvance(Long idSepDefinicionAvance, Long idBitacoraAvance, String tabla, String campo, String indicador, String descripcion, String estatusAplican, String corte, String registroFin) {
    this.idSepDefinicionAvance=idSepDefinicionAvance;
		this.idBitacoraAvance= idBitacoraAvance;
    this.tabla=tabla;
    this.campo=campo;
    this.indicador=indicador;
    this.descripcion=descripcion;
    this.estatusAplican=estatusAplican;
		this.corte= corte;
    this.registroFin=registroFin;
  }

  public Long getIdSepDefinicionAvance() {
    return idSepDefinicionAvance;
  }
	
	public Long getIdBitacoraAvance(){
		return this.idBitacoraAvance;
	}

  public String getTabla() {
    return tabla;
  }

  public String getCampo() {
    return campo;
  }

  public String getIndicador() {
    return indicador;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getEstatusAplican() {
    return estatusAplican;
  }

	public String getCorte() {
		return corte;
	}

  public String getRegistroFin() {
    return registroFin;
  }


}
