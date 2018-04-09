package mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.reglas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.formato.Error;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/12/2014
 *@time 11:26:41 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Generador {

	private Map<String,Object> variables;
	private String nombreArchivo;
	private String rutaDestino;
	private String patron;
	private String textoBacking;
	private String textoPagina;
	private String textoTransaccion;
	private String textoBean;
	private String pagina;
	private String backing;
	private String transaccion;
	private boolean crearBean;
	private static final String RUTA_PAGINA		= "web/Paginas/";
	private static final String RUTA_BACKING	= "src/java/mx/org/kaana/";
	private static final String RUTA_TEMPLATES= "/mx/org/kaana/libs/";

	public Generador() {
		this(Collections.EMPTY_MAP);
	}
	
	public Generador(Map<String, Object> variables) {
		this(null, null, variables, null, null);
	}
	
	public Generador(String nombreArchivo, String rutaDestino, Map<String, Object> variables, String pagina, String backing) {
		this(nombreArchivo, rutaDestino, variables, pagina, backing, null,false);
	}
	
	public Generador(String nombreArchivo, String rutaDestino, Map<String, Object> variables, String pagina, String backing, String transaccion, boolean crearBean) {
		this.nombreArchivo = nombreArchivo;
		this.rutaDestino	 = rutaDestino;
		this.variables		 = variables;
		this.pagina        = pagina;
		this.backing       = backing;
		this.transaccion   = transaccion;
		this.crearBean     = crearBean;
	}

  public String getTextoBean() {
    return textoBean;
  }

	public String getTextoTransaccion() {
		return textoTransaccion;
	}

	public String getPatron() {
		return patron;
	}
	
	public String getTextoBacking() {
		return textoBacking;
	}

	public String getTextoPagina() {
		return textoPagina;
	}
	
	public List<String> getParametros(String codigo){
		List<String> regresar			= null;
		StringTokenizer resultado = null;
		String token							= null;
		try {
			regresar = new ArrayList<>();
			resultado = new StringTokenizer(codigo, "{}", true);
			while (resultado.hasMoreTokens()) {
				token = resultado.nextToken();
				if(token.equals("{"))
					regresar.add(resultado.nextToken());
			} // while
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return regresar;
	} // getParametros
	
	public String getSentencia(String codigo, Map<String, Object> variables) throws Exception{
		try {
			this.variables = variables;
		} // try
		catch (Exception e) {
			throw  e;
		} // catch
		return getSentencia(codigo);
	} // getSentencia
	
	private String getSentencia(String codigo) throws Exception{
    StringTokenizer resultado = null;
    StringBuilder sentencia		= null;
		String token							= null;
    Object valor							= null;
    try {
			resultado = new StringTokenizer(codigo, "{}", true);
			sentencia = new StringBuilder();
			while (resultado.hasMoreTokens()) {
				token = resultado.nextToken();
				if(token.equals("{") && resultado.hasMoreTokens()) {
					token = resultado.nextToken();
					if(!token.equals("{")) {
						valor = variables.get(token);
						if (valor != null) {
							sentencia.append(valor);
							resultado.nextToken();
						} // if
						else
							sentencia.append("{".concat(token));
					} // if
					else {
						sentencia.append(token);
						token = resultado.nextToken();
						valor = variables.get(token);
						if (valor != null)
							sentencia.append(valor);
						else
							sentencia.append(token);
						resultado.nextToken();
					} // else
				} // if
				else
					sentencia.append(token);
			} // while
		} // try
		catch (Exception e) {
			throw  e;
		} // catch
		finally {
			Methods.clean(resultado);
		} // finally
    return sentencia!= null? sentencia.toString(): "";
  } // getSentencia
	
	public void generar() {
		String contenido = null;
		try {
			this.patron = Archivo.toFormatNameFile("");
			contenido = leerArchivo(RUTA_TEMPLATES, this.pagina);
			escribirArchivo(contenido, RUTA_PAGINA);
			contenido = leerArchivo(RUTA_TEMPLATES, this.backing);
			escribirArchivo(contenido, RUTA_BACKING);
			if(this.transaccion!=null){
				contenido= leerArchivo(RUTA_TEMPLATES, this.transaccion);
				escribirArchivo(contenido,"");
			} // if
      if(crearBean){
        contenido= crearContenidoBean();
        escribirArchivo(contenido,"bean");
      } // if
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	} // generar
	
	private String leerArchivo(String rutaTemplates, String nombreTemplate) {
		return leerArchivo(rutaTemplates.concat(nombreTemplate));
	}
	
	protected String leerArchivo(String rutaArchivo) {
		StringBuilder regresar	= null;
		BufferedReader br				= null;
		String line							= "";
		try {
			br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(rutaArchivo)));
      regresar = new StringBuilder();
			while(line != null) {
        line = br.readLine();
        if (line != null)
          regresar.append(getSentencia(line)).append((char)10);
      } // while
      br.close();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar!=null?regresar.toString():"";
	} // leerArchivo
	
	protected void escribirArchivo(String contenido, String ruta) {
		FileWriter writer = null;
		File dir					= null;
		String path				= null;
		String nombre			= null;
		String fileName   = null;
    try {
			//Guarda Archivo en Pc
			path = JsfUtilities.getRealPath().substring(0, JsfUtilities.getRealPath().indexOf("build")).concat((ruta.equals("bean")|| ruta.equals(""))?RUTA_BACKING:ruta).concat(ruta.equals(RUTA_PAGINA)?rutaDestino:rutaDestino.toLowerCase());
      if(path.indexOf("/kajool/")!=-1)
        path= path.replaceAll("/kajool/", "/kajool/procesos/");
      dir = new File(path.concat(ruta.equals(RUTA_BACKING)?"/backing":ruta.equals(RUTA_PAGINA)?"":ruta.equals("bean")?"/beans":"/reglas"));
      dir.mkdirs();
			nombre = ruta.equals(RUTA_PAGINA)?Cadena.toBeanNameNemonico(this.nombreArchivo).concat(".xhtml"):(ruta.equals("")?"Transaccion":ruta.equals("bean")?variables.get("nombreBean").toString():this.nombreArchivo).concat(".java");
      writer = new FileWriter(path.concat(ruta.equals(RUTA_BACKING)?"/backing/":ruta.equals(RUTA_PAGINA)?"/":ruta.equals("bean")?"/beans/":"/reglas/").concat(nombre));
      writer.write(contenido);
      writer.flush();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
			//Guarda archivo en temporal para descargar
     fileName= this.patron.concat(ruta.equals(RUTA_PAGINA)?"Pagina":ruta.equals(RUTA_BACKING)?"Backing":ruta.equals("bean")?"Bean":"Transaccion");
			if(ruta.equals(RUTA_PAGINA)) {
				this.textoPagina=contenido;
				nombre=fileName.concat(".").concat(EFormatos.XHTML.name().toLowerCase());
			} // if
			else if(ruta.equals(RUTA_BACKING)) {
				this.textoBacking=contenido;
				nombre=fileName.concat(".").concat("java");
			} // else if
      else if(ruta.equals("bean")){
				this.textoBean=contenido;
				nombre=fileName.concat(".").concat("java");
			}
      else{
				this.textoTransaccion=contenido;
				nombre=fileName.concat(".").concat("java");
			}
			nombre= Cadena.reemplazarCaracter(nombre, '/' , File.separatorChar);
			path = JsfUtilities.getRealPath(EFormatos.TXT.toPath()).concat(File.separator);
      try{
        dir= new File(JsfUtilities.getRealPath().concat("Temporal"));
        if(!dir.exists())
          dir.mkdir();
        dir= new File(JsfUtilities.getRealPath().concat("Temporal").concat(File.separator).concat("Txt"));
        if(!dir.exists())
          dir.mkdir();
        writer = new FileWriter(path.concat(nombre));
        writer.write(contenido);
        writer.flush();
        writer.close();
      } // try
      catch(IOException e){
        Error.mensaje(e);
      }
			Methods.clean(writer);
    } // finally
	} // escribirArchivo

  private String crearContenidoBean(){
    String regresar = "";
    regresar=leerArchivo(RUTA_TEMPLATES, "ClaseBean.txt");
    return regresar;
  } // crearContenidoBean
}
