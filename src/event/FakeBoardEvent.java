package event;

import model.FakeBoard;

/**
 * Kopia planszy przesyłana przez sieć.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class FakeBoardEvent extends GameEvent
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Kopia planszy. */
    private final FakeBoard fakeBoard;

    /**
     * Konstruktor.
     * 
     * @param fakeBoard Kopia planszy do przesłania.
     */
    public FakeBoardEvent(final FakeBoard fakeBoard)
    {
        this.fakeBoard = fakeBoard;
    }

    /**
     * Zwraca kopię planszy.
     * 
     * @return Kopia planszy.
     */
    public FakeBoard getFakeBoard()
    {
        return fakeBoard;
    }

}
