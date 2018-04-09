package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 7/09/2015
 * @time 11:24:24 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Car implements Serializable {

  public String id;
  public String brand;
  public int year;
  public String color;
  public int price;
  public boolean sold;

  public Car() {
  }

  public Car(String id, String brand, int year, String color) {
    this.id = id;
    this.brand = brand;
    this.year = year;
    this.color = color;
  }

  public Car(String id, String brand, int year, String color, int price, boolean sold) {
    this.id = id;
    this.brand = brand;
    this.year = year;
    this.color = color;
    this.price = price;
    this.sold = sold;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBrand() {
    return this.brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public int getYear() {
    return this.year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public boolean isSold() {
    return this.sold;
  }

  public void setSold(boolean sold) {
    this.sold = sold;
  }

  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    Car other = (Car) obj;
    if (this.id == null ? other.id != null : !this.id.equals(other.id)) {
      return false;
    }
    return true;
  }
}
