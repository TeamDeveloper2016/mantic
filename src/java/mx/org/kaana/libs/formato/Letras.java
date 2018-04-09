/*
 * Clase: Letras.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */
package mx.org.kaana.libs.formato;
/**
* Clase estatica para convertir cantidades a letras
* Permite especificar la cantidad de renglones que se desee con un largo especifico para cada uno
* separando en silabas las palabras que no caben en el renglon
* Permite incluir hasta 9 decimales traducirlos a letras siempre que sean hasta tres decimales
* Permite expresar el valor 1 de las unidades enteras como "UNO" , "UNA" o "UN"
*  segun se fije el parametro genero_unidad en 0,1 o 2
* Retorna un array de String igual a la cantidad de renglones especificados
* permite agregar texto : a) al principio ( prefijo_inicio )
*                         b) despues de la parte entera ( sufijo_enteros )
*                         c) antes de la parte decimal ( prefijo_decimal )
*                         d) despues de la parte decimal ( sufijo_decimal )
*                         e) al final  ( sufijo_final )
*   ( dentro de cada uno de estos textos , el caracter '|' se usa para separar en silabas)
*   ( si el texto comienza con un caracter '|' indica que no se debe dejar espacio entre la
*     palabra anterior y este texto . Por ejemplo para agregar "/100.-" a continuacion de los
*     decimales expresados en numeros ( 50/100.-  , 950/1000 , 567899/1000000 , etc. )
* El valor maximo a traducir : usando el metodo getTexto(String cantidad) = 999999999999999.999999999
* usando el metodo getTexto(double cantidad) = 9999999999999999.899999 ( este metodo puede variar la
* cantidad de decimales si el valor es grande , no asi el metodo anterior.
* Tiene metodos set... para cada uno de los parametros
*
* Fecha de creaci�n: (20/05/2003 18:30:15)
*

* @author: Ruben D. Mori
*
*/

 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;


public final class Letras {

  private static String[] xDos  = new String[]{"U|NO","UN","DOS","TRES","CUA|TRO","CIN|CO",
  "SEIS","SIE|TE","O|CHO","NUE|VE","DIEZ","ON|CE","DO|CE","TRE|CE",
  "CA|TOR|CE","QUIN|CE","DIE|CI|SEIS","DIE|CI|SIE|TE","DIE|CI|O|CHO",
  "DIE|CI|NUE|VE","VEIN|TE","VEIN|TI|UN","VEIN|TI|DOS","VEIN|TI|TRES",
  "VEIIN|TI|CUA|TRO","VEIN|TI|CIN|CO","VEIN|TI|SEIS","VEINTI|SIE|TE",
  "VEIN|TI|O|CHO","VEIN|TI|NUE|VE"};

  private static String[] xUno  = new String[]{"CIEN","CIEN|TO","DOS|CIEN|TOS","TRES|CIEN|TOS",
  "CUA|TRO|CIEN|TOS","QUI|NIEN|TOS","SEIS|CIEN|TOS","SE|TE|CIEN|TOS",
  "O|CHO|CIEN|TOS","NO|VE|CIEN|TOS"};

  private static String[] xDiez = new String[]{"VEIN|TI|U|NO","","","TREIN|TA","CUA|REN|TA","CIN|CUEN|TA",
  "SE|SEN|TA","SE|TEN|TA","O|CHEN|TA","NO|VEN|TA" };

  private static String[] xEsp = new String[]{"Y","MIL","MI|LLON","MI|LLO|NES","BI|LLON","BI|LLO|NES"};
  private static String prefijo_inicio = "(", sufijo_enteros = "PE|SOS",
    prefijo_decimales = "CON", sufijo_decimales = "|/100.-",
    sufijo_final = ")", palabra_cero = "CE|RO";

  private static char   caracter_proteccion = 42;

  private static int[]  renglones = new int[] { 300 };

  private static char[][] datos;

  private static double importe , divisor , xtres, cantidad;

  private static int    tres, dos , uno, paso, decena, unidad, haymillones,cant_decimales = 2,
    genero_unidad = 0 , renglon , ajuste , posicion, posicion_corte,
    silaba , decimales ;

