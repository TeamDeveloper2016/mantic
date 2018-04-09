package mx.org.kaana.kajool.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;


public class TareaServidor implements IBaseDto, Serializable {
	
	private static final long serialVersionUID=-7097874018818293624L;
	
	private Long idTareaServidor;
	private String path;	
	private String expresion;
	
	public TareaServidor () {
	  this ("","");
	}
	
	public TareaServidor (String path,String expresion) {
	  this.path     = path;		
		this.expresion= expresion;
	}	

	public Long getIdTareaServidor() {
		return idTareaServidor;
	}

	public void setIdTareaServidor(Long idTareaServidor) {
		this.idTareaServidor= idTareaServidor;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path= path;
	}

	public DetalleConfiguracion toDetalleConfiguracion() {
		DetalleConfiguracion regresar= null;		
		try {
      regresar = new DetalleConfiguracion();
      regresar.setTotalHilos(5L);
      regresar.setEntidades("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49");
		} //
		catch (Exception e) {
		  Error.mensaje(e);
		}// catch		
		return regresar;
	}
	
	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion=expresion;
	}

	@Override
	public Long getKey() {
		return getIdTareaServidor();
	}

	@Override
	public void setKey(Long key) {
		this.idTareaServidor= key;
	}

	@Override
	public Map toMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isValid() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object toValue(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toAllKeys() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toKeys() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Class toHbmClass() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
