/*
 * Expresion.java
 *
 * Created on 8 de agosto de 2011, 15:36 PM
 *
 * Write by, alejandro.jimenez
 *
 */
package mx.org.kaana.libs.parser;

import mx.org.kaana.libs.parser.funciones.Upper;
import mx.org.kaana.libs.parser.funciones.SubString;
import mx.org.kaana.libs.parser.funciones.Str;
import mx.org.kaana.libs.parser.funciones.SpaceBlank;
import mx.org.kaana.libs.parser.funciones.Semaforo;
import mx.org.kaana.libs.parser.funciones.NumeroOrden;
import mx.org.kaana.libs.parser.funciones.AllTrim;
import mx.org.kaana.libs.parser.funciones.Search;
import mx.org.kaana.libs.parser.funciones.Lower;
import mx.org.kaana.libs.parser.funciones.Comprometido;
import mx.org.kaana.libs.parser.funciones.Contar;
import mx.org.kaana.libs.parser.funciones.Empty;
import mx.org.kaana.libs.parser.funciones.Numerico;
import mx.org.kaana.libs.parser.funciones.Catalogo;
import mx.org.kaana.libs.parser.funciones.Contain;
import mx.org.kaana.libs.parser.funciones.Round;
import mx.org.kaana.libs.parser.funciones.Concepto;
import mx.org.kaana.libs.parser.funciones.Concat;
import mx.org.kaana.libs.parser.funciones.Equals;
import com.eteks.jeks.JeksExpression;
import com.eteks.jeks.JeksInterpreter;
import com.eteks.parser.CompiledExpression;
import com.eteks.parser.ExpressionParser;
import com.eteks.parser.FunctionParser;
import com.eteks.parser.PascalSyntax;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.parser.funciones.texto.WithValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Pascal {

  private static final Log LOG= LogFactory.getLog(Pascal.class);

  private ExpressionParser parser;
  private String formula;
  private Object resultado;

  /**
   * Creates a new instance of Expresion
   */
  public Pascal() {
    this("");
  }

  public Pascal(String formula) {
    this(formula, Collections.EMPTY_MAP);
  }

  public Pascal(String formula, Map<String, Object> params) {
    this.formula=formula;
    this.resultado=Boolean.FALSE;
    SecoSyntax syntax= new SecoSyntax();
    FunctionParser function= new FunctionParser(syntax);
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
    syntax.addFunction(new Str());

    ((PascalSyntax)function.getSyntax()).addFunction(new Search());
    ((PascalSyntax)function.getSyntax()).addFunction(new Round());
    ((PascalSyntax)function.getSyntax()).addFunction(new Upper());
    ((PascalSyntax)function.getSyntax()).addFunction(new Lower());
    ((PascalSyntax)function.getSyntax()).addFunction(new Concat());
    ((PascalSyntax)function.getSyntax()).addFunction(new AllTrim());
    ((PascalSyntax)function.getSyntax()).addFunction(new Empty());
    ((PascalSyntax)function.getSyntax()).addFunction(new Equals());
    ((PascalSyntax)function.getSyntax()).addFunction(new SubString());
    ((PascalSyntax)function.getSyntax()).addFunction(new Comprometido());
    ((PascalSyntax)function.getSyntax()).addFunction(new Concepto());
    ((PascalSyntax)function.getSyntax()).addFunction(new Contain());
    ((PascalSyntax)function.getSyntax()).addFunction(new WithValue());
    ((PascalSyntax)function.getSyntax()).addFunction(new Semaforo());
    ((PascalSyntax)function.getSyntax()).addFunction(new Catalogo());
    ((PascalSyntax)function.getSyntax()).addFunction(new Numerico());
    ((PascalSyntax)function.getSyntax()).addFunction(new NumeroOrden());
    ((PascalSyntax)function.getSyntax()).addFunction(new Contar());
    ((PascalSyntax)function.getSyntax()).addFunction(new SpaceBlank());
    ((PascalSyntax)function.getSyntax()).addFunction(new Str());
     this.parser=new ExpressionParser(function.getSyntax(), new JeksParams(params));
  }

  public Object evaluar() throws Exception {
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
      throw new IllegalArgumentException(e.getMessage()+" Formula [".concat(getFormula()).concat("]"));
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

  public void setParams(Map<String, Object> params) {
    ((JeksParams)getParser().getExpressionParameter()).setParams(params);
  }

  public Object evaluate(String formula, Map<String, Object> params) throws Exception {
    setFormula(formula);
    setParams(params);
    return evaluar();
  }

  public static void main(String[] args) throws Exception {
    Map<String, Object> params= new HashMap<String, Object>();
    params.put("AP4_1", "ags");
    params.put("AP4_2_2", "1");
    params.put("AP4_2_3", null);
    params.put("AP4_2_4", null);
    // AP4_1<>'' and AP4_2_2<> ''
    //Pascal pascal= new Expresion("EMPTY(CONCAT(AP4_1,AP4_2_2))", params);
    Pascal pascal= new Pascal("NOT(EMPTY(AP4_1)) AND NOT(EMPTY(AP4_2_2))", params);
    //Pascal pascal= new Pascal("IF((AP4_1<> BLANCO AND AP4_2_2<>BLANCO) THEN 1 ELSE 2)", params);
    //Pascal pascal= new Pascal("EMPTY(CONCAT(AP4_2_3, AP4_2_4))", params);
    //System.out.println(pascal.evaluar());
    //LOG.info(pascal.evaluar());

    params.clear();
    params.put("uno", "ags");
    params.put("dos",   "1");
    params.put("tres", null);
    pascal.setFormula("EMPTY(CONCAT(uno, dos))");
    pascal.setParams(params);
    LOG.info(pascal.evaluar());
  }

}
