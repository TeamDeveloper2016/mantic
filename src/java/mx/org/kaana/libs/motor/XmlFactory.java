package mx.org.kaana.libs.motor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Formatos;
import mx.org.kaana.xml.Dml;
import mx.org.kaana.xml.Modulos;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 19, 2012
 *@time 12:04:20 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class XmlFactory {

  private String model;
  private String dml;
  private String nombreDto;
  private String archivoXml;
  private static final String FILE_DINAMICOS = "/mx/org/kaana/kajool/db/kajool*/dinamicos.xml";
  private static final String CLASSES        = "classes";
  private static final String DML            = "{dml}";
  private static final String MODEL          = "{model}";

  private static String TEMPLATE_FILE;

  private boolean inicio;

  static {
    TEMPLATE_FILE  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    TEMPLATE_FILE += "<process>\n";
    TEMPLATE_FILE += "<model>\n";
    TEMPLATE_FILE += "{model}\n";
    TEMPLATE_FILE += "TEMP_MODEL\n";
    TEMPLATE_FILE += "</model>\n";
    TEMPLATE_FILE += "<dml>\n";
    TEMPLATE_FILE += "{dml}\n";
    TEMPLATE_FILE += "TEMP_DML\n";
    TEMPLATE_FILE += "</dml>\n";
    TEMPLATE_FILE += "</process>\n";
  }

  public XmlFactory(String nombreDto) {
    this.nombreDto = nombreDto;
  }

  public XmlFactory(String model, String dml) {
    this(model, dml, false);
  }

  public XmlFactory(String model, String dml, boolean inicio) {
    this(model, dml, FILE_DINAMICOS);
    this.inicio = inicio;
  }

  public XmlFactory(String model, String dml, String archivoXml) {
    this.model      = model;
    this.dml        = dml;
    this.archivoXml = archivoXml;
  }

  public void reload() {
    Dml.getInstance().reload();
  }

  public void write() {
    HashMap<String, String> properties = null;
    Formatos formatos    = null;
    StringBuilder lectura= null;
    String line          = null;
    BufferedReader br    = null;
    InputStream is       = null;
    try {
      lectura   = new StringBuilder();
      properties= new HashMap<String,String>();
      is        = this.getClass().getResourceAsStream(this.archivoXml);
      br        = new BufferedReader(new InputStreamReader(is));
      line      = "";
      if(!this.inicio) {
        properties.put("model", this.model.concat(" TEMP_MODEL"));
        properties.put("dml", this.dml.concat(" TEMP_DML"));
        properties.put("condicion", "{condicion}");
        while(line != null) {
          line = br.readLine();
          if (line != null) {
            formatos = new Formatos(line, properties);
            lectura.append(formatos.getSentencia()).append("\n");
          } // if
          formatos = null;
        } // while
      } // if
      else {
        properties.put("model", this.model);
        properties.put("dml", this.dml);
        //properties.put("condicion", this.dml.concat(" {condicion}"));
        formatos = new Formatos(TEMPLATE_FILE, properties);
        lectura.append(formatos.getSentencia()).append("\n");
      }
      br.close();
      String toFile = lectura.toString();
      toFile = toFile.replaceAll("TEMP_MODEL", MODEL);
      toFile = toFile.replaceAll("TEMP_DML", DML);
//      fileWrite= new FileWriter(file);
//      fileWrite.write(toFile);
//      fileWrite.flush();
      /***
       * Escribir a memoria fragmento de XML generado       *
       */
      Modulos modulo = new Modulos(Dml.getInstance().getDocumento(), null);
      modulo.toBuildString(toFile);
      modulo.toString();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      formatos= null;
    } // fianlly
  }

  public void delete() {
    File file            = null;
    FileReader fileReader= null;
    FileWriter fileWrite = null;
    BufferedReader br    = null;
    StringBuffer lectura = null;
    String line          = null;
    boolean excluir      = false;
    String path          = null;
    try {
      path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      path = path.replace(":", "");
      path = path.substring(0, path.indexOf(CLASSES) + CLASSES.length());
      file      = new File(path.concat(this.archivoXml));
      fileReader= new FileReader(file);
      br        = new BufferedReader(fileReader);
      lectura   = new StringBuffer();
      line      = "";
      while(line!=null) {
        line=br.readLine();
        if(line!=null) {
          if(line.contains(this.nombreDto))
            excluir=true;
          if(!(excluir)) {
            lectura.append(line);
            lectura.append("\n");
          } // if
          if(excluir&&(line.contains("</model>")||line.contains("</dml>")))
            excluir=false;
        } // if
      } // while
      br.close();
      fileWrite=new FileWriter(file);
      fileWrite.write(lectura.toString());
      fileWrite.flush();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      fileReader=null;
    } // fianlly
  }

}
