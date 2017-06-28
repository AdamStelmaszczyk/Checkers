package view;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import model.Cords;
import model.FakeBoard;
import model.FieldType;
import model.PlayerColor;
import controller.Server;
import controller.ServerConfig;
import event.BoardClickEvent;

/**
 * Klasa widoku - wyświetla GUI.
 * 
 * @author Adam Stelmaszczyk
 * @version 2011-05-27
 */
public class View
{
    /** Okno gry. */
    private MainWindow mainWindow;
    /** Guziki tworzące planszę. */
    private ButtonField[][] buttons;
    /** Guzik Host. */
    private JButton hostButton;
    /** Guzik Join. */
    private JButton joinButton;
    /** Obrazek pokazujący czyja tura. */
    private JLabel whoseTurnLabel;
    /** Połączenie sieciowe klienta. */
    private ClientNetwork clientNetwork;
    /** Mapa (słownik) odworowujący typ pola na nazwę pliku. */
    private final Map<FieldType, String> fieldTypeToName = new HashMap<FieldType, String>();
    /** Nasłuchiwacz kliknięć w planszę. Po kliknięciu współrzędne guzika są wysyłane na serwer. */
    private final ActionListener boardListener = new ActionListener()
    {
        public void actionPerformed(final ActionEvent e)
        {
            ButtonField button = (ButtonField) e.getSource();
            Cords cords = button.getCords();
            BoardClickEvent boardEvent = new BoardClickEvent(cords, clientNetwork.getMyColor());
            clientNetwork.send(boardEvent);
        }
    };
    /** Nasłuchiwacz guzika Host. */
    private final ActionListener hostButtonListener = new ActionListener()
    {
        public void actionPerformed(final ActionEvent e)
        {
            hostButtonClicked();
        }
    };
    /** Nasłuchiwacz guzika Join. */
    private final ActionListener joinButtonListener = new ActionListener()
    {
        public void actionPerformed(final ActionEvent e)
        {
            showHostInput();
        }
    };
    /** Nasłuchiwacz guzika Zasady. */
    private final ActionListener rulesButtonListener = new ActionListener()
    {
        public void actionPerformed(final ActionEvent e)
        {
            showRules();
        }
    };

