package model;

/**
 * Klasa przechowująca informację o stanie planszy - do kogo należy ruch i aktywny pionek.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class BoardState
{
    /** Kolor gracza do którego należy ruch. */
    private PlayerColor whoseTurnColor;
    /** Aktywny pionek. Ten, którym gracz chce się ruszyć. */
    private AbstractPiece activePiece;

    /**
     * Konstruktor.
     */
    BoardState(final PlayerColor whoseTurnColor)
    {
        this.whoseTurnColor = whoseTurnColor;
    }

    /**
     * Zwraca aktywny pionek.
     * 
     * @return Aktywny pionek.
     */
    AbstractPiece getActivePiece()
    {
        return activePiece;
    }

    /**
     * Czy kolej należy do czarnego gracza?
     * 
     * @return True, jeśli teraz kolej ma gracz czarny.
     */
    FieldType getWhoseTurnFieldType()
    {
        return (whoseTurnColor.isBlack()) ? FieldType.BLACK_PIECE : FieldType.WHITE_PIECE;
    }

    /**
     * Zwraca czyja tura.
     * 
     * @return Kolor gracza do którego należy ruch.
     */
    PlayerColor getWhoseTurnColor()
    {
        return whoseTurnColor;
    }

    /**
     * Ustawia aktywny pionek.
     * 
     * @param activePiece Aktywny pionek.
     */
    void setActivePiece(final AbstractPiece activePiece)
    {
        this.activePiece = activePiece;
    }

    /**
     * Zmiana gracza.
     */
    void setTurnToOpposite()
    {
        whoseTurnColor = whoseTurnColor.negate();
    }

    /**
     * Czy istnieje aktywny pionek?
     * 
     * @return True, jeśli istnieje aktywny pionek.
     */
    boolean isActivePiece()
    {
        return activePiece != null;
    }

}
