package mx.org.kaana.libs.motor;

import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 6, 2012
 *@time 2:18:39 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class GeneratorQuery extends GenerationFiles {

  public GeneratorQuery(String nombreTabla, String query, boolean nemonico, String nombreEncuesta) throws Exception {
    super(nombreTabla, query, nemonico, nombreEncuesta);
  }

  public GeneratorQuery(String nombreTabla, String query, String paquete, String llavePrimaria, boolean isBean, boolean isAsistente, boolean nemonico, String nombreEncuesta) throws Exception {
    this(nombreTabla, query, paquete, llavePrimaria, isBean, isAsistente, -1L, nemonico, nombreEncuesta);
  }

  public GeneratorQuery(String nombreTabla, String query, String paquete, String llavePrimaria, boolean isBean, boolean isAsistente, Long idFuenteDato, boolean nemonico, String nombreEncuesta) throws Exception {
    super(nombreTabla, query, paquete, llavePrimaria, isBean, isAsistente, idFuenteDato, false, nemonico, nombreEncuesta);
  }

  public GeneratorQuery(String nombreTabla, String query, String paquete, String llavePrimaria, boolean isBean, boolean nemonico, String nombreEncuesta) throws Exception {
    this(nombreTabla, query,  paquete, llavePrimaria, isBean, false, nemonico, nombreEncuesta);
  }

  public GeneratorQuery(String nombreTabla, String query, String paquete, boolean isBean, boolean nemonico, String nombreEncuesta) throws Exception {
    super(nombreTabla, query, paquete,  isBean, nemonico, nombreEncuesta);
  }

  protected void setNamePk() throws Exception {
    setPk(getLlavePrimaria());
  }

  protected void setDetailFields() throws Exception {
    setDetailFields(getQuery());
  }

  protected void setDetalleCampos() throws Exception {
    setDetalleCampos(getQuery());
  }

  public String getSqlXml() throws Exception {
    return getModel().concat(getDml());
  }

  public String getModel() {
    RenglonCampo renglon= null;
    StringBuffer sb     = new StringBuffer();
    try {
      setQuery(getQuery().replaceAll("\n","\n        "));
      // LEER CAMPOS DE LA TABLA
      if ( getDetailTable() != null) {
        for(String key: getDetailTable().keySet()) {
          renglon = getDetailTable().get(key);
          //setVariable(renglon.getCampo());
          if (sb.length()!= 0)
            sb.append(" and ");
          if (renglon.getTipo().equals("String"))
            sb.append(renglon.getCampo().toLowerCase().concat(" = ").concat("'{").concat(renglon.getAtributo()).concat("}'"));
          else
            sb.append(renglon.getCampo().toLowerCase().concat(" = ").concat("{").concat(renglon.getAtributo()).concat("}"));
        } // for key
      }
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    StringBuffer xml= new StringBuffer();
    setVariable(getNombreTabla());
    if ( getPaquete() != null && !getPaquete().isEmpty()) {
      String paq = getPaquete().replace("/", ".");
      xml.append("  <entity id=\"" + Cadena.toClassNameEspecial(getVariable()) + "Dto\">\n");
      xml.append("      <pattern id=\"dao\">".concat(paq).concat(".dao.").concat(Cadena.toClassNameEspecial(getVariable())).concat("Dao</pattern>\n"));
      xml.append("      <pojo id=\"dto\">".concat(paq).concat(".dto.").concat(Cadena.toClassNameEspecial(getVariable())).concat("Dto</pojo>\n"));
      xml.append("    </entity> \n");
    }
    return xml.toString();
  }

  public String getDml() {
    boolean contieneWhere= false;
    StringBuffer xml     = new StringBuffer();
    contieneWhere        = getQuery().toLowerCase().contains("where");
    xml.append("\n    <unit id=\"" + Cadena.toClassNameEspecial(getVariable()) + "Dto\">");
    xml.append("\n      <select id=\"row\">\n");
    if (!contieneWhere ) {
      xml.append("        ");
      xml.append(getQuery().concat("\n        where"));
      xml.append("\n          {condicion}");
    }
    else {
      xml.append("        ");
      xml.append(getQuery());
    } // if - else
    xml.append("\n      </select>");
    xml.append("\n    </unit>");
    return xml.toString();
  }

  protected String getParse(String typeFile) throws Exception {
    return getParse(typeFile, "Query");
  }

  protected void setUniqueKey() throws Exception {
  }


}