  private static final Log LOG= LogFactory.getLog(Letras.class);

  private static boolean traduce_decimales = false;

  public static void setRenglones(int[] xrenglones )    {
    renglones = xrenglones;
  }

  public static void setGenero_unidad(int xgenero_unidad )    {
    genero_unidad = xgenero_unidad;
  }

  public static void setCantidad_decimales(int xcant_decimales)    {
    cant_decimales = xcant_decimales;
  }

  public static void setPrefijo_inicio(String xprefijo_inicio)    {
    prefijo_inicio = xprefijo_inicio;
  }

  public static void setSufijo_final(String xsufijo_final)    {
    sufijo_final = xsufijo_final;
  }

  public static void setSufijo_enteros(String xsufijo_enteros)    {
    sufijo_enteros = xsufijo_enteros;
  }

  public static void setPrefijo_decimales(String xprefijo_decimales)  {
    prefijo_decimales = xprefijo_decimales;
  }

  public static void setSufijo_decimales(String xsufijo_decimales)    {
    sufijo_decimales = xsufijo_decimales;
  }

  public static void setCaracter_proteccion(char xcaracter_proteccion)    {
    caracter_proteccion = xcaracter_proteccion;
  }

  public static void setTraduce_decimales(boolean xtraduce_decimales) {
    traduce_decimales = xtraduce_decimales;
  }

  public static void setPalabra_cero(String xpalabra_cero) {
    palabra_cero = xpalabra_cero;
  }

  private static String[] getTexto(double importe, int qdecimales) {

    String xdecimales= String.valueOf(importe);
    decimales = qdecimales;
//    LOG.debug(xdecimales+ "=>"+ importe);

    divisor   = 1.00E12;

    haymillones = 0;

    if ( cant_decimales > 3 )  traduce_decimales = false;

    ajuste = 10;

    for ( int i=1; i < cant_decimales; i++ )    {
      ajuste = ajuste * 10;
    }

    if ( cant_decimales == 0 ) ajuste = 0;

    datos = new char[renglones.length][renglones[0]];


    for ( int x=0; x < renglones.length; x++ )  {
      datos[x] = new char[renglones[x]];
      for ( int y=0; y < renglones[x]; y++ )  {
        datos[x][y] = caracter_proteccion;
      }
    }

    xDos[0] = "U|NO";
    xDiez[0] = "VEIN|TI|UNO";
    if ( genero_unidad == 2 )   {
      xDos[0] = "UN";
      xDiez[0] = "VEIN|TI|UN";
    }
    if ( genero_unidad == 1 )   {
      xDos[0] = "U|NA";
      xDiez[0] = "VEIN|TI|U|NA";
    }

    renglon = posicion = 0;


    for ( paso = 0; paso < 5; paso++ ) {
      xtres  = ( importe / divisor );
      tres   = (int) xtres;
      importe = importe - (double)(tres * divisor);
      divisor = divisor / 1000;
      if ( tres > 0 ) traducir(tres);
    }

    if ( ( palabra_cero.length() > 0 ) && renglon == 0 && posicion == 0 )   {
      if ( prefijo_inicio.length() > 0 ) pasarTexto(prefijo_inicio);
      pasarTexto(palabra_cero);
    }

    if (( sufijo_enteros.length() > 0 ) && ( ( renglon > 0 ) || ( posicion > prefijo_inicio.length())))
      pasarTexto(sufijo_enteros);

    tres = (int) ( importe * ajuste );

    if ( decimales > 0 ) tres = decimales;

    if ( tres > 0 && ( prefijo_decimales.length() > 0 ) && ( renglon != 0 || posicion != 0))
      pasarTexto(prefijo_decimales);

    if(xdecimales.indexOf(".")>= 0)  {
      xdecimales= xdecimales.substring(xdecimales.indexOf(".")+ 1, xdecimales.length());
//      LOG.debug(xdecimales);
      if(xdecimales.length()< cant_decimales)
        for(int x= xdecimales.length(); x< cant_decimales; x++)
          xdecimales+= "0";
      if(Double.parseDouble(xdecimales)!= tres)
        tres= (int)Double.parseDouble(xdecimales);
    }
//    LOG.debug(xdecimales+ "=>"+ tres+ "=>"+ importe+ "=>"+ ajuste);
    if ( tres > 0 && traduce_decimales )    {
      paso = 5;
      traducir(tres);
    }

    if ( !traduce_decimales )  pasarTexto(("" + xdecimales + ""));

    if ( ( sufijo_decimales.length() > 0 )) pasarTexto(sufijo_decimales);

    if ( sufijo_final.length() > 0 ) pasarTexto(sufijo_final);

    String[] texto = new String[datos.length];

    for ( int i=0; i < datos.length; i++ )  {
      texto[i] = new String(datos[i]);
    }


    return texto;
  }

