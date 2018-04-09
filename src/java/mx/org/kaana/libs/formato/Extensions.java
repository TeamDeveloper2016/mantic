package mx.org.kaana.libs.formato;

public enum Extensions  {

  DOC("application/msword", "application/msword"),
  DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
  XLS("application/vnd.ms-excel", "application/vnd.ms-excel"),
  XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
  DBF("application/x-dbf", "application/octet-stream"),
  TXT("text/plain", "text/plain"),
  JPG("image/jpeg", "image/pjpeg"),
  ZIP("application/zip", "application/x-zip-compressed"),
  PDF("application/pdf","application/pdf"),
  JAR("application/x-java-archive", "application/x-zip-compressed"),
  RTF("application/msword", "application/msword"),
  HTML("text/html", "text/html"),
  CSV("text/plain", "text/plain"),
  APK("application/vnd.android.package-archive", "application/vnd.android.package-archive");

  private Extensions(String contenTypeLinux, String contentTypeWindows) {
     this.contentTypeLinux   = contenTypeLinux;
     this.contentTypeWindows = contentTypeWindows;
  }

  private final String contentTypeLinux;
  private final String contentTypeWindows;


  public String getContenTypeLinux() {
    return contentTypeLinux;
  }

  public String getContentTypeWindows() {
    return contentTypeWindows;
  }

  public boolean equals (String contenType) {
    return getContenTypeLinux().equals(contenType) || getContentTypeWindows().equals(contenType);
  }

  public String toName() {
    return this.name().toLowerCase();
  }

}

