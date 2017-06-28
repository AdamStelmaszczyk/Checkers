package model;

/**
 * Klasa opisująca pionek.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class Checker extends AbstractPiece
{
    /** Zwykłe ruchy pionka. */
    private CheckerMove checkerMove;
    /** Bicia pionka. */
    private CheckerJump checkerJump;

    /**
     * Konstruktor.
     * 
     * @param color Kolor jaki ma mieć pionek.
     */
    Checker(final Cords cords, final PlayerColor color, final Board board)
    {
        super(cords, color);
        addMoves(board);
    }

    /**
     * Zwraca jaki typ pola reprezentuje ten pionek.
     * 
     * @return Typ pola.
     */
    @Override
    FieldType getFieldType()
    {
        return isBlack() ? FieldType.BLACK_PIECE : FieldType.WHITE_PIECE;
    }

    /**
     * Czy ten pionek powinien awansować na damkę?
     * 
     * @return True, jeśli pionek awansuje na damkę.
     */
    boolean isPromotion()
    {
        if ((isBlack() && cords.getY() == 0) || (!isBlack() && cords.getY() == 7))
        {
            return true;
        }
        return false;
    }

    /**
     * @see model.AbstractPiece#hasJump()
     */
    @Override
    boolean hasJump()
    {
        return checkerJump.doesExist();
    }

    /**
     * Przypisuje pionkowi zbiór ruchów.
     */
    @Override
    protected void addMoves(final Board board)
    {
        checkerMove = new CheckerMove(this, board);
        checkerJump = new CheckerJump(this, board);
        movesSet.add(checkerMove);
        movesSet.add(checkerJump);
    }

}
