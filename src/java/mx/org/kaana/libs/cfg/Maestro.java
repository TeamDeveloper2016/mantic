package mx.org.kaana.libs.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:58:24 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Maestro {

	private static final Logger LOG=Logger.getLogger(Maestro.class);
	private Configuracion configuracion;
	private List<Detalle> niveles;

	public Maestro(Configuracion configuracion, List<Detalle> niveles) {
		this.configuracion=configuracion;
		this.niveles      =niveles;
	}

	public Configuracion getConfiguracion() {
		return configuracion;
	}

	public List<Detalle> getNiveles() {
		return niveles;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	private void addLevel(DetalleConfiguracion detalleConfiguracion) {
		Detalle detalle=new Detalle(
			detalleConfiguracion.getNivel(),
			detalleConfiguracion.getLongitud(),
			detalleConfiguracion.getIdJustificacion(),
			detalleConfiguracion.getRelleno(),
			detalleConfiguracion.getDominio(),
			"");
		this.niveles.add(detalle);
		LOG.debug(detalle.toString());
	}

	private String toCode(char token, int levels) {
		StringBuilder sb=new StringBuilder();
		for (int x=0; x<this.niveles.size()&&x<levels; x++) {
			sb.append(this.niveles.get(x).toCode());
			if (token!=0&&x!=levels-1) {
				sb.append(token);
			}
		} // for
		return sb.toString();
	}

	private String cutLevel(Integer count, Integer longitud, String value) {
		String regresar="";
		if (count+longitud<=value.length()) {
			regresar=value.substring(count, count+longitud);
		} // if
		else if (count<value.length()) {
			regresar=value.substring(count);
		} // else if
		return regresar;
	}

	public void cleanLevels() {
		for (Detalle detalle : this.niveles) {
			detalle.setValue("");
		} // for
	}

	public void clean(int level) {
		if (level>=0&&level<this.niveles.size()) {
			Detalle detail=this.niveles.get(level);
			detail.setValue(detail.toEmpty());
		} // if	
	}

	public String toEmpty(int level) {
		String regresar="";
		if (level>=0&&level<this.niveles.size()) {
			Detalle detail=this.niveles.get(level);
			regresar=detail.toEmpty();
		} // if	
		return regresar;
	}

	public void setLikeKey(int nivel) {
		if (nivel<this.niveles.size()) {
			Detalle detail=this.niveles.get(nivel);
			detail.toLike();
		} // if	
		else {
			throw new RuntimeException("El nivel no esta en el rango de la clave");
		} // else
	}

	public String getLikeKey(int nivel) {
		String regresar="";
		if (nivel<this.niveles.size()) {
			Detalle detail=this.niveles.get(nivel);
			regresar=detail.toLike();
		} // if	
		else {
			throw new RuntimeException("El nivel no esta en el rango de la clave");
		} // else
		return regresar;
	}

	public void setKeyLevel(String key, int nivel) {
		if (nivel<this.niveles.size()) {
			Detalle detail=this.niveles.get(nivel);
			if (key!=null) {
				if (key.length()<=detail.getLongitud()) {
					detail.setValue(key);
					detail.toCode();
				}	// if
				else {
					detail.setValue(key.substring(0, detail.getLongitud()));
				} // else
			} // if
		} // if	
		else {
			throw new RuntimeException("El nivel no esta en el rango de la clave");
		} // else
	}

	private String toCode(int levels) {
		return toCode('\u0000', levels);
	}

	public String toCode() {
		return toCode(this.niveles.size());
	}

	public String toCode(char token) {
		return toCode(token, this.niveles.size());
	}

	private String toCode(char token, String value, int levels) {
		cleanLevels();
		Integer count=0;
		for (int x=0; x<this.niveles.size()&&x<levels; x++) {
			this.niveles.get(x).setValue(cutLevel(count, this.niveles.get(x).getLongitud(), value));
			count+=this.niveles.get(x).getLongitud();
		} // for
		return toCode(token);
	}

	public String toCode(char token, String value) {
		return toCode(token, value, this.niveles.size());
	}

	public String toCode(String value, int levels) {
		return toCode('\u0000', value, levels);
	}

	public String toCode(String value) {
		return toCode(value, this.niveles.size());
	}

	public String toKey(int levels) {
		return toCode(levels);
	}

	public String toKey(char token, String value, int levels) {
		toCode(token, value, levels);
		return toCode(levels);
	}

	public String toKey(char token, int levels) {
		return toCode(token, levels);
	}

	public String toKey(String value, int levels) {
		toCode(value, levels);
		return toKey(levels);
	}

	public String toEmpty(char token, int level, int levels) {
		StringBuilder sb=new StringBuilder("");
		for (int x=level; x<this.niveles.size()&&x<levels; x++) {
			sb.append(this.niveles.get(x).toEmpty());
			if (token!=0&&x!=levels-1) {
				sb.append(token);
			} // if
		} // for
		return sb.toString();
	}

	private List<String> toCodeAll(char token, int levels) {
		List<String> regresar=new ArrayList<String>();
		StringBuilder sb=new StringBuilder();
		for (int x=0; x<this.niveles.size()&&x<levels; x++) {
			sb.append(this.niveles.get(x).toCode());
			if (token!=0&&x!=levels-1) {
				sb.append(token);
			} // if
			regresar.add(sb.toString().concat(toEmpty(token, x+1, levels)));
		} // for
		return regresar;
	}

	private List<String> toCodeAll(String value, int levels) {
		return toCodeAll('\u0000', value, levels);
	}

	private List<String> toCodeAll(char token, String value, int levels) {
		loadValues(value, levels);
		return toCodeAll(token, levels);
	}

	public List<String> toCodeAll(char token, String value) {
		return toCodeAll(token, value, this.niveles.size());
	}

	public List<String> toCodeAll(String value) {
		return toCodeAll(value, this.niveles.size());
	}

	private List<String> toKeyAll(char token, int levels) {
		List<String> regresar=new ArrayList<String>();
		StringBuilder sb=new StringBuilder();
		for (int x=0; x<this.niveles.size()&&x<levels; x++) {
			sb.append(this.niveles.get(x).toCode());
			regresar.add(sb.toString());
			if (token!=0&&x!=levels-1) {
				sb.append(token);
			} // if
		} // for
		return regresar;
	}

	public List<String> toKeyAll(String value, int levels) {
		return toKeyAll('\u0000', value, levels);
	}

	public List<String> toKeyAll(char token, String value, int levels) {
		loadValues(value, levels);
		return toKeyAll(token, levels);
	}

	private void loadValues(String value, int levels) {
		cleanLevels();
		Integer count=0;
		for (int x=0; x<this.niveles.size()&&x<levels; x++) {
			this.niveles.get(x).setValue(cutLevel(count, this.niveles.get(x).getLongitud(), value));
			count+=this.niveles.get(x).getLongitud();
		} // for
	}

	protected String[] uniqueKey(List<String> list, String value) {
		Set<String> keys=new TreeSet<String>();
		keys.addAll(list);
		return keys.toArray(new String[0]);
	}

	public String toOnlyKey(String value, int level) {
		loadValues(value, level);
		return toKey(level>0 ? level-1 : 0);
	}

	public String toOnlyKey(char token, String value, int level) {
		loadValues(value, level);
		return toKey(token, level>0 ? level-1 : 0);
	}

	public String getValueKey(int level) {
		String regresar="";
		if (level<this.niveles.size()) {
			regresar=this.niveles.get(level).toCode();
		}
		return regresar;
	}

	public String toValueKey(String value, int level) {
		loadValues(value, level);
		return getValueKey(level>0 ? level-1 : 0);
	}

	public String toValueKey(int level) {
		return getValueKey(level>0 ? level-1 : 0);
	}

	@Override
	public String toString() {
		return "Maestro["+"configuracion="+configuracion+", niveles="+niveles+']';
	}
}
