package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import mx.org.kaana.kajool.procesos.acceso.beans.Car;
import mx.org.kaana.kajool.procesos.acceso.beans.Document;


/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 7/09/2015
 * @time 11:24:56 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class CarService {

  private static final String[] colors = new String[10];
  private static final String[] brands;

  public List<Car> createCars(int size) {
    ArrayList<Car> list = new ArrayList<Car>();
    for (int i = 0; i < size; ++i) {
      list.add(new Car(this.getRandomId(), this.getRandomBrand(), this.getRandomYear(), this.getRandomColor(), this.getRandomPrice(), this.getRandomSoldState()));
    }
    return list;
  }

  private String getRandomId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  private int getRandomYear() {
    return (int) (Math.random() * 50.0 + 1960.0);
  }

  private String getRandomColor() {
    return colors[(int) (Math.random() * 10.0)];
  }

  private String getRandomBrand() {
    return brands[(int) (Math.random() * 10.0)];
  }

  private int getRandomPrice() {
    return (int) (Math.random() * 100000.0);
  }

  private boolean getRandomSoldState() {
    return Math.random() > 0.5;
  }

  public List<String> getColors() {
    return Arrays.asList(colors);
  }

  public List<String> getBrands() {
    return Arrays.asList(brands);
  }

  static {
    CarService.colors[0] = "Black";
    CarService.colors[1] = "White";
    CarService.colors[2] = "Green";
    CarService.colors[3] = "Red";
    CarService.colors[4] = "Blue";
    CarService.colors[5] = "Orange";
    CarService.colors[6] = "Silver";
    CarService.colors[7] = "Yellow";
    CarService.colors[8] = "Brown";
    CarService.colors[9] = "Maroon";
    brands = new String[10];
    CarService.brands[0] = "Alejandro";
    CarService.brands[1] = "Miguel Angel";
    CarService.brands[2] = "Claudio Fernando";
    CarService.brands[3] = "Maria del rosario";
    CarService.brands[4] = "Arturo";
    CarService.brands[5] = "Pedro Alfredo";
    CarService.brands[6] = "Jorge Alberto";
    CarService.brands[7] = "Laura Susana";
    CarService.brands[8] = "Rosa Elva";
    CarService.brands[9] = "Yadhira";
  }

  public TreeNode createDocuments() {
    TreeNode root = new DefaultTreeNode(new Document("Files", "-", "Folder"), null);

    TreeNode documents = new DefaultTreeNode(new Document("Documents", "-", "Folder"), root);
    TreeNode pictures = new DefaultTreeNode(new Document("Pictures", "-", "Folder"), root);
    TreeNode movies = new DefaultTreeNode(new Document("Movies", "-", "Folder"), root);

    TreeNode work = new DefaultTreeNode(new Document("Work", "-", "Folder"), documents);
    TreeNode primefaces = new DefaultTreeNode(new Document("PrimeFaces", "-", "Folder"), documents);

    //Documents
    TreeNode expenses = new DefaultTreeNode("document", new Document("Expenses.doc", "30 KB", "Word Document"), work);
    TreeNode resume = new DefaultTreeNode("document", new Document("Resume.doc", "10 KB", "Word Document"), work);
    TreeNode refdoc = new DefaultTreeNode("document", new Document("RefDoc.pages", "40 KB", "Pages Document"), primefaces);

    //Pictures
    TreeNode barca = new DefaultTreeNode("picture", new Document("barcelona.jpg", "30 KB", "JPEG Image"), pictures);
    TreeNode primelogo = new DefaultTreeNode("picture", new Document("logo.jpg", "45 KB", "JPEG Image"), pictures);
    TreeNode optimus = new DefaultTreeNode("picture", new Document("optimusprime.png", "96 KB", "PNG Image"), pictures);

    //Movies
    TreeNode pacino = new DefaultTreeNode(new Document("Al Pacino", "-", "Folder"), movies);
    TreeNode deniro = new DefaultTreeNode(new Document("Robert De Niro", "-", "Folder"), movies);

    TreeNode scarface = new DefaultTreeNode("mp3", new Document("Scarface", "15 GB", "Movie File"), pacino);
    TreeNode carlitosWay = new DefaultTreeNode("mp3", new Document("Carlitos' Way", "24 GB", "Movie File"), pacino);

    TreeNode goodfellas = new DefaultTreeNode("mp3", new Document("Goodfellas", "23 GB", "Movie File"), deniro);
    TreeNode untouchables = new DefaultTreeNode("mp3", new Document("Untouchables", "17 GB", "Movie File"), deniro);

    return root;
  }

  public TreeNode createCheckboxDocuments() {
    TreeNode root = new CheckboxTreeNode(new Document("Files", "-", "Folder"), null);

    TreeNode documents = new CheckboxTreeNode(new Document("Documents", "-", "Folder"), root);
    TreeNode pictures = new CheckboxTreeNode(new Document("Pictures", "-", "Folder"), root);
    TreeNode movies = new CheckboxTreeNode(new Document("Movies", "-", "Folder"), root);

    TreeNode work = new CheckboxTreeNode(new Document("Work", "-", "Folder"), documents);
    TreeNode primefaces = new CheckboxTreeNode(new Document("PrimeFaces", "-", "Folder"), documents);

    //Documents
    TreeNode expenses = new CheckboxTreeNode("document", new Document("Expenses.doc", "30 KB", "Word Document"), work);
    TreeNode resume = new CheckboxTreeNode("document", new Document("Resume.doc", "10 KB", "Word Document"), work);
    TreeNode refdoc = new CheckboxTreeNode("document", new Document("RefDoc.pages", "40 KB", "Pages Document"), primefaces);

    //Pictures
    TreeNode barca = new CheckboxTreeNode("picture", new Document("barcelona.jpg", "30 KB", "JPEG Image"), pictures);
    TreeNode primelogo = new CheckboxTreeNode("picture", new Document("logo.jpg", "45 KB", "JPEG Image"), pictures);
    TreeNode optimus = new CheckboxTreeNode("picture", new Document("optimusprime.png", "96 KB", "PNG Image"), pictures);

    //Movies
    TreeNode pacino = new CheckboxTreeNode(new Document("Al Pacino", "-", "Folder"), movies);
    TreeNode deniro = new CheckboxTreeNode(new Document("Robert De Niro", "-", "Folder"), movies);

    TreeNode scarface = new CheckboxTreeNode("mp3", new Document("Scarface", "15 GB", "Movie File"), pacino);
    TreeNode carlitosWay = new CheckboxTreeNode("mp3", new Document("Carlitos' Way", "24 GB", "Movie File"), pacino);

    TreeNode goodfellas = new CheckboxTreeNode("mp3", new Document("Goodfellas", "23 GB", "Movie File"), deniro);
    TreeNode untouchables = new CheckboxTreeNode("mp3", new Document("Untouchables", "17 GB", "Movie File"), deniro);

    return root;
  }

}
