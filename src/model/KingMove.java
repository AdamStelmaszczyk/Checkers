package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Zwykłe ruchy damki.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class KingMove extends AbstractMove
{
    /**
     * Konstruktor.
     * 
     * @param king Damka.
     */
    KingMove(final King king, final Board board)
    {
        super(king, board);
    }

    /**
     * @see model.AbstractMove#isLegalMove(model.Cords)
     */
    @Override
    boolean isLegalMove(final Cords to)
    {
        // Na docelowych współrzędnych stoi pionek.
        if (board.isPiece(to))
        {
            return false;
        }
    
        // Nie można ruszyć się w to pole, dystans się nie zgadza.
        Cords from = piece.getCords();
        int distance = board.getDistance(from, to);
        if (distance == 0)
        {
            return false;
        }
    
        // Nie można przeskakiwać swoich pionków.
        PlayerColor myColor = piece.getColor();
        int myPiecesNumber = board.getPiecesBetween(from, to, myColor);
        if (myPiecesNumber != 0)
        {
            return false;
        }
    
        // To ma być zwykły ruch a nie bicie, więc nie powinniśmy przeskoczyć też żadnego przeciwnika.
        PlayerColor enemyColor = myColor.negate();
        int enemyPiecesNumber = board.getPiecesBetween(from, to, enemyColor);
        if (enemyPiecesNumber != 0)
        {
            return false;
        }
    
        // Sprawdzamy czy któryś z naszych pionków nie ma bicia.
        Map<Cords, AbstractPiece> myPieces = board.getPieces(myColor);
        Collection<AbstractPiece> collection = myPieces.values();
        Iterator<AbstractPiece> it = collection.iterator();
        while (it.hasNext())
        {
            AbstractPiece piece = it.next();
            // Jeśli ma, to ten zwykły ruch jest niepoprawny, bo gracz powinien bić.
            if (piece.hasJump())
            {
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
            int i = 1;
            while (true)
            {
                Cords to = from.add(direction.multiply(i));
                if (!to.isLegalCords())
                {
                    break;
                }
                if (isLegalMove(to))
                {
                    return true;
                }
                i++;
            }
        }
        return false;
    }

}
