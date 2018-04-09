package mx.org.kaana.libs.reportes;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 10, 2014
 *@time 1:01:28 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

/*
 * CodigoBarras.java
 *
 * Created on 14 de junio de 2005, 12:24 PM
 */

import com.idautomation.linear.BarCode;
import com.idautomation.linear.encoder.barCodeEncoder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class CodigoBarras extends BarCode {

  private static final long serialVersionUID = 1L;
  private String nivel_mando = null;
  private String curp         = null;
  private double altoBarras   = 0.7D;///0.5D;
  private int longBarras      = 35;//35
  private int anchoBarras     = 261;
  private boolean mostrarTexto= false;
  private String formato      = null;
  private String ruta         = null;
  public final String JPEG    = "jpeg";
  public final String GIF     = "gif";
  public final String PNG     = "png";


  /** Creates a new instance of generaCodigo */
  public CodigoBarras() {
    curp        = "JIGA760130HASMRL04";
    formato     = JPEG;
    ruta        = "/";
    altoBarras  =  0.7D;///0.5D;
    anchoBarras = 261;
    longBarras      = 35;//35
    mostrarTexto= false;
  }

  /** Creates a new instance of generaCodigo */
  public CodigoBarras(String curp) {
    this.curp   = curp;
    formato     = JPEG;
    ruta        = "/home/default/";
    altoBarras  =  0.7D;///0.5D;
    anchoBarras = 261;
    longBarras      = 35;//35
    mostrarTexto= false;
  }

  /** Creates a new instance of generaCodigo */
  public CodigoBarras(String curp, String formato) {
    this.curp   = curp;
    this.formato= JPEG;
    this.formato= "jpeg,gif,png".indexOf(formato.toLowerCase())>= 0? formato.toLowerCase(): this.formato;
    altoBarras  =  0.7D;///0.5D;
    anchoBarras = 261;
    longBarras  = 35;//35
    mostrarTexto= false;
  }

 public java.lang.String getNivel_mando() {
    return nivel_mando;
  }
  public void setNivel_mando(java.lang.String nivel_mando) {
    this.nivel_mando = nivel_mando;
  }

/**
   * Getter for property curp.
   * @return Value of property curp.
   */
  public java.lang.String getCurp() {
    return curp;
  }

  /**
   * Setter for property curp.
   * @param curp New value of property curp.
   */
  public void setCurp(java.lang.String curp) {
    this.curp = curp;
  }

  /**
   * Getter for property formato.
   * @return Value of property formato.
   */
  public java.lang.String getFormato() {
    return formato;
  }

  /**
   * Setter for property formato.
   * @param formato New value of property formato.
   */
  public void setFormato(java.lang.String formato) {
    this.formato = "jpeg,gif,png".indexOf(formato.toLowerCase())>= 0? formato.toLowerCase(): this.formato;
  }

  /**
   * Getter for property ruta.
   * @return Value of property ruta.
   */
  public java.lang.String getRuta() {
    return ruta;
  }

  /**
   * Setter for property ruta.
   * @param ruta New value of property ruta.
   */
  public void setRuta(java.lang.String ruta) {
    this.ruta = ruta;
  }

  public void datosComunes() {
  // generaCodigoBarras nivel_mando= new generaCodigoBarras();
    this.barType = this.CODE39EXT;
    this.textFont= new java.awt.Font("Arial", 0, 11);
    this.leftMarginCM=0.0D;
    this.checkCharacter= false;
    this.showText= mostrarTexto;
    this.barHeightCM= altoBarras;
    //this.setBackground(new Color(255,255,255));
    this.setBarHeightCM(altoBarras);
    this.backColor= new Color(255, 255, 255);
    this.barColor = new Color(0, 0, 0);
  }; // datosComunes

  public void procesar() {
    this.code= getCurp();
    datosComunes();
    barCodeEncoder bce= new barCodeEncoder((BarCode)this, getFormato().toUpperCase(), getRuta()+ getCurp().toLowerCase()+ "."+ getFormato());
  }; // procesar

  public void procesar(String curp) {
    setCurp(curp);
    procesar();
  }; // procesar

  public void procesar(String curp, String ruta) {
    setCurp(curp);
    setRuta(ruta);
    procesar();
  }; // procesar

  public BufferedImage codigoMemoria() {
    datosComunes();
    this.setDataToEncode(getCurp());
	  this.setSize(getAnchoBarras(), getLongBarras());
	  BufferedImage bufferedImage= new BufferedImage(getAnchoBarras(), getLongBarras(), BufferedImage.TYPE_INT_RGB);
    Graphics2D    graphics = bufferedImage.createGraphics();
    //graphics.setXORMode(Color.WHITE);
    this.paint(graphics);
    return bufferedImage;
  }; // codigoMemoria

  public BufferedImage codigoMemoria(String curp) {
    setCurp(curp);
    return codigoMemoria();
  }; // codigoMemoria

  public void finalize() {
    curp= null;
  }; // finalize

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    CodigoBarras codigo= new CodigoBarras("JIGA760130HASMRL04");
    System.out.println("Curp:"+ codigo.getCurp());
    codigo.procesar();
  }

  /**
   * Getter for property altoBarras.
   * @return Value of property altoBarras.
   */
  public double getAltoBarras() {
    return altoBarras;
  }

  /**
   * Setter for property altoBarras.
   * @param altoBarras New value of property altoBarras.
   */
  public void setAltoBarras(double altoBarras) {
    this.altoBarras = altoBarras;
  }

  /**
   * Getter for property mostrarTexto.
   * @return Value of property mostrarTexto.
   */
  public boolean isMostrarTexto() {
    return mostrarTexto;
  }

  /**
   * Setter for property mostrarTexto.
   * @param mostrarTexto New value of property mostrarTexto.
   */
  public void setMostrarTexto(boolean mostrarTexto) {
    this.mostrarTexto = mostrarTexto;
  }

  /**
   * Getter for property anchoBarras.
   * @return Value of property anchoBarras.
   */
  public int getAnchoBarras() {
    return anchoBarras;
  }

  /**
   * Setter for property anchoBarras.
   * @param anchoBarras New value of property anchoBarras.
   */
  public void setAnchoBarras(int anchoBarras) {
    this.anchoBarras = anchoBarras;
  }

  /**
   * Getter for property longBarras.
   * @return Value of property longBarras.
   */
  public int getLongBarras() {
    return longBarras;
  }

  /**
   * Setter for property longBarras.
   * @param longBarras New value of property longBarras.
   */
  public void setLongBarras(int longBarras) {
    this.longBarras = longBarras;
  }

}

