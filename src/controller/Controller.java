package controller;

import java.util.HashMap;
import java.util.Map;

import model.Cords;
import model.Model;
import model.PlayerColor;
import event.BoardClickEvent;
import event.EndGameEvent;
import event.FakeBoardEvent;
import event.GameEvent;
import event.GameQueue;
import event.PlayerEscapeEvent;
import event.StartGameEvent;
import event.WaitEvent;
import event.YourColorEvent;

/**
 * Kontroler serwera.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class Controller implements Runnable
{
    /** Referencja na model dla kontrolera. */
    private final Model model;
    /** Kolejka zdarzeń gry. */
    private final GameQueue gameQueue;
    /** Połączenie sieciowe serwera. */
    private final ServerNetwork serverNetwork;
    /** Mapa odwzorowująca klasy zdarzeń gry w akcje, jakie należy podjąć, aby obsłużyc dany typ zdarzenia. */
    private final Map<Class<? extends GameEvent>, Action> gameEventToAction = new HashMap<Class<? extends GameEvent>, Action>();
    /** Wątek kontrolera sprawdzający kolejkę. */
    private final Thread thread;

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
     * Akcja w odpowiedzi na zdarzenie na planszy.
     */
    private class BoardClickAction extends Action
    {
        @Override
        void execute(final GameEvent event)
        {
            // Pobiera zdarzenie kliknięcia w planszę.
            BoardClickEvent boardClickEvent = (BoardClickEvent) event;

            // Pobiera kolor gracza.
            PlayerColor playerColor = boardClickEvent.getColor();

            // Jesli tura nie nalezy do tego gracza to informujemy go, zeby poczekal.
            if (model.getWhoseTurnColor() != playerColor)
            {
                serverNetwork.sendToPlayer(playerColor, new WaitEvent());
                return;
            }

            // W przeciwnym razie:
            // Pobiera współrzędne kliknięcia z obiektu zdarzenia.
            Cords click = boardClickEvent.getCords();

            // Informuje model o kliknięciu.
            model.doClick(click);

            // Tworzy obiekt zdarzenia FakeBoard.
            FakeBoardEvent fakeBoardEvent = new FakeBoardEvent(model.getFakeBoard());

            // Wysyła kopie planszy do graczy.
            serverNetwork.sendToAll(fakeBoardEvent);

            // Jeśli koniec gry, to wyślij tą informację do wszystkich i zamknij serwer.
            if (model.isGameOver())
            {
                serverNetwork.sendToAll(new EndGameEvent(model.getWinnerColor()));
                serverNetwork.stop();
            }
        }
    }

    /**
     * Reakcja na ucieczkę jednego z graczy. Wysyła powiadomienia do graczy i zamyka serwer.
     */
    private class PlayerEscapeAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            serverNetwork.sendToAll(new PlayerEscapeEvent());
            serverNetwork.stop();
        }
    }

    /**
     * Reakcja na początek gry.
     */
    private class StartGameAction extends Action
    {
        @Override
        void execute(final GameEvent gameEvent)
        {
            // Wysyła graczom ich kolory.
            serverNetwork.sendToPlayer(PlayerColor.BLACK, new YourColorEvent(PlayerColor.BLACK));
            serverNetwork.sendToPlayer(PlayerColor.WHITE, new YourColorEvent(PlayerColor.WHITE));
            
            // Wysyła graczom startowe plansze.
            serverNetwork.sendToPlayer(PlayerColor.BLACK, new FakeBoardEvent(model.getFakeBoard()));
            serverNetwork.sendToPlayer(PlayerColor.WHITE, new FakeBoardEvent(model.getFakeBoard()));
        }
    }

    /**
     * Konstruktor. Inicjalizuje mapę odwzorowująca zdarzenia gry w akcje.
     * 
     * @param model Referencja na model dla kontrolera.
     * @param gameQueue Kolejka zdarzeń gry.
     * @param serverNetwork Połączenie sieciowe serwera.
     */
    Controller(final Model model, final GameQueue gameQueue, final ServerNetwork serverNetwork)
    {
        this.model = model;
        this.gameQueue = gameQueue;
        this.serverNetwork = serverNetwork;
        this.thread = new Thread(this);
        gameEventToAction.put(BoardClickEvent.class, new BoardClickAction());
        gameEventToAction.put(StartGameEvent.class, new StartGameAction());
        gameEventToAction.put(PlayerEscapeEvent.class, new PlayerEscapeAction());
    }

    /**
     * Główny wątek programu do końca życia sprawdza kolejkę zdarzeń. Jeśli kolejka jest pusta, wątek usypia.
     */
    public void run()
    {
        while (thread != null)
        {
            // Pobiera zdarzenie z kolejki, jak nic nie ma to usypia.
            GameEvent gameEvent = gameQueue.take();

            // Pobiera klasę tego zdarzenia.
            Class<?> eventClass = gameEvent.getClass();

            // Wybiera akcję związaną z pobraną klasą zdarzenia.
            Action gameAction = gameEventToAction.get(eventClass);

            // Wykonuje tą akcję, w argumencie przesyła zdarzenie, aby później wydobyć sobie szczegóły.
            gameAction.execute(gameEvent);
        }
    }

    /**
     * Startuje wątek kontrolera sprawdzający kolejkę.
     */
    void start()
    {
        thread.start();
    }

}