  private static void iniciarSilaba() {

    posicion_corte = posicion;
    silaba = 0;
  }

  private static void paseCaracter(char caracter)  {
    datos[renglon][posicion] = caracter;
    silaba++;
    posicion++;
  }

  private static void sumeRenglon()   {
    renglon++;
    posicion = 0;
    iniciarSilaba();
  }


  private static void pasarTexto(String palabra) {


    char[] desglose = palabra.toCharArray();

    if (posicion > 0 && ( posicion < ( renglones[renglon] -1 )) &&
      ( desglose[0] != 124 ))  {
      datos[renglon][posicion] = 32;
      posicion ++;
    } else

      if (posicion > 0 && ( posicion == ( renglones[renglon] -1 )) &&
      ( desglose[0] != 124 ))  {
      datos[renglon][posicion] = 32;
      sumeRenglon();
      }

    iniciarSilaba();

    for ( int i=0; i < desglose.length; i++ ) {

      if ( desglose[i] == 124 && i > 0  && ( posicion == ( renglones[renglon] -1 )))   {
        datos[renglon][posicion] = 45;
        sumeRenglon();
      } else

        if ( desglose[i] == 124 && i > 0  && ( posicion < ( renglones[renglon] -1 )))   {
        iniciarSilaba();
        } else

          if ( desglose[i] != 124 && ( posicion < ( renglones[renglon] -1 )))   {
        paseCaracter(desglose[i]);
          } else

            if ( (desglose[i] != 124) && ( posicion == ( renglones[renglon] -1 ))
            && ( i == ( desglose.length - 1)) )  {
        paseCaracter(desglose[i]);
        sumeRenglon();
            } else

              if ( desglose[i] != 124 && ( posicion == ( renglones[renglon] -1 ))
              && ( i < ( desglose.length - 1)))   {

        posicion = posicion_corte;
        datos[renglon][posicion] = 45;
        posicion++;

        if ( posicion <  renglones[renglon]  ) {
          for ( posicion=posicion; posicion < renglones[renglon]; posicion++ )   {
            datos[renglon][posicion] = 45;
          }
        }
        int xsilaba = silaba;
        sumeRenglon();

        for ( int z = (i - xsilaba); z < ( i + 1 ); z++ )  {
          datos[renglon][posicion] = desglose[z];
          posicion++;
        }
              }
    }
  }

