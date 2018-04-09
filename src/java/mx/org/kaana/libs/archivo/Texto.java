package mx.org.kaana.libs.archivo;

import com.linuxense.javadbf.DBFException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.LinkPage;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.xml.Tokens;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Texto extends Tokens implements Serializable{
  
  private static final long serialVersionUID = -2325488266534124312L;
  private static Log LOG                  = LogFactory.getLog(Texto.class);
	
	public Texto(String nombre, Modelo definicion) throws Exception {
		super(nombre, definicion);
	}

	private void header(BufferedWriter out) throws DBFException, IOException {
		int count   = 0;
		for (MetaField field: getFields()) {
			LOG.debug(field.getName()+ ": "+ field.getLength());
			// verificar la longitud del campo y los caracteres con los que terminar el nombre __N (entero), __F (flotante), __T (flotante 2 decimales)
			out.write(fixName(field.getField(), false));
			if(count!= getFields().size())
  			out.write("\t");
			count++;
		} // for
		out.write("\n");
	}
	
	private String toDelete(String value) {
		return value.replace('\n', ' ').replace('\t', ' ');
	}
	
	private void detail(BufferedWriter out, List<Entity> records) throws DBFException, IOException {
		int record= 0;
		for(Entity entity: records) {
			int count= 0;
 			LOG.debug("Record : "+ ++record);
  		for (MetaField field: getFields()) {
	  		   Value value= entity.get(field.getName());
				// verificar los que el data solo sean datos de clases wrappers
				if(field.getField().endsWith(Dbase.FORCE_INTEGER) || field.getField().endsWith(Dbase.FORCE_DOUBLE) || field.getField().endsWith(Dbase.FORCE_FLOAT)) 
					out.write(value.getData()!= null? value.getData().toString(): "");
				else 
					switch(field.getType()) {
						case java.sql.Types.BIGINT:
              out.write(value.getData()!= null? ((BigInteger)value.getData()).toString(): "");
              break;
						case java.sql.Types.INTEGER:
						case java.sql.Types.SMALLINT:
						case java.sql.Types.NUMERIC:
						case java.sql.Types.DECIMAL:
						case java.sql.Types.DOUBLE:
						case java.sql.Types.FLOAT:
						case java.sql.Types.REAL:
							out.write(value.getData()!= null? ((BigDecimal)value.getData()).toString(): "");
							break;
						case java.sql.Types.DATE:
							out.write(value.getData()!= null? Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)value.getData()): "");
							break;
						case java.sql.Types.TIME:
							out.write(value.getData()!= null? Fecha.formatear(Fecha.FECHA_ESTANDAR, (Time)value.getData()): "");
							break;
						case java.sql.Types.TIMESTAMP:
							out.write(value.getData()!= null? Fecha.formatear(Fecha.FECHA_ESTANDAR, (Timestamp)value.getData()): "");
							break;
						case java.sql.Types.ARRAY:
						case java.sql.Types.CHAR:
						case java.sql.Types.LONGNVARCHAR:
						case java.sql.Types.LONGVARCHAR:
						case java.sql.Types.NCHAR:
						case java.sql.Types.NVARCHAR:
						case java.sql.Types.VARCHAR:
         			out.write(value.getData()!= null? toDelete(value.getData().toString()): "");
							break;
					} // switch				
  			LOG.debug("   "+ field.getField()+ ": "+ value.getData()+ " -> "+ value.getData());
				count++;
				if(count!= entity.size())
   			  out.write("\t");
			} // for
   		out.write("\n");
		} // for
	}
	
	public void procesar(boolean all) throws Exception {
		int top           = (new Long(Constantes.SQL_TOPE_REGISTROS)).intValue();
		BufferedWriter out= null;
		try {
			PageRecords pages= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), 0, top);
			if ((pages!= null) && (!pages.getList().isEmpty()))  {
   			pages.calculate(all);
				out = new BufferedWriter(new FileWriter(getNombre()));
		    header(out);
				List<LinkPage> list= pages.getPages();
				int conteo         = pages.getList().size();
				if(pages.getList()!= null && !pages.getList().isEmpty() && conteo != 0) {
					detail(out, (List)pages.getList());
					for(LinkPage page: list) {
					  PageRecords values= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), (int)(page.getIndex()* top), top);
						detail(out, (List)values.getList());
					} // for
				} // if	
			} // if
		} // try
		catch (Exception e) {
      Error.mensaje(e);
		} // catch
		finally {
			if(out!= null)
        out.close();
		} // finally
	} 
	
	public void procesar() throws Exception {
		procesar(false);
	}
}
