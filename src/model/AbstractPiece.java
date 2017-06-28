package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Abstrakcyjna klasa opisująca ogólny pionek (figurę) na planszy.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
abstract class AbstractPiece
{
    /** Współrzędne pionka na planszy. */
    protected Cords cords;
    /** Kolor pionka. */
    protected final PlayerColor color;
    /** Zbiór klas (rodzajów) ruchów, jakie może wykonywać pionek. */
    protected final Set<AbstractMove> movesSet = new HashSet<AbstractMove>();

    /**
     * Konstruktor.
     * 
     * @param cords Współrzędne pionka.
     * @param color Kolor pionka.
     */
    AbstractPiece(final Cords cords, final PlayerColor color)
    {
        this.cords = cords;
        this.color = color;
    }
    
    /**
     * Zwraca typ pola jaki reprezentuje pionek.
     * 
     * @return Typ pola.
     */
    abstract FieldType getFieldType();
    
    /**
     * Czy ten pionek ma jakieś bicie?
     * 
     * @return True, jeśli ma.
     */
    abstract boolean hasJump();

    /**
     * Przypisuje zbiór ruchów dla figury.
     */
    abstract protected void addMoves(final Board board);

    /**
     * Zwraca aktywny typ pola jaki reprezentuje pionek.
     * 
     * @return Aktywny typ pola.
     */
    FieldType getActiveFieldType()
    {
        FieldType fieldType = getFieldType();
        return fieldType.getActive();
    }

    /**
     * Zwraca współrzędne pionka.
     * 
     * @return Współrzędne pionka.
     */
    Cords getCords()
    {
        return cords;
    }

    /**
     * Zwraca kolor pionka.
     * 
     * @return Kolor pionka.
     */
    PlayerColor getColor()
    {
        return color;
    }

    /**
     * Zwraca czy pionek jest czarny.
     * 
     * @return True, jeśli pionek jest czarny.
     */
    boolean isBlack()
    {
        PlayerColor color = getColor();
        return color.isBlack();
    }

    /**
     * Zwraca true, jeśli podany ruch jest ruchem do przodu, uwzględniając kolor (stronę) gracza.
     * 
     * @param to Współrzędne końca.
     * @return True, jeśli jest to ruch do przodu.
     */
    boolean isFrontMove(final Cords to)
    {
        Cords from = getCords();
        if (isBlack())
        {
            return from.getY() > to.getY();
        }
        return from.getY() < to.getY();
    }

    /**
     * Czy ten ruch jest prawidłowy?
     * 
     * @param to Współrzędne docelowe.
     * @return True, jeśli ruch w podane pole jest prawidłowy.
     */
    boolean isLegalMove(final Cords to)
    {
        Iterator<AbstractMove> moveIterator = movesSet.iterator();
        while (moveIterator.hasNext())
        {
            AbstractMove move = moveIterator.next();
            if (move.isLegalMove(to))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Czy pionek należy do gracza mającego teraz ruch?
     * 
     * @param whoseTurnColor Czyja tura.
     * @return True, jeśli pionek należy do gracza mającego teraz ruch.
     */
    boolean isMine(final PlayerColor whoseTurnColor)
    {
        return getColor() == whoseTurnColor;
    }

    /**
     * Czy ten pionek to zwykły pionek (checker)?
     * 
     * @return True, jeśli to zwykły pionek.
     */
    boolean isChecker()
    {
        return getClass() == Checker.class;
    }

    /**
     * Ustawia współrzędne pionka.
     * 
     * @param cords Współrzędne pionka.
     */
    void setCords(final Cords cords)
    {
        this.cords = cords;
    }

    /**
     * Czy ten pionek w ogóle ma jakiś ruch?
     * 
     * @return True, jeśli może się gdzieś ruszyć.
     */
    boolean hasMove()
    {
        Iterator<AbstractMove> moveIterator = movesSet.iterator();
        while (moveIterator.hasNext())
        {
            AbstractMove move = moveIterator.next();
            if (move.doesExist())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Wykonaj ruch pionkiem w podane pole.
     * 
     * @param to Współrzędne docelowe.
     * @param whoseTurn Obiekt przechowujący informacje do kogo należy ruch.
     */
    void doMove(final Cords to, final BoardState whoseTurn)
    {
        Iterator<AbstractMove> moveIterator = movesSet.iterator();
        while (moveIterator.hasNext())
        {
            AbstractMove move = moveIterator.next();
            if (move.isLegalMove(to))
            {
                move.execute(to, whoseTurn);
                return;
            }
        }
    }

}
