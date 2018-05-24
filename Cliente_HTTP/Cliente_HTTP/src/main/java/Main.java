import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            print("Escriba una URL Valida:");
            String url = scanner.nextLine();
            print("Fetching %s...", url);

            Document doc = Jsoup.connect(url).get();
            Elements forms = doc.select("form");
            Elements imagenes = doc.select("img");
            Elements parafos = doc.select("p");
            //doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            String lineas = doc.toString();

            print("Lineas: (%d)", lineas.split("\r\n|\r|\n").length);

            print("\nImagenes: (%d)", imagenes.size());

            print("\nParafos: (%d)", parafos.size());

            print("\nFormularios por metodo GET: (%d)", doc.select("form[method=get]").size());

            print("\nFormularios por metodo POST: (%d)\n", doc.select("form[method=post]").size());

            for (Element form : forms) {
                print("Formulario: <%s>  (%s)", form.attr("method"), trim(form.text(), 40));
                if (form.attr("method").contentEquals("post")) {
                    Jsoup.connect(form.attr("action"))
                            .data("asignatura", "practica1")
                            .header("matricula", "2012-1039")
                            .post();
                    print("Respuesta: <%d> (%s)",
                            Jsoup.connect(form.attr("action")).response().statusCode(),
                            Jsoup.connect(form.attr("action")).response().statusMessage());

                }
                Elements inputs = form.select("input");
                for (Element input : inputs) {
                    print(" * Inputs: nombre=(%s)  type=(%s)", input.attr("name"), input.attr("type"));
                }
            }


        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}




