package event;

import model.PlayerColor;

/**
 * Zdarzenie wysyłane przez serwer na początku gry do klientów informujące ich o kolorze.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class YourColorEvent extends GameEvent
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Kolor jaki otrzyma gracz. */
    private final PlayerColor color;

    /**
     * Konstruktor.
     * 
     * @param color Kolor jaki otrzyma gracz.
     */
    public YourColorEvent(final PlayerColor color)
    {
        this.color = color;
    }

    /**
     * Zwraca kolor jaki otrzyma gracz.
     * 
     * @return Kolor jaki otrzyma gracz.
     */
    public PlayerColor getColor()
    {
        return color;
    }

}
