package mx.org.kaana.libs.formato;

import java.util.Properties;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

/**
 * Clase que encripta y desencripta cadenas con la libreria BouncyCastle
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 20/08/2015
 * @time 09:19:48 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class BouncyEncryption extends Properties {

  public static final String KAJOOL_CLAVE = "KajoolJanal2016";
  private static final long serialVersionUID = 4723854671077799971L;

  /**
	 * Devuelve cadena desencriptada
	 * @param cadena String a desencriptar
   * @return instancia de Document con sentencias sql
	 */
  public static String decrypt(String cadena) throws Exception {
    BlowfishEngine engine = new BlowfishEngine();
    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
    StringBuilder regresar = new StringBuilder();
    KeyParameter key = new KeyParameter(KAJOOL_CLAVE.getBytes());
    cipher.init(false, key);
    byte salida[] = Base64.decode(cadena);
    byte salidaDos[] = new byte[cipher.getOutputSize(salida.length)];
    int tamanioDos = cipher.processBytes(salida, 0, salida.length, salidaDos, 0);
    cipher.doFinal(salidaDos, tamanioDos);
    String auxiliar = new String(salidaDos);
    for (int i = 0; i < auxiliar.length(); i++) {
      char c = auxiliar.charAt(i);
      if (c != 0)
        regresar.append(c);
    } // for

    return regresar.toString();
  }

  /**
	 * Devuelve cadena encriptada
	 * @param cadena String a encriptar
   * @return instancia de Document con sentencias sql
	 */
  public static String encrypt(String cadena) throws Exception {
    BlowfishEngine engine = new BlowfishEngine();
    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
    KeyParameter key = new KeyParameter(KAJOOL_CLAVE.getBytes());
    cipher.init(true, key);
    byte entrada[] = cadena.getBytes();
    byte salida[] = new byte[cipher.getOutputSize(entrada.length)];
    int tamanio = cipher.processBytes(entrada, 0, entrada.length, salida, 0);
    try {
      cipher.doFinal(salida, tamanio);
    } // try
    catch (CryptoException e) {
      throw new Exception(e);
    } // catch
    String s = new String(Base64.encode(salida));
    return s;
  }

  public static void main(String[] args) {
    String originalString = "kajool2016"; // String to be encrypted.
    try {
      String encryptedString = encrypt(originalString);
      System.out.println("Original String: " + originalString);
      System.out.println("Encrypted String: " + encryptedString);
      String decryptedString = decrypt(encryptedString);
      System.out.println("Decrypted String: " + decryptedString);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
