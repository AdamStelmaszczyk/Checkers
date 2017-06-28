package model;

/**
 * Dwa podstawowe kolory - biały i czarny.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public enum PlayerColor
{
    BLACK
    {
        @Override
        public boolean isBlack()
        {
            return true;
        }

        @Override
        public PlayerColor negate()
        {
            return WHITE;
        }
    },
    WHITE
    {
        @Override
        public boolean isBlack()
        {
            return false;
        }

        @Override
        public PlayerColor negate()
        {
            return BLACK;
        }
    };

    /**
     * Zwraca true, jeśli czarny kolor.
     * 
     * @return True, jeśli czarny kolor.
     */
    public abstract boolean isBlack();

    /**
     * Zwraca przeciwny kolor.
     * 
     * @return Przeciwny kolor.
     */
    public abstract PlayerColor negate();
}
