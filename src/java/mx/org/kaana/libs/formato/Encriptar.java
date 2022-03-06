/*
 * Clase: Encriptar.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: Team Developer 2016 <team.developer@kaana.org.mx>
 */
package mx.org.kaana.libs.formato;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class Encriptar {

  public static final String _CLAVE= "\u0053\u0049\u0041\u004E\u004F\u004D\u0049\u004E\u0041\u0032\u0030\u0030\u0035";
  private int keyLen;
  private int keyPos;
  private int offset;
  private int srcPos;
  private int srcAsc;
  private int tmpSrcAsc;
  private int range;
  private BigInteger bits;
  private String dest;
  private String pass;
  private String llave;
  private boolean fijar;
  private static final Log LOG= LogFactory.getLog(Encriptar.class);

  public Encriptar() {
    this(_CLAVE);
  }

  public Encriptar(String llave) {
    inicializa();
    setLlave(llave);
  }

  public Encriptar(String pass, String llave) {
    inicializa();
    setPass(pass);
    setLlave(llave);
  }

  private void inicializa() {
    dest= "";
    keyPos= 0;
    srcPos= 0;
    srcAsc= 0;
    range = 256;
	}

  public void setPass(String pass) {
    this.pass= pass;
  }

  public String getPass() {
    return pass;
  }

  public void setLlave(String llave) {
    this.llave= llave;
  }

  public String getLlave() {
    return llave;
  }

  public String encriptar() {
    return encriptar(getPass(), getLlave());
  }

  public String encriptar(String Src) {
    return encriptar(Src, getLlave());
  }

  public String getEncriptado(String Src) {
    setFijar(true);
    return encriptar(Src, _CLAVE);
  }

  public String encriptar(String Src, String Key) {
    inicializa();
    keyLen= Key.length();
    if (isFijar())
      offset= (range-1);
    else
      offset= new Random().nextInt(range);
    dest  = Integer.toHexString(offset).length()==1?"0"+Integer.toHexString(offset):Integer.toHexString(offset);
    for (srcPos= 0; srcPos< Src.length(); srcPos++) {
      srcAsc= ((byte)(Src.charAt(srcPos))+offset) % (range - 1);
      if (keyPos < keyLen-1)
        keyPos++;
      else
        keyPos= 0;
      bits= new BigInteger(String.valueOf(srcAsc), 10);
      srcAsc=bits.xor(new BigInteger(String.valueOf((byte)(Key.charAt(keyPos))), 10)).intValue();
      dest+= Integer.toHexString(srcAsc).length()==1?"0"+Integer.toHexString(srcAsc):Integer.toHexString(srcAsc);
      offset= srcAsc;
    }
    return dest;
  }

  public String desencriptar() {
    return desencriptar(encriptar(getPass()), getLlave()) ;
  }

  public String desencriptar(String Src) {
    return desencriptar(Src, getLlave()) ;
  }

  public String datosAcceso(String dato, String driver) { //wakko
    return desencriptar(dato, driver);
  }

  public String desencriptar(String src, String Key) {
    inicializa();
    keyLen= Key.length();
    offset= Integer.parseInt(src.substring(0, 2), 16);
    srcPos= 2;
    while (srcPos < src.length()) {
      srcAsc= Integer.parseInt(src.substring(srcPos, srcPos+2), 16);
      if (keyPos < keyLen-1)
        keyPos++;
      else
        keyPos= 0;
      bits= new BigInteger(String.valueOf(srcAsc), 10);
      tmpSrcAsc=bits.xor(new BigInteger(String.valueOf((byte)(Key.charAt(keyPos))), 10)).intValue();
      if (tmpSrcAsc > offset)
        tmpSrcAsc= tmpSrcAsc - offset;
      else
        tmpSrcAsc= (range - 1) + tmpSrcAsc - offset;
      dest+= (char)(tmpSrcAsc);
      offset= srcAsc;
      srcPos+= 2;
    }
    return dest;
  }

  public boolean isFijar() {
    return fijar;
  }

  public void setFijar(boolean fijar) {
    this.fijar = fijar;
  }

  public void finalize() {
    if(bits!= null)
      bits = null;
  }

  public static void main(String [] args) throws IOException, Exception {
    Encriptar encriptado= new Encriptar();
    //pass.setFijar(true);
    //                     dia,horas,minutos,segundos  
    // 2022-03-05 20:25:49 %d%H%i%S
		String texto= encriptado.encriptar("05202549", encriptado._CLAVE);
    //String texto = encriptado.encriptar("jdbc:oracle:thin:@10.1.8.41:1521:bddesa", _CLAVE);
   // String texto = encriptado.encriptar("Hola", _CLAVE);
    //LOG.debug("encriptado: " + texto);
    //LOG.debug(texto);
    //String texto= encriptado.desencriptar("ir2SvBxAiWIbCDhVGdDv6A==", _CLAVE);
    System.out.println(texto);
    //LOG.debug("\u0053\u0049\u0041\u004E\u004F\u004D\u0049\u004E\u0041\u0032\u0030\u0030\u0035");
    //encriptado.des
    //12345678901234567890123456789    
    //443c2f2e2d2c291708
    //f56ee15dde5adb5bd5
}

};
