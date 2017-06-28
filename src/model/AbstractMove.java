package model;

import java.util.ArrayList;

/**
 * Abstrakcyjna klasa ruchu, z której dziedziczą wszelkie strategie ruchu.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
abstract class AbstractMove
{
    /** Pionek, którym się poruszamy. */
    protected final AbstractPiece piece;
    /** Plansza, na której stoi pionek. */
    protected final Board board;
    /** 4 podstawowe kierunki ruchu, po skosie. */
    protected final ArrayList<Cords> directions = new ArrayList<Cords>();

    /**
     * Konstruktor.
     * 
     * @param piece Pionek, którym się poruszamy.
     */
    AbstractMove(final AbstractPiece piece, final Board board)
    {
        this.piece = piece;
        this.board = board;
        directions.add(new Cords(1, 1));
        directions.add(new Cords(1, -1));
        directions.add(new Cords(-1, 1));
        directions.add(new Cords(-1, -1));
    }

    /**
     * Czy ruch w podane pole jest poprawny?
     * 
     * @param to Współrzędne docelowe.
     * @return True, jeśli ruch poprawny.
     */
    abstract boolean isLegalMove(Cords to);

    /**
     * Czy istnieje choć 1 poprawny ruch z tego pola w którym znajduje się pionek?
     * 
     * @return True, jeśli istnieje conajmniej 1 poprawny ruch.
     */
    abstract boolean doesExist();
    
    /**
     * Wykonaj ruch w podane pole.
     * 
     * @param to Współrzędne docelowe.
     * @param boardState Obiekt przechowujący informacje do kogo należy ruch.
     */
    void execute(final Cords to, final BoardState boardState)
    {
        if (!isLegalMove(to))
        {
            throw new RuntimeException("Niedozwolony ruch.");
        }
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

        // Zmieniamy kolej gracza i wygaszamy aktywny pionek.
        boardState.setTurnToOpposite();
        boardState.setActivePiece(null);
    }

}
