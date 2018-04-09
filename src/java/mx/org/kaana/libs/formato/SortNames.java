package mx.org.kaana.libs.formato;

public enum SortNames {

  SORT_TEXT("sortText"), SORT_TEXTDES("sortTextDesc"), SORT_NUMERIC("sortNumeric"), SORT_NUMERICDES("sortNumericDesc"), RESERVED("reserved");

  private String name;

  private SortNames(String name) {
    this.name= name;
  }

  public String getName() {
    return name;
  }

}
