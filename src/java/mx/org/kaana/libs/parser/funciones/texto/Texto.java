package mx.org.kaana.libs.parser.funciones.texto;

import com.eteks.parser.Function;

public abstract class Texto  implements Function{
	private static final long serialVersionUID=-7663473481477389466L;

  protected void checkParameters(Object [] parametersValue) {
    for(Object cadena: parametersValue) {
      if(!(cadena instanceof String))
        throw new IllegalArgumentException(cadena.toString().concat(" no es una cadena. !"));
    } // for
  }
}
