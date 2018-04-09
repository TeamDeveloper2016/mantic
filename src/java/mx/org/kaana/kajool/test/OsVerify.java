package mx.org.kaana.kajool.test;

import mx.org.kaana.libs.recurso.Configuracion;

/**
 * @company INEGI
 * @project IKTAN (Seguimiento y control de proyectos)
 * @date 7/12/2016
 * @time 10:39:20 AM
 * @author Alejandro Jiménez García <alejandro.jimenez@inegi.org.mx>
 */
public class OsVerify {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    System.out.println(Configuracion.getInstance().getPropiedadServidor("sistema.download"));
    System.out.println("os.name: "+ System.getProperty("os.name"));
    System.out.println("os.arch: "+ System.getProperty("os.arch"));
    System.out.println("os.version: "+ System.getProperty("os.version"));
    System.out.println("user.home: "+ System.getProperty("user.home"));
    System.out.println("user.name: "+ System.getProperty("user.name"));
    System.out.println("user.dir: "+ System.getProperty("user.dir"));
    System.out.println("line.separator: "+ System.getProperty("line.separator"));
    System.out.println("path.separator: "+ System.getProperty("path.separator"));
    System.out.println("file.separator: "+ System.getProperty("file.separator"));
  }
}
