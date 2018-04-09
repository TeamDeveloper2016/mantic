package mx.org.kaana.libs.archivo;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.LinkPage;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.procesos.reportes.beans.Field;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.kajool.procesos.reportes.enums.ETypeField;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.xml.Tokens;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Dbase extends Tokens implements Serializable{
  
  private static final long serialVersionUID= 7181686962555656463L;
  private static Log LOG                    = LogFactory.getLog(Dbase.class);	
	private int columns;
	private boolean validate;
  private List<Field> campos;

	public Dbase(String nombre, Modelo definicion) throws Exception {
		this(nombre, definicion, false);		
	}
	
	public Dbase(String nombre, Modelo definicion, boolean validate) throws Exception {
		this(nombre, definicion, validate, 0);
	}
	
	public Dbase(String nombre, Modelo definicion, boolean validate, int columns) throws Exception {
		this(nombre, definicion, validate, 0, Collections.EMPTY_LIST);
	}
  
	public Dbase(String nombre, Modelo definicion, boolean validate, int columns, List<Field> campos) throws Exception {
		super(nombre, definicion);
		this.columns = columns;
		this.validate= validate;
    this.campos= campos;
	}

	private void header(DBFWriter writer) throws DBFException {
		int count         = 0;
		this.columns      = getFields().size();
		List<String> names= new ArrayList<>();
		DBFField fields[] = new DBFField[this.columns];
		LOG.info(this.getFields());
		try {
			for (MetaField field: getFields()) {
				LOG.info(field.getName()+ ": "+ field.getLength());
				fields[count] = new DBFField();
				String alias= fixName(field.getField());
				int repeat  = -1;
				while(names.indexOf(alias)>= 0 && repeat< 10) {
					repeat++;
					alias= alias.substring(0, alias.length()- 1)+ repeat;
				} // while	
				names.add(alias);
				fields[count].setName(alias);
				if(this.validate) {
					fields[count].setDecimalCount(field.getScale() >= 0 ? field.getScale() : 0);
					fields[count].setFieldLength((field.getLength() + field.getScale()) <= 0 ? 1 : (field.getLength() + field.getScale()));
				} // if
        else {
					fields[count].setDecimalCount(field.getScale());
					fields[count].setFieldLength(field.getLength()+ field.getScale());
				} // else
				// verificar la longitud del campo y los caracteres con los que terminar el nombre __N (entero), __F (flotante), __T (flotante 2 decimales)
				if(field.getField().endsWith(FORCE_INTEGER)){
					fields[count].setDecimalCount(0);
					fields[count].setDataType(DBFField.FIELD_TYPE_N);
				} // if
				else if(field.getField().endsWith(FORCE_FLOAT)){
					fields[count].setDecimalCount(3);
					fields[count].setDataType(DBFField.FIELD_TYPE_N);
				} // else if
				else if(field.getField().endsWith(FORCE_DOUBLE)){
					fields[count].setDecimalCount(2);
					fields[count].setDataType(DBFField.FIELD_TYPE_N);
				} // else if
				else		
					switch(field.getType()) {
						case java.sql.Types.BIGINT:
						case java.sql.Types.INTEGER:
						case java.sql.Types.SMALLINT:
						case java.sql.Types.NUMERIC:
						case java.sql.Types.DECIMAL:
						case java.sql.Types.DOUBLE:
						case java.sql.Types.FLOAT:
						case java.sql.Types.REAL:
							fields[count].setDataType(DBFField.FIELD_TYPE_N);
							break;
						case java.sql.Types.DATE:
						case java.sql.Types.TIME:
						case java.sql.Types.TIMESTAMP:
							fields[count].setDataType(DBFField.FIELD_TYPE_D);
							break;
						case java.sql.Types.ARRAY:
						case java.sql.Types.CHAR:
						case java.sql.Types.LONGNVARCHAR:
						case java.sql.Types.LONGVARCHAR:
						case java.sql.Types.NCHAR:
						case java.sql.Types.NVARCHAR:
						case java.sql.Types.VARCHAR:
							fields[count].setDataType(DBFField.FIELD_TYPE_C);
							break;
					} // switch
        int index= this.campos.indexOf(new Field(alias.toLowerCase()));
        if(!this.campos.isEmpty() && index>= 0) {
          Field item= this.campos.get(index);
          switch(item.getType()) {
            case VARCHAR:
              fields[count].setDataType(DBFField.FIELD_TYPE_C);
              break;
            case FLOAT:
            case INTEGER:
              fields[count].setDataType(DBFField.FIELD_TYPE_N);
              break;
            case DATE:
              fields[count].setDataType(DBFField.FIELD_TYPE_D);
              break;
          } // switch
          if(!item.getType().equals(ETypeField.DATE)) {
            fields[count].setFieldLength(item.getLength());
            fields[count].setDecimalCount(item.getScale());
          } // if  
        } // if
				count++;
			} // for
		} // try
		finally {
			Methods.clean(names);
		} // finally
  	writer.setFields(fields);
	}
	
	private void detail(DBFWriter writer, List<Entity> records) throws DBFException {
		int record= 0;
		for(Entity entity: records) {
  		Object rowData[] = new Object[this.columns];
			int count= 0;
 			LOG.debug("Record : "+ ++record);
  		for (MetaField field: getFields()) {
	  		try {
        Value value= entity.get(field.getName());
				if(field.getField().endsWith(FORCE_INTEGER) || field.getField().endsWith(FORCE_DOUBLE) || field.getField().endsWith(FORCE_FLOAT)) 
					rowData[count]= value.getData()!= null? Numero.getDouble(value.getData().toString()): null;
				else
					switch(field.getType()) {
						case java.sql.Types.BIGINT:
						case java.sql.Types.INTEGER:
						case java.sql.Types.SMALLINT:
						case java.sql.Types.NUMERIC:
						case java.sql.Types.DECIMAL:
						case java.sql.Types.DOUBLE:
						case java.sql.Types.FLOAT:
						case java.sql.Types.REAL:
							rowData[count]= value.getData()!= null? value.getData() instanceof Integer? ((Integer)value.getData()).doubleValue(): value.getData() instanceof BigInteger? ((BigInteger)value.getData()).doubleValue(): ((BigDecimal)value.getData()).doubleValue(): null;
							break;
						case java.sql.Types.DATE:
							rowData[count]= value.getData()!= null? new Date(((Date)value.getData()).getTime()):null;
							break;
						case java.sql.Types.TIME:
							rowData[count]= value.getData()!= null? new Date(((Time)value.getData()).getTime()):null;
							break;
						case java.sql.Types.TIMESTAMP:
							rowData[count]= value.getData()!= null? new Date(((Timestamp)value.getData()).getTime()):null;
							break;
						case java.sql.Types.ARRAY:
						case java.sql.Types.CHAR:
						case java.sql.Types.LONGNVARCHAR:
						case java.sql.Types.LONGVARCHAR:
						case java.sql.Types.NCHAR:
						case java.sql.Types.NVARCHAR:
						case java.sql.Types.VARCHAR:
							rowData[count]= value.getData();
							break;
					} // switch				
  			LOG.debug("   "+ field.getField()+ " ["+ field.getType()+ "]: "+ value.getData()+ " -> "+ rowData[count]);
				count++;  
        } catch (Exception e) {
          System.out.println(e);
          throw e;
        }
        
			} // for
			writer.addRecord(rowData);
			rowData = null;
		} // for
	}
	
	public void procesar(boolean all) throws Exception {
		int top         = (new Long(Constantes.SQL_TOPE_REGISTROS)).intValue();
    DBFWriter writer= null;
		try {
			PageRecords pages= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), 0, top);
			if ((pages!= null) && (!pages.getList().isEmpty()))  {
				writer= new DBFWriter(new File(getNombre()));
				pages.calculate(all);
				List<LinkPage> list= pages.getPages();
				int conteo= pages.getList().size();
				if(pages.getList()!= null && !pages.getList().isEmpty() && conteo != 0) {
					header(writer);
					detail(writer, (List)pages.getList());
					for(LinkPage page: list) {
						PageRecords values= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), (int)(page.getIndex()* top), top);
						detail(writer, (List)values.getList());
					} // for
				} // if	
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
		finally {
  		// Esto es para cerrar el archivo
			if(writer!= null)
				writer.write(null);
			writer= null;
		} // finally
	}
	
	public void procesar() throws Exception {
		procesar(false);
	}
}
