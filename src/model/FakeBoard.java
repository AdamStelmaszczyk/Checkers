package model;

import java.io.Serializable;

/**
 * Klasa opakowująca bezpieczną kopię planszy. Przechowuje stan planszy - pionki i do kogo należy tura.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class FakeBoard implements Serializable
{
    /** Numer wersji. */
    private static final long serialVersionUID = 1;
    /** Informacja o stanie pól na planszy. */
    private final FieldType board[][] = new FieldType[8][8];
    /** Informacja o tym czyja tura. */
    private FieldType whoseTurn;

    /**
     * Konstruktor.
     */
    public FakeBoard()
    {
        for (int y = 0; y < board[0].length; y++)
        {
            for (int x = 0; x < board.length; x++)
            {
                Cords cords = new Cords(x, y);
                board[x][y] = (cords.isBlack()) ? FieldType.BLACK_FIELD : FieldType.WHITE_FIELD;
            }
        }
    }

    /**
     * Zwraca typ pola o podanych współrzędnych.
     * 
     * @param cords Współrzędne.
     * @return Typ pola.
     */
    public FieldType getFieldType(final Cords cords)
    {
        return board[cords.getX()][cords.getY()];
    }

    /**
     * Zwraca typ pola reprezentujący turę gracza.
     * 
     * @return Typ pola reprezentujący turę gracza. Czarny lub biały pionek.
     */
    public FieldType getWhoseTurn()
    {
        return whoseTurn;
    }

    /**
     * Ustawia dane pole na podanych współrzędnych.
     * 
     * @param cords Współrzędne.
     * @param fieldType Typ pola.
     */
    void setFieldType(final Cords cords, final FieldType fieldType)
    {
        board[cords.getX()][cords.getY()] = fieldType;
    }

    /**
     * Ustawia typ pionka reprezentujący turę gracza.
     * 
     * @param fieldType Typ pola reprezentujący turę gracza. Czarny lub biały pionek.
     */
    void setWhoseTurn(final FieldType fieldType)
    {
        whoseTurn = fieldType;
    }

}
