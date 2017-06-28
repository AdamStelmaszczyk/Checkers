package event;

import model.Cords;
import model.PlayerColor;

/**
 * Kliknięcie na planszy.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class BoardClickEvent extends GameEvent
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Współrzędne kliknięcia. */
    private final Cords click;
    /** Kolor gracza. */
    private final PlayerColor playerColor;

    /**
     * Konstruktor.
     * 
     * @param click Współrzędne kliknięcia.
     */
    public BoardClickEvent(final Cords click, final PlayerColor playerColor)
    {
        this.click = click;
        this.playerColor = playerColor;
    }

    /**
     * Zwraca kolor gracza, który kliknął.
     * 
     * @return Kolor gracza.
     */
    public PlayerColor getColor()
    {
        return playerColor;
    }

    /**
     * Zwraca współrzędne kliknięcia.
     * 
     * @return Współrzędne kliknięcia.
     */
    public Cords getCords()
    {
        return click;
    }

}
