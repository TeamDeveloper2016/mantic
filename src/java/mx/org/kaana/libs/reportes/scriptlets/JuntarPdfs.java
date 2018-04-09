package mx.org.kaana.libs.reportes.scriptlets;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import java.io.FileOutputStream;
import java.util.List;
import mx.org.kaana.libs.archivo.Archivo;

public class JuntarPdfs {

  private List origen;
  private String destino;
  private boolean intercalar;

  public JuntarPdfs(List origen, String destino, boolean intercalar) {
    this.origen=origen;
    this.destino=destino;
    this.intercalar=intercalar;
  }

  public boolean concatenar() throws Exception{
    boolean regresa= false;
    try {
      String outFile        = destino;
      Document document     = null;
      PdfCopy writer        = null;
      PdfReader reader      = new PdfReader((String)this.origen.get(0));
      PdfImportedPage pagina= null;
      PRAcroForm form       = null;
      boolean hayPaginas    = true;
      int cont              = 1;
      int salir             = 0;
      // step 1: creation of a document-object
      document = new Document(reader.getPageSizeWithRotation(1));
      // step 2: we create a writer that listens to the document
      Archivo.verificaRuta(outFile);
      writer = new PdfCopy(document, new FileOutputStream(outFile));
      // step 3: we open the document
      document.open();
      // step 4: Escribir el documento
      if (!this.intercalar) {
        for (int i = 0; i < this.origen.size(); i++) {
          reader = new PdfReader((String)this.origen.get(i));
          //readers.add(reader);
          int n = reader.getNumberOfPages();
          for (int j = 1; j <= n; j++) {
            pagina = writer.getImportedPage(reader, j);
            writer.addPage((PdfImportedPage)pagina);
            form = reader.getAcroForm();
            if (form != null)
              writer.copyAcroForm(reader);
          } // for
        } // for
      } // if
      else {
        while (hayPaginas) {
          for (int i = 0; i < this.origen.size(); i++) {
            reader = new PdfReader((String)this.origen.get(i));						
						if (cont<=reader.getNumberOfPages() && reader.getFileLength()> 1000) {
							pagina = writer.getImportedPage(reader, cont);
							writer.addPage((PdfImportedPage)pagina);
							form = reader.getAcroForm();
							if (form != null)
								writer.copyAcroForm(reader);
						} // if
						else
							salir++;						
          } // for
          if (salir == this.origen.size())
            hayPaginas = false;
          cont++;
          salir = 0;
        }
      }
      // step 5: we close the document
      document.close();
      regresa = true;
    } 
    catch (Exception e) {
      throw e;
    }
    return regresa;
  } // concatenar
  
}
