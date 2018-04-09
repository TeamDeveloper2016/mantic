package mx.org.kaana.kajool.procesos.reportes.beans;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.procesos.reportes.reglas.IBaseExporter;
import mx.org.kaana.kajool.procesos.reportes.reglas.IFoxPro;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Exportar implements IFoxPro, Serializable{
  
  private static final long serialVersionUID = 7162455234313188179L;
  private List<Modelo> definiciones;
	private IBaseExporter catalogo;
	private Boolean validate;
	private Boolean asignarNombreDbf;

	public Exportar(IBaseExporter catalogo, List<Modelo> exportacion) {
		this(catalogo, exportacion, false, false);
	}
	
	public Exportar(IBaseExporter catalogo, List<Modelo> exportacion, boolean validate, boolean asignarNombreDbf) {
		this.catalogo        = catalogo;
		this.definiciones    = exportacion;
		this.validate        = validate;
		this.asignarNombreDbf= asignarNombreDbf;
	}

	public IBaseExporter getCatalogoEscuelas() {
		return catalogo;
	}

	@Override
	public String getNombre() {
		return this.catalogo.getArchivoZip();
	}

	@Override
	public String getRegresar() {
		return this.catalogo.getPaginaRegreso();
	}

	@Override
	public Boolean getComprimir() {
		return Boolean.TRUE;
	}

	@Override
	public List<Modelo> getDefiniciones() {
		return this.definiciones;
	}

	@Override
	public String getNombreDBF(Integer index) {
		return this.catalogo.getDbfFile()[index];
	}

	@Override
	public String getPatron() {
		return this.catalogo.getPatron();
	}
	
	@Override
	public Boolean getValidate() {
		return this.validate;
	}	
	
	@Override
	public Boolean getAsignarNombreDbf() {
		return this.asignarNombreDbf;
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		for (Modelo definicion : this.definiciones) 
			Methods.clean(definicion.getParams());		
		Methods.clean(this.definiciones);
	} // finalize 		
}
