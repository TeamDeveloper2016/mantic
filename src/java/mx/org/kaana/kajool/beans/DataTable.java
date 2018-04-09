package mx.org.kaana.kajool.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.List;

public class DataTable implements Serializable {
  
  private static final long serialVersionUID = 946865456675265527L;
  
  private List<String> headers;
  private List<Row> rows;

  public DataTable(List<String> headers, List<Row> rows) {
    this.headers = headers;
    this.rows = rows;
  }

  public List<String> getHeaders() {
    return headers;
  }

  public void setHeaders(List<String> headers) {
    this.headers = headers;
  }

  public List<Row> getRows() {
    return rows;
  }

  public void setRows(List<Row> rows) {
    this.rows = rows;
  }
  
}
