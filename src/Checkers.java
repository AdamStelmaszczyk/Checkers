import view.View;

/**
 * Klasa klienta gry - składająca się z widoku i jednostki siecowej.
 * 
 * @author Adam Stelmaszczyk
 * @see <a href="http://mion.elka.pw.edu.pl/~astelma1/">Strona domowa</a>
 * @version 2011-05-27
 */
public class Checkers
{
    /**
     * Uruchamia grę.
     * 
     * @param args Argumenty wywołania programu, nie używane.
     */
    public static void main(final String[] args)
    {
        View view = new View();
        view.showInitially();
    }

}
