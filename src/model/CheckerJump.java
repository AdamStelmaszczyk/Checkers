package model;

/**
 * Bicia pionka.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class CheckerJump extends AbstractJump
{
    /**
     * Konstruktor.
     * 
     * @param piece Pionek.
     */
    CheckerJump(final Checker piece, final Board board)
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
    
        // Pionki bijąc przemieszczają się dokładnie o 2 pola.
        Cords from = piece.getCords();
        int distance = board.getDistance(from, to);
        if (distance != 2)
        {
            return false;
        }
    
        // Musimy przeskakiwać nad pionkiem przeciwnika.
        PlayerColor myColor = piece.getColor();
        PlayerColor enemyColor = myColor.negate();
        int enemyPiecesNumber = board.getPiecesBetween(from, to, enemyColor);
        if (enemyPiecesNumber != 1)
        {
            return false;
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
            Cords to = from.add(direction.multiply(2));
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
