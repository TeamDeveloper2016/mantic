package mx.org.kaana.libs.motor.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 4, 2012
 *@time 3:20:11 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DetalleMotor implements Serializable {

  private static final long serialVersionUID=2638834281477803338L;

  private String paquete;
  private Long encuestaSeleccionada;
  private String encuestaSeleccionadaTexto;
  private Long encuestaSeleccionadaEncuesta;
  private String nombreEncuesta;
  private String esSinonimo;
	private String esNemonico;
  private String tablaSinonimo;
  private String llaveSinonimo;
  private Long tablaSeleccionada;
  private String nombreTabla;
  private String resultadoXML;
  private String resultadoDto;
  private Long campoLlaveSeleccionado;
  private String campoLlave;
  private String esBean;
  private String resultadoXMLConsulta;
  private String conDao;
	private Boolean sinonimo;

  public String getResultadoDto() {
    return resultadoDto;
  }

  public void setResultadoDto(String resultadoDto) {
    this.resultadoDto = resultadoDto;
  }

  public String getEncuestaSeleccionadaTexto() {
    return encuestaSeleccionadaTexto;
  }

  public void setEncuestaSeleccionadaTexto(String encuestaSeleccionadaTexto) {
    this.encuestaSeleccionadaTexto = encuestaSeleccionadaTexto;
  }

  public Long getEncuestaSeleccionada() {
    return encuestaSeleccionada;
  }

  public void setEncuestaSeleccionada(Long encuestaSeleccionada) {
    this.encuestaSeleccionada=encuestaSeleccionada;
  }

	public String getEsNemonico() {
		return esNemonico;
	}

	public void setEsNemonico(String esNemonico) {
		this.esNemonico=esNemonico;
	}

  public String getEsSinonimo() {
    return esSinonimo;
  }

  public void setEsSinonimo(String esSinonimo) {
    this.esSinonimo=esSinonimo;
  }

  public String getLlaveSinonimo() {
    return llaveSinonimo;
  }

  public void setLlaveSinonimo(String llaveSinonimo) {
    this.llaveSinonimo=llaveSinonimo;
  }

  public String getPaquete() {
    return paquete;
  }

  public void setPaquete(String paquete) {
    this.paquete=paquete;
  }

  public String getTablaSinonimo() {
    return tablaSinonimo;
  }

  public void setTablaSinonimo(String tablaSinonimo) {
    this.tablaSinonimo=tablaSinonimo;
  }

  public String getNombreEncuesta() {
    return nombreEncuesta;
  }

  public void setNombreEncuesta(String nombreEncuesta) {
    this.nombreEncuesta=nombreEncuesta;
  }

  public String getNombreTabla() {
    return nombreTabla;
  }

  public void setNombreTabla(String nombreTabla) {
    this.nombreTabla=nombreTabla;
  }

  public Long getTablaSeleccionada() {
    return tablaSeleccionada;
  }

  public void setTablaSeleccionada(Long tablaSeleccionada) {
    this.tablaSeleccionada=tablaSeleccionada;
  }

  public String getResultadoXML() {
    return resultadoXML;
  }

  public void setResultadoXML(String resultadoXML) {
    this.resultadoXML=resultadoXML;
  }

  public String getCampoLlave() {
    return campoLlave;
  }

  public void setCampoLlave(String campoLlave) {
    this.campoLlave=campoLlave;
  }

  public String getEsBean() {
    return esBean;
  }

  public void setEsBean(String esBean) {
    this.esBean=esBean;
  }

  public Long getEncuestaSeleccionadaEncuesta() {
    return encuestaSeleccionadaEncuesta;
  }

  public void setEncuestaSeleccionadaEncuesta(Long encuestaSeleccionadaEncuesta) {
    this.encuestaSeleccionadaEncuesta=encuestaSeleccionadaEncuesta;
  }

  public Long getCampoLlaveSeleccionado() {
    return campoLlaveSeleccionado;
  }

  public void setCampoLlaveSeleccionado(Long campoLlaveSeleccionado) {
    this.campoLlaveSeleccionado=campoLlaveSeleccionado;
  }

  public String getResultadoXMLConsulta() {
    return resultadoXMLConsulta;
  }

  public void setResultadoXMLConsulta(String resultadoXMLConsulta) {
    this.resultadoXMLConsulta=resultadoXMLConsulta;
  }

  public String getConDao() {
    return conDao;
  }

  public void setConDao(String conDao) {
    this.conDao = conDao;
  }

	public Boolean getSinonimo() {
		return this.esSinonimo.equals("1");
	}

	public void clean() {
    this.campoLlave           = "";
    this.encuestaSeleccionada = -1L;
    this.esBean               = "1";
    this.conDao               = "0";
		this.esNemonico           = "0";
    this.esSinonimo           = "1";
    this.llaveSinonimo        = "";
    this.nombreEncuesta       = "";
    this.nombreTabla          = "";
    this.resultadoXML         = "";
    this.resultadoDto         = "";
    this.tablaSeleccionada    = -1L;
    this.tablaSinonimo        = "";
    this.encuestaSeleccionadaEncuesta = -1L;
    this.campoLlaveSeleccionado = -1L;
  }

}
