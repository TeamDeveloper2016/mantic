package mx.org.kaana.libs.ftp;

import cz.dhl.ftp.FtpConnect;
import cz.dhl.ftp.FtpOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Error;
import net.sf.cleanftp.CleanFTP;
import net.sf.cleanftp.file.RemoteFile;

public class Ftp {

	private FtpProperties ftpProperties;

	public Ftp(FtpProperties ftpProperties) {
		setFtpProperties(ftpProperties);
	}

	private void setFtpProperties(FtpProperties ftpProperties) {
		this.ftpProperties= ftpProperties;
	}

	public FtpProperties getFtpProperties() {
		return ftpProperties;
	}

	public boolean mkdir(String path) throws Exception {
		FtpConnect connect     = null;
		cz.dhl.ftp.FtpFile file= null;
		cz.dhl.ftp.Ftp cl      = new cz.dhl.ftp.Ftp();
		RemoteFile remotefile  = null;
		boolean regresar       = true;
		try {
			remotefile=exist(path);
			if (remotefile==null) {
				connect=getConnection();
				cl.connect(connect);
				file=new cz.dhl.ftp.FtpFile(path, cl);
				regresar=file.mkdir();
			} // if		  
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			if (cl!=null&&cl.isConnected()) {
				cl.disconnect();
			} // if
		} // finally
		return regresar;
	} // mkdir

	public RemoteFile exist(String pathFile) {
		CleanFTP ftp         = new CleanFTP();
		List<RemoteFile> file= null;
		RemoteFile remoteFile= null;
		try {
			ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
			ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
			file=ftp.list(pathFile);
			if (file!=null&&!file.isEmpty()) {
				remoteFile=file.get(0);
			} // if
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (ftp!=null) {
				try {
					ftp.closeConnection();
				} // try
				catch (Exception e) {
					Error.mensaje(e);
				} // catch
			} // catch   
		} // finally
		return remoteFile;
	} // exist

	private FtpConnect getConnection() {
		FtpConnect regresar= FtpConnect.newConnect("ftp://".concat(ftpProperties.getHost()));;
		regresar.setUserName(this.ftpProperties.getUser());
		regresar.setPassWord(this.ftpProperties.getPassword());
		regresar.setPortNum(this.ftpProperties.getPort());
		return regresar;
	} // getConnection

