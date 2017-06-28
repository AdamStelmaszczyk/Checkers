package model;

/**
 * Abstrakcyjna klasa bicia, z której dziedziczą wszystkie strategie bicia.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
abstract class AbstractJump extends AbstractMove
{
    /**
     * Konstruktor.
     * 
     * @param piece Pionek, którym się poruszamy.
     */
    AbstractJump(final AbstractPiece piece, final Board board)
    {
        super(piece, board);
    }

    /**
     * @see model.AbstractMove#execute(model.Cords, model.BoardState)
     */
    @Override
    void execute(final Cords to, final BoardState boardState)
    {
        if (!isLegalMove(to))
        {
            throw new RuntimeException("Niepoprawne bicie.");
        }

        // Pobieramy zbitego pionka i usuwamy go. Następnie ruszamy się naszym pionkiem.
        Cords victimCords = board.getVictim(piece.getCords(), to, piece.getColor());
        board.removePiece(victimCords);
        board.doMove(piece.getCords(), to);

        // Jesli ruszalismy sie zwyklym pionkiem to sprawdzamy czy nie awansujemy na damkę.
        if (piece.isChecker())
        {
            Checker checker = (Checker) piece;
            if (checker.isPromotion())
            {
                King king = board.promoteChecker(checker);
                boardState.setActivePiece(king);
            }
        }

        // Jeśli nie ma dalej bicia to zmieniamy kolej gracza i wygaszamy aktywny pionek.
        if (!doesExist())
        {
            boardState.setTurnToOpposite();
            boardState.setActivePiece(null);
        }
    }

}
