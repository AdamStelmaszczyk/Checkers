package view;

/**
 * Klasa przechowująca stałe wykorzystywane przy rysowaniu okna i interfejsu.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class ViewConfig
{
    /**
     * Nazwy plików obrazków. Odpowiednio dla:
     * czarnego pola, białego pola,
     * czarnego pionka, białego pionka,
     * czarnej damki, białej damki,
     * czarnego aktynego pionka, białego aktywnego pionka,
     * czarnej aktywnej damki, białej aktywnej damki
     */
    static final String[] FILENAME = { 
        "black_field.jpg", "white_field.jpg", 
        "black_piece.jpg", "white_piece.jpg",
        "black_king.jpg", "white_king.jpg", 
        "black_piece_active.jpg", "white_piece_active.jpg",
        "black_king_active.jpg", "white_king_active.jpg" 
    };
    /** Szerokość okna w pikselach. */
    static final int WINDOW_WIDTH = 770;
    /** Wysokość okna w pikselach. */
    static final int WINDOW_HEIGHT = 570;
    /** Lewy górny róg planszy, x. */
    static final int BOARD_X = 30;
    /** Lewy górny róg planszy, y. */
    static final int BOARD_Y = 30;
    /** Szerokość pola w pikselach. */
    static final int FIELD_WIDTH = 60;
    /** Wysokość pola w pikselach. */
    static final int FIELD_HEIGHT = 60;
    /** Szerokość planszy. */
    static final int BOARD_WIDTH = 8 * FIELD_WIDTH;
    /** Wysokość planszy. */
    static final int BOARD_HEIGHT = 8 * FIELD_HEIGHT;
    /** Początek panelu po prawej stronie. */
    static final int RIGHT_PANEL_X = BOARD_X + BOARD_WIDTH + 30;
    /** Szerokość panelu po prawej stronie. */
    static final int RIGHT_PANEL_WIDTH = 200;

}
