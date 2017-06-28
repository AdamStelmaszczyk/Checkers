package event;

import model.PlayerColor;

/**
 * Zdarzenie końca gry.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class EndGameEvent extends GameEvent
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Kolor gracza, który zwyciężył. */
    private final PlayerColor winnerColor;

    /**
     * Konstruktor.
     * 
     * @param winnerColor Kolor gracza, który zwyciężył.
     */
    public EndGameEvent(final PlayerColor winnerColor)
    {
        this.winnerColor = winnerColor;
    }

    /**
     * Zwraca kolor gracza, który zwyciężył.
     * 
     * @return Kolor gracza, który zwyciężył.
     */
    public PlayerColor getWinnerColor()
    {
        return winnerColor;
    }

}
