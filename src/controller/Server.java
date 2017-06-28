package controller;

import java.io.IOException;

import model.Model;
import event.GameQueue;

/**
 * Klasa serwera gry - składającego się z jednostki sieciowej, kolejki, kontrolera i modelu.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class Server
{
    /** Połączenie sieciowe serwera. */
    private final ServerNetwork serverNetwork;
    /** Referencja na kontroler. */
    private final Controller controller;

    /**
     * Konstruktor.
     */
    public Server()
    {
        Model model = new Model();
        GameQueue gameQueue = new GameQueue();
        serverNetwork = new ServerNetwork(gameQueue);
        controller = new Controller(model, gameQueue, serverNetwork);
    }

    /**
     * Startuje serwer.
     * 
     * @throws IOException
     */
    public void start() throws IOException
    {
        // Jednostka sieciowa zaczyna nasłuchiwać połączeń.
        serverNetwork.start();

        // Kontroler zaczyna sprawdzać kolejkę.
        controller.start();
    }

}
