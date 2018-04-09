package mx.org.kaana.libs.parser;

import com.eteks.parser.PascalSyntax;

public class SecoSyntax extends PascalSyntax {

  private boolean extendedSyntax;

  public SecoSyntax () {
    this(true);
  }

  public SecoSyntax (boolean extendedSyntax) {
    setExtendedSyntax(extendedSyntax);
    if (extendedSyntax) {
      setConstantKey("true", CONSTANT_TRUE);
      setConstantKey("false", CONSTANT_FALSE);
    }
  }

  private void setExtendedSyntax(boolean extendedSyntax) {
    this.extendedSyntax = extendedSyntax;
  }

  public boolean isExtendedSyntax() {
    return extendedSyntax;
  }

	@Override
  public Object getLiteral(String expression, StringBuffer extractedLiteral) {
    Object literal = getLiteralNumber (expression, extractedLiteral);
    if (literal != null)
      return literal;
    else
      if (extendedSyntax)
        return getLiteralString (expression, extractedLiteral);
      else
       return null;
  }

  protected Object getLiteralString(String expression, StringBuffer extractedString) {
    if (expression.length() >= 2)
      if (expression.charAt(0) == '\'') {
        if (expression.length() > 2) {
          // Parse character
          StringBuffer buffer = new StringBuffer();
          int c = getCharacter(expression, 1, buffer);
          if (c != -1 && expression.length() >= 2 + buffer.length() && expression.charAt(buffer.length() + 1) == '\'') {
            extractedString.append('\'');
            extractedString.append(buffer.toString());
            extractedString.append('\'');
            return new Character((char)c);
          } // if
        } // if
      } // if
      else
        if (expression.charAt(0) == '\"') {
          // Parse string
          StringBuffer literalString = new StringBuffer();
          StringBuffer buffer = new StringBuffer();
          int index;
          for (index = 1; index < expression.length() && expression.charAt(index) != '\"'; index += buffer.length(), buffer.setLength(0)) {
            // v1.0.1 Used 1 instead of index
            int c = getCharacter(expression, index, buffer);
            if (c == -1)
              return null;
            else
              literalString.append((char)c);
          } // for
          // v1.0.1 Bad comparison
          if(index < expression.length() && expression.charAt(index) == '\"') {
            extractedString.append(expression.substring(0, index + 1));
            return literalString.toString();
          } // if
        } // if
    return null;
  }

  protected int getCharacter(String expression, int index, StringBuffer extractedString) {
    char c = expression.charAt(index);
    if (c != '\\') {
      extractedString.append(c);
      return c;
    } // if
    else
      if (index + 1 < expression.length())
        switch (expression.charAt(index + 1)) {
        case 'b':
          extractedString.append("\\b");
          return '\b';
        case 't':
          extractedString.append("\\t");
          return '\t';
        case 'n':
          extractedString.append("\\n");
          return '\n';
        case 'f':
          extractedString.append("\\f");
          return '\f';
        case 'r':
          extractedString.append("\\r");
          return '\r';
        case '\"':
          extractedString.append("\\\"");
          return '\"';
        case '\'':
          extractedString.append("\\\'");
          return '\'';
        case '\\':
          extractedString.append("\\\\");
          return '\\';
        default:
          if ((c = expression.charAt(index + 1)) >= '0' && c <= '7') {
            // Parse octal character
            int i = 2;
            while (i < 4 && index + i < expression.length() && (c = expression.charAt(index + i)) >= '0' && c <= '7')
              i++;
            if (i < 4 || expression.charAt(index + 1) <= '3') {
              // Numbers with 3 octal digits may start only with 0 1 2 or 3
              extractedString.append(expression.substring(index, index + i));
              return Integer.parseInt(expression.substring(index + 1, index + i), 8);
            } // if
          } // if
          else
            if (index + 5 < expression.length())
              if (expression.charAt(index + 1) == 'u') {
                // Parse Unicode character
                for (int i = 2; i < 6; i++)
                  if (!((c = expression.charAt(index + i)) >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F'))
                    return -1;
                extractedString.append(expression.substring(index, index + 6));
                return Integer.parseInt(expression.substring(index + 2, index + 6), 16);
              } // if
        } // switch

    return -1;
  }

}
