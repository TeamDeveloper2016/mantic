package mx.org.kaana.kajool.db.comun.dto;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public interface IValue  {

  public String    toString(String name);
  public Long      toLong(String name);
  public Integer   toInteger(String name);
  public Float     toFloat(String name);
  public Double    toDouble(String name);
  public Short     toShort(String name);
  public Boolean   toBoolean (String name);
  public Date      toDate (String name);
  public Timestamp toTimestamp(String name);
  public Time      toTime (String name);

}


