package mx.org.kaana.libs.recurso;

import java.io.File;
import java.io.FileFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/06/2018
 *@time 05:09:52 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class LoadImages {

	public static String getImage(String name) {
		String regresar= "";
		File file= toListFile(name);
		if(file== null || !file.exists() || file.isDirectory())
		  regresar= JsfBase.getRealPath("/resources/janal/img/sistema/bonanza.svg");
		else
			regresar= file.getAbsolutePath();
		return regresar;
	} 
	
	private static File toListFile(String name) {
		File dir = new File(Configuracion.getInstance().getPropiedadSistemaServidor("path.image"));
		FileFilter fileFilter = new WildcardFileFilter(name+ "*.jpg", IOCase.INSENSITIVE);
		File[] files = dir.listFiles(fileFilter);
		return files== null || files.length<= 0? new File(Configuracion.getInstance().getPropiedadSistemaServidor("path.image")+ name+ ".jpg"): files[0];
	}
	
}
