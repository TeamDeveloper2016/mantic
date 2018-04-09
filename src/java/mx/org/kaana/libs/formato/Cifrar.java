package mx.org.kaana.libs.formato;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/03/2015
 *@time 01:51:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Cifrar {
	
	private Cifrar() {
	}
	
	public static String descifrar(String cadena) throws Exception{
		int tamanio       = -1;
		String regresar   = null;
		char auxiliar     = ' ';
		char caracteres[] = null;
		try {
			tamanio=cadena.length();
			regresar= "";
			for(int i=1;i<tamanio;i++){
				if((i%2)==0){
					regresar=regresar+cadena.charAt(i-1);
				} // if
			} // for
			tamanio= regresar.length();
			auxiliar = regresar.charAt(0);
			caracteres = regresar.toCharArray();
			caracteres[0]=caracteres[tamanio-1];
			caracteres[tamanio-1]=auxiliar;
			regresar="";
			for(int i=0;i<caracteres.length;i++){
				regresar=regresar+caracteres[i];
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	}
	
	public static String cifrar(String cadena) throws Exception{
		int tamanio      = -1;
		int	numeroAscii  = -1;
		String regresar  = null;
		char auxiliar    = ' ';
		char caracteres[]= null;
		try {
			tamanio=cadena.length();
			regresar= "";
			caracteres=cadena.toCharArray();
			auxiliar=caracteres[0];
			caracteres[0]= caracteres[tamanio-1];
			caracteres[tamanio-1]=auxiliar;
			numeroAscii=122;
			for(int i=1;i<=tamanio;i++){
				if((i%2)!=0){
					regresar=regresar+ ((char)numeroAscii)+ caracteres[i-1];
					numeroAscii--;
					regresar=regresar+ ((char)numeroAscii);
					numeroAscii--;
				} // if
				else{
					regresar=regresar+caracteres[i-1];
				} // else
			}
			if (tamanio% 2 ==0)
				regresar=regresar+((char)numeroAscii);	
			} // try
			catch (Exception e) {
				throw e;
			} // catch
		return regresar;
	}
	
}
