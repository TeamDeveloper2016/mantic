package mx.org.kaana.libs.progreso;

import java.io.OutputStream;


// Referenced classes of package sia.beans.utilerias:
//            Printer

class PrintStream extends java.io.PrintStream implements Printer {

  public PrintStream(OutputStream outputstream) {
    super(outputstream);
  }

  public PrintStream(OutputStream outputstream, boolean flag) {
    super(outputstream, flag);
  }
}
