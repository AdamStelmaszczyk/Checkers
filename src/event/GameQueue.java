package event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Kolejka zdarzeń gry. Zdarzenia oczekują tutaj na obsłużenie.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class GameQueue
{
    /** Kolejka FIFO usypiająca wątki. */
    private final BlockingQueue<GameEvent> queue;

    /**
     * Konstruktor.
     */
    public GameQueue()
    {
        queue = new LinkedBlockingQueue<GameEvent>();
    }

    /**
     * Wstawia do kolejki nowy obiekt.
     * 
     * @param event Zdarzenie gry.
     */
    public void put(final GameEvent event)
    {
        try
        {
            queue.put(event);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera z kolejki obiekt. Jesli nie ma żadnego, usypia wątek.
     * 
     * @return Zdarzenie gry.
     */
    public GameEvent take()
    {
        GameEvent ret = null;
        try
        {
            ret = queue.take();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

}
