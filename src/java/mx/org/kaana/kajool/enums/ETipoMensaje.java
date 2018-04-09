package mx.org.kaana.kajool.enums;

import javax.faces.application.FacesMessage;

public enum ETipoMensaje {

	ERROR(FacesMessage.SEVERITY_ERROR,"Error"),
	INFORMACION(FacesMessage.SEVERITY_INFO,"Notificación"),
	ALERTA(FacesMessage.SEVERITY_WARN,"Alerta"),
	FATAL(FacesMessage.SEVERITY_FATAL,"Fatal");
	
	private FacesMessage.Severity tipo;
  private String tituloMensaje;
	
	private ETipoMensaje(FacesMessage.Severity tipo, String tituloMensaje) {
		this.tipo = tipo;
    this.tituloMensaje = tituloMensaje;
	}
	
	public FacesMessage.Severity getTipoMensaje() {
		return tipo;
	}

  public String getTituloMensaje () {
    return tituloMensaje;
  }
}
