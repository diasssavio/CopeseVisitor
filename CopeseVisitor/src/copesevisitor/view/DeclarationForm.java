package copesevisitor.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import copesevisitor.persistence.DBManager;
import copesevisitor.persistence.PersonDAO;
import copesevisitor.persistence.ActivityexecutionDAO;
import copesevisitor.model.Activityexecution;
import copesevisitor.model.Person;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import java.util.List;

import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author Sávio Dias
 */
public class DeclarationForm extends JFrame 
{
    private ActivityexecutionDAO executionDAO;
    private List<Activityexecution> executions;
    private Person person;
    
    private DefaultTableModel model;
    
    /**
     * Creates new form DeclarationForm
     */
    public DeclarationForm() throws SQLException
    {
        initComponents();
        this.setLocationRelativeTo( null );
        
        executionDAO = new ActivityexecutionDAO( DBManager.getInstance().getConnection() );
        person = new PersonDAO( DBManager.getInstance().getConnection() ).first();
        updateTableActivity();
    }

    /**
     * Converte a escala métrica em pontos (utilizados na iText API)
     * @param metters valor em escala métrica
     * @return pontos que representam o valor passado na escala métrica
     */
    private float mettersToPoints( float metters )
    {
        return ( metters * 72.0f ) / 2.54f;
    }
    
    /**
     * Gera a declaração de execução de atividades de determinada pessoa
     * @return declaração 
     */
    private Document generateDeclaration() throws SQLException, DocumentException, FileNotFoundException, IOException
    {
        Document declaration = new Document( PageSize.A4, mettersToPoints(3f), mettersToPoints(3f), mettersToPoints(2.5f), mettersToPoints(2.5f) );
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        
        PdfWriter.getInstance( declaration, new FileOutputStream( "declaration.pdf" ) );
        declaration.open();
        
        // Document header
        Paragraph paragraph = new Paragraph( "ANEXO II", font );
        paragraph.setAlignment( Element.ALIGN_CENTER );
        declaration.add( paragraph );
        
        paragraph = new Paragraph( "DECLARAÇÃO DE EXECUÇÃO DE ATIVIDADES", font );
        paragraph.setAlignment( Element.ALIGN_CENTER );
        declaration.add( paragraph );
        
        // Creating table
        PdfPTable table = new PdfPTable( new float[] { 0.5f, 0.2f, 0.3f } );
        
        // Table header
        String headerText = "Pela presente DECLARAÇÃO DE EXECUÇÃO DE ATIVIDADES, eu " + person.getName() 
                + "matrícula SIAPE nº " + person.getSiape() + "ocupante do cargo de " + person.getUftlink()
                + ", em exercício na (o)" + ", declaro ter participado, no ano em curso, das seguintes atividades"
                + " relacionadas a curso, concurso público ou exame vestibular, previstas no art. 76-A da Lei"
                + " nº 8.112, de 1990, e no Decreto nº 6114 de 15 de maio, de 2007:";
        
        paragraph = new Paragraph( headerText, font );
        paragraph.setAlignment( Element.ALIGN_JUSTIFIED );
        PdfPCell header = new PdfPCell( paragraph );
        header.setColspan( 3 );
        table.addCell( header );
        
        // Table activities
        table.addCell( "Atividades" );
        table.addCell( "Instituição" );
        table.addCell( "Horas Trabalhadas" );
        
        Double hours = 0.0;
        for( Activityexecution activity : executions )
        {
            paragraph = new Paragraph( activity.getDescription(), font );
            paragraph.setAlignment( Element.ALIGN_CENTER );
            table.addCell( paragraph );
            
            paragraph = new Paragraph( activity.getInstitution(), font );
            paragraph.setAlignment( Element.ALIGN_CENTER );
            table.addCell( paragraph );
            
            // Calculating the hours
            paragraph = new Paragraph( String.format( "%3.1f", activity.getHoursworked() ), font );
            paragraph.setAlignment( Element.ALIGN_CENTER );
            table.addCell( paragraph );
            
            hours += activity.getHoursworked();
        }
        
        // Total activities
        paragraph = new Paragraph( "TOTAL DE HORAS TRABALHADAS NO ANO EM CURSO", font );
        paragraph.setAlignment( Element.ALIGN_LEFT );
        PdfPCell total = new PdfPCell( paragraph );
        total.setColspan( 2 );
        table.addCell( total );
        
        paragraph = new Paragraph( hours.toString() );
        paragraph.setAlignment( Element.ALIGN_CENTER );
        table.addCell( paragraph );
        
        // Document end ---- TODO
        String endText = "Declaro, sob minha inteira responsabilidade, serem exatas e verdadeiras as "
                + "informações aqui prestadas, sob pena de responsabilidades administrativa, civil e penal.";
        paragraph = new Paragraph( endText, font );
        
        Calendar calendar = Calendar.getInstance();
        paragraph.add( "\nPalmas, " + calendar.get( Calendar.DAY_OF_MONTH ) + " de " + calendar.get( Calendar.MONTH ) + " de " + calendar.get( Calendar.YEAR ) + "." );
        
        paragraph.add("\n______________________________________");
        paragraph.add("\nAssinatura do Servidor");
        
        paragraph.setAlignment( Element.ALIGN_JUSTIFIED );
        PdfPCell end = new PdfPCell( paragraph );
        end.setColspan( 3 );
        table.addCell( end );
        
        table.setHorizontalAlignment( Element.ALIGN_CENTER );
        
        declaration.close();
        
        return declaration;
    }
    
