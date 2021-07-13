package mx.org.kaana.libs.wassenger;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/06/2021
 *@time 03:28:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Data implements Serializable {

 /* [
     {
       "id":"60d4ba052739feb85874b575",
       "url":"https://cafu.jvmhost.net/Temporal/Pdf/CAFU_2021062410492325_orden_de_compra_detalle.pdf",
       "format":"native",
       "filename":"CAFU_2021062410492325_orden_de_compra_detalle.pdf",
       "size":258187,
       "origin":"remote",
       "mime":"application/pdf",
       "ext":"pdf",
       "kind":"document",
       "sha2":"23fdeb0fdd59ad0be05d111a0e0d24bfd6076ce9812c81537216f00f30420500",
       "tags":[],
       "status":"active",
       "mode":"default",
       "createdAt":"2021-06-24T16:59:49.283Z",
       "expiresAt":"2021-10-22T16:59:49.271Z",
       "stats":
         {
           "downloads":0,
           "deliveries":0
          }
    }
  ] */ 
  private static final long serialVersionUID = 2049738893516784029L;

  private String id;
  private String url;
  private String format;
  private String filename;
  private Long size;
  private String origin;
  private String mime;
  private String ext;
  private String kind;
  private String sha2;
  // private String tags;
  private String status;
  private String mode;
  private String createdAt;
  private String expiresAt;
  private Stats stats;

  public Data() {
  }

  public Data(String id, String url, String format, String filename, Long size, String origin, String mime, String ext, String kind, String sha2, String tags, String status, String mode, String createdAt, String expiresAt, Stats stats) {
    this.id = id;
    this.url = url;
    this.format = format;
    this.filename = filename;
    this.size = size;
    this.origin = origin;
    this.mime = mime;
    this.ext = ext;
    this.kind = kind;
    this.sha2 = sha2;
    // this.tags = tags;
    this.status = status;
    this.mode = mode;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.stats = stats;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public String getExt() {
    return ext;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getSha2() {
    return sha2;
  }

  public void setSha2(String sha2) {
    this.sha2 = sha2;
  }

//  public String getTags() {
//    return tags;
//  }
//
//  public void setTags(String tags) {
//    this.tags = tags;
//  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(String expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Stats getStats() {
    return stats;
  }

  public void setStats(Stats stats) {
    this.stats = stats;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Data other = (Data) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  
  @Override
  public String toString() {
    // return "Data{" + "id=" + id + ", url=" + url + ", format=" + format + ", filename=" + filename + ", size=" + size + ", origin=" + origin + ", mime=" + mime + ", ext=" + ext + ", kind=" + kind + ", sha2=" + sha2 + ", tags=" + tags + ", status=" + status + ", mode=" + mode + ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", stats=" + stats + '}';
    return "Data{" + "id=" + id + ", url=" + url + ", format=" + format + ", filename=" + filename + ", size=" + size + ", origin=" + origin + ", mime=" + mime + ", ext=" + ext + ", kind=" + kind + ", sha2=" + sha2 + ", status=" + status + ", mode=" + mode + ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", stats=" + stats + '}';
  }
  
}