    /**
     * Konstruktor. Tworzy skojarzenia nazw plików z typami pól na planszy.
     */
    public View()
    {
        // Pobiera nazwy typów pól.
        FieldType[] fieldTypes = FieldType.values();

        // Ich liczba musi się zgadzać z liczbą plików obrazków.
        if (fieldTypes.length != ViewConfig.FILENAME.length)
        {
            System.err.println(getClass() + Messages.getString("View.3"));
            throw new RuntimeException();
        }

        // Jeśli się zgadza, inicjalizujemy mapę odwzorowującą typy pól na nazwy plików graficznych.
        for (int i = 0; i < fieldTypes.length; i++)
        {
            fieldTypeToName.put(fieldTypes[i], ViewConfig.FILENAME[i]);
        }
        
        // Przekazuje wątkowi Swinga parę rzeczy do zrobienia.
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                mainWindow = new MainWindow(Messages.getString("View.0"));
                buttons = new ButtonField[8][8];
                hostButton = new JButton(Messages.getString("View.1"));
                joinButton = new JButton(Messages.getString("View.2"));
                whoseTurnLabel = new JLabel();
            }
        });
    }

    /**
     * Rysuje wstępnie to, co może na początku. Bez połączenia z serwerem. Zarys planszy, guziki.
     */
    public void showInitially()
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                // Dodaje panel planszy do okna.
                JPanel board = new JPanel(new GridLayout(8, 8));
                board.setBounds(ViewConfig.BOARD_X, ViewConfig.BOARD_Y, ViewConfig.BOARD_WIDTH,
                        ViewConfig.BOARD_HEIGHT);
                mainWindow.add(board);

                // Dodaje guziki do planszy.
                for (int y = 0; y < buttons[0].length; y++)
                {
                    for (int x = 0; x < buttons.length; x++)
                    {
                        buttons[x][y] = new ButtonField(new Cords(x, y));
                        buttons[x][y].addActionListener(boardListener);
                        board.add(buttons[x][y]);
                    }
                }

                // Deaktywacja planszy.
                setEnabledBoard(false);

                // Dodaje panel po prawej stronie do okna.
                JPanel rightPanel = new JPanel(null);
                rightPanel.setBounds(ViewConfig.RIGHT_PANEL_X, ViewConfig.BOARD_Y, ViewConfig.RIGHT_PANEL_WIDTH,
                        ViewConfig.WINDOW_HEIGHT);
                mainWindow.add(rightPanel);

                // Guzik Host.
                hostButton.setBounds(0, 0, ViewConfig.RIGHT_PANEL_WIDTH, 30);
                hostButton.addActionListener(hostButtonListener);
                rightPanel.add(hostButton);

                // Guzik Join.
                joinButton.setBounds(0, 40, ViewConfig.RIGHT_PANEL_WIDTH, 30);
                joinButton.addActionListener(joinButtonListener);
                rightPanel.add(joinButton);

                // Guzik Zasady.
                JButton rulesButton = new JButton(Messages.getString("View.4"));
                rulesButton.setBounds(0, 80, ViewConfig.RIGHT_PANEL_WIDTH, 30);
                rulesButton.addActionListener(rulesButtonListener);
                rightPanel.add(rulesButton);

                // Czyja tura.
                whoseTurnLabel.setBorder(new TitledBorder(Messages.getString("View.5")));
                whoseTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
                whoseTurnLabel.setBounds(0, 120, ViewConfig.RIGHT_PANEL_WIDTH, 120);
                rightPanel.add(whoseTurnLabel);
            }
        });
    }

    /**
     * Wł/wył guziki na planszy.
     * 
     * @param enable True, jeśli guziki na planszy mają być włączone.
     */
    void setEnabledBoard(final boolean enable)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                whoseTurnLabel.setEnabled(enable);
                for (ButtonField col[] : buttons)
                {
                    for (ButtonField button : col)
                    {
                        button.setEnabled(enable);
                    }
                }
            }
        });
    }

    /**
     * Wł/wył guzik "Host".
     * 
     * @param enable True, jeśli guzik ma być włączony.
     */
    void setEnabledHostButton(final boolean enable)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                hostButton.setEnabled(enable);
            }
        });
    }

    /**
     * Wł/wył guzik "Join".
     * 
     * @param enable True, jeśli guzik ma być włączony.
     */
    void setEnabledJoinButton(final boolean enable)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                joinButton.setEnabled(enable);
            }
        });
    }

    /**
     * Rysuje wszystko na podstawie kopii planszy.
     * 
     * @param fakeBoard Kopia planszy do narysowania.
     */
    void showAll(final FakeBoard fakeBoard)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                showBoard(fakeBoard);
                showWhoseTurn(fakeBoard.getWhoseTurn());
            }
        });
    }

    /**
     * Wyświetla okienko do wprowadzenia hosta.
     */
    void showHostInput()
    {
        final View view = this;
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                String text = Messages.getString("View.6");
                String input = (String) JOptionPane.showInputDialog(mainWindow, text, Messages.getString("View.7"),
                        JOptionPane.QUESTION_MESSAGE, null, null, Messages.getString("View.8"));
                if (input == null)
                {
                    return;
                }
                clientNetwork = new ClientNetwork(input, view);
                try
                {
                    clientNetwork.start();
                }
                catch (UnknownHostException e)
                {
                    showMessageDialog(Messages.getString("View.9"), Messages.getString("View.10")); 
                    return;
                }
                catch (IOException e)
                {
                    showMessageDialog(Messages.getString("View.11"), Messages.getString("View.12")); 
                    return;
                }
                // Udało się połączyć - wyłączamy guziki.
                setEnabledHostButton(false);
                setEnabledJoinButton(false);
            }
        });
    }

    /**
     * Wyświetla informację w okienku z przyciskiem ok.
     * 
     * @param msg Informacja.
     * @param title Napis tytułowy na belce okienka.
     */
    void showMessageDialog(final String msg, final String title)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JOptionPane.showMessageDialog(mainWindow, msg, title, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Wyświetla powitanie i jaki mamy kolor.
     * 
     * @param myColor Nasz kolor.
     */
    void showMyColor(final PlayerColor myColor)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                String text;
                FieldType fieldType;
                if (myColor.isBlack())
                {
                    text = Messages.getString("View.13");
                    fieldType = FieldType.BLACK_PIECE;
                }
                else
                {
                    text = Messages.getString("View.14");
                    fieldType = FieldType.WHITE_PIECE;
                }
                String fileName = fieldTypeToName.get(fieldType);
                ImageLoader loader = new ImageLoader(fileName);
                ImageIcon icon = loader.getImage();
                JOptionPane.showMessageDialog(mainWindow, text, Messages.getString("View.15"),
                        JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });
    }

    /**
     * Wyświetla informację o zwycięzcy partii.
     * 
     * @param winnerColor Kolor zwycięzcy.
     */
    void showWinner(final PlayerColor winnerColor)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                String text;
                FieldType fieldType;
                if (winnerColor.isBlack())
                {
                    text = Messages.getString("View.16");
                    fieldType = FieldType.BLACK_PIECE;
                }
                else
                {
                    text = Messages.getString("View.17");
                    fieldType = FieldType.WHITE_PIECE;
                }
                String fileName = fieldTypeToName.get(fieldType);
                ImageLoader loader = new ImageLoader(fileName);
                ImageIcon icon = loader.getImage();
                JOptionPane.showMessageDialog(mainWindow, text, Messages.getString("View.18"),
                        JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });
    }

    /**
     * Rysuje planszę na podstawie kopii.
     * 
     * @param fakeBoard Kopia planszy do narysowania.
     */
    private void showBoard(final FakeBoard fakeBoard)
    {
        for (ButtonField col[] : buttons)
        {
            for (ButtonField button : col)
            {
                FieldType fieldType = fakeBoard.getFieldType(button.getCords());
                button.setImage(fieldTypeToName.get(fieldType));
            }
        }
    }

    /**
     * Pokazuje czyja tura.
     * 
     * @param whoseTurn Pionek aktualnego gracza.
     */
    private void showWhoseTurn(final FieldType whoseTurn)
    {
        String fileName = fieldTypeToName.get(whoseTurn);
        ImageLoader loader = new ImageLoader(fileName);
        whoseTurnLabel.setIcon(loader.getImage());
    }

    /**
     * Wyświetla zasady gry.
     */
    private void showRules()
    {
        String text = Messages.getString("View.23") 
            + Messages.getString("View.24")
            + Messages.getString("View.25") 
            + Messages.getString("View.26") 
            + Messages.getString("View.27");
        showMessageDialog(text, Messages.getString("View.28"));
    }

    /**
     * Kliknięto guzik "Host". Startuje serwer i klienta, po czym wyłącza guziki.
     */
    private void hostButtonClicked()
    {
        // Startuje serwer.
        Server server = new Server();
        try
        {
            server.start();
        }
        catch (IOException exception)
        {
            showMessageDialog(Messages.getString("View.19") + ServerConfig.PORT_NUMBER
                    + Messages.getString("View.20"), Messages.getString("View.21"));
            return;
        }

        // Startuje klienta.
        clientNetwork = new ClientNetwork(Messages.getString("View.22"), this);
        try
        {
            clientNetwork.start();
        }
        catch (UnknownHostException exception)
        {
            exception.printStackTrace();
            return;
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            return;
        }

        // Udało się - wyłącza guziki.
        setEnabledHostButton(false);
        setEnabledJoinButton(false);
    }

}
