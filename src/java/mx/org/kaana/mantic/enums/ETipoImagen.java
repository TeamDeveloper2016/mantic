package mx.org.kaana.mantic.enums;

public enum ETipoImagen {

  PNG (1L),
  JPG (2L),
  TIFF(3L),
  BMP (4L),
  GIF (5L),
  JPEG(6L);

  private Long idTipoImagen;

  private ETipoImagen(Long idTipoImagen) {
    this.idTipoImagen = idTipoImagen;
  }

  public Long getIdTipoImagen() {
    return idTipoImagen;
  }
}