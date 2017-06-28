package view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import model.FakeBoard;
import model.PlayerColor;
import controller.ServerConfig;
import event.EndGameEvent;
import event.FakeBoardEvent;
import event.GameEvent;
import event.PlayerEscapeEvent;
import event.WaitEvent;
import event.YourColorEvent;

/**
 * Obsługa sieciowa dla klienta.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class ClientNetwork implements Runnable
{
    /** Wątek klienta nasłuchujący czy przyszły jakieś obiekty. */
    private Thread thread;
    /** Host do którego się podłączamy. */
    private final String host;
    /** Gniazdo klienta. */
    private Socket clientSocket;
    /** Strumień wyjściowy klienta na obiekty. */
    private ObjectOutputStream output;
    /** Strumień wejściowy klienta na obiekty. */
    private ObjectInputStream input;
    /** Mapa odwzorowująca klasy zdarzeń gry w akcje, jakie należy podjąć, aby obsłużyc dany typ zdarzenia. */
    private final Map<Class<? extends GameEvent>, Action> gameEventToAction = new HashMap<Class<? extends GameEvent>, Action>();
    /** Referencja na widok. */
    private final View view;
    /** Nasz kolor. */
    private PlayerColor myColor;

    /**
     * Klasa abstrakcyjna obsługi zdarzenia. Z niej dziedziczą wszystkie akcje.
     */
    abstract private class Action
    {
        /**
         * Wykonuje swoją akcję.
         */
        abstract void execute(GameEvent event);
    }

    /**
     * Reakcja na koniec gry zgodny z zasadami gry. Pokazuje kto wygrał.
     */
    private class EndGameAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            EndGameEvent endGameEvent = (EndGameEvent) gameEvent;
            PlayerColor winnerColor = endGameEvent.getWinnerColor();
            view.showWinner(winnerColor);
            stop();
        }
    }

    /**
     * Reakcja klienta na otrzymanie kopii planszy. Pokazuje ją.
     */
    private class FakeBoardAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            FakeBoardEvent fakeBoardEvent = (FakeBoardEvent) gameEvent;
            FakeBoard fakeBoard = fakeBoardEvent.getFakeBoard();
            view.showAll(fakeBoard);
        }
    }

    /**
     * Reakcja klienta na otrzymanie zdarzenia Wait.
     * Zerwano połączenie z serwerem, bo przeciwnik uciekł - wyświetlamy stosowny komunikat.
     */
    private class PlayerEscapeAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            view.showMessageDialog("Przeciwnik uciekł.", "Koniec gry");
            stop();
        }
    }

    /**
     * Reakcja klienta na otrzymanie zdarzenia Wait.
     */
    private class WaitAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            view.showMessageDialog("Poczekaj na swoją kolej.", "Wskazówka");
        }
    }

    /**
     * Reakcja klienta na otrzymanie koloru.
     */
    private class YourColorAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            YourColorEvent colorEvent = (YourColorEvent) gameEvent;
            myColor = colorEvent.getColor();
            view.showMyColor(myColor);
            view.setEnabledBoard(true);
        }
    }

    /**
     * Konstruktor. Inicjalizuje mapę odwzorowująca zdarzenia gry w akcje.
     * 
     * @param host Host do którego się podłączamy.
     * @param view Referencja na widok.
     */
    ClientNetwork(final String host, final View view)
    {
        this.host = host;
        this.view = view;
        this.thread = new Thread(this);
        gameEventToAction.put(YourColorEvent.class, new YourColorAction());
        gameEventToAction.put(FakeBoardEvent.class, new FakeBoardAction());
        gameEventToAction.put(WaitEvent.class, new WaitAction());
        gameEventToAction.put(PlayerEscapeEvent.class, new PlayerEscapeAction());
        gameEventToAction.put(EndGameEvent.class, new EndGameAction());
    }

    /**
     * Wątek klienta sprawdza czy nie przyszły jakieś nowe obiekty od serwera.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        GameEvent gameEvent = null;
        while (thread != null)
        {
            try
            {
                gameEvent = (GameEvent) input.readObject();
            }
            catch (IOException e)
            {
                PlayerEscapeAction action = new PlayerEscapeAction();
                action.execute(null);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                break;
            }

            // Pobiera klasę odebranego zdarzenia.
            Class<?> eventClass = gameEvent.getClass();
            
            // Wybiera akcję związaną z pobraną klasą zdarzenia.
            Action gameAction = gameEventToAction.get(eventClass);
            
            // Wykonuje tą akcję, w argumencie przesyła zdarzenie, aby później wydobyć sobie szczegóły.
            gameAction.execute(gameEvent);
        }

        // Zamyka strumienie i gniazdo klienta.
        try
        {
            output.close();
            input.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca nasz kolor.
     * 
     * @return Nasz kolor.
     */
    PlayerColor getMyColor()
    {
        return myColor;
    }

    /**
     * Wysyła zdarzenie na serwer.
     * 
     * @param gameEvent Zdarzenie do wysłania.
     */
    void send(final GameEvent gameEvent)
    {
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
     * Startuje wątek klienta.
     * 
     * @throws IOException
     * @throws UnknownHostException
     */
    void start() throws UnknownHostException, IOException
    {
        clientSocket = new Socket(host, ServerConfig.PORT_NUMBER);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        input = new ObjectInputStream(clientSocket.getInputStream());
        thread.start();
    }

    /**
     * Zatrzymuje wątek klienta, włącza guziki, wyłącza planszę.
     */
    void stop()
    {
        thread = null;
        view.setEnabledBoard(false);
        view.setEnabledHostButton(true);
        view.setEnabledJoinButton(true);
    }

}