    /**
     * 
     * @return 
     */
    private Document generateTermCommitment()
    {
        Document term = new Document( PageSize.A4 );
        
        return term;
    }
    
    /**
     * Pesquisa e atualiza tabela de atividades executadas
     */
    private void updateTableActivity() throws SQLException
    {
        executions = executionDAO.listExecutionByPerson( person );
        Object[][] tableValue = new Object[executions.size()][5];
        
        for( Integer i = 0; i < executions.size(); i++ )
        {
            tableValue[i][0] = executions.get( i ).getDescription();
            tableValue[i][1] = executions.get( i ).getInstitution();
            tableValue[i][2] = executions.get( i ).getHoursworked();
            tableValue[i][3] = executions.get( i ).getYear();
            tableValue[i][4] = executions.get( i ).getPerson().getName();
        }
        
        model = new DefaultTableModel( tableValue, new String [] { "Atividade", "Instituição", "Horas Trabalhadas", "Ano", "Pessoa" } ) {
            Class[] types = new Class [] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean [] { true, true, true, true, false };
            
            public Class getColumnClass(int columnIndex) { return types [columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        };
        
        jTable1.setModel( model );
        jScrollPane1.setViewportView(jTable1);
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
        jPerson = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonNew = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Declaração de Execução de Atividades");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Pessoa:");

        jPerson.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPersonKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Atividade", "Instituição", "Horas Trabalhadas", "Ano", "Pessoa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/new.png"))); // NOI18N
        jButtonNew.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/save.png"))); // NOI18N
        jButtonSave.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/print.png"))); // NOI18N
        jButtonPrint.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPerson)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Action Performed de Novo
     * @param evt 
     */
    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        new ActivityExecutionForm( person ).setVisible( true );
        try{ updateTableActivity(); }
        catch( SQLException e ) { e.printStackTrace(); }
    }//GEN-LAST:event_jButtonNewActionPerformed

    /**
     * Action Performed de Salvar
     * @param evt 
     */
    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try
        {
            for( Integer i = 0; i < model.getRowCount(); i++ )
            {
                executions.get( i ).setDescription( (String) model.getValueAt( i , 0 ) );
                executions.get( i ).setInstitution( (String) model.getValueAt( i , 1 ) );
                executions.get( i ).setHoursworked( Float.parseFloat( model.getValueAt(i , 2 ).toString() ) );
                executions.get( i ).setYear( Integer.parseInt( model.getValueAt( i , 3 ).toString() ) );
                
                executionDAO.update( executions.get( i ) );
            }
            JOptionPane.showMessageDialog( null, "Salvo com sucesso!");
        }
        catch( SQLException e ) { e.printStackTrace(); }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    /**
     * Action Performed de Imprimir
     * @param evt 
     */
    private void jButtonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintActionPerformed
        try{ generateDeclaration(); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( DocumentException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( FileNotFoundException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( IOException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jButtonPrintActionPerformed

    /**
     * 
     * @param evt 
     */
    private void jPersonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPersonKeyReleased
        try
        {
            person = new PersonDAO( DBManager.getInstance().getConnection() ).getPersonByPieceOfName( jPerson.getText() );
            if( person != null )
                updateTableActivity();
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jPersonKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonPrint;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jPerson;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
