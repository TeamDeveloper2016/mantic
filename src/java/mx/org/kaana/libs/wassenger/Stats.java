package mx.org.kaana.libs.wassenger;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/06/2021
 *@time 03:28:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Stats implements Serializable {

  private static final long serialVersionUID = 7689209653343788663L;
  
  private Long downloads;
  private Long deliveries;

  public Stats() {
    this(0L, 0L);
  }

  public Stats(Long downloads, Long deliveries) {
    this.downloads = downloads;
    this.deliveries = deliveries;
  }

  public Long getDownloads() {
    return downloads;
  }

  public void setDownloads(Long downloads) {
    this.downloads = downloads;
  }

  public Long getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(Long deliveries) {
    this.deliveries = deliveries;
  }

  @Override
  public String toString() {
    return "Stats{" + "downloads=" + downloads + ", deliveries=" + deliveries + '}';
  }
  
}
