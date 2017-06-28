package model;

/**
 * Bicia damki.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class KingJump extends AbstractJump
{
    /**
     * Konstruktor.
     * 
     * @param king Damka.
     * @param board
     */
    KingJump(final King king, final Board board)
    {
        super(king, board);
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
        int myPieces = board.getPiecesBetween(from, to, myColor);
        if (myPieces != 0)
        {
            return false;
        }

        // Można przeskoczyć tylko 1 pionek przeciwnika.
        PlayerColor enemyColor = myColor.negate();
        int enemyPieces = board.getPiecesBetween(from, to, enemyColor);
        if (enemyPieces != 1)
        {
            return false;
        }

        return true;
    }

}
