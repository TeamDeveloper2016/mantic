package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 11/07/2018
 * @time 12:12:28 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Faltante extends TcManticFaltantesDto implements Serializable {

	private static final long serialVersionUID=-8507433804239996836L;
  private String codigo;
  private String nombre;

	public Faltante(Long idUsuario, Long idFaltante, String observaciones, Double cantidad, Long idVigente, Long idArticulo) {
		super(idUsuario, idFaltante, observaciones, cantidad, idVigente, idArticulo);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticFaltantesDto.class;
	}

	@Override
	public String toString() {
		return "Faltante{codigo="+ this.codigo+ ", nombre="+ this.nombre+ ", "+ super.toString()+ '}';
	}
	
}
