package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Klasa opisująca planszę.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
class Board
{
    /** Mapa białych pionków. */
    private final Map<Cords, AbstractPiece> whitePieces = new HashMap<Cords, AbstractPiece>();
    /** Mapa czarnych pionków. */
    private final Map<Cords, AbstractPiece> blackPieces = new HashMap<Cords, AbstractPiece>();

    /**
     * Konstruktor. Rozmieszcza pionki jak na początku gry.
     */
    Board(final Model model)
    {
        // Białe pionki na górze.
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                Cords cords = new Cords(x, y);
                if (cords.isBlack())
                {
                    whitePieces.put(cords, new Checker(cords, PlayerColor.WHITE, this));
                }
            }
        }
        // Czarne pionki na dole.
        for (int y = 5; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                Cords cords = new Cords(x, y);
                if (cords.isBlack())
                {
                    blackPieces.put(cords, new Checker(cords, PlayerColor.BLACK, this));
                }
            }
        }
    }

    /**
     * Zwraca bezpieczną kopię planszy.
     * 
     * @return Obiekt FakeBoard odpowiadający stanowi planszy.
     */
    FakeBoard getFake()
    {
        FakeBoard fake = new FakeBoard();
        Collection<AbstractPiece> collection = whitePieces.values();
        Iterator<AbstractPiece> it = collection.iterator();
        while (it.hasNext())
        {
            AbstractPiece piece = it.next();
            fake.setFieldType(piece.getCords(), piece.getFieldType());
        }
        collection = blackPieces.values();
        it = collection.iterator();
        while (it.hasNext())
        {
            AbstractPiece piece = it.next();
            fake.setFieldType(piece.getCords(), piece.getFieldType());
        }
        return fake;
    }

    /**
     * Zwraca pionek o podanych współrzędnych.
     * 
     * @param cords Współrzędne.
     * @return Pionek o podanych współrzędnych.
     */
    AbstractPiece getPiece(final Cords cords)
    {
        if (whitePieces.containsKey(cords))
        {
            return whitePieces.get(cords);
        }
        if (blackPieces.containsKey(cords))
        {
            return blackPieces.get(cords);
        }
        throw new NoSuchElementException("Nie ma pionka na danym polu.");
    }

    /**
     * Zwraca mapę pionków danego koloru.
     * 
     * @param playerColor Kolor pionków.
     * @return Mapa pionków danego koloru.
     */
    Map<Cords, AbstractPiece> getPieces(final PlayerColor playerColor)
    {
        return playerColor.isBlack() ? blackPieces : whitePieces;
    }

    /**
     * Zwraca parę liczb oznaczającą kierunek, np. (1, 1).
     * 
     * @param from Współrzędne początkowe.
     * @param to Współrzędne celu.
     * @return Współrzędne kierunkowe.
     */
    Cords getDirectionCords(final Cords from, final Cords to)
    {
        int dx = (to.getX() < from.getX()) ? -1 : 1;
        int dy = (to.getY() < from.getY()) ? -1 : 1;
        return new Cords(dx, dy);
    }

    /**
     * Zwraca pierwszego spotkanego wrogiego pionka na drodze do podanych współrzędnych.
     * 
     * @param from Współrzędne początkowe.
     * @param to Współrzędne docelowe.
     * @return Wrogi pionek.
     */
    Cords getVictim(final Cords from, final Cords to, final PlayerColor myColor)
    {
        Cords direction = getDirectionCords(from, to);
        int distance = getDistance(from, to);
        for (int i = 1; i < distance; i++)
        {
            Cords actual = from.add(direction.multiply(i));
            if (isPiece(actual) && !getPiece(actual).isMine(myColor))
            {
                return actual;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Zwraca odległość do pola docelowego, w polach. Stojąc pionkiem w środku tak wyglądają odległości:<br>
     * 20002<br>
     * 01010<br>
     * 00000<br>
     * 01010<br>
     * 20002
     * 
     * @param from Współrzędne początkowe.
     * @param to Współrzędne pola docelowego.
     * @return O ile pól oddalone są podane współrzędne. Jeśli pola nie da się osiągnąć zwracane jest 0.
     */
    int getDistance(final Cords from, final Cords to)
    {
        if (to.isBlack() == false)
        {
            return 0;
        }
        
        int xDistance = Math.abs(from.getX() - to.getX());
        int yDistance = Math.abs(from.getY() - to.getY());
        
        if (xDistance != yDistance)
        {
            return 0;
        }
        
        return xDistance;
    }

    /**
     * Ile pionków danego koloru stoi na na odcinku (from, to>?
     * 
     * @param from Współrzędne początkowe.
     * @param to Współrzędne docelowe.
     * @param color Kolor pionków.
     * @return Ilość pionków danego koloru stojąca na drodze.
     */
    int getPiecesBetween(final Cords from, final Cords to, final PlayerColor color)
    {
        int piecesBetween = 0;
        Cords direction = getDirectionCords(from, to);
        int distance = getDistance(from, to);
        for (int i = 1; i < distance; i++)
        {
            Cords actual = from.add(direction.multiply(i));
            if (isPiece(actual) && (getPiece(actual).getColor() == color))
            {
                piecesBetween++;
            }
        }
        return piecesBetween;
    }

    /**
     * Czy podanemu graczowi skończyły się pionki?
     * 
     * @param playerColor Kolor gracza.
     * @return True, jeśli gracz o podanym kolorze nie ma pionków.
     */
    boolean isLackOfPieces(final PlayerColor playerColor)
    {
        Map<Cords, AbstractPiece> playerPieces = getPieces(playerColor);
        return playerPieces.isEmpty();
    }

    /**
     * Czy na podanych współrzędnych stoi pionek?
     * 
     * @param cords Współrzędne.
     * @return True, jeśli stoi tam jakiś pionek.
     */
    boolean isPiece(final Cords cords)
    {
        return whitePieces.containsKey(cords) || blackPieces.containsKey(cords);
    }

    /**
     * Rusza pionek na podane współrzędne.
     * 
     * @param from Współrzędne startowe.
     * @param to Współrzędne docelowe.
     */
    void doMove(final Cords from, final Cords to)
    {
        if (whitePieces.containsKey(from))
        {
            AbstractPiece piece = whitePieces.remove(from);
            piece.setCords(to);
            whitePieces.put(to, piece);
        }
        if (blackPieces.containsKey(from))
        {
            AbstractPiece piece = blackPieces.remove(from);
            piece.setCords(to);
            blackPieces.put(to, piece);
        }
    }

    /**
     * Awansuje pionek na damkę.
     * 
     * @param checker Referencja na pionek.
     * @return Referencja na damkę.
     */
    King promoteChecker(final Checker checker)
    {
        Map<Cords, AbstractPiece> pieces = getPieces(checker.getColor());
        pieces.remove(checker.getCords());
        King king = new King(checker, this);
        pieces.put(checker.getCords(), king);
        return king;
    }

    /**
     * Usuwa pionek o podanych współrzędnych.
     * 
     * @param cords Współrzędne pionka.
     */
    void removePiece(final Cords cords)
    {
        whitePieces.remove(cords);
        blackPieces.remove(cords);
    }

}
