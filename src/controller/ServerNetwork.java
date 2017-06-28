package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.PlayerColor;
import event.GameEvent;
import event.GameQueue;
import event.StartGameEvent;

/**
 * Połączenie sieciowe serwera.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class ServerNetwork implements Runnable
{
    /** Kolejka zdarzeń. */
    private final GameQueue gameQueue;
    /** Wątek serwera oczekujący na połączenia. */
    private Thread thread;
    /** Gniazdo serwera. */
    private ServerSocket serverSocket;
    /** Wątki obsługujące klientów. */
    private final Map<PlayerColor, ServerThread> serverThreads = new HashMap<PlayerColor, ServerThread>();

    /**
     * Konstruktor.
     * 
     * @param gameQueue Kolejka zdarzeń.
     */
    ServerNetwork(final GameQueue gameQueue)
    {
        this.gameQueue = gameQueue;
        this.thread = new Thread(this);
    }

    /**
     * Nasłuchuje na połączenia klientów.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        while (thread != null)
        {
            try
            {
                // Czeka na połączenie klienta.
                Socket clientSocket = serverSocket.accept();

                // W momencie połączenia tworzy nowy wątek dla klienta.
                ServerThread serverThread = new ServerThread(gameQueue, clientSocket);
                serverThread.start();

                // Przydziala kolor graczowi.
                if (serverThreads.isEmpty())
                {
                    serverThreads.put(PlayerColor.BLACK, serverThread);
                }
                else
                {
                    // Mamy komplet graczy - kończymy nasłuchiwać na połączenia klientów i zaczynamy grę.
                    serverThreads.put(PlayerColor.WHITE, serverThread);
                    gameQueue.put(new StartGameEvent());
                    thread = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Zamykamy gniazo serwera.
        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wysyła dane zdarzenie do wszystkich graczy.
     * 
     * @param gameEvent Zdarzenie do wysłania.
     */
    void sendToAll(final GameEvent gameEvent)
    {
        sendToPlayer(PlayerColor.BLACK, gameEvent);
        sendToPlayer(PlayerColor.WHITE, gameEvent);
    }

    /**
     * Wysyła zdarzenie gameEvent do danego gracza o kolorze playerColor.
     * 
     * @param playerColor Kolor gracza.
     * @param gameEvent Zdarzenie do wysłania.
     */
    void sendToPlayer(final PlayerColor playerColor, final GameEvent gameEvent)
    {
        ServerThread serverThread = serverThreads.get(playerColor);
        serverThread.send(gameEvent);
    }

    /**
     * Startuje wątek nasłuchujący na połączenia klientów.
     * 
     * @throws IOException
     */
    void start() throws IOException
    {
        this.serverSocket = new ServerSocket(ServerConfig.PORT_NUMBER);
        thread.start();
    }

    /**
     * Zatrzymuje wątek nasłuchujący na połączenia klientów oraz wszystkie wątki obsługujące klientów.
     */
    void stop()
    {
        Collection<ServerThread> collection = serverThreads.values();
        Iterator<ServerThread> it = collection.iterator();
        while (it.hasNext())
        {
            ServerThread serverThread = it.next();
            serverThread.stop();
        }
        thread = null;
    }

}
