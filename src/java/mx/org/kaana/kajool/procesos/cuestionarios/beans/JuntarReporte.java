package mx.org.kaana.kajool.procesos.cuestionarios.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/09/2015
 *@time 10:31:10 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.kajool.procesos.reportes.reglas.IJuntar;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;

public class JuntarReporte implements IJuntar, Serializable{
	
	private static final long serialVersionUID=-3210445171965129880L;
  private List<Definicion> definicion;
  private EReporte seleccionado;
	private String forzarRegresar;

	public JuntarReporte(List<Definicion> definicion, EReporte seleccionado) {
    this.definicion   = definicion;
    this.seleccionado = seleccionado;
  }

	public JuntarReporte(List<Definicion> definicion, EReporte seleccionado, String forzarRegresar) {
		this.definicion=definicion;
		this.seleccionado=seleccionado;
		this.forzarRegresar=forzarRegresar;
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
    return this.forzarRegresar!= null? this.forzarRegresar: this.seleccionado.getPaginaRegreso().concat(this.seleccionado.getPaginaRegreso().contains("?")? Constantes.REDIRECIONAR_AMPERSON: Constantes.REDIRECIONAR);
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
    return Boolean.FALSE;	
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
					Methods.clean(item.getParams());
					Methods.clean(item.getParametros());
				} // for 
			} // if
			Methods.clean(this.definicion);
		}
		finally {
			super.finalize();
		}
  }  
}
