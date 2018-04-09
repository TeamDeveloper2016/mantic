package mx.org.kaana.kajool.db.comun.sql;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import mx.org.kaana.libs.formato.Fecha;

public class Value implements Serializable, Comparable<Value> {
	private static final long serialVersionUID=5231961032480104777L;

  private String name;
  private Object data;
  private String field;

  public Value(String name) {
    this(name, null);
  }

  public Value(String name, Object data) {
		this(name, data, name);
  }

  public Value(String name, Object data, String field) {
    this.name = name;
    this.data = data;
    this.field= field;
  }

  public Object getData() {
    return data;
  }

  public String getData$() {
    String regresar = null;
    if (data instanceof Date)
      regresar = Fecha.formatear(Fecha.FECHA_CORTA, (Date)data);
    else if (data instanceof Time)
      regresar = Fecha.formatear(Fecha.HORA_CORTA, (Time)data);
    else if (data instanceof Timestamp)
      regresar = Fecha.formatear(Fecha.FECHA_HORA, (Timestamp)data);
    else
      regresar = data.toString();
    return regresar;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getName() {
    return name;
  }

  private void setName(String name) {
    this.name = name;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }

  public String toString(String value) {
    Object regresar = getData();
    return regresar!= null? regresar.toString(): value;
  }

  public Long toLong(Long value) {
    Object regresar = getData();
    return regresar!=null? ((Number)regresar).longValue(): value;
  }

  public Integer toInteger(Integer value) {
    Object regresar = getData();
    return regresar!=null? ((Number)regresar).intValue(): value;
  }

  public Double toDouble(Double value) {
    Object regresar = getData();
    return regresar!=null? ((Number)regresar).doubleValue(): value;
  }

  public Float toFloat(Float value) {
    Object regresar = getData();
    return regresar!=null? ((Number)regresar).floatValue(): value;
  }

  public Short toShort(Short value) {
    Object regresar = getData();
    return regresar!=null? ((Number)regresar).shortValue(): value;
  }

  public Boolean toBoolean(Boolean value) {
    Object regresar = getData();
    return regresar!=null ? (Boolean)regresar: value;
  }

  public Date toDate(Date value) {
    Object regresar = getData();
    return regresar!=null? (Date)regresar: value;
  }

  public Timestamp toTimestamp(Timestamp value) {
    Object regresar = getData();
    return regresar!=null? (Timestamp)regresar: value;
  }

  public Time toTime(Time value) {
    Object regresar = getData();
    return regresar!=null? (Time)regresar: value;
  }

  @Override
  public String toString() {
    return toString(null);
  }

  public Long toLong() {
    return toLong(null);
  }

  public Integer toInteger() {
    return toInteger(null);
  }

  public Double toDouble() {
    return toDouble(null);
  }

  public Float toFloat() {
    return toFloat(null);
  }

  public Short toShort() {
    return toShort(null);
  }

  public Boolean toBoolean() {
    return toBoolean(null);
  }

  public Date toDate() {
    return toDate(null);
  }

  public Timestamp toTimestamp() {
    return toTimestamp(null);
  }

  public Time toTime() {
    return toTime(null);
  }

  public Long getToLong() {
    return toLong(null);
  }

  public Integer getToInteger() {
    return toInteger(null);
  }

  public Double getToDouble() {
    return toDouble(null);
  }

  public Float getToFloat() {
    return toFloat(null);
  }

  public Short getToShort() {
    return toShort(null);
  }

  public Boolean getToBoolean() {
    return toBoolean(null);
  }

  public Date getToDate() {
    return toDate(null);
  }

  public Timestamp getToTimestamp() {
    return toTimestamp(null);
  }

  public Time getToTime() {
    return toTime(null);
  }

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(getClass()!=obj.getClass()) {
			return false;
		}
		final Value other=(Value) obj;
		if(this.data!=other.data&&(this.data==null||!this.data.equals(other.data))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=89*hash+(this.data!=null ? this.data.hashCode() : 0);
		return hash;
	}

	@Override
		public int compareTo(Value o) {
		int regresar;
		try {
	    if (data instanceof Date)
			regresar= ((Date)this.data).before((Date)o.data) ? -1 :(((Date)this.data).equals(((Date)o.data))  ? 0 : 1);
    else if (data instanceof Time)
			regresar= ((Time)this.data).before((Time)o.data) ? -1 :(((Time)this.data).equals(((Time)o.data))  ? 0 : 1);
    else if (data instanceof Timestamp)
			regresar= ((Timestamp)this.data).before((Timestamp)o.data) ? -1 :(((Timestamp)this.data).equals(((Timestamp)o.data))  ? 0 : 1);
    else
			regresar = this.data.toString().compareTo(o.toString());
		} // try
		catch(Exception e) {
			regresar = 0;
		} // catch
		return regresar;
	}
	
}
