package mx.org.kaana.mantic.test.charset;

import java.text.Normalizer;
import org.apache.commons.io.Charsets;
import static org.apache.commons.io.Charsets.ISO_8859_1;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2018
 * @time 03:32:35 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Caracteres {
	private static final Log LOG=LogFactory.getLog(Caracteres.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String textwithaccent="Thís ís a text with accent";
		String textwithletter="Ñandú";

		String text1=new String(textwithaccent.getBytes(Charsets.ISO_8859_1), Charsets.ISO_8859_1);
		String text2=new String(textwithletter.getBytes(ISO_8859_1), ISO_8859_1);
		
		LOG.info(text1);
		LOG.info(text2);
		String texto= "BOQUILLA ROJA 0° PARA PISTOLA Ñandú";
		LOG.info(Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
		
		String codigo= "[5/8]()()\\4";
		codigo= codigo.replaceAll("([(,),',*,!,|,<,>,?,¿,&,%,$,#,;,:,{,},\\[,\\],~,\"])", "").trim();
		LOG.info(codigo);
	}

}