  private static void traducir(int mil)    {

    if ( renglon == 0 && posicion == 0 && prefijo_inicio.length() > 0 )
      pasarTexto(prefijo_inicio);
    uno  =  mil / 100;
    dos  =  mil - ( uno * 100 );
    decena = dos / 10;
    unidad = dos - ( decena * 10 );
    if ( mil == 100 )  pasarTexto(xUno[0]);
    else if ( uno > 0 ) pasarTexto(xUno[uno]);


    if ( (dos > 1 && dos < 30 ) && ( paso != 4 || dos != 21 ))  pasarTexto(xDos[dos]);
    if ( paso == 4 && dos == 21 )   pasarTexto(xDiez[0]);
    if ( paso == 4 && dos == 1 )    pasarTexto(xDos[0]);

    if ( dos > 29 ) {
      pasarTexto(xDiez[decena]);
      if ( unidad > 0 )   {
        pasarTexto(xEsp[0]);
        if ( paso != 4 || unidad != 1 ) pasarTexto(xDos[unidad]);
        if ( paso == 4 && unidad == 1 ) pasarTexto(xDos[0]);
      }
    }
    if ( paso == 0 && mil == 1 )    {
      pasarTexto(xEsp[4]);
    }
    if ( paso == 0 && mil > 1 )     {
      pasarTexto(xEsp[5]);
    }
    if (( paso == 2 && mil > 1 ) || haymillones == 1 )  {
      pasarTexto(xEsp[3]);
      haymillones = 0;
    }
    if ( paso == 1 && mil > 0 )     {
      pasarTexto(xEsp[1]);
      haymillones = 1;
    }
    if (( paso == 2 && mil == 1 && haymillones == 0 ))  {
      pasarTexto(xEsp[2]);
    }
    if ( paso == 3 && mil > 0 )     {
      pasarTexto(xEsp[1]);
    }

  }
  public static String[] getTexto(String cantidad) {
    String parte_entera = ((cantidad.substring(0,cantidad.indexOf('.'))) + ".0");
    cantidad = cantidad.substring(cantidad.indexOf('.') + 1 );
    if ( cantidad.length() > cant_decimales ) cantidad = cantidad.substring(0,cant_decimales);
    if ( cantidad.length() < cant_decimales )    {
      for ( int i = cantidad.length(); i < cant_decimales; i++ )   {
        cantidad = cantidad + "0";
      }
    }
    return getTexto( Double.parseDouble(parte_entera),Integer.parseInt(cantidad) );
  }

  public static String[] getTexto(double cantidad) {
    return getTexto(cantidad,0);
  }

  public String getPorciento(String cifra) {
    String regresar= "";
    try {
      double cantidad = Double.parseDouble(cifra);
      setCaracter_proteccion(' ');
      setPrefijo_decimales("PUNTO");
      setSufijo_decimales("POR|CIEN|TO");
      setCantidad_decimales(2);
      setTraduce_decimales(true);
      setPrefijo_inicio("(");
      setSufijo_final(")");
      setSufijo_enteros("");
      String[] texto = Letras.getTexto(cantidad);
      for ( int i = 0 ; i < texto.length; i++ )   {
        regresar+= texto[i];
      }
      regresar= cifra+ "% "+ regresar.trim();
    } catch(Exception e) {
      Error.mensaje(e);
    }; // try
    return regresar;
  }; //

  public String getMoneda(String cifra) {
    return getMoneda(cifra, true);
  }; //

  public String getMoneda(String cifra, boolean regresarCifra) {
    String regresar= "";
    try {
      double cantidad = Double.parseDouble(Numero.formatear("##0.00", Double.parseDouble(cifra)));
      setCaracter_proteccion(' ');
      setPrefijo_decimales("");
      setSufijo_decimales("/100 M.N.");
      setCantidad_decimales(2);
      setTraduce_decimales(false);
      setPrefijo_inicio("(");
      setSufijo_final(")");
      setSufijo_enteros("PE|SOS");
      String[] texto = Letras.getTexto(cantidad);
      for (int i = 0 ; i < texto.length; i++ )   {
        regresar+= texto[i];
      }; // for
      if(regresarCifra)
        regresar = Numero.formatear("$#,##0.00", Double.parseDouble(cifra))+ " "+ regresar.trim();
      else
        regresar= regresar.trim();
    } catch(Exception e) {
      Error.mensaje(e);
    }; // try
    return regresar;
  }; //

  public static void main(String[] args) {
    try {
      Letras miCifras= new Letras();
      LOG.debug("Valor pasado : 13.34" + " => "+ miCifras.getPorciento("13.34"));
      LOG.debug("Valor pasado : 0.0" + " => "+ miCifras.getPorciento("0.0"));
      LOG.debug("Valor pasado : 23413" + " => "+ miCifras.getMoneda("23413"));
      LOG.debug("Valor pasado : 23413.57" + " => "+ miCifras.getMoneda("23413.57"));
      LOG.debug("Valor pasado : 23413.57" + " => "+ miCifras.getMoneda("23413.57", false));
      LOG.debug("Valor pasado : 1001.95" + " => "+ miCifras.getMoneda("1001.95"));
      LOG.debug("Valor pasado : 1001.95" + " => "+ miCifras.getMoneda("1001.95", false));
    } catch(Exception e) {
       Error.mensaje(e);
    }; // try

  }


}
