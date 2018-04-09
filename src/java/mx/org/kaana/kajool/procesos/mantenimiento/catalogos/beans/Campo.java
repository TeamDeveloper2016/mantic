package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.db.dto.TcJanalCamposDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 03:25:09 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Campo extends TcJanalCamposDto implements IBaseDto, Serializable {

	
	private String auxiliar;

	public Campo() {
		this(-1L);
	}
	
	public Campo(Long key) {
		super(key);
	}

	public Campo(String auxiliar, String nombre, Long mostrar, String tipo, Long idCatalogo, Long verMas, String formato, String descripcion, Long orden, String mascara, Long seleccionar, Long modificar, Long idCampo, String validacion, Long dependencia, String duplicados, String error, Long capturar, Long buscar, Long encriptado, Long longitud, String foraneo) {
		super(nombre, mostrar, tipo, idCatalogo, verMas, formato, descripcion, mascara, orden, seleccionar, modificar, idCampo, validacion, dependencia, duplicados, error, capturar, buscar, encriptado, longitud, foraneo);
		this.auxiliar=auxiliar;
	}
	
	public String getAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(String auxiliar) {
		this.auxiliar=auxiliar;
	}

	public ETipoDato getETipo() {
		ETipoDato regresar= ETipoDato.TEXT;
		switch(super.getTipo().trim().charAt(0)) {
			case 'V':
				regresar= ETipoDato.TEXT;
				break;
			case 'N':
				regresar= ETipoDato.LONG;
				break;
			case 'F':
				regresar= ETipoDato.DOUBLE;
				break;
			case 'B':
				regresar= ETipoDato.BLOB;
				break;
			case 'D':
				regresar= ETipoDato.DATE;
				break;
			case 'H':
				regresar= ETipoDato.TIME;
				break;
			case 'T':
				regresar= ETipoDato.TIMESTAMP;
				break;
	  } // case
		return regresar;
	}

	
	@Override
	public Map toMap() {
		Map regresar= super.toMap();
		regresar.put("auxiliar", getAuxiliar());
		return regresar;
	}
	
	@Override
	public String toString() {
		return "Campo{"+ super.toString() +", auxiliar="+auxiliar+'}';
	}

  @Override
  public Long getKey() {
    return super.getKey();
  }

  @Override
  public void setKey(Long key) {
    super.setKey(key);
  }

  @Override
  public Object[] toArray() {
    return super.toArray();
  }

  @Override
  public boolean isValid() {
    return super.isValid();
  }

  @Override
  public Object toValue(String name) {
    return super.toValue(name);
  }

  @Override
  public String toAllKeys() {
    return super.toAllKeys();
  }

  @Override
  public String toKeys() {
    return super.toKeys();
  }

  @Override
  public Class toHbmClass() {
    return super.toHbmClass();
  }
		
}
