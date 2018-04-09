package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/12/2014
 *@time 10:20:20 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EComponente {
	VALIDACION									("Validacion","				'{criterio}'	: {validaciones : 'libre',	mascara: 'libre'}"),
	TEXT_FIELD									("Caja de texto","				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\"/>\n" +"				<p:inputText id=\"{criterio}\" value=\"#{{backing}.attrs.{criterio}}\"/><p:spacer/>"),
	SELECT_ONE_MENU							("Combo","				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\"/>\n" +"				<p:selectOneMenu id=\"{criterio}\" value=\"#{{backing}.attrs.{criterio}}\">\n" +"					<p:ajax event=\"change\" process=\"@this\"/>\n"+
																"					<f:selectItems value=\"#{{backing}.lista{lista}}\"/>\n"+ "				</p:selectOneMenu><p:spacer/>"),
	COLUMN											("Columna","			<p:column headerText=\"{alias}\" class=\"{alineacion} {contenido}\">				\n" +"				<h:outputText value=\"#{row['{nombre}']}\"/>\n" +"			</p:column>"),
	ATRIBUTOS										("", "	private List<UISelectItem> lista{lista};"),
	ATRIBUTOS_BEAN  						("", "	private {tipoDato} {nombreAtributo};"),
	GETTERS											("", "	public List<UISelectItem> getLista{lista}() {\n" +"		return lista{lista};\n" +"	}"),
	GETTERS_BEAN								("", "	public {tipoDato} get{nombreAtributoGet}() {\n" +"		return {nombreAtributo};\n" +"	}"),
	SETTERS_BEAN								("", "	public void set{nombreAtributoGet}({tipoDato} {nombreAtributo}) {\n" +"		this.{nombreAtributo}= {nombreAtributo};\n" +"	}"),
	FORMAT											("","		columnas.add(new Columna(\"{nombre}\", EFormatoDinamicos.{formato}));"),
	INIT_TEXT_FIELD							("","			this.attrs.put(\"{criterio}\",\"\");"),
	INIT_SELECT_ONE_MENU				("","			this.attrs.put(\"{criterio}\",-1);\n			{initSelectOneMenu}"),
	INIT_SELECT_ONE_MENU_ACCION	("","			{initSelectOneMenu}"),
	INIT_DATE                  	("","			this.attrs.put(\"{criterio}\", new Date(Calendar.getInstance().getTimeInMillis()));\n"),
	BOTON_ACEPTAR								("","		<p:commandButton id=\"aceptar\" value=\"Aceptar\" action=\"#{{backing}.doAceptar}\" onstart=\"return janal.execute();\" process=\"@this\" />"),
	BOTON_CANCELAR							("","		<p:commandButton id=\"cancelar\" value=\"Cancelar\" action=\"filtro\" immediate=\"true\" ajax=\"false\" />"),
	VALIDACION_ACCION						("Validacion accion","				'{criterio}'	: {validaciones : '{validaciones}',	mascara: '{mascara}'}"),
	DATE												("Fecha", "				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\" />\n" +"				<p:calendar id=\"{criterio}\" showOn=\"button\" pattern=\"dd/MM/yyyy\" value=\"#{{backing}.attrs.{criterio}}\"  locale=\"es\" converter=\"janal.convertidor.{converter}\"/><p:spacer/>"),
	DATE_ACCION									("Fecha", "				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\" />\n" +"				<p:calendar id=\"{criterio}\" showOn=\"button\" pattern=\"dd/MM/yyyy\" value=\"#{{backing}.bean.{criterio}}\"  locale=\"es\" converter=\"janal.convertidor.{converter}\"/><p:spacer/>"),
	TEXT_FIELD_ACCION						("Caja de texto","				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\"/>\n" +"				<p:inputText id=\"{criterio}\" value=\"#{{backing}.bean.{criterio}}\" size=\"{size}\"/><p:spacer/>"),
	SELECT_ONE_MENU_ACCION			("Combo","				<p:outputLabel for=\"{criterio}\" value=\"{alias}:\"/>\n" +"				<p:selectOneMenu id=\"{criterio}\" value=\"#{{backing}.bean.{criterio}}\">\n" +"					<p:ajax event=\"change\" process=\"@this\"/>\n"+
													    	"					<f:selectItems value=\"#{{backing}.lista{lista}}\"/>\n"  +"				</p:selectOneMenu><p:spacer/>"),
	PARAMS											("", "		Map<String, Object> params= new HashMap();"),
	CLEAR_PARAMS								("", "		finally{\n			Methods.clean(params);\n		}"),
  FIELDSET                    ("", "			<p:fieldset legend=\"{nombreGrupo}\" toggleable=\"true\"> \n{elementos}\n			</p:fieldset>\n")
	;
	
	private String descripcion;
	private String sintaxis;
	
	private EComponente(String descripcion, String sintaxis) {
		this.descripcion = descripcion;
		this.sintaxis = sintaxis;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getSintaxis() {
		return sintaxis;
	}	
}
