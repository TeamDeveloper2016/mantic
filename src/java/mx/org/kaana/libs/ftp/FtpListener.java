package mx.org.kaana.libs.ftp;

import java.io.Writer;
import mx.org.kaana.libs.archivo.BarraProgreso;
import net.sf.cleanftp.DownloadStatus;
import net.sf.cleanftp.FileTransferListener;
import net.sf.cleanftp.UploadStatus;


public class FtpListener extends BarraProgreso implements FileTransferListener {

  private Writer out;

  public FtpListener(Writer out) {
    this.setOut(out);
  }

  public void uploadStatusChanged(UploadStatus status) {
    int total = Long.valueOf(status.getFile().length()).intValue();
    this.setDebug(false);
    this.monitoreo(total, getOut());
    this.progreso(Long.valueOf(status.getBytesSent()).intValue());
  }

  public void downloadStatusChanged(DownloadStatus downloadStatus) {
    int total = (int)(downloadStatus.getFile().getLength());
    this.setDebug(false);
    this.monitoreo(total, getOut());
    this.progreso(Long.valueOf(downloadStatus.getBytesReceived()).intValue());
  }

  public void setOut(Writer out) {
    this.out = out;
  }

  public Writer getOut() {
    return out;
  }
  
  public void finalize() {
    setOut(null);
  }
}
