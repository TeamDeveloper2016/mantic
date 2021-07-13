package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/06/2021
 *@time 03:28:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Upload implements Serializable {

  private static final long serialVersionUID = 3295341602514898155L;
  
  private HttpResponse<String> file;
  private Media media;
  private Integer status;
  private String statusText;

  public Upload(HttpResponse<String> file) {
    this.file  = file;
    this.status= file.getStatus();
    this.statusText= file.getStatusText();
    this.init();
  }

  public HttpResponse<String> getFile() {
    return file;
  }

  public Media getMedia() {
    return this.media;
  }  

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }
  
  private void init() {
    Gson gson = new Gson();
    if(this.isOk())
      this.media = gson.fromJson(this.file.getBody(), Media.class);
  }
  
  public boolean isOk() {
    return Objects.equals(this.status, 201);
  }
          
}
