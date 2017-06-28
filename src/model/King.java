package model;

/**
 * Klasa opisująca damkę.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class King extends AbstractPiece
{
    /** Zwykłe ruchy damki. */
    private KingMove kingMove;
    /** Bicia damki. */
    private KingJump kingJump;

    /**
     * Konstruktor.
     * 
     * @param color Kolor damki.
     */
    King(final Cords cords, final PlayerColor color, final Board board)
    {
        super(cords, color);
        addMoves(board);
    }

    /**
     * Konstruktor kopiujący.
     * 
     * @param piece Pionek stanowiący wzór.
     */
    King(final Checker piece, final Board board)
    {
        super(piece.getCords(), piece.getColor());
        addMoves(board);
    }

    /**
     * Zwraca jaki typ pola reprezentuje ta damka.
     * 
     * @return Typ pola.
     */
    @Override
    FieldType getFieldType()
    {
        return (isBlack()) ? FieldType.BLACK_KING : FieldType.WHITE_KING;
    }

    /**
     * @see model.AbstractPiece#hasJump()
     */
    @Override
    boolean hasJump()
    {
        return kingJump.doesExist();
    }
    
    /**
     * Przypisuje damce jej zbiór ruchów.
     */
    @Override
    protected void addMoves(final Board board)
    {
        kingMove = new KingMove(this, board);
        kingJump = new KingJump(this, board);
        movesSet.add(kingMove);
        movesSet.add(kingJump);
    }

}