	public boolean upload(String origen, String destino, Writer out) {
		FtpOutputStream os     = null;
		boolean regresar       = false;
		FtpConnect cn          = getConnection();
		cz.dhl.ftp.Ftp cl      = new cz.dhl.ftp.Ftp();
		cz.dhl.ftp.FtpFile file= null;
		try {
			cl.connect(cn);
			file=new cz.dhl.ftp.FtpFile(destino, cl);
			if (!file.exists()) {
				file.mkdirs();
			} // if
			boolean append= false;
			os= new FtpOutputStream(file, append);
			java.io.File archivo= new java.io.File(origen);
			FileInputStream flujo= new FileInputStream(archivo);
			byte[] bytes= new byte[(int) archivo.length()];
			flujo.read(bytes);
			flujo.close();
			os.write(bytes);
			os.close();
			regresar= true;
			if (os!=null) {
				try {
					os.close();
				} // try
				catch (Exception e) {
					Error.mensaje(e);
					regresar=false;
				} // catch
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			regresar=false;
		} // catch 
		finally {
			if (cl!=null&&cl.isConnected()) {
				cl.disconnect();
			} // if
		} // finally
		return regresar;
	} // upload

	public boolean download(String origen, String destino, Writer out) throws Exception {
		CleanFTP ftp    = null;
		boolean regresar= false;
		try {
			//String temp=destino.substring(0, destino.lastIndexOf("" + java.io.File.separatorChar));
			//Archivo.verificaRuta(temp + java.io.File.separatorChar );
			ftp= new CleanFTP();
			ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
			ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
			final FtpListener listener=new FtpListener(out);
			ftp.addFileTransferListener(listener);
			ftp.setTransferTypeToBinary();
			regresar=ftp.download(origen, destino, true);
			ftp.closeConnection();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (ftp!=null) {				
					ftp.closeConnection();				
			} // if
			ftp= null;
		} // finally
		return regresar;
	} // download

	public boolean downloadList(List<FtpElementoLista> lista) {
		CleanFTP ftp     = null;
		boolean resultado= true;
		try {
			ftp= new CleanFTP();
			for (FtpElementoLista elemento : lista) {
				ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
				ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
				ftp.setTransferTypeToBinary();
				Archivo.verificaRuta(elemento.getDestino().substring(0, elemento.getDestino().lastIndexOf("/")+1));
				ftp.download(elemento.getOrigen(), elemento.getDestino(), true);
				ftp.closeConnection();
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			resultado=false;
		} // catch
		finally {
			ftp=null;
		} // finally
		return resultado;
	} // downloadList

	public boolean delete(String archivo) {
		CleanFTP ftp    = null;
		boolean regresar= false;
		try {
			ftp= new CleanFTP();
			ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
			ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
			ftp.setTransferTypeToBinary();
			regresar=ftp.delete(archivo);
			ftp.closeConnection();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (ftp!=null) {
				try {
					ftp.closeConnection();
				} // try
				catch (IOException ex) {
				} // catch
			} // if
			ftp=null;
		} // finally
		return regresar;
	} // delete

	public boolean delete(List<String> archivos) throws Exception {
		CleanFTP ftp    = null;
		boolean regresar= false;
		try {
			if (archivos!=null && archivos.size()!=0) {
				ftp=new CleanFTP();
				ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
				ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
				ftp.setTransferTypeToBinary();
				regresar=ftp.delete(archivos.toArray(new String[archivos.size()]));
				ftp.closeConnection();
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			if (ftp!=null) {
				ftp.closeConnection();
			} // if
			ftp=null;
		} // finally
		return regresar;
	} // delete

	public boolean deleteAllFiles(String ruta) {
		List<String> archivos= null;
		boolean regresar     = false;
		try {
			archivos= new ArrayList<>();
			archivos=navegacionRuta(ruta, archivos);
			regresar=delete(archivos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (archivos!=null) {
				archivos.clear();
			} // if
			archivos=null;
		} // finally
		return regresar;
	} // deleteAllFiles

	public List<String> navegacionRuta(String ruta, List<String> archivos) throws Exception {
		CleanFTP ftp= null;
		try {
			ftp= new CleanFTP();
			ftp.openConnection(getFtpProperties().getHost(), getFtpProperties().getPort());
			ftp.login(getFtpProperties().getUser(), getFtpProperties().getPassword());
			ftp.setTransferTypeToBinary();
			List<RemoteFile> files=ftp.list(ruta);
			if (files!=null) {
				for (RemoteFile file : files) {
					if (file.isDirectory()) {
						navegacionRuta(ruta.concat("/").concat(file.getName()), archivos);
					} // if
					else {
						archivos.add(ruta.concat("/").concat(file.getName()));
					} // else
				} // for        
			} // if
			ftp.closeConnection();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (ftp!=null) {
				ftp.closeConnection();
			} // if
		} // finally
		return archivos;
	} // navegacionRuta

	public void copy(String origen, String destino, Writer out) {
		java.io.File file= null;
		String path      = null;
		String temp      = null;
		try {
			path= getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			temp= path.toString().concat("ftp/");
			Archivo.verificaRuta(temp);
			String archivo=origen.substring(origen.lastIndexOf("/")+1);
			download(origen, temp.concat(archivo), out);
			upload(temp.concat(archivo), destino, out);
			file=new java.io.File(temp.concat(archivo));
			file.delete();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch     
	} // copy

	public static void main(String... args) throws IOException, Exception {
		Ftp ftp= new Ftp(new FtpProperties("10.1.8.14", "inegi/cilci.enigh", "Tezozomoc2008", 21));
		ftp.download("captura_mcs2013/Datos/ENT_01/DEC_0/ZAZJAAZ/MayZAZJAAZ.csp","C:/Desarrollo/Plataforma/Netbeans/IKTAN14062013/mixto/build/web/WEB-INF/comunicacion/exportar/mcs2013/ZAZJAAZ/MayZAZJAAZ.csp",null);
	} // main
}
