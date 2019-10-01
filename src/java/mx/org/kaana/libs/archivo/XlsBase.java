package mx.org.kaana.libs.archivo;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class XlsBase implements Serializable {

	private static final long serialVersionUID = 5339778084264506908L;	
	protected WritableWorkbook libro;
  protected WritableSheet hoja;
  protected boolean debug;
  protected int posicionColumna;
  protected int posicionFila;
  private int numeroColumna;
  private int indiceColumna;
  private String[] formatos;
  private static final Log LOG= LogFactory.getLog(XlsBase.class);
  
  public void setLibro(WritableWorkbook libro) {
    this.libro = libro;
  }

  public WritableWorkbook getLibro() {
    return libro;
  }

  public void setHoja(WritableSheet hoja) {
    this.hoja = hoja;
  }

  public WritableSheet getHoja() {
    return hoja;
  }

  public void setPosicionColumna(int posicionColumna) {
    this.posicionColumna = posicionColumna;
  }

  public int getPosicionColumna() {
    return posicionColumna;
  }

  public void setPosicionFila(int posicionFila) {
    this.posicionFila = posicionFila;
  }

  public int getPosicionFila() {
    return posicionFila;
  }

  public void setNumeroColumna(int numeroColumna) {
    this.numeroColumna = numeroColumna;
  }

  public int getNumeroColumna() {
    return numeroColumna;
  }

  public void setIndiceColumna(int indiceColumna) {
    this.indiceColumna = indiceColumna;
  }

  public int getIndiceColumna() {
    return indiceColumna;
  }

  public void setFormatos(String[] formatos) {
    this.formatos = formatos;
  }

  public String[] getFormatos() {
    return formatos;
  }
  
	@Override
  public void finalize() {
    libro   = null;
    hoja    = null;
    formatos= null;
  } // finalize
  
  public void demoDatos(int columnas, int filas) {
    try {
      for (int x = 0; x < columnas; x++) {
        if (debug) {
          Label label = new Label(0, x, "[" + x + "]");
          hoja.addCell(label);
        } // if
        Label label = new Label(filas + 2, x, "=suma(" + (char)(65 + x) + "2:" + (char)(65 + x) + (filas + 1) + ")");
        hoja.addCell(label);
        for (int y = 0; y < filas; y++) {
          if (x % 2 == 0) {
            Number number = new Number(y + 1, x, 1231234.12341234);
            hoja.addCell(number);
            libro.write();
          } // if
          else {
            Label label2 = new Label(y + 1, x, "[" + y + "," + x + "]");
            hoja.addCell(label2);
            libro.write();
          } // else
        } // for y
      } // for x
      libro.close();
    } // try
    catch (Exception e) {
       Error.mensaje(e);
    }// try
  } 
   
  protected abstract String getNombresColumnas();
  
  protected abstract int getColumnasInformacion();
	
	public abstract boolean generarRegistros(boolean titulo) throws Exception;
  
  protected void procesarEncabezado(String algunos) {
    try {
      StringTokenizer stringTokenizer= new StringTokenizer(algunos.toUpperCase(), ",");
      String alias= "";
      int x       = 1;
      while (stringTokenizer.hasMoreTokens()) {
        alias = stringTokenizer.nextToken();
        Label label = new Label(getPosicionColumna()+ x- 1, getPosicionFila(), alias);
        hoja.addCell(label);  
        x++;
      }// while
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }// try
  }
    
  public String formatearValor(String celda, String formato) {
    try {
      int codigo  = Integer.parseInt(formato);
      double valor= 0;
      switch (codigo) {
        case 0: // no hacer ninguna validacion
        break;
        case 1: // formato moneda
          valor = Double.parseDouble(celda);
          celda = Numero.formatear("$ ###,###.00", valor);
          break;
        case 2: // separacion de miles
          valor = Double.parseDouble(celda);
          celda = Numero.formatear("###,###.00", valor);
          break;
        case 3: // separacion de miles sin decimales
          valor = Double.parseDouble(celda);
          celda = Numero.formatear("###,###", valor);
          break;
        case 4: // Letra capital la primer letra
          celda = Cadena.letraCapital(celda);
          break;
        case 5: // Letra capital por cada palabra en la cadena
          celda = Cadena.nombrePersona(celda);
          break;
        case 6: // Letra en mayusculas
          celda = celda.toUpperCase();
          break;
        case 7: // Letra en minusculas
          celda = celda.toLowerCase();
          break;
        case 14: // desencriptar palabra
          Encriptar desencriptar = new Encriptar();
          celda = desencriptar.desencriptar(celda, desencriptar._CLAVE);
          desencriptar = null;
          break;
        case 15: // encriptar palabra
          Encriptar encriptar = new Encriptar();
          celda = encriptar.encriptar(celda, encriptar._CLAVE);
          encriptar = null;
          break;
        case 16: // wakko
          valor = Double.parseDouble(celda);
          celda = Numero.formatear("#.00", valor);
          break;
      }// switch
      if (codigo > 7 && codigo < 14) {
        GregorianCalendar calendario = new GregorianCalendar();
        int xAnio = Integer.parseInt(celda.substring(0, 4));
        int xMes = Integer.parseInt(celda.substring(4, 6)) - 1;
        int xDia = Integer.parseInt(celda.substring(6, 8));
        calendario.set(xAnio, xMes, xDia);
        switch (codigo) {
        case 8: // Fecha en dd/mes/yyyy   26/Noviembre/2003
          celda =
             calendario.get(calendario.DATE) + "/" + Fecha.getNombreMes(calendario.get(calendario.MONTH)) + "/" + celda.substring(0,4);
          break;
        case 9: // Fecha en dd/mm/yyyy    26/11/2003
          celda = celda.substring(6, 8) + "/" + celda.substring(4, 6) + "/" + celda.substring(0, 4);
          break;
        case 10: // Fecha en:  nombre del dia, dd/mm/yyyy    Miercoles, 26/11/2003
          celda = Fecha.getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + celda.substring(6, 8) + "/" + celda.substring(4,6) +
              "/" + celda.substring(0, 4);
          break;
        case 11: // Fecha en:  nombre del dia, dia mes a?o   Miercoles, 26 de Noviembre del 2003
          celda = Fecha.getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + calendario.get(calendario.DATE) + " de " + Fecha.getNombreMes(calendario.get(calendario.MONTH)) + " de " + calendario.get(calendario.YEAR);
          break;
        case 12: // Fecha en dd/mm/yy   26/11/03
          celda = celda.substring(6, 8) + "/" + celda.substring(4, 6) + "/" + celda.substring(2, 4);
          break;
        case 13: // Fecha en:  dia mes a?o  26 de Noviembre del 2003
          celda =
              calendario.get(calendario.DATE) + " de " + Fecha.getNombreMes(calendario.get(calendario.MONTH)) + " de " +
              calendario.get(calendario.YEAR);
          break;
        }// switch
      }// if
    }
    catch (Exception e) {
      Error.mensaje(e);
    }    
    return celda;
  } 
}