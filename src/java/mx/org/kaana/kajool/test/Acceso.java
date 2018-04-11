package mx.org.kaana.kajool.test;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalGruposDto;
import mx.org.kaana.libs.formato.BouncyEncryption;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/08/2016
 * @time 12:03:45 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Acceso {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    String password = "ir2SvBxAiWIbCDhVGdDv6A==";
    
   // System.out.print(DaoFactory.getInstance().toEntitySet("select ID_AYUDA AS ID_KEY from TC_JANAL_ayudAS"));
    //System.out.println(DaoFactory.getInstance().toEntitySet("ManticVisatsDto","row",Collections.emptyMap()));
    TcJanalGruposDto  tcJanalGrups= new TcJanalGruposDto();
    tcJanalGrups.setClave("99");
    tcJanalGrups.setDescripcion("99");
    tcJanalGrups.setDescripcion("99");
    tcJanalGrups.setIdUsuario(1L);
    //DaoFactory.getInstance().insert(tcJanalGrups);
    
    System.out.println(BouncyEncryption.decrypt(password));
		/*	
    System.out.println(BouncyEncryption.encrypt("davalos"));
    System.out.println(BouncyEncryption.encrypt("ramirez"));
    System.out.println(BouncyEncryption.encrypt("jimenez"));
    System.out.println(BouncyEncryption.encrypt("alvarez"));  
    System.out.println(BouncyEncryption.encrypt("vazquez"));  
    System.out.println(BouncyEncryption.encrypt("mantic2018"));  
   */
  }
}
