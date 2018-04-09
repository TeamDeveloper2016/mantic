package mx.org.kaana.libs.progreso;

import java.io.OutputStream;
import java.io.Writer;

import java.text.MessageFormat;


public class ProgressMonitor {

  private int min;
  private int max;
  private int index;
  private double percent;
  private double percentInterval;
  double a;
  int b;
  int c;
  private MessageFormat mf;
  private Printer out;
  private final Object args[] = new Object[2];

  public ProgressMonitor() {
    a = percent;
  }

  public void setBarra() {
    c= 0;
  }

  public void setMin(int i) {
    min = i;
  }

  public void setMax(int i) {
    max = i;
  }

  public void setFormat(String s) {
    mf = new MessageFormat(s);
  }

  public void setProgress(int i) {
//        i++;
    index = i;
    percent = (double)(index * 100) / (double)max;
    if(args[1]== null)
      args[1]= "0";
    if(i >= max)
      printProgress();
    args[1] = (int)percent + "";
  }

  public void setMessage(String s) {
    args[0] = s;
  }

  public void setPercentInterval(double d) {
    percentInterval = d;
    b = (int)(100D / d);
  }

  public void printProgress() {
    boolean flag = false;
    if(percentInterval != 0.0D) {
      int i = (int)(percent / percentInterval);
      if(!flag) {
        if(c < i) {
          out.println(mf.format(((Object) (args))));
          c++;
        }
      }
    } else {
      out.println(mf.format(((Object) (args))));
    }
  }

  public void setPrinter(Writer writer) {
    out = new PrintWriter(writer);
  }

  public void setPrinter(OutputStream outputstream) {
    out = new PrintStream(outputstream);
  }

  public void finalize () {
    mf = null;
    out = null;
  }

}
