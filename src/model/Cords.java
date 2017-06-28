package model;

import java.io.Serializable;

/**
 * Przechowuje współrzędne pól na planszy oraz informację czy te współrzędne są prawidłowe.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class Cords implements Serializable
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Współrzędne pól na planszy. Stałe. Żeby mieć inne współrzędne, trzeba powołać nowy obiekt. */
    final private int x, y;

    /**
     * Konstruktor współrzędnych.
     * 
     * @param x Współrzędna x.
     * @param y Współrzędna y.
     */
    public Cords(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other)
    {
        boolean result = false;
        if (other instanceof Cords)
        {
            Cords that = (Cords) other;
            result = ((this.getX() == that.getX()) && (this.getY() == that.getY()));
        }
        return result;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 37 * (37 + getX()) + getY();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return x + " " + y;
    }

    /**
     * Zwraca współrzędna x.
     * 
     * @return Współrzędna x.
     */
    int getX()
    {
        return x;
    }

    /**
     * Zwraca współrzędna y.
     * 
     * @return Współrzędna y.
     */
    int getY()
    {
        return y;
    }

    /**
     * Zwraca true, jeśli pole o przechowywanych współrzędnych ma kolor czarny (leży na przekątnej).
     * 
     * @return True, jeśli współrzędne wskazują na czarne pole (leża na przekątnej).
     */
    boolean isBlack()
    {
        return (x + y) % 2 == 1;
    }

    /**
     * Sprawdza czy przechowywane współrzędne są prawidłowe.
     * 
     * @return True, jeśli współrzędne są prawidłowe, tj. mieszczą się na planszy.
     */
    boolean isLegalCords()
    {
        if ((y < 0) || (y >= 8) || (x < 0) || (x >= 8))
        {
            return false;
        }
        return true;
    }

    /**
     * Dodaje współrzędne.
     * 
     * @param cords Współrzędne do dodania.
     * @return Nowe współrzedne będące wynikiem dodawania.
     */
    Cords add(final Cords cords)
    {
        return new Cords(this.x + cords.x, this.y + cords.y);
    }
    
    /**
     * Mnoży współrzędne przez liczbę (skalar).
     * 
     * @param multiplier Mnożna.
     * @return Nowe współrzedne będące wynikiem mnożenia.
     */
    Cords multiply(final int multiplier)
    {
        Cords ret = new Cords(this.x * multiplier, this.y * multiplier);
        return ret;
    }

}
