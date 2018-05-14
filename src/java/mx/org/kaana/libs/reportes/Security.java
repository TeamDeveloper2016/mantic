package mx.org.kaana.libs.reportes;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import mx.org.kaana.libs.Constantes;
import org.apache.log4j.Logger;

public class Security {

  private static final Logger LOG = Logger.getLogger(Security.class);
	private String name;
	private String password;

	public Security(String name) {
		this(name, "");
	}

	public Security(String name, String password) {
		this.name=name;
		this.password=password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=67*hash+Objects.hashCode(this.name);
		hash=67*hash+Objects.hashCode(this.password);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Security other=(Security) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.password, other.password)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Security{"+"name="+name+", password="+password+'}';
	}
	 
	public void execute() throws Exception {
		this.execute(PdfWriter.ALLOW_PRINTING);
	}
	
	public void execute(int permissions) throws Exception {
		try {
			PdfReader reader = new PdfReader(this.name);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(this.name.concat(Constantes.SEPARADOR_PROPIEDADES).concat(Constantes.NOMBRE_DE_APLICACION)));
			stamper.setEncryption(this.password.getBytes(), this.password.getBytes(), permissions, PdfWriter.ENCRYPTION_AES_256);
			stamper.close();
			reader.close();
			File file = new File(this.name);
			if(file.delete()) {
			  File rename= new File(this.name.concat(Constantes.SEPARADOR_PROPIEDADES).concat(Constantes.NOMBRE_DE_APLICACION));	
				if(!rename.renameTo(file))
				  LOG.warn("No se puedo renombrar el archivo ".concat(this.name.concat(Constantes.SEPARADOR_PROPIEDADES).concat(Constantes.NOMBRE_DE_APLICACION)));	
			} // if
			else
		    LOG.warn("No se puedo eliminar el archivo ".concat(this.name.concat(Constantes.SEPARADOR_PROPIEDADES).concat(Constantes.NOMBRE_DE_APLICACION)));	
		} // try
		catch (DocumentException|IOException e) {
			throw e;
		} // try
	}
	
	public static void main(String ... args) throws Exception {
	  Security security= new Security("d:\\temporal\\xIktan_20170315021512654_Tabulado.pdf");
		security.execute();
	}
}
