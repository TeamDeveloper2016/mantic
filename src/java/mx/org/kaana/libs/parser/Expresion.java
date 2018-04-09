/*
 * Expresion.java
 *
 * Created on 2 de diciembre de 2007, 08:38 PM
 *
 * Write by, alejandro.jimenez
 *
 */

package mx.org.kaana.libs.parser;

import com.eteks.jeks.JeksExpression;
import com.eteks.jeks.JeksExpressionSyntax;
import com.eteks.jeks.JeksFunctionParser;
import com.eteks.jeks.JeksFunctionSyntax;
import com.eteks.jeks.JeksInterpreter;
import com.eteks.parser.CompiledExpression;
import com.eteks.parser.ExpressionParser;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.parser.funciones.AllTrim;
import mx.org.kaana.libs.parser.funciones.Catalogo;
import mx.org.kaana.libs.parser.funciones.Comprometido;
import mx.org.kaana.libs.parser.funciones.Concat;
import mx.org.kaana.libs.parser.funciones.Concepto;
import mx.org.kaana.libs.parser.funciones.Consultar;
import mx.org.kaana.libs.parser.funciones.Contain;
import mx.org.kaana.libs.parser.funciones.Contar;
import mx.org.kaana.libs.parser.funciones.Empty;
import mx.org.kaana.libs.parser.funciones.Equals;
import mx.org.kaana.libs.parser.funciones.Lower;
import mx.org.kaana.libs.parser.funciones.Numerico;
import mx.org.kaana.libs.parser.funciones.NumeroOrden;
import mx.org.kaana.libs.parser.funciones.Round;
import mx.org.kaana.libs.parser.funciones.Search;
import mx.org.kaana.libs.parser.funciones.Semaforo;
import mx.org.kaana.libs.parser.funciones.SpaceBlank;
import mx.org.kaana.libs.parser.funciones.SubString;
import mx.org.kaana.libs.parser.funciones.Upper;
import mx.org.kaana.libs.parser.funciones.texto.WithValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author alejandro.jimenez
 */
public class Expresion {

  private ExpressionParser parser;
  private String formula;
  private Object resultado;

  private static final Log LOG= LogFactory.getLog(Expresion.class);
  /**
   * Creates a new instance of Expresion
   */
  public Expresion() {
    this("");
  }

  public Expresion(String formula) {
    this(formula, Collections.EMPTY_MAP);
  }

  public Expresion(String formula, Map<String, Object> params) {
    setFormula(formula);
    setResultado(Boolean.FALSE);
    JeksExpressionSyntax syntax= new JeksExpressionSyntax();
    JeksFunctionParser function= new JeksFunctionParser(syntax);
    syntax.addFunction(new Search());
    syntax.addFunction(new Round());
    syntax.addFunction(new Upper());
    syntax.addFunction(new Lower());
    syntax.addFunction(new Concat());
    syntax.addFunction(new AllTrim());
    syntax.addFunction(new Empty());
    syntax.addFunction(new Equals());
    syntax.addFunction(new SubString());
    syntax.addFunction(new Comprometido());
    syntax.addFunction(new Concepto());
    syntax.addFunction(new Contain());
    syntax.addFunction(new WithValue());
    syntax.addFunction(new Semaforo());
    syntax.addFunction(new Catalogo());
    syntax.addFunction(new Numerico());
    syntax.addFunction(new NumeroOrden());
    syntax.addFunction(new Contar());
    syntax.addFunction(new SpaceBlank());
    syntax.addFunction(new Consultar());

    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Search());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Round());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Upper());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Lower());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Concat());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new AllTrim());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Empty());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Equals());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new SubString());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Comprometido());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Concepto());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Contain());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new WithValue());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Semaforo());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Catalogo());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Numerico());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new NumeroOrden());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Contar());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new SpaceBlank());
    ((JeksFunctionSyntax)function.getSyntax()).addFunction(new Consultar());
    setParser(new ExpressionParser(function.getSyntax(), new JeksParams(params)));
  }

  public Object evaluar() {
    try {
      if(getFormula().length()> 0) {
        CompiledExpression expression= getParser().compileExpression("=".concat(getFormula()));
        JeksExpression evaluate= new JeksExpression(expression);
        setResultado(evaluate.getValue(new JeksInterpreter()));
      } // if
      else
        setResultado(Boolean.TRUE);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return getResultado();
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula(String formula) {
    if(formula!= null)
      this.formula = formula.trim();
    else
      throw new IllegalArgumentException("La formula no puede ser nula");
    setResultado("");
  }

  public Object getResultado() {
    return resultado;
  }

  private void setResultado(Object resultado) {
    this.resultado = resultado;
  }

  private ExpressionParser getParser() {
    return parser;
  }

  private void setParser(ExpressionParser parser) {
    this.parser = parser;
  }

  public void finalize() {
    setParser(null);
  }

  public static void main(String[] args) {
  //  Expresion expresion= new Expresion("CONTAIN(\"aAND(|b|c|d|e|f|g|i|\",\"[i,j]\")");
  //  Expresion expresion= new Expresion("CATALOGO(\"TcMuestrasDto\",\"folio~entidad\",\"entidad\",\"2830620~TAMAULIPAS\")");
  //  expresion.setFormula("CONTAIN(\"a|b|c|d|e|f|g|i|\",\"[i,j]\")");
  //  LOG.debug(expresion.getResultado().toString());
  //  Expresion expresion= new Expresion("SPACEBLANK(\" 1          1  \",\"20\")");
  //  Finales *********************************************
  //  expresion.setFormula("SEARCH(\"logan\",\"loganfalcus\")");
  //  expresion.setFormula("ROUND(2.489)");
  //  expresion.setFormula("UPPER(\"las mayusculas\")");
  //  expresion.setFormula("LOWER(\"LAS MINUSCULAS\")");
  //  expresion.setFormula("CONCAT(\"UNO\", \" MAS\", \" UNO\", \" IGUAL\", \" A DOS ENAMORADOS\")");
  //  expresion.setFormula("ALLTRIM(\"   los blancos no saben \")");
  //  expresion.setFormula("EMPTY(\"\")");
  //  expresion.setFormula("EQUALS(\"lagos\", \"LAGOS\")");
  //  expresion.setFormula("SUBSTRING(\"tenislos\", 1, 2)");
  //  expresion.setFormula("CONTAIN(\"[g,i]\", \"a|b|c|d|e|f|g|i|\")");
  //  LOG.debug(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
  //  expresion.setFormula("CONCAT(\"UNO\", \" MAS\", \" UNO\", \" IGUAL\", \" A MUCHOS\")");
  //  LOG.debug(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
  //  expresion.setFormula("LOWER(CONCAT(\"UNO\", \" MAS\", \" UNO\", \" IGUAL\", \" A MUCHOS\"))");
  //  LOG.debug(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
  //  expresion.setFormula("NOT(EQUALS(\"uno\",\"UNO\"))");
  //  LOG.debug(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
  //  expresion.setFormula("EMPTY(\"\")");
  //  LOG.debug(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
    Map<String, Object> params= new HashMap<String, Object>();
    params.put("AP4_1", "1");
    params.put("AP4_2_2", "1");
    params.put("BLANCO", 1);
    // AP4_1<>'' and AP4_2_2<> ''
    //Expresion expresion= new Expresion("EMPTY(CONCAT(AP4_1,AP4_2_2))", params);
    Expresion expresion= new Expresion("IF(2>1 AND 3<4, 1, 2o )", params);
    System.out.println(expresion.evaluar());
  }

}
