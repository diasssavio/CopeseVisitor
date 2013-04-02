package copesevisitor.view;
import copesevisitor.persistence.PersonDAO;
import copesevisitor.persistence.DBManager;
import copesevisitor.model.Person;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class ContactsForm extends javax.swing.JFrame
{
    private PersonDAO personDAO;
    
    /**
     * Creates new form ContactsForm
     */
    public ContactsForm() 
    {
        initComponents();
        
        setLocationRelativeTo( null );
        
        personDAO = new PersonDAO( DBManager.getInstance().getConnection() );
    }

    /**
     * Pesquisa e atualiza tabela de dados de contatos
     */
    private void updateTableContact() throws SQLException, ParseException
    {
        List<Person> result = personDAO.getByPieceOfName( jName.getText() );
        String[][] tableValue = new String[result.size()][5];
        
        for( Integer i = 0; i < result.size(); i++ )
        {
            tableValue[i][0] = result.get( i ).getName();
            tableValue[i][1] = result.get( i ).getPhone1();
            tableValue[i][2] = result.get( i ).getPhone2() != null ? result.get( i ).getPhone2() : "------";
            tableValue[i][3] = result.get( i ).getPhone3() != null ? result.get( i ).getPhone3() : "------";
            tableValue[i][4] = result.get( i ).getEmail();
        }
        
        jTableContact.setModel( new DefaultTableModel( tableValue, new String [] { "Nome", "Telefone 1", "Telefone 2", "Telefone 3", "e-mail" } )
        {
            Class[] types = new Class [] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean [] { false, false, false, false, false };

            public Class getColumnClass(int columnIndex) { return types [columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });
        jScrollPane1.setViewportView( jTableContact );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableContact = new javax.swing.JTable();
        jButtonInfo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informações p/ contato");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Nome:");

        jName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jNameKeyReleased(evt);
            }
        });

        jTableContact.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Telefone 1", "Telefone 2", "Telefone 3", "e-mail"
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
        jScrollPane1.setViewportView(jTableContact);

        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/information.png"))); // NOI18N
        jButtonInfo.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jName)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        String info = "";
        try
        {
            Person person = personDAO.findByName( jTableContact.getValueAt(jTableContact.getSelectedRow() , 0).toString() );
            
            info += "CPF: " + person.getCpf() + "\n";
            info += "Nome: " + person.getName() + "\n";
            info += "Data de Nascimento: " + person.getBirthdate() + "\n";
            info += "RG: " + person.getRg() + "\n";
            info += "Orgão Expeditor: " + person.getWichorgan() + "\n";
            info += "Email: " + person.getEmail() + "\n";
            info += "Sexo: " + person.getGender() + "\n";
            info += "PIS/PASEP: " + ( person.getPispasep() != null ? person.getPispasep() : "" ) + "\n";
            info += "Siape: " + ( person.getSiape() != null ? person.getSiape() : "" ) + "\n";
            info += "Telefone 1: " + person.getPhone1() + "\n";
            info += "Telefone 2: " + ( person.getPhone2() != null ? person.getPhone2() : "" ) + "\n";
            info += "Telefone 3: " + ( person.getPhone3() != null ? person.getPhone3() : "" ) + "\n";
            info += "Vínculo com a UFT: " + person.getUftlink() + "\n";
            info += "Local que se encontra: " + ( person.getPlace() != null ? person.getPlace().getName() : "" ) + "\n";
            info += "Endereço: " + person.getAddress().toString() + "\n";
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        
        
        new InfoForm( info ).setVisible( true );
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jNameKeyReleased
        try{ updateTableContact(); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( ParseException e ) { e.printStackTrace(); }
    }//GEN-LAST:event_jNameKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableContact;
    // End of variables declaration//GEN-END:variables
}