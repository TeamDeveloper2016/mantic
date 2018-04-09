package mx.org.kaana.kajool.servicios.ws.publicar.beans;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 23/11/2016
 *@time 04:25:27 PM 
 *@author Usuario <usuario.usuario@inegi.org.mx>
 */

public class VisitasMovil implements Serializable {
	
	private List<TrJanalVisitasDto> visitas;

	public List<TrJanalVisitasDto> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<TrJanalVisitasDto> visitas) {
		this.visitas=visitas;
	}

  @Override
  public String toString() {
    return "VisitasMovil{" + "visitas=" + visitas + '}';
  }

}
