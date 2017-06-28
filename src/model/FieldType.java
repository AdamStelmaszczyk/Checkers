package model;

/**
 * Typ pola na planszy. Białe/czarne, zajęte przez pionek czarny/biały, zajęte przez damkę czarną/białą.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public enum FieldType
{
    BLACK_FIELD
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    },
    WHITE_FIELD
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    },
    BLACK_PIECE
    {
        @Override
        public FieldType getActive()
        {
            return BLACK_PIECE_ACTIVE;
        }
    },
    WHITE_PIECE
    {
        @Override
        public FieldType getActive()
        {
            return WHITE_PIECE_ACTIVE;
        }
    },
    BLACK_KING
    {
        @Override
        public FieldType getActive()
        {
            return BLACK_KING_ACTIVE;
        }
    },
    WHITE_KING
    {
        @Override
        public FieldType getActive()
        {
            return WHITE_KING_ACTIVE;
        }
    },
    BLACK_PIECE_ACTIVE
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    },
    WHITE_PIECE_ACTIVE
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    },
    BLACK_KING_ACTIVE
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    },
    WHITE_KING_ACTIVE
    {
        @Override
        public FieldType getActive()
        {
            return this;
        }
    };

    /**
     * Zwraca aktywny typ pola.
     * 
     * @return Aktywny typ pola.
     */
    abstract public FieldType getActive();

}
