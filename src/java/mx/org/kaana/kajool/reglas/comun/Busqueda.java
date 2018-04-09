package mx.org.kaana.kajool.reglas.comun;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jun 11, 2012
 * @time 2:58:36 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Error;

public class Busqueda {

  private List<String> disenio;
  private String valor;

  public Busqueda(List<String> disenio, String valor) {
    this.disenio= disenio;
    this.valor  = valor;
  }

  public String format() {
    StringBuilder resultado= new StringBuilder();
    int count=0;
    if(this.disenio!= null && this.disenio.size()> 0) {
      resultado.append("(");
      for(String campo: this.disenio) {
        if (count!= 0)
          resultado.append(" or ");
        String[] criterio= valor.split(" ");
        for (int i = 0; i < criterio.length; i++)  {
          resultado.append("(upper (");
          resultado.append(campo);
          resultado.append(") like '%");
          resultado.append(criterio[i].toUpperCase());
          resultado.append("%'");
          resultado.append(")");
          if (i+1<criterio.length)
            resultado.append(" and ");
        } // for
        count++;
      } // for
      resultado.append(")");
    } // if
    return resultado.toString();
  }

  public String formatAnd(String valor, String campo) {
    String [] cadena = null;
    String regresa   = null;
    int i = 0;
    try {
      cadena = valor.split(" ");
      if(cadena.length > 1) {
        for(i = 0; i < cadena.length -1; i++) {
          regresa = cadena[i].concat(" and upper(").concat(campo).concat(") like %");
        } // for
        regresa = regresa.concat(" ").concat(cadena[i]);
      } // if
      else
        regresa = cadena[0];
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    return regresa;
  } // formatAnd

  public static void main(String[] args) {
    List<String> list= new ArrayList<String>();
    list.add("login");
    list.add("id_perfil");
    //Condicion condicion= new Condicion(list, "aramirez.sandoval");
    //System.out.println(condicion.get());
  }
	
}
