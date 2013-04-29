package copesevisitor.view;

import copesevisitor.model.Person;
import copesevisitor.model.Activityexecution;
import copesevisitor.persistence.ActivityexecutionDAO;
import copesevisitor.persistence.DBManager;

import java.util.Calendar;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Sávio Dias
 */
public class ActivityExecutionForm extends JFrame 
{
    private Person person;
    private ActivityexecutionDAO executionDAO;
    
    /**
     * Creates new form ActivityExecutionForm
     */
    public ActivityExecutionForm( Person person )
    {
        initComponents();
        this.setLocationRelativeTo( null );
        
        this.person = person;
        executionDAO = new ActivityexecutionDAO( DBManager.getInstance().getConnection() );
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
        jActivity = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jCampus = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jHoursWorked = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jEdict = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nova Execução de Atividades");

        jLabel1.setText("Atividade:");

        jLabel2.setText("Campus:");

        jLabel3.setText("Horas Trabalhadas:");

        jButtonSave.setText("Salvar");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jLabel4.setText("Edital:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(jButtonSave)
                .addContainerGap(168, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jEdict)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCampus)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jHoursWorked, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jActivity))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jActivity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jCampus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jHoursWorked, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jEdict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSave)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        Activityexecution execution = new Activityexecution();
        execution.setDescription( jActivity.getText() );
        execution.setCampus( jCampus.getText() );
        execution.setEdict( jEdict.getText() );
        execution.setHoursworked( Float.parseFloat( jHoursWorked.getText() ) );
        execution.setStatus( false );
        execution.setPerson( person );
        
        try{ executionDAO.insert( execution ); }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        
        dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jActivity;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JTextField jCampus;
    private javax.swing.JTextField jEdict;
    private javax.swing.JTextField jHoursWorked;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
