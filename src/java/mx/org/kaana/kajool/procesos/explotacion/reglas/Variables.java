package mx.org.kaana.kajool.procesos.explotacion.reglas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.beans.Variable;

/**
 *@company KAANA
 *@project IKTAN (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 28/11/2016
 *@time 01:20:23 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Variables {

  private static final List<Variable> TODOS;
  private static final List<Variable> CARATULA;
  private static final List<Variable> INTEGRANTES;
  private static final List<Variable> FAMILIA;
  private static final List<Variable> MODULO;
  private static final List<Variable> VISITAS;
	private static final List<Variable> ENTIDADES;
	private static final List<Variable> MUNICIPIOS;
	private static final List<Variable> LOCALIDADES;
  
  static {
    TODOS= new ArrayList<>();
    TODOS.add(new Variable("idFamilia", "Id.Familia", "ID.FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
		TODOS.add(new Variable("ent", "Ent", "ENT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    TODOS.add(new Variable("mun", "Mun", "MUN", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    TODOS.add(new Variable("loc", "Loc", "LOC", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    TODOS.add(new Variable("folio", "Consecutivo", "CONSECUTIVO", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
    TODOS.add(new Variable("nombres", "Nombre", "NOMBRE", "ColWid10 janal-column-left", EFormatoDinamicos.MAYUSCULAS));    
    
    CARATULA= new ArrayList<>();
    CARATULA.add(new Variable("idFamilia", "Id.Familia", "ID.FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));						
    CARATULA.add(new Variable("consecutiv", "Consecutivo", "CONSECUTIVO", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("cveUpm", "UPM", "UPM", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("folioCuest", "Folio Cuest", "FOLIO CUEST", "ColWid10 janal-column-center", EFormatoDinamicos.NUMERO_SIN_DECIMALES));    
    CARATULA.add(new Variable("nomTit", "Nombre", "NOMBRE", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("patTit", "Ap. Paterno", "AP. PATERNO", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("matTit", "Ap. Materno", "AP. MATERNO", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("calle", "Calle", "CALLE", "ColWid15 janal-column-left", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("numExt", "No Ext", "NO. EXT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("numInt", "No Int", "NO. INT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("colonia", "Colonia", "COLONIA", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("cp", "CP", "CP", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));    
    CARATULA.add(new Variable("referencia", "Referencia", "REFERENCIA", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
		CARATULA.add(new Variable("ent", "Ent", "ENT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("mun", "Mun", "MUN", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    CARATULA.add(new Variable("loc", "Loc", "LOC", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    
		INTEGRANTES = new ArrayList<>();
    INTEGRANTES.add(new Variable("idFamilia", "Id Familia", "ID FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE)); 
    INTEGRANTES.add(new Variable("folioCuest" , "Folio Cuest", "FOLIO CUEST", "ColWid10 janal-column-center", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("consecutiv" , "Consecutiv", "CONSECUTIV", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("cveUpm" , "UPM", "UPM", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));		
    INTEGRANTES.add(new Variable("rg" , "Rg", "RG", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("apaterno" , "Ap. Paterno", "AP. PATERNO", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("amaterno" , "Ap. Materno", "AP. MATERNO", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("nombre" , "Nombre", "NOMBRE", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));    
    INTEGRANTES.add(new Variable("sexo" , "Sexo", "SEXO", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("edad" , "Edad", "EDAD", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("esc" , "Esc", "ESC", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("parent" , "Parent", "PARENT", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("vresid" , "V Resid", "V RESID", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("alim" , "Alim", "ALIM", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("salud" , "Salud", "SALUD", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("educ" , "Educ", "EDUC", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("otros" , "Otros", "OTROS", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("ninguno" , "Ninguno", "NINGUNO", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("aapoyo" , "A Apoyo", "A APOYO", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
    INTEGRANTES.add(new Variable("activo"		, "Activo", "ACTIVO", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));
		INTEGRANTES.add(new Variable("informante"	, "Informante", "INFORMANTE", "ColWid20 janal-column-left", EFormatoDinamicos.LIBRE));
		
		FAMILIA = new ArrayList<>();
		FAMILIA.add(new Variable("idFamilia", "Id Familia", "ID.FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("folioCuest"	, "Folio Cuest", "FOLIO", "ColWid10 janal-column-center", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("consecutiv"	, "Consecutivo", "CONSECUTIVO", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("cveUpm"	, "Upm", "UPM", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE)); 		
    FAMILIA.add(new Variable("mpFrRe", "Mp Fr Re", "MP FR RE", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("mpAsist", "Mp Asist", "MO ASIST", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("mpInf"	, "Mp Inf", "MP INF", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 		
    FAMILIA.add(new Variable("mpTmpo"	, "Mp Tmpo", "MP TMPO", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("mpTrto"	, "Mp Trto", "MP TRTO", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("amejora"	, "A Mejora", "A MEJORA", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("asat"	, "A Sat", "A SAT", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("aoprt"	, "A Oprt", "A OPRT", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("asatT", "A Sat T", "A SAT T", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("aotorga"	, "A Otorga", "A OTORGA", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE)); 
    FAMILIA.add(new Variable("aescBe"	, "A Esc Be", "A ESC BE", "ColWid5 janal-column-right", EFormatoDinamicos.LIBRE));      
		
    MODULO = new ArrayList<>();
		MODULO.add(new Variable("idFamilia", "Id Familia", "ID.FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
    MODULO.add(new Variable("folioCuest"	, "Folio Cuest", "FOLIO", "ColWid10 janal-column-center", EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("consecutiv"	, "Consecutivo", "CONSECUTIVO", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("cveUpm"	, "UPM", "UPM", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));		
    MODULO.add(new Variable("proDes","Pro Des","PRO DES","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("grTrab","Gr Trab","GR TRAB","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("grApo","Gr Apo","GR APO","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("grProOpo","Gr Pro Opo","GR PRO OPO","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
		MODULO.add(new Variable("grApoMej","Gr Apo Mej","GR APO MEJ","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
    
		
    VISITAS = new ArrayList<>();
		VISITAS.add(new Variable("idFamilia", "Id Familia", "ID.FAMILIA", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));		
		VISITAS.add(new Variable("consecutiv"	, "Consecutivo", "CONSECUTIVO", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
		VISITAS.add(new Variable("cveUpm"	, "UPM", "UPM", "ColWid10 janal-column-right", EFormatoDinamicos.LIBRE));
		VISITAS.add(new Variable("visita","Visita","VISITA","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
    VISITAS.add(new Variable("nomEnt", "Entrevistador", "ENTREVISTADOR", "ColWid25 janal-column-left", EFormatoDinamicos.LIBRE));
		VISITAS.add(new Variable("fecha","Fecha","FECHA","ColWid10 janal-column-center",EFormatoDinamicos.FECHA_HORA_CORTA));
		VISITAS.add(new Variable("hrIni","Hora inicio","HORA INICIO","ColWid5 janal-column-center",EFormatoDinamicos.LIBRE));
    VISITAS.add(new Variable("hrTer","Hora termino","HORA TERMINO","ColWid5 janal-column-center",EFormatoDinamicos.LIBRE));
		VISITAS.add(new Variable("res","Resultado","RESULTADO","ColWid5 janal-column-right",EFormatoDinamicos.LIBRE));
		
		ENTIDADES = new ArrayList<>();
		ENTIDADES.add(new Variable("ent", "Ent", "ENT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
		ENTIDADES.add(new Variable("descripcion", "Descripcion", "DESCRIPCION", "ColWid250 janal-column-left", EFormatoDinamicos.LIBRE));
		
		MUNICIPIOS = new ArrayList<>();		
		MUNICIPIOS.add(new Variable("ent", "Ent", "ENT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
		MUNICIPIOS.add(new Variable("mun", "Mun", "MUN", "ColWid10 janal-column-center", EFormatoDinamicos.LIBRE));
		MUNICIPIOS.add(new Variable("descripcion", "Descripcion", "DESCRIPCION", "ColWid250 janal-column-left", EFormatoDinamicos.LIBRE));
		
		LOCALIDADES = new ArrayList<>();
		LOCALIDADES.add(new Variable("ent", "Ent", "ENT", "ColWid5 janal-column-center", EFormatoDinamicos.LIBRE));
		LOCALIDADES.add(new Variable("mun", "Mun", "MUN", "ColWid10 janal-column-center", EFormatoDinamicos.LIBRE));
		LOCALIDADES.add(new Variable("loc", "Loc", "LOC", "ColWid15 janal-column-center", EFormatoDinamicos.LIBRE));
		LOCALIDADES.add(new Variable("descripcion", "Descripcion", "DESCRIPCION", "ColWid250 janal-column-left", EFormatoDinamicos.LIBRE));				
  }
  
  private Variables() {
  }

	public static List<Variable> getCARATULA() {
		return Collections.unmodifiableList(CARATULA);
	}
  
	public static List<Variable> getINTEGRANTES() {
		return Collections.unmodifiableList(INTEGRANTES);
	}
	
  public static List<Variable> getFAMILIA() {
		return Collections.unmodifiableList(FAMILIA);
	}
  
	public static List<Variable> getMODULO() {
		return Collections.unmodifiableList(MODULO);
	}
	
  public static List<Variable> getVISITAS() {
		return Collections.unmodifiableList(VISITAS);
	}
  
  public static List<Variable> getTODOS() {
		return Collections.unmodifiableList(TODOS);
	}

	public static List<Variable> getENTIDADES() {
		return Collections.unmodifiableList(ENTIDADES);
	}

	public static List<Variable> getMUNICIPIOS() {
		return Collections.unmodifiableList(MUNICIPIOS);
	}

	public static List<Variable> getLOCALIDADES() {
		return Collections.unmodifiableList(LOCALIDADES);
	}
}
