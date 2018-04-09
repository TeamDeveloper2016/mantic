package mx.org.kaana.libs.parser.funciones;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Rango;

public abstract class NullField {

  protected void checkParameters(Object [] parametersValue) {
    int index= 0;
    for (Object cadena: parametersValue) {
      if (cadena== null)
        parametersValue[index]= "";
      else
        if(cadena.equals(Rango.NULO) || cadena.equals(Rango.BLANCO))
          parametersValue[index]= "";
        else
          if(cadena.equals(Rango.NO_ESPECIFICADO))
            parametersValue[index]= Constantes.AMPERSON;
      index++;
    } // for
  }

}
