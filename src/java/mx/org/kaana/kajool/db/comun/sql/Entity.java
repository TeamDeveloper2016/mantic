package mx.org.kaana.kajool.db.comun.sql;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.dto.IValue;

/**
 *
 * @author KAANA
 */
public class Entity extends HashMap<String, Value> implements IValue, IBaseDto, Serializable {
	
	private static final long serialVersionUID=-6128600239501961407L;

  public Entity() {
  }

  public Entity(Long key) {
    this.put("idKey", new Value("idKey", new Long(-1)));
  }

  public String toField(String name) {
    Value regresar = null;
    if (this.containsKey(name))
      regresar = get(name);
		else
      throw new RuntimeException("El nombre de campo [".concat(name).concat("] no existe."));
    return regresar.getData() == null ? "" : regresar.getData().toString();
  }

  public Object toObject(String name) {
    Value regresar = null;
    if (this.containsKey(name))
      regresar = get(name);
    else
      throw new RuntimeException("El nombre de campo [".concat(name).concat("] no existe."));
    return regresar.getData();
  }

  @Override
  public Object toValue(String name) {
    return toObject(name);
  }

  @Override
  public Long getKey() {
    Long regresar = null;
    if(this.containsKey("idKey"))
      regresar = this.get("idKey").toLong();
    if (regresar == null)
      for (String key: keySet()) {
        if (key.indexOf("idKey") >= 0) {
          if ( this.get(key).getData() instanceof String)
            regresar = Numero.getLong(this.get(key).toString());
          else
            regresar = this.get(key).toLong();
          break;
        } // if
      } // for
		if(regresar== null)
      throw new RuntimeException("La vista/consulta no tiene definido un campo con el alias de id_key");
    return regresar;
	}

  public String getKeyName() {
    String regresar = null;
    if(this.containsKey("idKey"))
      regresar = "idKey";
    if (regresar == null)
      for (String key: keySet()) {
        if (key.indexOf("idKey") >= 0) {
          regresar = key;
          break;
        } // if
      } // for
    return regresar;
	}
	
  @Override
  public void setKey(Long key) {
  }

  @Override
  public Map<String, Object> toMap() {
    Map<String, Object> regresar = new HashMap<String, Object>();
    for(Map.Entry<String, Value> entry : this.entrySet())
      regresar.put(entry.getKey(), entry.getValue());
    return regresar;
  }

  public Map<String, Object> toBdFieldMap() {
    Map<String, Object> regresar = new HashMap<String, Object>();
    for(Map.Entry<String, Value> entry : this.entrySet())
      regresar.put(entry.getValue().getField(), entry.getValue().getData());
    return regresar;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(Constantes.SEPARADOR);
    for(Map.Entry<String, Value> entry : this.entrySet()) {
      if (entry.getValue().getName().toLowerCase().startsWith(Constantes.CAMPO_LLAVE)) {
        regresar.append(entry.getValue().getName());
        regresar.append("~");
        regresar.append(entry.getValue().getData());
        regresar.append(Constantes.SEPARADOR);
      } // if
    } // for
    return regresar.length() > 1 ? regresar.toString() : "";
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    int count             = 0;
    regresar.append(Constantes.SEPARADOR);
    for(Map.Entry<String, Value> entry : this.entrySet()) {
      if (entry.getValue().getName().toLowerCase().startsWith(Constantes.CAMPO_LLAVE)) {
        regresar.append(entry.getValue().getData());
        regresar.append(Constantes.SEPARADOR);
        count++;
      } // if
    } // for
    return regresar.length() > 1 ? count > 1 ? regresar.toString() : regresar.substring(1, regresar.length() - 1) : "";
  }

  @Override
  public String toString() {
    return String.valueOf(this.getKey());
  }

  public String toValues() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("[");
    for(Map.Entry<String, Value> entry : this.entrySet()) {
      regresar.append(entry.getValue().getName());
      regresar.append("=");
      if (entry.getValue().getData() != null) {
        regresar.append(entry.getValue().getData$());
      } // if
      regresar.append(",");
    } // for
    regresar.delete(regresar.length() - 1, regresar.length());
    regresar.append("]");
    return regresar.toString();
  }

  @Override
  public String toString(String name) {
    Object regresar = toObject(name);
    return regresar != null ? regresar.toString() : null;
  }

  @Override
  public Long toLong(String name) {		
    Object regresar = toObject(name);
    return regresar != null ? ((Number) regresar).longValue() : null;
  }

  @Override
  public Integer toInteger(String name) {
    Object regresar = toObject(name);
    return regresar != null ? ((Number) regresar).intValue() : null;
  }

  @Override
  public Double toDouble(String name) {
    Object regresar = toObject(name);
    return regresar != null ? ((Number) regresar).doubleValue() : null;
  }

  @Override
  public Float toFloat(String name) {
    Object regresar = toObject(name);
    return regresar != null ? ((Number) regresar).floatValue() : null;
  }

  @Override
  public Short toShort(String name) {
    Object regresar = toObject(name);
    return regresar != null ? ((Number) regresar).shortValue() : null;
  }

  @Override
  public Boolean toBoolean(String name) {
    Object regresar = toObject(name);
    return regresar != null ? (Boolean) regresar : null;
  }

  @Override
  public Date toDate(String name) {
    Object regresar = toObject(name);
    return regresar != null ? (Date) regresar : null;
  }

  @Override
  public Timestamp toTimestamp(String name) {
    Object regresar = toObject(name);
    return regresar != null ? (Timestamp) regresar : null;
  }

  @Override
  public Time toTime(String name) {
    Object regresar = toObject(name);
    return regresar != null ? (Time) regresar : null;
  }

  @Override
  public Object[] toArray() {
    return super.values().toArray();
  }

  @Override
  public Class toHbmClass() {
    return Entity.class;
  }


}
