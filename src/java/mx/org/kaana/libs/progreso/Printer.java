package mx.org.kaana.libs.progreso;


public interface Printer {

    public abstract boolean checkError();

    public abstract void close();

    public abstract void flush();

    public abstract void print(boolean flag);

    public abstract void print(char c);

    public abstract void print(char ac[]);

    public abstract void print(double d);

    public abstract void print(float f);

    public abstract void print(int i);

    public abstract void print(long l);

    public abstract void print(Object obj);

    public abstract void print(String s);

    public abstract void println();

    public abstract void println(boolean flag);

    public abstract void println(char c);

    public abstract void println(char ac[]);

    public abstract void println(double d);

    public abstract void println(float f);

    public abstract void println(int i);

    public abstract void println(long l);

    public abstract void println(Object obj);

    public abstract void println(String s);

}
