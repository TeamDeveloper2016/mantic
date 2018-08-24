package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.kajool.procesos.reportes.reglas.IJuntar;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reportes.IReportAttribute;

public class JuntarReporte implements IJuntar, Serializable{
	
	private static final long serialVersionUID=-3210445171965129880L;
  private List<Definicion> definicion;
  private IReportAttribute seleccionado;
	private String forzarRegresar;
	private Boolean intercalar;
	private Boolean separados;

	public JuntarReporte(List<Definicion> definicion, IReportAttribute seleccionado) {
    this(definicion, seleccionado, null, false, false);
  }

	public JuntarReporte(List<Definicion> definicion, IReportAttribute seleccionado, String forzarRegresar) {
		this(definicion, seleccionado, forzarRegresar, false, false );
	}

  public JuntarReporte(List<Definicion> definicion, IReportAttribute seleccionado, String forzarRegresar, Boolean intercalar, Boolean separados) {
    this.definicion = definicion;
    this.seleccionado = seleccionado;
    this.forzarRegresar = forzarRegresar;
    this.intercalar = intercalar;
    this.separados = separados;
  }
  
	public String getForzarRegresar() {
		return forzarRegresar;
	}

	public void setForzarRegresar(String forzarRegresar) {
		this.forzarRegresar=forzarRegresar;
	}

  @Override
  public String getNombre() {
    return seleccionado.getNombre();    
  }

  @Override
  public String getRegresar() {
    return this.forzarRegresar!= null? this.forzarRegresar: this.seleccionado.getRegresar().concat(this.seleccionado.getRegresar().contains("?")? Constantes.REDIRECIONAR_AMPERSON: Constantes.REDIRECIONAR);
  }

  @Override
  public Boolean getComprimir() {
    return Boolean.TRUE;
  }

  @Override
  public List<Definicion> getDefiniciones() {
    return this.definicion;
  }

  @Override
  public Boolean getIntercalar() {
    return this.intercalar;	
  }

  @Override
  public Boolean getSeparar() {
    return separados;
  }

  @Override
  public Boolean getAutomatico() {
    return Boolean.TRUE;
  }

  @Override
  protected void finalize() throws Throwable {
		try {
			if(this.definicion!= null && !this.definicion.isEmpty()) {
				for(Definicion item: this.definicion) {
					clean(item.getParams());
					clean(item.getParametros());
				} // for 
			} // if
			clean(this.definicion);
		}
		finally {
			super.finalize();
		}
  }  
  
  public static Object clean(Object instancia) {
    try {
      if (instancia!= null) {
        if (instancia instanceof AbstractMap)
          ((AbstractMap)instancia).clear();
        else 
          if (instancia instanceof Collection)
            ((Collection)instancia).clear();    
          else
            if (instancia instanceof AbstractCollection)
              ((AbstractCollection)instancia).clear();
      } // if
    } // try
    catch ( Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
    } // catch
    return instancia;
  }

}
