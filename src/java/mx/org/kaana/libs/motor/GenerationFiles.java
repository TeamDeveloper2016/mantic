package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 2, 2012
 *@time 4:00:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Formatos;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class GenerationFiles {

  private final Log LOG= LogFactory.getLog(GenerationFiles.class);
  private String pk;
  private Map<String, RenglonCampo> detailTable;
  private Map<String, RenglonCampo> detailUnique;
  private String nombreTabla;
  private Map<String, String> properties;
  private String paquete;
  private String nombreVariable;
  private String escritura;
  private String fileName;
  private String variable;
  private String query;
  private String llavePrimaria;	
  private boolean nemonico;
  private boolean encuesta;
  private boolean sinonimo;
  private boolean asistente;
  private boolean dao;
  private Long idFuenteDato;
  private boolean tablaDto;
	private String nombreEncuesta;

  public GenerationFiles(String nombreTabla, String query, boolean nemonico, String nombreEncuesta) {
    this.nombreTabla   = nombreTabla;
    this.query         = query;
		this.nombreEncuesta= nombreEncuesta;
	}

  public GenerationFiles(String nombreTabla, String paquete, boolean sinonimo, String llavePrimaria, boolean nemonico, boolean dao, String nombreEncuesta) throws Exception {
    setEncuesta(false);
    setSinonimo(sinonimo);
		setNemonico(nemonico);
		setNombreEncuesta(nombreEncuesta);
    setDao(dao);
    setNombreTabla(nombreTabla);
    setPaquete(paquete);
    setDetailFields();
    setLlavePrimaria(llavePrimaria);
    setNamePk();
    setUniqueKey();
    setProperties(new HashMap<String, String>());
    this.tablaDto = true;
    loadProperties();
  }

  public GenerationFiles(String nombreTabla, String paquete, String query, String llavePrimaria, boolean sinonimo, boolean nemonico, String nombreEncuesta) throws Exception {
    this(nombreTabla, paquete, query, llavePrimaria, sinonimo, false, nemonico, nombreEncuesta);
  }

  public GenerationFiles(String nombreTabla, String query, String paquete, String llavePrimaria, boolean sinonimo, boolean isAsistente, boolean nemonico, String nombreEncuesta) throws Exception {
    this(nombreTabla, query, paquete, llavePrimaria, sinonimo, isAsistente, -1L, true, nemonico, nombreEncuesta);
  }

  public GenerationFiles(String nombreTabla, String query, String paquete, String llavePrimaria, boolean sinonimo, boolean isAsistente, Long idFuenteDato, boolean tablaDto, boolean nemonico, String nombreEncuesta) throws Exception {
    setEncuesta(false);
    setSinonimo(sinonimo);
		setNemonico(nemonico);
    setQuery(query);
    setIdFuenteDato(idFuenteDato);
    setLlavePrimaria(llavePrimaria);
    setNombreTabla(nombreTabla);
    setPaquete(paquete);
		setNombreEncuesta(nombreEncuesta);
    setDetailFields();
    setNamePk();
    setProperties(new HashMap<String, String>());
    setAsistente(isAsistente);
    setTablaDto(tablaDto);
    loadProperties();
  }

  public GenerationFiles(String nombreTabla, String query, String paquete, boolean sinonimo, boolean nemonico, String nombreEncuesta) throws Exception {
    setEncuesta(true);
    setSinonimo(sinonimo);
		setNemonico(nemonico);
		setNombreEncuesta(nombreEncuesta);
    setQuery(query);
    setNombreTabla(nombreTabla);
    setPaquete(paquete);
    setDetalleCampos();
    setNamePk();
    setProperties(new HashMap<String, String>());
    loadProperties();
  }

  public boolean isTablaDto() {
    return tablaDto;
  }

  public void setTablaDto(boolean tablaDto) {
    this.tablaDto=tablaDto;
  }

	public boolean isNemonico() {
		return nemonico;
	}

  public boolean isDao() {
    return dao;
  }

  public void setDao(boolean dao) {
    this.dao = dao;
  }

	public void setNemonico(boolean nemonico) {
		this.nemonico=nemonico;
	}

  public void setNombreTabla(String nombreTabla) {
    this.nombreTabla = nombreTabla;
  }

  public String getNombreTabla() {
    return nombreTabla;
  }

	public String getNombreEncuesta() {
		return nombreEncuesta;
	}

	public void setNombreEncuesta(String nombreEncuesta) {
		this.nombreEncuesta=nombreEncuesta;
	}

  public void setPaquete(String paquete) {
    this.paquete = getNamePackage(paquete);
  }

  public String getPaquete() {
    return paquete;
  }

  public void setPk(String pk) {
    this.pk = pk;
  }

  public String getPk() {
    return pk;
  }

  public void setDetailTable(Map<String, RenglonCampo> detailTable) {
    this.detailTable = detailTable;
  }

  public Map<String, RenglonCampo> getDetailTable() {
    return detailTable;
  }

  public Map<String, RenglonCampo> getDetailUnique() {
    return detailUnique;
  }

  public void setDetailUnique(Map<String, RenglonCampo> detailUnique) {
    this.detailUnique = detailUnique;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setNombreVariable(String nombreVariable) {
    this.nombreVariable = nombreVariable;
  }

  public String getNombreVariable() {
    return nombreVariable;
  }

  public void setEscritura(String escritura) {
    this.escritura = escritura;
  }

  public String getEscritura() {
    return escritura;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public String getVariable() {
    return variable;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setLlavePrimaria(String llavePrimaria) {
    this.llavePrimaria = llavePrimaria;
  }

  public String getLlavePrimaria() {
    return llavePrimaria;
  }

  public void setSinonimo(boolean sinonimo) {
    this.sinonimo = sinonimo;
  }

  public boolean isSinonimo() {
    return sinonimo;
  }

  public void setAsistente(boolean asistente) {
    this.asistente = asistente;
  }

  public boolean isAsistente() {
    return asistente;
  }

  public void setIdFuenteDato(Long idFuenteDato) {
    this.idFuenteDato = idFuenteDato;
  }

  public Long getIdFuenteDato() {
    return idFuenteDato;
  }

  protected abstract void setNamePk() throws Exception;

  protected abstract void setUniqueKey() throws Exception;

  protected abstract void setDetailFields() throws Exception;

  protected void setDetailFields(String consulta) throws Exception {
    Map<String, RenglonCampo> regresar= new HashMap<String, RenglonCampo>();
    Map<String, Integer>      auxiliar= new HashMap<String, Integer>();
    Connection connection = null;
    Statement stm         = null;
    ResultSet rst         = null;
    ResultSetMetaData rsmd= null;
    String nameColumna    = null;
    String typeData       = null;
    try {
      connection= DaoFactory.getInstance().getConnection(getIdFuenteDato());
      stm       = connection.createStatement();
      rst       = stm.executeQuery(consulta);
      rsmd      = rst.getMetaData();
      for(int x= 1; x <= rsmd.getColumnCount(); x++) {
        nameColumna= rsmd.getColumnName(x);
        typeData   = rsmd.getColumnTypeName(x);
				
        if((rsmd.getScale(x)> 0)&&(!typeData.equals("TIMESTAMP"))) {
          typeData = "DOUBLE";
        } // if
        regresar.put(nameColumna, new RenglonCampo(nameColumna, typeData, auxiliar, isNemonico()));
      } // for x
      regresar= actualizarAtributosDuplicados(regresar, auxiliar);
    }
    catch(Exception e) {
      Error.mensaje(e);
    } // try
    finally {
      if(rst != null)
        rst.close();
      rst=null;
      if(stm != null)
        stm.close();
      stm=null;
      if(connection != null)
        connection.close();
      connection=null;
      if(auxiliar!= null)
        auxiliar.clear();
      auxiliar= null;
    } // finally
    setPaquete(getPaquete().replace(".", "/"));
    setDetailTable(regresar);
  }

  private Map<String, RenglonCampo> actualizarAtributosDuplicados(Map<String, RenglonCampo> regresar, Map<String, Integer> auxiliar) {
    RenglonCampo renglon= null;
    Integer ascii       = 96;
    Integer contador    = 0;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: regresar.keySet()) {
        renglon = regresar.get(key);
        contador= auxiliar.get(renglon.getAtributo());
        if(contador> 0) {
          auxiliar.put(renglon.getAtributo(), contador- 1);
          renglon.setAtributo(renglon.getAtributo().concat(String.valueOf((char)(contador+ ascii))));
          regresar.put(key, renglon);
        } // if
      } // for
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // try
    return regresar;
  }

  protected abstract void setDetalleCampos() throws Exception;

  protected void setDetalleCampos(String consulta) throws Exception {
    Map<String, RenglonCampo> regresar= new HashMap<>();
    Map<String, Integer>      auxiliar= new HashMap<>();
    Connection connection = null;
    Statement stm         = null;
    ResultSet rst         = null;
    String typeData       = null;
    try {
      connection = DaoFactory.getInstance().getConnection();
      stm = connection.createStatement();
      rst = stm.executeQuery(consulta);
      //rst.first();
      typeData = "VARCHAR2";
      String nombre = null;
      while (rst.next()) {
        nombre = rst.getString("mnemonico");
        regresar.put(nombre, new RenglonCampo(nombre, typeData, auxiliar, isNemonico()));
      } // while
    }
    catch(Exception e) {
      Error.mensaje(e);
    } // try
    finally {
      if(rst != null)
        rst.close();
      rst=null;
      if(stm != null)
        stm.close();
      stm=null;
      if(connection != null)
        connection.close();
      connection=null;
      if(auxiliar!= null)
        auxiliar.clear();
      auxiliar= null;
    } // finally
    setPaquete(getPaquete().replace(".", "/"));
    setDetailTable(regresar);
  }

  protected abstract String getParse(String typeFile) throws Exception;

  /*protected String getParse(String typeFile, String pakageName) throws Exception {
    FileReader fileReader= null;
    StringBuilder lectura = new StringBuilder();
    Formatos formatos    = null;
    final String RUTA_CLASES = "build";
    try {
      if(isEncuesta())
        pakageName = "Encuesta";
      String binPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
      binPath= binPath.replace("file:","");
      binPath = binPath.substring(0, binPath.indexOf(RUTA_CLASES)).concat("src/java");
      binPath = binPath.concat("/mx/org/kaana/libs/".concat(pakageName)).concat(typeFile).concat(".txt");
      fileReader = new FileReader (new File(binPath));
      BufferedReader br = new BufferedReader (fileReader);
      String line = "";
      while(line != null) {
        line = br.readLine();
        if (line != null) {
          formatos = new Formatos(line, getProperties());
          lectura.append(formatos.getSentencia()).append("\n");
        } // if
        formatos = null;
      } // while
      br.close();
    }
    catch (Exception e) {
      Error.mensaje(e);
    }
    finally {
      formatos   = null;
      fileReader = null;
    } // try
    return lectura.toString();
  }
  */

  protected String getParse(String typeFile, String pakageName) throws Exception {
    StringBuilder lectura = new StringBuilder();
    Formatos formatos    = null;
    final String RUTA_CLASES = "build";
    try {
      if(isEncuesta())
      pakageName = "Encuesta";
      BufferedReader br =   new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/mx/org/kaana/libs/".concat(pakageName).concat(typeFile).concat(".txt"))));
      String line = "";
      while(line != null) {
        line = br.readLine();
        if (line != null) {
          formatos = new Formatos(line, getProperties());
          lectura.append(formatos.getSentencia()).append("\n");
        } // if
        formatos = null;
      } // while
      br.close();
    }
    catch (Exception e) {
      Error.mensaje(e);
    }
    finally {
      formatos   = null;
    } // try
    return lectura.toString();
  }

  public void loadProperties() {

    getProperties().put("paquete",getPaquete().replace("/","."));
    setVariable( this.tablaDto ? Cadena.toClassNameEspecial(getNombreTabla()): getNombreTabla().toUpperCase());
    getProperties().put("nombreClaseDto", getVariable().concat("Dto"));
    getProperties().put("nombreClaseDao", getVariable().concat("Dao"));


    getProperties().put("direccionPojo", getPaquete().replace("/",".").concat(".dto.").concat(getVariable().concat("Dto")));
    getProperties().put("tabla", isVista() ? "" :getNombreTabla());

    getProperties().put("atributos", getAttributes());
    getProperties().put("valoresInicio", getStartValues());

    getProperties().put("tipoNombreVariables", getTypeNameVariables());
    getProperties().put("setVariables", getSetVariables());
    getProperties().put("setyget", getGettersAndSetters());


    getProperties().put("metodoToString", getToString());
    getProperties().put("metodoToMap", getToMap());
    getProperties().put("metodoToArray", getToArray());

    if(!isEncuesta()) {
      getProperties().put("PkTypeVariable", isNemonico() ? Cadena.toBeanNameNemonico(getPk()) : Cadena.toBeanNameEspecial(getPk()));
    } // if

    getProperties().put("PkTypeClass", isNemonico() ? Cadena.toClassNameNemonico(getPk()) : Cadena.toClassNameEspecial(getPk()));

    if(isEncuesta()){
      getProperties().put("mapaVariables", getMapaVariablesEncuesta());
    } // if

    // LLAVE DE INICIO
    getProperties().put("llaveInicial", "{");

    // LLAVE DE CIERRE
    getProperties().put("llaveCierre", "}");

    // SEQUENCE
    //getProperties().put("sequence", ("SEQ_").concat(getNombreTabla().toUpperCase()));
    getProperties().put("nombreTabla", getNombreTabla().toUpperCase());
  }

  protected String getPathCfg() {
    String paq = getPaquete().concat(".dto.");
    return ("<mapping class=\"").concat((paq).concat(Cadena.toClassNameEspecial(getVariable())).concat("Dto")).concat("\"/>");
  }

  public String getName(String typeFile, String extension) {
    if ( this.isTablaDto() )
      setVariable( Cadena.toClassNameEspecial(getNombreTabla()));
    else
      setVariable(  getNombreTabla());
    return getVariable().concat(typeFile).concat(extension);
  }

  public void toWrite(String typeFile, boolean localKajool) throws IOException {
    FileWriter writer= null;
    String binPath = "";
    final String RUTA_CLASES = "build";
    try {
      binPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
      binPath = binPath.replace("file:", "");
      binPath = binPath.substring(0, binPath.indexOf(RUTA_CLASES)).concat("src/java/");
      if(isEncuesta())
        binPath = binPath.concat(getPaquete()).concat("/");
      else
        binPath = binPath.concat(getPaquete()).concat("/").concat(typeFile).concat("/");
      File dir = new File(binPath);
      dir.mkdirs();
      if(localKajool)
        binPath= binPath.replace("src/java", this.nombreEncuesta.equals("No aplica")?"/src":this.nombreEncuesta.toUpperCase().concat("/src"));
      else
        binPath= !(this.nombreEncuesta.equals("No aplica"))?binPath.replace("/kajool/", "/".concat(this.nombreEncuesta).concat("/")):binPath;
      writer = new FileWriter(binPath.concat(getFileName()));
      writer.write(getEscritura());
      if (writer!= null)
        writer.flush();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
       if (writer!= null)
         writer.close();
       writer = null;
    } // finally
  }

  public void toWriteAsistente(String typeFile) throws IOException {
    FileWriter writer= null;
    String binPath = "";
    try {
      binPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString().concat(getPaquete()).concat("/").concat(typeFile).concat("/");
      binPath = binPath.replace("file:", "");
      File dir = new File(binPath);
      dir.mkdirs();
      writer = new FileWriter(binPath.concat(getFileName()));
      writer.write(getEscritura());
      if (writer!= null)
        writer.flush();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
       if (writer!= null)
         writer.close();
       writer = null;
    } // finally
  }

  private boolean isVista() {
    return getVariable().concat("Dto").toUpperCase().startsWith("VISTA");
  }

  private String toKeyAndSequence(String keyAttribute) {
    StringBuilder sb = new StringBuilder();
    String nameSeq   = keyAttribute.concat("_sequence");

    sb.append("@Id\n");
    if ( !isVista() ) {
      sb.append("  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)\n	");      
    } // if
    return sb.toString();
  }

  private String getAttributes() {
    StringBuilder sb     = new StringBuilder();
    RenglonCampo renglon= null;
    int x               = 1;
    String id           = "";
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {				
        renglon = getDetailTable().get(key);
        if ( renglon.getCampo().equalsIgnoreCase(getPk()))
          sb.append(toKeyAndSequence(renglon.getAtributo()));
				if(renglon.getTipo().equals("Blob"))
					sb.append("@Lob\n  ");
        sb.append(MessageFormat.format(id.concat("@Column (name=\"{0}\"){1}"), new Object[]{renglon.getCampo(), "\n"}));
        sb.append(MessageFormat.format("  private {0} {1};", new Object[]{renglon.getTipo(), renglon.getAtributo()}));
        if(x< getDetailTable().size())
           sb.append("\n  ");
        x++;
      } // for
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }


  private String getCodeProperties() {
    StringBuilder sb     = new StringBuilder();
    RenglonCampo renglon= null;
    int x               = 1;
    try {
     // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon = getDetailTable().get(key);
        if (!(renglon.getCampo().equals(getPk()))) {
          sb.append(MessageFormat.format("<property column=\"{0}\" name=\"{1}\"/>",new Object[]{renglon.getCampo(), renglon.getAtributo()}));
          if(x< getDetailTable().size())
            sb.append("\n\t\t");
        } // if
        x++;
      } // for
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getSetVariables() {
    StringBuilder sb     = new StringBuilder();
    int x               = 1;
    RenglonCampo renglon= null;
    try {
     // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        if (renglon.getCampo().equalsIgnoreCase("registro"))
          sb.append(MessageFormat.format("set{0}({1});", new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())), "new Timestamp(Calendar.getInstance().getTimeInMillis())"}));
        else
          sb.append(MessageFormat.format("set{0}({1});", new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())) , renglon.getAtributo()}));
        if(x< getDetailTable().size())
          sb.append("\n    ");
        x++;
      } // for x
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getGettersAndSetters() {
    StringBuilder sb     = new StringBuilder();
    int x               = 1;
    RenglonCampo renglon= null;
    try {
     // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        if (x!= 1)
          sb.append("  ");
        // SET
        sb.append(MessageFormat.format("public void set{0}({1} {2}) ",new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())), renglon.getTipo(), renglon.getAtributo()})).append("{\n");
        sb.append(MessageFormat.format("    this.{0} = {0};\n",new Object[]{renglon.getAtributo()}));
        sb.append(MessageFormat.format("  }\n\n",new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())), renglon.getTipo(),variable}));
        // GET
        sb.append(MessageFormat.format("  public {1} get{0}() ",new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())), renglon.getTipo()})).append("{\n");
        sb.append(MessageFormat.format("    return {0};\n",new Object[]{renglon.getAtributo()}));
        sb.append("  }");
        if (x< getDetailTable().size())
          sb.append("\n\n");
        x++;
      } // while
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getMapaVariablesEncuesta(){
    StringBuilder sb     = new StringBuilder();
    RenglonCampo renglon= null;
    try{
      for(String key: getDetailTable().keySet()){
        renglon= getDetailTable().get(key);
        sb.append(MessageFormat.format("mapVariables.put(\"{1}\",get{0}());\n",new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length())), renglon.getAtributo()}));
      }
      sb.append("\ndtoFiller.llenarDto(mapDto, mapVariables, compuestoFolios, encuesta);");
    } // try
     catch (Exception e) {
       Error.mensaje(e);
     } // try
     return sb.toString();
  }

  private String getStartValues() {
    String typeData      = null;
    StringBuilder regresar= new StringBuilder();
    RenglonCampo renglon = null;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        if (!renglon.getCampo().equalsIgnoreCase("registro")) {
          if (!(regresar.length()== 0))
            regresar.append(", ");
          if (renglon.getTipo().equals("Long") && renglon.getCampo().equals(getPk()))
            typeData= "new Long(-1L)";
          else if (renglon.getTipo().equals("String") || renglon.getTipo().equals("Char"))
            typeData= "null";
          else if (renglon.getTipo().equals("Date"))
            typeData= "new Date(Calendar.getInstance().getTimeInMillis())";
          else if (renglon.getTipo().equals("Timestamp"))
            typeData= "new Timestamp(Calendar.getInstance().getTimeInMillis())";
          else if (renglon.getTipo().equals("Time"))
            typeData= "new Time(Calendar.getInstance().getTimeInMillis())";
          else if (renglon.getTipo().equals("Long"))
            typeData= "null";
          else if (renglon.getTipo().equals("Double"))
            typeData= "null";
          else if (renglon.getTipo().equals("Integer"))
            typeData= "nul";
          else
            typeData= "null";
          regresar.append(MessageFormat.format("{0}", new Object[]{typeData}));
        } // if
      } // while
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return regresar.toString();
  }

  public String getTypeNameVariables() {
    StringBuilder sb     = new StringBuilder();
    RenglonCampo renglon= null;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        if (!(renglon.getCampo().equalsIgnoreCase("registro"))) {
          if (!(sb.length()== 0))
            sb.append(", ");
          sb.append(MessageFormat.format("{0} {1}",new Object[]{renglon.getTipo(), renglon.getAtributo()}));
        } // if
      } // for x
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getNamePackage(String name) {
    if (name.startsWith(".") && name.endsWith("."))
      return name.substring(1, name.length()- 1);
    if (name.startsWith("."))
      return name.substring(1);
    if (name.endsWith("."))
      return name.substring(0, name.length()- 1);
    return name;
  }

  public void generator(boolean localKajool) throws Exception {
    String [] finNombre= {"Dto", "Dao"};
    String [] typeFiles= {"Dto", "Dao"};
    String [] extension= {".java", ".java"};
    try {
      for(int x=0; x< typeFiles.length; x++) {
        setFileName(getName(finNombre[x], extension[x]));
        setEscritura(getParse(typeFiles[x]));
        if (isAsistente())
          toWriteAsistente(typeFiles[x].toLowerCase());
        else
          toWrite(typeFiles[x].toLowerCase(),localKajool);
        if(!this.dao)
          x++;
      } // for
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void generatorEncuesta(boolean localKajool) throws Exception {
    String finNombre= "Dto";
    String typeFiles= "Dto";
    String extension= ".java";
    try {
      setFileName(getName(finNombre, extension));
      setEscritura(getParse(typeFiles));
      toWrite(typeFiles.toLowerCase(), localKajool);
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  protected String getCamposSelect() throws Exception {
    RenglonCampo renglon = null;
    StringBuilder regresar = new StringBuilder();
    try {
      for(String key: getDetailTable().keySet()) {
        renglon = getDetailTable().get(key);
        regresar.append(key.toLowerCase());
        regresar.append(",");
      } // for
    } // end try
    catch(Exception e) {
      Error.mensaje(e);
    }
    return regresar.toString().substring(0, regresar.toString().length() - 1);
  }

  protected String getSqlXml() throws Exception {
    StringBuilder xml= new StringBuilder();
    String paq       = getPaquete().replace("/", ".");
    if(this.dao) {
      xml.append("    <entity id=\"" + getVariable() + "Dto\">\n");
      xml.append("      <pattern id=\"dao\">".concat(paq).concat(".dao.").concat(getVariable()).concat("Dao</pattern>\n"));
      xml.append("      <pojo id=\"dto\">".concat(paq).concat(".dto.").concat(getVariable()).concat("Dto</pojo>\n"));
      xml.append("    </entity> \n");
    } // if
    xml.append("\n    <unit id=\"" + getVariable() + "Dto\">");
    xml.append("\n      <select id=\"row\">");
    xml.append("\n        select      ");
    xml.append("\n          ".concat("*"));
    xml.append("\n        from      ");
    xml.append("\n          ".concat(getNombreTabla().toLowerCase()));
    xml.append("\n        where ");
    xml.append("\n          {condicion} ");
    xml.append("\n      </select>");
    xml.append("\n      <select id=\"identically\">");
    xml.append("\n        select      ");
    xml.append("\n          ".concat("*"));
    xml.append("\n        from      ");
    xml.append("\n          ".concat(getNombreTabla().toLowerCase()));
    xml.append("\n        where      ");
    xml.append("\n          ".concat(getCamposTabla(true).toString()));
    xml.append("\n      </select>");
    xml.append("\n    </unit>");
    return xml.toString();
  }

  public StringBuilder getCamposTabla (boolean llave) {
    RenglonCampo renglon= null;
    StringBuilder sb     = new StringBuilder();
    try {
      // CAMPOS DE LA TABLA
      for(String key: getDetailUnique().keySet()) {
        renglon = getDetailTable().get(key);
        if((!(renglon.getCampo().equals(getPk())))||(llave== true)) {
          if (sb.length()!= 0)
            sb.append(" and ");
          if (renglon.getTipo().equals("String"))
            sb.append(renglon.getCampo().toLowerCase().concat(" = ").concat("'{").concat(renglon.getAtributo()).concat("}'"));
          else
            sb.append(renglon.getCampo().toLowerCase().concat(" = ").concat("{").concat(renglon.getAtributo()).concat("}"));
        } // if
      } // for key
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb;
  }

  public void setEncuesta(boolean encuesta) {
    this.encuesta = encuesta;
  }

  public boolean isEncuesta() {
    return encuesta;
  }

  private String getToMap() {
    StringBuilder sb     = new StringBuilder();
    int x               = 1;
    RenglonCampo renglon= null;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        sb.append(MessageFormat.format("\t\tregresar.put(\"{0}\", get{1}());", new Object[]{renglon.getAtributo(),renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length()))}));
        if (x< getDetailTable().size())
          sb.append("\n");
        x++;
      } // for x
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getToArray() {
    StringBuilder sb     = new StringBuilder();
    int x               = 1;
    RenglonCampo renglon= null;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        sb.append(MessageFormat.format("get{0}()", new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length()))}));
        if (x< getDetailTable().size())
          sb.append(", ");
        x++;
      } // for x
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  private String getToString() {
    StringBuilder sb     = new StringBuilder();
    int x               = 1;
    RenglonCampo renglon= null;
    boolean before      = false;
    try {
      // LEER CAMPOS DE LA TABLA
      for(String key: getDetailTable().keySet()) {
        renglon= getDetailTable().get(key);
        if(before)
          sb.append("\t\tregresar.append(Constantes.SEPARADOR);\n");
        sb.append(MessageFormat.format("\t\tregresar.append(get{0}());", new Object[]{renglon.getAtributo().substring(0,1).toUpperCase().concat(renglon.getAtributo().substring(1,renglon.getAtributo().length()))}));
        before= true;
        if (x< getDetailTable().size())
          sb.append("\n");
        x++;
      } // for x
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return sb.toString();
  }

  @Override
  protected void finalize() throws Throwable {
    Methods.clean(this.detailTable);
    Methods.clean(this.detailUnique);
    Methods.clean(this.properties);
  }

}
