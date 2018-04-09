package mx.org.kaana.libs.formato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rango implements Serializable {

  private static final long serialVersionUID=7796120086005247322L;	
	
  public static final Double NULO           = -100000000D;
  public static final Double BLANCO         = -100000001D;
  public static final Double NO_ESPECIFICADO= -100000002D;

  private Double min;
  private Double max;

  public Rango() {
    this(0D);
  }

  public Rango(Double min) {
    this(min, min);
  }

  public Rango (int min, int max) {
    this (new Double(min), new Double(max));
  }

  public Rango(Double min, Double max) {
    setMin(min);
    setMax(max);
  }

  private void setMin(Double min) {
    this.min = min;
  }

  public Double getMin() {
    return min;
  }

  private void setMax(Double max) {
    this.max = max;
  }

  public Double getMax() {
    return max;
  }

  public boolean isDentro(int valor) {
    return isDentro(new Double(valor));
  }

  public boolean isDentro(Double valor) {
    return valor>= getMin() && valor<= getMax();
  }

  public boolean isRango() {
    return getMin()!= getMax();
  }

  public String toString(int length) {
    StringBuilder sb= new StringBuilder();
    sb.append("[");
    if(getMin().equals(Rango.NULO) || getMin().equals(Rango.BLANCO) || getMin().equals(Rango.NO_ESPECIFICADO))
      sb.append(toCadena());
    else {
      sb.append(length!= 0? Cadena.rellenar(String.valueOf(getMin().longValue()), length, '0', true)+ getMin().toString().substring(getMin().toString().indexOf(".")): getMin().toString());
      sb.append("..");
      sb.append(length!= 0? Cadena.rellenar(String.valueOf(getMax().longValue()), length, '0', true)+ getMax().toString().substring(getMax().toString().indexOf(".")): getMax().toString());
    }
    sb.append("]");
    return sb.toString();
  }

  private String toCadena() {
    if(this.getMin().equals(Rango.NULO))
      return "nulo";
    else
      if(this.getMin().equals(Rango.BLANCO))
        return "blanco";
      else
        return "noespecificado";
  }

	
	@Override
  public String toString() {
    StringBuilder sb= new StringBuilder();
    sb.append("[");
    if(getMin().equals(Rango.NULO) || getMin().equals(Rango.BLANCO) || getMin().equals(Rango.NO_ESPECIFICADO))
      sb.append(toCadena());
    else {
      sb.append(getMin());
      sb.append("..");
      sb.append(getMax());
    } //
    sb.append("]");
    return sb.toString();
  }

  public List <Double> toValues() {
    List <Double> regresar= new ArrayList<>();
    Double x= getMin();
    while(x<= getMax()) {
      if(!getMin().equals(Rango.NULO) && !getMin().equals(Rango.BLANCO) && !getMin().equals(Rango.NO_ESPECIFICADO))
        regresar.add(x);
      x++;
    } // while
    return regresar;
  }

  public List <String> toValues(int length) {
    List <String> regresar= new ArrayList <> ();
    Double x= getMin();
    while(x<= getMax()) {
      if(getMin().equals(Rango.NULO) || getMin().equals(Rango.BLANCO) || getMin().equals(Rango.NO_ESPECIFICADO))
        regresar.add(toCadena());
      else
        regresar.add(Cadena.rellenar(String.valueOf(x.longValue()), length, '0', true)+ x.toString().substring(x.toString().indexOf('.')));
      x++;
    } // while
    return regresar;
  }

}
