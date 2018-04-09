package mx.org.kaana.kajool.procesos.mantenimiento.menus.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:34:12 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;

public class Consulta extends Menu implements Serializable {

	private static final long serialVersionUID=-3352363788067967671L;
	private static Consulta instance;
	private static Object mutex;

	static {
		mutex=new Object();
	}

	public Consulta() {
	}

	public List<String> loadImages() {
		List<String> regresar=null;
		File directory=null;
		File[] imagenes=null;
		try {
			regresar=new ArrayList<String>();
			directory=new File(JsfBase.getRealPath().concat(Constantes.RUTA_IMAGENES_MENU));
			if (directory.isDirectory()) {
				imagenes=directory.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.getName().toLowerCase().endsWith(".jpg")||file.getName().toLowerCase().endsWith(".png");
					} // public boolean
				});
				for (File imagen : imagenes) {
					regresar.add(imagen.getName());
				} // for
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar;
	}
}
