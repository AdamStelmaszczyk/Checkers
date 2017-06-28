package event;

import java.io.Serializable;

/**
 * Klasa abstrakcyjna zdarzenia gry. Z niej dziedziczą wszystkie zdarzenia gry.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public abstract class GameEvent implements Serializable
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    
}
