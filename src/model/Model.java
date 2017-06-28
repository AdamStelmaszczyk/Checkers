package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Klasa modelu - samej logiki gry, zasad.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class Model
{
    /** Bieżąca sytuacja na planszy, rozmieszczenie pionków. */
    private final Board board;
    /** Stan planszy. Obiekt przechowujący informację do kogo należy tura oraz informację o aktywnym pionku. */
    private final BoardState boardState;

    /**
     * Konstruktor.
     */
    public Model()
    {
        this.board = new Board(this);
        this.boardState = new BoardState(PlayerColor.BLACK);
    }

    /**
     * Zwraca kopię planszy.
     * 
     * @return Kopia planszy.
     */
    public FakeBoard getFakeBoard()
    {
        // Robi kopię planszy z prawdziwej, zaznacza pionki.
        FakeBoard fakeBoard = board.getFake();

        // Zaznacza aktywne pole na kopii planszy.
        if (boardState.isActivePiece())
        {
            AbstractPiece activePiece = boardState.getActivePiece();
            fakeBoard.setFieldType(activePiece.getCords(), activePiece.getActiveFieldType());
        }

        // Zaznacza czyja kolej.
        fakeBoard.setWhoseTurn(boardState.getWhoseTurnFieldType());

        return fakeBoard;
    }

    /**
     * Zwraca kolor gracza do którego należy teraz ruch.
     * 
     * @return Kolor gracza do którego należy teraz ruch.
     */
    public PlayerColor getWhoseTurnColor()
    {
        return boardState.getWhoseTurnColor();
    }

    /**
     * Kto wygrał?
     * 
     * @return Kolor gracza, który wygrał.
     */
    public PlayerColor getWinnerColor()
    {
        // Jeśli gra się nie skonczyła a proszą nas o zwycięzcę to zwracamy null - jeszcze nie wiemy kto wygrał.
        if (!isGameOver())
        {
            return null;
        }

        // Zakładamy, że gra skończyła się przez utratę pionków.
        if (board.isLackOfPieces(PlayerColor.BLACK))
        {
            return PlayerColor.WHITE;
        }
        if (board.isLackOfPieces(PlayerColor.WHITE))
        {
            return PlayerColor.BLACK;
        }

        // Jeśli jesteśmy tutaj to gra skończyła się, bo nie mamy ruchu - przegrywamy.
        PlayerColor myColor = getWhoseTurnColor();
        return myColor.negate();
    }

    /**
     * Zwraca true, jeśli gra dobiegła końca.
     * Gra się kończy jeśli albo któryś z graczy nie ma pionków, albo aktualny gracz nie ma ruchu.
     * 
     * @return True, jeśli gra dobiegła końca.
     */
    public boolean isGameOver()
    {
        return board.isLackOfPieces(PlayerColor.BLACK) || board.isLackOfPieces(PlayerColor.WHITE) || !hasMove();
    }

    /**
     * Informuje model o kliknięciu na planszy.
     * 
     * @param click Kliknięte współrzędne.
     */
    public void doClick(final Cords click)
    {
        // Klik w pionek.
        if (board.isPiece(click))
        {
            // Pobieramy go.
            AbstractPiece piece = board.getPiece(click);
    
            // Klik w nasz pionek. On teraz będzie aktywny.
            if (piece.isMine(getWhoseTurnColor()))
            {
                boardState.setActivePiece(piece);
            }
    
            // Klik w nie nasz pionek.
            return;
        }
    
        // Klik w puste pole.
        // Jesli jest aktywny pionek, pobierz go i wykonaj nim ruch.
        if (boardState.isActivePiece())
        {
            AbstractPiece activePiece = boardState.getActivePiece();
            activePiece.doMove(click, boardState);
        }
    }

    /**
     * Czy aktualny gracz ma jakiś ruch?
     * 
     * @return True, jeśli ma jakiś ruch.
     */
    private boolean hasMove()
    {
        Map<Cords, AbstractPiece> myPieces = board.getPieces(getWhoseTurnColor());
        Collection<AbstractPiece> collection = myPieces.values();
        Iterator<AbstractPiece> it = collection.iterator();
        while (it.hasNext())
        {
            AbstractPiece piece = it.next();
            if (piece.hasMove())
            {
                return true;
            }
        }
        return false;
    }

}
