package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import event.GameEvent;
import event.GameQueue;
import event.PlayerEscapeEvent;

/**
 * Klasa obsługująca jednego klienta.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class ServerThread implements Runnable
{
    /** Wątek obsługujący jednego klienta. */
    private Thread thread;
    /** Kolejka zdarzeń gry. */
    private final GameQueue gameQueue;
    /** Gniazdo klienta. */
    private final Socket clientSocket;
    /** Strumień wejścia od klienta. */
    private final ObjectInputStream input;
    /** Strumień wyjścia do klienta. */
    private final ObjectOutputStream output;

    /**
     * Konstruktor.
     * 
     * @param gameQueue Kolejka zdarzeń gry.
     * @param clientSocket Gniazdo klienta.
     * @throws IOException
     */
    ServerThread(final GameQueue gameQueue, final Socket clientSocket) throws IOException
    {
        this.gameQueue = gameQueue;
        this.clientSocket = clientSocket;
        this.thread = new Thread(this);
        this.input = new ObjectInputStream(clientSocket.getInputStream());
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Wątek czytający zdarzenia przesyłane przez klienta.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        while (thread != null)
        {
            // Czeka na obiekty przesyłane przez graczy.
            try
            {
                GameEvent gameEvent = (GameEvent) input.readObject();
                gameQueue.put(gameEvent);
            }
            catch (IOException e)
            {
                // Zerwano połączenie z graczem.
                gameQueue.put(new PlayerEscapeEvent());
                break;
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                break;
            }
        }

        // Zamykamy strumienie i gniazdo dla klienta.
        try
        {
            input.close();
            output.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wysyła zdarzenie do klienta.
     * 
     * @param gameEvent
     */
    void send(final GameEvent gameEvent)
    {
        if (clientSocket.isClosed())
        {
            return;
        }
        
        try
        {
            output.writeObject(gameEvent);
            output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Startuje wątek czytający zdarzenia od klienta.
     */
    void start()
    {
        thread.start();
    }

    /**
     * Zatrzymuje wątek czytający zdarzenia od klienta.
     */
    void stop()
    {
        thread = null;
    }

}
