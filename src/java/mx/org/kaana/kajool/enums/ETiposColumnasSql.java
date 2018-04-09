package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 2, 2012
 *@time 4:24:52 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


public enum ETiposColumnasSql {

  //ORACLE
  NUMBER("NUMBER"),
  VARCHAR2("VARCHAR"),
  TIMESTAMP("TIMESTAMP"),
  DATE("DATE"),
	BLOB("BLOB"),
  DOUBLE("DOUBLE"),

  //MYSQL
   VARCHAR("VARCHAR"),
   INT("INT"),
   INTEGER("INTEGER");

   private String tipo;

   private ETiposColumnasSql(String tipo) {
     this.tipo = tipo;
   }

   public String getTipo() {
     return this.tipo;
   }

}
