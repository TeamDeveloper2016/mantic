package mx.org.kaana.libs.formato;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Rangos {

  private static final Log LOG       = LogFactory.getLog(Rangos.class);
  private static final Integer LENGTH= 0;

  private Integer length;
  private String token;
  private String series;
  private List <Rango> elements;

  public Rangos() {
    this("12,13.15,18.20");
  }

  public Rangos(String series) {
     this(series, '.');
  }

  public Rangos(String series, char token) {
    this(series, "["+ token+"]");
  }

  public Rangos(String series, String token) {
    this(series, token, LENGTH);
  }

  public Rangos(String series, char token, Integer length) {
    this(series, "["+ token+"]", length);
  }

  public Rangos(String series, String token, Integer length) {
    setToken(token);
    setElements(new ArrayList<Rango>());
    setLength(length);
    setSeries(series);
  }

  private void setElements(List elements) {
    this.elements = elements;
  }

  public List <Rango> getElements() {
    return elements;
  }

  public String getToken() {
    return token;
  }

  private void setToken(String token) {
    this.token = token;
  }

  public void setSeries(String series) {
    if(series!= null) {
      this.series = series;
      parser();
    } // if
  }

  private String getSeries() {
    return series;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getLength() {
    return length;
  }

  private void periodo(String ... values) {
    getElements().add(simple(values[0].trim(), values[1].trim()));
  }

  private Rango simple(String value) {
    Rango rango = null;
    if(value.toLowerCase().equals("nulo"))
      rango = new Rango(Rango.NULO);
    else
      if(value.toLowerCase().equals("blanco"))
        rango = new Rango(Rango.BLANCO);
      else
        if(value.toLowerCase().equals("noespecificado"))
          rango = new Rango(Rango.NO_ESPECIFICADO);
        else
          if(Character.isDigit(value.trim().charAt(0)))
            rango = new Rango(Numero.getDouble(value));
          else
            throw new RuntimeException("Los parametros de la expresion de Rangos esta mal elaborada.");
    return rango;
  }

  private Rango simple(String star, String end) {
    return new Rango(Numero.getDouble(star), Numero.getDouble(end));
  }

  private void parser() {
    getElements().clear();
    StringTokenizer st= new StringTokenizer(getSeries(), "[],");
    while (st.hasMoreTokens())  {
      String elemento = st.nextToken().trim();
      String[] valores= elemento.split(getToken());
      if(!getLength().equals(LENGTH) || valores[0].trim().startsWith(LENGTH.toString())) {
        int zeros= toCountZero(valores[0].trim());
        if(zeros> getLength())
          setLength(zeros);
      } // if
      if(valores.length> 1)
        periodo(valores);
      else
        getElements().add(simple(valores[0].trim()));
    } // while
    st= null;
  }

  private int toCountZero(String valor) {
    if(valor.indexOf(".")> 0)
      valor= valor.substring(0, valor.indexOf("."));
    int contador= 0;
    while(valor!= null && contador< valor.length() && valor.charAt(contador)== '0')
      contador++;
    return contador> 0? valor.length(): LENGTH;
  }

  public boolean isDentro(Double valor) {
    for(Rango rango: getElements()) {
      if(rango.isDentro(valor))
        return true;
    } // for
    return false;
  }

  public boolean isDentro(String value, boolean checkSize) {
    Double dato = 0.0D;
    boolean size= true;
    if(value== null)
      dato= Rango.BLANCO;
    else
      if(Cadena.isVacio(value))
        dato= Rango.BLANCO;
      else
        if(value.trim().toUpperCase().equals(Constantes.AMPERSON))
          dato= Rango.NO_ESPECIFICADO;
        else {
          dato= Double.parseDouble(value);
          if(getLength()> 0 && checkSize) {
            int index= value.indexOf('.');
            if(index< 0)
              index= value.length();
            size= getLength().equals(value.substring(0, index).length());
          } // if
        } // else
    return isDentro(dato) && size;
  }

  public boolean isDentro(String value) {
    return isDentro(value, true);
  }

  public boolean isDentro(Integer value) {
    if(value== null)
      value= Rango.NULO.intValue();
    return isDentro(value.doubleValue());
  }

  public boolean isDentro(Long value) {
    if(value== null)
      value= Rango.NULO.longValue();
    return isDentro(value.doubleValue());
  }

  private String[] toStrings() {
    Set <String> regresar= new TreeSet<String>();
    for(Rango rango: getElements()) {
      regresar.addAll(rango.toValues(getLength()));
    } // for
    return regresar.toArray(new String[0]);
  }

  private Double[] toIntegers() {
    Set <Double> regresar= new TreeSet<Double>();
    for(Rango rango: getElements()) {
      regresar.addAll(rango.toValues());
    } // for
    return regresar.toArray(new Double[0]);
  }
	
	public String toElements() {
		StringBuilder regresar= new StringBuilder();
		boolean primera       = false;		
		for(Double numero: toIntegers()) {
			if(primera)
				regresar.append(",");			
			regresar.append(Integer.toString(numero.intValue()));
			primera= true;
		} // for
		return regresar.toString();
	}

  private Object[] toValues() {
    if(!getLength().equals(LENGTH))
      return toStrings();
    else
      return toIntegers();
  }

  @Override
  public String toString() {
    StringBuilder sb= new StringBuilder();
    sb.append("[");
    for(Rango rango: getElements()) {
      sb.append(rango.toString(getLength()));
    } // for
    sb.append(",'");
	  sb.append(getToken());
    sb.append("']");
    return sb.toString();
  }

  @Override
  public void finalize() throws Throwable {
    try {
      Methods.clean(this.elements);
    } // try
    finally {
      super.finalize();
    } // finally
  }

  public boolean evaluate(String series, String token, Double value) {
    setToken(token);
    Methods.clean(this.elements);
    setElements(new ArrayList<Rango>());
    setLength(LENGTH);
    setSeries(series);
    return isDentro(value);
  }

  public static void main(String[] args) {
    Rangos rangos= new Rangos("[20.1_30.1,001.1_010.8, blanco, nulo]", '_');
    LOG.debug("010.7 esta en [20.1_30.1,001.1_010.8, blanco, nulo]");
    LOG.debug(rangos.isDentro("010.7"));
    LOG.debug(rangos.isDentro(""));
    LOG.debug(rangos);
    for (Object value: rangos.toValues())
      LOG.debug(value);
    LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    rangos= new Rangos("[20.1..30.1,001.1..010.8, blanco, nulo]", "\\.\\.");
    LOG.debug("001.7 esta en [20.1_30.1,001.1_010.8, blanco, nulo]");
    LOG.debug(rangos.isDentro("001.7"));
    LOG.debug(rangos);
    for (Object value: rangos.toValues())
      LOG.debug(value);
  }

}
