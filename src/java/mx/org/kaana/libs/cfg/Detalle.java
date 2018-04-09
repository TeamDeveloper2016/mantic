package mx.org.kaana.libs.cfg;

import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:55:49 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Detalle extends DetalleConfiguracion implements IBaseDto {

	private static final long serialVersionUID=5881850291998239828L;
	private Long idKey;
	private Long idCfgClave;
	private String value;
	private String descripcion;

	public Detalle() {
		super(0, 0, 0, "", "");
		this.idKey=0L;
		this.idCfgClave=0L;
		this.value="";
		this.descripcion="";
	}

	public Detalle(Integer nivel, Integer longitud, Integer justificacion, String relleno, String dominio, String value) {
		this(0L, 0L, value, "", nivel, longitud, justificacion, relleno, dominio);
	}

	public Detalle(Long idKey, Long idCfgClave, String value, String descripcion, Integer nivel, Integer longitud, Integer justificacion, String relleno, String dominio) {
		super(nivel, longitud, justificacion, relleno, dominio);
		this.idKey=idKey;
		this.idCfgClave=idCfgClave;
		this.value=value;
		this.descripcion=descripcion;
		this.value=value;
	}

	@Override
	public Long getKey() {
		return this.idKey;
	}

	@Override
	public void setKey(Long key) {
		this.idKey=key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value=value;
	}

	public Long getIdCfgClave() {
		return idCfgClave;
	}

	public void setIdCfgClave(Long idCfgClave) {
		this.idCfgClave=idCfgClave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		sb.append(getNivel());
		sb.append(",");
		sb.append(getLongitud());
		sb.append(",");
		sb.append(getIdJustificacion());
		sb.append(",");
		sb.append(getRelleno());
		sb.append(",");
		sb.append(getDominio());
		sb.append(",");
		sb.append(getValue());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append("]");
		return sb.toString();
	}

	public String toLike() {
		this.value=Cadena.rellenar("", getLongitud(), '_', toJustificacion());
		return this.value;
	}

	public String toCode() {
		return getIdJustificacion()==1 ? getValue() : Cadena.rellenar(getValue(), getLongitud(), getRelleno().charAt(0), toJustificacion());
	}

	public String toEmpty() {
		return getIdJustificacion()==1 ? getRelleno() : Cadena.rellenar(getRelleno(), getLongitud(), getRelleno().charAt(0), toJustificacion());
	}

	private boolean toJustificacion() {
		return getIdJustificacion()==2;
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
