package mx.org.kaana.xml;

import mx.org.kaana.kajool.enums.ESql;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.init.Settings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Dml {

	private static final Log LOG=LogFactory.getLog(Dml.class);

	private static final String EXP_INI="/kaana/dml/unit[@id='";
	private static final String INS_EXP="']/insert[@id='";
	private static final String DEL_EXP="']/delete[@id='";
	private static final String SEL_EXP="']/select[@id='";
	private static final String UPD_EXP="']/update[@id='";
	private static Dml instance;
	private static Object mutex;

	private Document documento;
	private boolean nulosComoString;

	static {
		mutex=new Object();
	}

	private Dml() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fabrica=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=fabrica.newDocumentBuilder();
		InputStream in=this.getClass().getResourceAsStream(Settings.getInstance().getCustomDml());
		// janal default janal.xml
		if (in==null) {
			in=this.getClass().getResourceAsStream(Settings.getInstance().getDefaultDml());
		}
		this.documento=builder.parse(in);
		Modulos modulus=new Modulos(this.documento, Configuracion.getInstance().toFileModule());
		modulus.toBuild();
		LOG.debug(modulus.toString());
		modulus=null;
	}

	public static Dml getInstance() {
		synchronized (mutex) {
			if (instance==null) {
				try {
					instance=new Dml();
				} // try 
				catch (Exception e) {
					Error.mensaje(e);
				} // catch
			} // if
		}
		return instance;
	}

	public void reload() {
		try {
			instance=new Dml();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch    
	}

	public String getDelete(String subModulo, String id, Map parametros) throws XPathExpressionException {
		return evaluate(EXP_INI.concat(subModulo).concat(DEL_EXP).concat(id).concat("']"), parametros);
	}

	public String getDelete(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
		Variables variables=new Variables(parametros, token);
		return getDelete(subModulo, id, variables.getMap());
	}

	public String getDelete(String subModulo, String id, String parametros) throws XPathExpressionException {
		return getDelete(subModulo, id, parametros, Constantes.SEPARADOR.charAt(0));
	}

	public String getInsert(String subModulo, String id, Map parametros) throws XPathExpressionException {
		return evaluate(EXP_INI.concat(subModulo).concat(INS_EXP).concat(id).concat("']"), parametros);
	}

	public String getInsert(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
		Variables variables=new Variables(parametros, token);
		return getInsert(subModulo, id, variables.getMap());
	}

	public String getInsert(String subModulo, String id, String parametros) throws XPathExpressionException {
		return getInsert(subModulo, id, parametros, Constantes.SEPARADOR.charAt(0));
	}

	public String getSelect(String subModulo, String id, Map parametros) throws XPathExpressionException {
		return evaluate(EXP_INI.concat(subModulo).concat(SEL_EXP).concat(id).concat("']"), parametros);
	}

	public String getSelect(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
		Variables variables=new Variables(parametros, token);
		return getSelect(subModulo, id, variables.getMap());
	}

	public String getSelect(String subModulo, String id, String parametros) throws XPathExpressionException {
		return getSelect(subModulo, id, parametros, Constantes.SEPARADOR.charAt(0));
	}

	public String getUpdate(String subModulo, String id, Map parametros) throws XPathExpressionException {
		return evaluate(EXP_INI.concat(subModulo).concat(UPD_EXP).concat(id).concat("']"), parametros);
	}

	public String getUpdate(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
		Variables variables=new Variables(parametros, token);
		return getUpdate(subModulo, id, variables.getMap());
	}

	public String getUpdate(String subModulo, String id, String parametros) throws XPathExpressionException {
		return getUpdate(subModulo, id, parametros, Constantes.SEPARADOR.charAt(0));
	}

	public String getDML(String subModulo, String id, Map parametros, ESql kind) throws XPathExpressionException {
		switch (kind) {
			case DELETE:
				return getDelete(subModulo, id, parametros);
			case INSERT:
				return getInsert(subModulo, id, parametros);
			case SELECT:
				return getSelect(subModulo, id, parametros);
			case UPDATE:
				return getUpdate(subModulo, id, parametros);
		}
		return null;
	}

	public String getDML(String subModulo, String id, String parametros, char token, ESql kind) throws XPathExpressionException {
		Variables variables=new Variables(parametros, token);
		return getDML(subModulo, id, variables.getMap(), kind);
	}

	public String getDML(String subModulo, String id, String parametros, ESql kind) throws XPathExpressionException {
		return getDML(subModulo, id, parametros, Constantes.SEPARADOR.charAt(0), kind);
	}

	private String command(String expresion) throws XPathExpressionException {
		XPathFactory xPFabrica=XPathFactory.newInstance();
		XPath xPath=xPFabrica.newXPath();
		String sql=xPath.evaluate(expresion, getDocumento());
		if (sql==null) {
			sql="";
		}
		if (sql.length()>0) {
			LOG.debug("DML (".concat(sql).concat(")"));
		}
		return sql;
	}

	public boolean isKajool() throws XPathExpressionException {
		String propiedad=Configuracion.getInstance().getPropiedad("sistema.kajool");
		boolean regresar=!Cadena.isVacio(propiedad);
		if (regresar) {
			regresar=Boolean.valueOf(propiedad);
		}
		return regresar;
	}

	private String evaluate(String expresion, Map parametros) throws XPathExpressionException {
		String sql=command(expresion);
		LOG.info("EXpresion".concat(expresion));
		if (expresion.indexOf("TrSentenciasXmlDto")==-1) { //&& Configuracion.getInstance().isEtapaProduccion())
			if (isKajool()) {
				//guardarSentenciaXml(expresion);
			}
		}
		if (sql.length()>0) {
			sql=Cadena.replaceParams(sql, parametros);
			LOG.debug("DML (".concat(sql).concat(")"));
		} // if
		else {
			LOG.warn("La sentencia se encuentra vacia ".concat(expresion));
		}
		return sql;
	}

	public String selectSQL(String subModulo, String id) throws XPathExpressionException {
		return command(EXP_INI.concat(subModulo).concat(SEL_EXP).concat(id).concat("']"));
	}

	public Document getDocumento() {
		return documento;
	}

	public void setNulosComoString(boolean nulosComoString) {
		this.nulosComoString=nulosComoString;
	}

	public boolean isNulosComoString() {
		return nulosComoString;
	}

	public boolean exists(String subModulo, String id) throws XPathExpressionException {
		if (command(EXP_INI.concat(subModulo).concat(SEL_EXP).concat(id).concat("']")).length()<=0) {
			if (command(EXP_INI.concat(subModulo).concat(INS_EXP).concat(id).concat("']")).length()<=0) {
				if (command(EXP_INI.concat(subModulo).concat(UPD_EXP).concat(id).concat("']")).length()<=0) {
					if (command(EXP_INI.concat(subModulo).concat(DEL_EXP).concat(id).concat("']")).length()<=0) {
						return false;
					}
					else {
						return true;
					}
				}
				else {
					return true;
				}
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	@Override
	public void finalize() {
		this.documento=null;
	}

	private void guardarSentenciaXml(String expresion) {
		Map<String, Object> params=new HashMap();
		try {
			String[] dato=new String[3];
			int valor=0;
			for (int i=0; i<expresion.length(); i++) {
				if (expresion.charAt(i)=='=') {
					i+=2;
					dato[valor]="";
					while (expresion.charAt(i)!=']') {
						dato[valor]+=expresion.charAt(i);
						i+=1;
					}//while
					dato[valor]=dato[valor].substring(0, dato[valor].length()-1);
					valor+=1;
				}//if
				if (expresion.charAt(i)==']'&&i<expresion.length()-1) {
					i+=2;
					dato[valor]="";
					while (expresion.charAt(i)!='[') {
						dato[valor]+=expresion.charAt(i);
						i+=1;
					}//while
					valor+=1;
				}//if
			}//for
			params.put("proceso", dato[0]);
			params.put("tipoSentencia", dato[1]);
			params.put("identificador", dato[2]);
			if (DaoFactory.getInstance().toEntity("TrSentenciasXmlDto", "identically", params)==null) {
				DaoFactory.getInstance().execute(getInsert("TrSentenciasXmlDto", "guardar", params));
			}
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		}//finally
	}//guardarSentenciaXml
}
