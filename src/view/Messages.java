package view;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Klasa obsługująca ładowanie stałych tekstowych.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class Messages
{
    /** Nazwa dla tej grupy wiadomości. */
    private static final String BUNDLE_NAME = "view.messages";
    /** Obiekt zwracający napisy. */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Zwraca napis o podanym kluczu.
     * 
     * @param key Klucz.
     * @return Napis związany z podanym kluczem.
     */
    public static String getString(final String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}
