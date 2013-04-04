package copesevisitor.view;

import copesevisitor.model.Category;
import copesevisitor.model.Place;
import copesevisitor.model.Person;
import copesevisitor.model.Activity;
import copesevisitor.model.Event;
import copesevisitor.persistence.DBManager;
import copesevisitor.persistence.CategoryDAO;
import copesevisitor.persistence.PlaceDAO;
import copesevisitor.persistence.PersonDAO;
import copesevisitor.persistence.ActivityDAO;
import copesevisitor.persistence.EventDAO;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * CopeseVisitor.java
 *
 * Created on 21/02/2013, 10:12:35
 */


/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class CopeseVisitor extends javax.swing.JFrame
{
    private CategoryDAO categoryDAO;
    private PlaceDAO placeDAO;
    private PersonDAO personDAO;
    private ActivityDAO activityDAO;
    private EventDAO eventDAO;
    
    /** Creates new form CopeseVisitor */
    public CopeseVisitor() throws SQLException, ParseException
    {
        // Initing instances
        categoryDAO = new CategoryDAO( DBManager.getInstance().getConnection() );
        placeDAO = new PlaceDAO( DBManager.getInstance().getConnection() );
        personDAO = new PersonDAO( DBManager.getInstance().getConnection() );
        activityDAO = new ActivityDAO( DBManager.getInstance().getConnection() );
        eventDAO = new EventDAO( DBManager.getInstance().getConnection() );
        initComponents();
        
        this.setLocationRelativeTo( null );
        
        // Loading some components
        loadPlacesComboBox();
        loadEventsComboBox();
        
        updateTablePeople();
        updateTableActivity();
        updateTableActivities();
    }
    
    /**
     * Pesquisa e atualiza tabela de entrada de pessoal
     * @throws SQLException
     * @throws ParseException 
     */
    private void updateTablePeople() throws SQLException, ParseException
    {
        List<Person> result = personDAO.getPeopleByPieceOfName( jName.getText() );
        String[][] tableValue = new String[result.size()][7];
        
        for( Integer i = 0; i < result.size(); i++ )
        {
            tableValue[i][0] = result.get( i ).getName();
            tableValue[i][1] = result.get( i ).getCpf();
            tableValue[i][2] = result.get( i ).getUftlink();
            tableValue[i][3] = result.get( i ).getPlace() != null ? result.get( i ).getPlace().getName() : "------";
            
            Activity activity = activityDAO.selectOpenActivity( result.get( i ) );
            tableValue[i][4] = activity != null ? activity.getEntrancetime().toString() : "------";
            tableValue[i][5] = activity != null ? activity.getDescription() : "------";
            tableValue[i][6] = activity != null ? ( activity.getEvent() != null ? activity.getEvent().getName() : "------" ) : "------";
        }
        
        jTable2.setModel( new DefaultTableModel( tableValue, new String [] { "Nome Completo", "CPF", "Vínculo com a UFT", "Local", "Data de entrada", "Atividade", "Evento" } )
        {
            Class[] types = new Class [] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) { return types [columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });
        jScrollPane2.setViewportView( jTable2 );
    }
    
    /**
     * Pesquisa e atualiza tabela de atividades abertas
     * @throws SQLException
     * @throws ParseException 
     */
    private void updateTableActivity() throws SQLException
    {
        List<Activity> result = activityDAO.listAllOpenActivities();
        String[][] tableValue = new String[result.size()][5];
        
        for( Integer i = 0; i < result.size(); i++ )
        {
            tableValue[i][0] = result.get( i ).getDescription();
            tableValue[i][1] = result.get( i ).getEntrancetime().toString();
            tableValue[i][2] = result.get( i ).getPlace().getName();
            tableValue[i][3] = result.get( i ).getPerson().getName();
            tableValue[i][4] = result.get( i ).getEvent() != null ? result.get( i ).getEvent().getName() : "------";
        }
        
        jTable3.setModel( new DefaultTableModel( tableValue, new String [] { "Descrição", "Data de Entrada", "Local", "Pessoa", "Evento" } )
        {
            Class[] types = new Class [] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean [] { false, false, false, false, false };

            public Class getColumnClass(int columnIndex) { return types [columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });
        jScrollPane3.setViewportView( jTable3 );
    }
    
    /**
     * Pesquisa e atualiza tabela de atividades fechadas
     * @throws SQLException
     * @throws ParseException 
     */
    private void updateTableActivities() throws SQLException
    {
        List<Activity> result = activityDAO.listAllActivities();
        String[][] tableValue = new String[result.size()][6];
        
        for( Integer i = 0; i < result.size(); i++ )
        {
            tableValue[i][0] = result.get( i ).getDescription();
            tableValue[i][1] = result.get( i ).getEntrancetime().toString();
            tableValue[i][2] = result.get( i ).getDeparturetime() != null ? result.get( i ).getDeparturetime().toString() : "------";
            tableValue[i][3] = result.get( i ).getPlace().getName();
            tableValue[i][4] = result.get( i ).getPerson().getName();
            tableValue[i][5] = result.get( i ).getEvent() != null ? result.get( i ).getEvent().getName() : "------";
        }
        
        jTable4.setModel( new DefaultTableModel( tableValue, new String [] { "Descrição", "Data de Entrada", "Data de saída", "Local", "Pessoa", "Evento" } )
        {
            Class[] types = new Class [] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) { return types [columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });
        jScrollPane4.setViewportView( jTable4 );
    }
    
    /**
     * Retorna a pessoa selecionada na tabela
     * @return pessoa selecionada na tabela
     * @throws SQLException 
     * @throws ParseException 
     */
    private Person getSelectedPerson() throws SQLException, ParseException
    {
        return personDAO.findByName( (String) jTable2.getValueAt( jTable2.getSelectedRow(), 0 ) );
    }
    
    /**
     * Carrega Combo Box de categorias
     * @throws SQLException 
     */
    private void loadPlacesComboBox() throws SQLException
    {
        jComboPlace.removeAllItems();
        for( Category category : categoryDAO.listAllCategories() )
            for( Place place : categoryDAO.listPlacesByCategory( category ) )
                jComboPlace.addItem( category.getName() + " - " + place.getName() );
    }
    
    /**
     * Carrega Combo Box de eventos
     * @throws SQLException 
     */
    private void loadEventsComboBox() throws SQLException
    {
        jComboEvent.removeAllItems();
        jComboEvent.addItem( "Nenhum evento" );
        for( Event event : eventDAO.listAllEvents() )
            jComboEvent.addItem( event.getName() );
    }
    
    /**
     * Retorna o nome do local selecionado no combo box
     * @return nome do local selecionado
     */
    private String getPlaceName() throws RuntimeException
    {
        String temp = (String)jComboPlace.getSelectedItem();
        if( temp.equals( "Local" ) )
            throw new RuntimeException( "Selecione um local válido!" );
        return temp.split( "\\ - " )[1];
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonIn = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jButtonDetails = new javax.swing.JButton();
        jName = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jComboPlace = new javax.swing.JComboBox();
        jDescription = new javax.swing.JTextField();
        jButtonOut = new javax.swing.JButton();
        jComboEvent = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuExit = new javax.swing.JMenuItem();
        jMenuRegister = new javax.swing.JMenu();
        jMenuPerson = new javax.swing.JMenuItem();
        jMenuPlace = new javax.swing.JMenuItem();
        jMenuEvent = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuContacts = new javax.swing.JMenuItem();
        jMenuDeclaration = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CopeseVisitor - Gerenciamento de Visitantes");
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel4.setBackground(new java.awt.Color(214, 217, 223));
        jPanel4.setEnabled(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/header.png"))); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel3FocusGained(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome Completo", "CPF", "Vínculo Com a UFT", "Local", "Data de Entrada", "Atividade", "Evento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButtonIn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonIn.setText("Entrar");
        jButtonIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInActionPerformed(evt);
            }
        });

        jLabel28.setText("Nome:");

        jButtonDetails.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonDetails.setText("Detalhes");
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });

        jName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jNameKeyReleased(evt);
            }
        });

        jLabel29.setText("Local:");

        jDescription.setText("descrição");

        jButtonOut.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonOut.setText("Sair");
        jButtonOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOutActionPerformed(evt);
            }
        });

        jLabel30.setText("Evento:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jComboPlace, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel30)
                                .addGap(18, 18, 18)
                                .addComponent(jComboEvent, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButtonDetails)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                .addComponent(jButtonIn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonOut, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDescription))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonOut, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonIn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButtonDetails, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jComboPlace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jComboEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Fluxo", null, jPanel3, "Realizar entrada de pessoal");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Data de entrada", "Local", "Pessoa", "Evento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 865, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Em execução", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Data de entrada", "Data de saída", "Local", "Pessoa", "Evento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 865, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Todas", jPanel2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenuFile.setText("Arquivo");

        jMenuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuExit.setText("Sair");
        jMenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuExit);

        jMenuBar1.add(jMenuFile);

        jMenuRegister.setText("Cadastrar");

        jMenuPerson.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuPerson.setText("Pessoa");
        jMenuPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPersonActionPerformed(evt);
            }
        });
        jMenuRegister.add(jMenuPerson);

        jMenuPlace.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuPlace.setText("Local");
        jMenuPlace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPlaceActionPerformed(evt);
            }
        });
        jMenuRegister.add(jMenuPlace);

        jMenuEvent.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuEvent.setText("Evento");
        jMenuEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEventActionPerformed(evt);
            }
        });
        jMenuRegister.add(jMenuEvent);

        jMenuBar1.add(jMenuRegister);

        jMenuReports.setText("Relatórios");

        jMenuContacts.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuContacts.setText("Contatos");
        jMenuContacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuContactsActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuContacts);

        jMenuDeclaration.setText("Declaração de Execução de Atividades");
        jMenuDeclaration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDeclarationActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuDeclaration);

        jMenuItem2.setText("Termo de Compromisso - Banca");
        jMenuReports.add(jMenuItem2);

        jMenuBar1.add(jMenuReports);

        jMenuHelp.setText("Ajuda");

        jMenuAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuAbout.setText("Sobre o CopeseVisitor");
        jMenuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuAbout);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Action Performed do menu "Cadastrar/Pessoa"
     * @param evt 
     */
    private void jMenuPersonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPersonActionPerformed
        try { new PersonForm( null ).setVisible( true ); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jMenuPersonActionPerformed

    /**
     * Action Performed do menu "Cadastrar/Local"
     * @param evt 
     */
    private void jMenuPlaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlaceActionPerformed
        try { new PlaceForm().setVisible( true ); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jMenuPlaceActionPerformed

    /**
     * Action Performed do clique na tab "Entrada"
     * @param evt 
     */
    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        try{ 
            loadPlacesComboBox(); 
            loadEventsComboBox();
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    /**
     * Action Performed do menu "Ajuda/Sobre o CopeseVisitor"
     * @param evt 
     */
    private void jMenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAboutActionPerformed
        new AboutForm().setVisible( true );
    }//GEN-LAST:event_jMenuAboutActionPerformed

    /**
     * Action Performed do botão "Entrada/Informações"
     * @param evt 
     */
    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed
        String infoToShow = "";
        for( int i = 0; i < jTable2.getColumnCount(); i++ )
            infoToShow += jTable2.getColumnName( i ) + ": " + jTable2.getValueAt( jTable2.getSelectedRow() , i) + "\n";
        
        new InfoForm( infoToShow ).setVisible( true );
    }//GEN-LAST:event_jButtonDetailsActionPerformed

    /**
     * Action Performed do botão "Entrada/Entrar"
     * @param evt 
     */
    private void jButtonInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInActionPerformed
        try
        {
            // Updating person table
            Place place = placeDAO.findByName( getPlaceName() );
            Person toIn = getSelectedPerson();
            if( toIn.getPlace() != null )
            {
                Activity activity = activityDAO.selectOpenActivity( toIn );
                activity.setDeparturetime( new Date( System.currentTimeMillis() ) );
                activityDAO.update( activity );
            }
            toIn.setPlace( place );
            personDAO.update( toIn );
            
            // Updating activity table
            Event event = eventDAO.findByName( jComboEvent.getSelectedItem().toString() );
            Activity activity = new Activity();
            activity.setDescription( jDescription.getText() );
            Date now = new Date( System.currentTimeMillis() );
            activity.setEntrancetime( now );
            activity.setPlace( place );
            activity.setPerson( toIn );
            activity.setEvent( event );
            activityDAO.insert( activity );
            
            JOptionPane.showMessageDialog( null, toIn.getName() + " entrou em " + place.getName() + " às " + now  + " com sucesso!");
            
            updateTablePeople();
            updateTableActivity();
            updateTableActivities();
        }
        catch( SQLException e ) { e.printStackTrace(); }
        catch( ParseException e ) { e.printStackTrace(); }
        catch( RuntimeException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jButtonInActionPerformed

    /**
     * Gained Focus do formulário "CopeseVisitor"
     * @param evt 
     */
    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        try{ 
            loadPlacesComboBox(); 
            loadEventsComboBox();
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_formWindowGainedFocus

    /**
     * Key Released do campo de texto "Entrada/Nome"
     * @param evt 
     */
    private void jNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jNameKeyReleased
        try 
        { 
            updateTablePeople(); 
            updateTableActivity();
            updateTableActivities();
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( ParseException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jNameKeyReleased

    private void jButtonOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOutActionPerformed
        try
        {
            Person toOut = getSelectedPerson();
            if( toOut.getPlace() != null )
            {
                toOut.setPlace( null );
                personDAO.update( toOut );
                
                Activity activity = activityDAO.selectOpenActivity( toOut );
                Date now = new Date( System.currentTimeMillis() );
                activity.setDeparturetime( now );
                activityDAO.update( activity );
                
                JOptionPane.showMessageDialog( null, toOut.getName() + " saiu às " + now );
                
                updateTablePeople();
                updateTableActivity();
                updateTableActivities();
            }
            else
                JOptionPane.showMessageDialog( null, toOut.getName() + " não está cadastrada em lugar nenhum" );
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( ParseException e ) { e.printStackTrace(); }
    }//GEN-LAST:event_jButtonOutActionPerformed

    /**
     * 
     * @param evt 
     */
    private void jMenuEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEventActionPerformed
        try{ new EventForm().setVisible( true ); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jMenuEventActionPerformed

    /**
     * Action Performed do menu "Arquivo/Sair"
     * @param evt 
     */
    private void jMenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuExitActionPerformed
        System.exit( 1 );
    }//GEN-LAST:event_jMenuExitActionPerformed

    private void jPanel3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel3FocusGained
        try{ 
            loadPlacesComboBox(); 
            loadEventsComboBox();
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jPanel3FocusGained

    private void jMenuContactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuContactsActionPerformed
        new ContactsForm().setVisible( true );
    }//GEN-LAST:event_jMenuContactsActionPerformed

    private void jMenuDeclarationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDeclarationActionPerformed
        try{ new DeclarationForm().setVisible( true ); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jMenuDeclarationActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CopeseVisitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CopeseVisitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CopeseVisitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CopeseVisitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run()
            {
                try { new CopeseVisitor().setVisible(true); }
                catch( SQLException e ) { e.printStackTrace(); }
                catch( ParseException e ) { e.printStackTrace(); }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDetails;
    private javax.swing.JButton jButtonIn;
    private javax.swing.JButton jButtonOut;
    private javax.swing.JComboBox jComboEvent;
    private javax.swing.JComboBox jComboPlace;
    private javax.swing.JTextField jDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuAbout;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuContacts;
    private javax.swing.JMenuItem jMenuDeclaration;
    private javax.swing.JMenuItem jMenuEvent;
    private javax.swing.JMenuItem jMenuExit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuPerson;
    private javax.swing.JMenuItem jMenuPlace;
    private javax.swing.JMenu jMenuRegister;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
