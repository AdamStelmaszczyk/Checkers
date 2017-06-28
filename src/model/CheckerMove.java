package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Zwykłe ruchy pionka.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class CheckerMove extends AbstractMove
{
    /**
     * Konstruktor.
     * 
     * @param piece Pionek.
     */
    CheckerMove(final Checker piece, final Board board)
    {
        super(piece, board);
    }

    /**
     * @see model.AbstractMove#isLegalMove(model.Cords)
     */
    @Override
    boolean isLegalMove(final Cords to)
    {
        // Miejsce w które mamy zamiar się ruszyć jest zajęte.
        if (board.isPiece(to))
        {
            return false;
        }
    
        // Pionki nie mogą ruszać się do tyłu.
        if (!piece.isFrontMove(to))
        {
            return false;
        }
    
        // Sprawdzamy czy ruch był tylko o 1 pole do przodu.
        Cords from = piece.getCords();
        int distance = board.getDistance(from, to);
        if (distance != 1)
        {
            return false;
        }
    
        // Sprawdzamy czy któryś z naszych pionków nie ma bicia.
        Map<Cords, AbstractPiece> myPieces = board.getPieces(piece.getColor());
        Collection<AbstractPiece> collection = myPieces.values();
        Iterator<AbstractPiece> it = collection.iterator();
        while (it.hasNext())
        {
            AbstractPiece piece = it.next();
            if (piece.hasJump())
            {
                // Jeśli ma, to ten zwykły ruch jest niepoprawny, bo gracz powinien bić.
                return false;
            }
        }
    
        return true;
    }

    /**
     * @see model.AbstractMove#doesExist()
     */
    @Override
    boolean doesExist()
    {
        Cords from = piece.getCords();
        for (Cords direction : directions)
        {
            Cords to = from.add(direction);
            if (to.isLegalCords() == false)
            {
                continue;
            }
            if (isLegalMove(to))
            {
                return true;
            }
        }
        return false;
    }

}
