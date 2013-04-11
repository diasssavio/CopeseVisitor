package copesevisitor.view;

import copesevisitor.model.Bank;
import copesevisitor.model.Bankaccount;
import copesevisitor.model.Address;
import copesevisitor.model.Person;
import copesevisitor.persistence.AddressDAO;
import copesevisitor.persistence.BankDAO;
import copesevisitor.persistence.BankaccountDAO;
import copesevisitor.persistence.DBManager;
import copesevisitor.persistence.PersonDAO;
import javax.swing.JOptionPane;
import java.text.DateFormat;
import java.text.ParseException;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class PersonForm extends javax.swing.JFrame
{
    private BankDAO bankDAO;
    private BankaccountDAO accountDAO;
    private PersonDAO personDAO;
    private AddressDAO addressDAO;
    
    private Person person;
    private Address address;
    private Bankaccount account;
    
    /**
     * Cria um novo formulário PersonForm
     * @param anPerson Pessoa para ser mostrada no formulário
     * @throws SQLException 
     */
    public PersonForm( Person anPerson ) throws SQLException
    {
        bankDAO = new BankDAO( DBManager.getInstance().getConnection() );
        accountDAO = new BankaccountDAO( DBManager.getInstance().getConnection() );
        personDAO = new PersonDAO( DBManager.getInstance().getConnection() );
        addressDAO = new AddressDAO( DBManager.getInstance().getConnection() );
        
        initComponents();
        loadBanksComboBox();
        jOutsourced.setEnabled( false );
        jExternal.setEnabled( false );
        this.setLocationRelativeTo( null );
        
        if( anPerson == null )
            person = personDAO.first();
        else
            person = anPerson;
        
        if( person == null )
            cleanForm();
        else
        {
            address = person.getAddress();
            account = person.getBankaccount();
            updateView();
        }
    }

    /**
     * Limpa os dados do formulário
     */
    private void cleanForm()
    {
        address = null;
        account = null;
        person = null;
        
        // Dados pessoais
        jName.setText( null );
        jFormattedCPF.setValue( null );
        jFormattedBirthDate.setValue( null );
        jRG.setText( null );
        jWichOrgan.setText( null );
        jEmail.setText( null );
        jComboGender.setSelectedIndex( -1 );
        jFormattedPIS.setValue( null );
        jSiape.setText( null );
        jFormattedPhone1.setValue( null );
        jFormattedPhone2.setValue( null );
        jFormattedPhone3.setValue( null );

        // Dados do endereço
        jAddress.setText( null );
        jNumber.setText( null );
        jComplement.setText( null );
        jNeighborhood.setText( null );
        jCity.setText( null );
        jCity.setText( null );
        jComboUF.setSelectedIndex( -1 );
        jFormattedCEP.setValue( null );

        // Vinculo com a UFT
        jComboBank.setSelectedIndex( -1 );
        jAgency.setText( null );
        jChecking.setText( null );
        setSelectedRadioBox( null, null );
    }
    
    /**
     * Preenche o formulário com os dados da pessoa atual
     */
    private void updateView()
    {
        if( person != null )
        {
            // Dados pessoais
            jName.setText( person.getName() );
            jFormattedCPF.setText( person.getCpf() );
            jFormattedBirthDate.setText( person.getBirthdate() != null ? DateFormat.getDateInstance().format( person.getBirthdate() ) : null );
            jRG.setText( person.getRg() );
            jWichOrgan.setText( person.getWichorgan() );
            jEmail.setText( person.getEmail() );
            jComboGender.setSelectedItem( person.getGender() );
            jFormattedPIS.setText( person.getPispasep() );
            jSiape.setText( person.getSiape() );
            jFormattedPhone1.setText( person.getPhone1() );
            jFormattedPhone2.setText( person.getPhone2() );
            jFormattedPhone3.setText( person.getPhone3() );

            // Dados do endereço
            jAddress.setText( person.getAddress().getAddress() );
            jNumber.setText( person.getAddress().getNumber() != null ? person.getAddress().getNumber().toString() : null );
            jComplement.setText( person.getAddress().getComplement() );
            jNeighborhood.setText( person.getAddress().getNeighborhood() );
            jCity.setText( person.getAddress().getCity() );
            jCity.setText( person.getAddress().getCity() );
            jComboUF.setSelectedItem( person.getAddress().getUf() );
            jFormattedCEP.setText( person.getAddress().getCep() );

            // Vinculo com a UFT
            jComboBank.setSelectedItem( person.getBankaccount().getBank().getName() );
            jAgency.setText( person.getBankaccount().getAgency() );
            jChecking.setText( person.getBankaccount().getChecking() );
            setSelectedRadioBox( person.getUftlink(), person.getStocking() );
        }
        else
            cleanForm();
    }
    
    /**
     * Preenche o combo box com os bancos cadastrados
     * @throws SQLException 
     */
    private void loadBanksComboBox() throws SQLException
    {
        jComboBank.removeAllItems();
        for( Bank bank : bankDAO.listAllBanks() )
            jComboBank.addItem( bank.getName() );
    }
    
    /**
     * Retorna uma String que representa o radio box selecionado
     * @return String que representa o radio box selecionado
     */
    private String getSelectedRadioBox()
    {
        String uftLink = "";
        
        if( jRadioProfessor.isSelected() ) uftLink = jRadioProfessor.getText();
        if( jRadioTechnician.isSelected() ) uftLink = jRadioTechnician.getText();
        if( jRadioStudent.isSelected() ) uftLink = jRadioStudent.getText();
        if( jRadioTrainee.isSelected() ) uftLink = jRadioTrainee.getText();
        if( jRadioColleger.isSelected() ) uftLink = jRadioColleger.getText();
        if( jRadioOutsourced.isSelected() ) uftLink = jRadioOutsourced.getText();
        if( jRadioExternal.isSelected() ) uftLink = jRadioExternal.getText();
        
        return uftLink;
    }
    
    /**
     * Marca o radio box correspondente à String passada
     * @param uftLink String que representa o radio box
     */
    private void setSelectedRadioBox( String uftLink, String stocking )
    {
        try
        {
            if( uftLink.equals( jRadioProfessor.getText() ) ) jRadioProfessor.setSelected( true );
            else if( uftLink.equals( jRadioTechnician.getText() ) ) jRadioTechnician.setSelected( true );
            else if( uftLink.equals( jRadioStudent.getText() ) ) jRadioStudent.setSelected( true );
            else if( uftLink.equals( jRadioTrainee.getText() ) ) jRadioTrainee.setSelected( true );
            else if( uftLink.equals( jRadioColleger.getText() ) ) jRadioColleger.setSelected( true );
            else if( uftLink.equals( jRadioExternal.getText() ) )
            {
                jRadioExternal.setSelected( true );
                jExternal.setText( stocking );
                return;
            }
            else if( uftLink.equals( jRadioOutsourced.getText() ) )
            {
                jRadioOutsourced.setSelected( true );
                jOutsourced.setText( stocking );
                return;
            }
            
            jStocking.setText( stocking );
        }
        catch( NullPointerException e )
        {
            jRadioProfessor.setSelected( false );
            jRadioTechnician.setSelected( false );
            jRadioStudent.setSelected( false );
            jRadioTrainee.setSelected( false );
            jRadioColleger.setSelected( false );
            jRadioOutsourced.setSelected( false );
            jOutsourced.setText( null );
            jOutsourced.setEnabled( false );
            jExternal.setText( null );
            jExternal.setEnabled( false );
            jStocking.setText( null );
        }
    }
    
    /**
     * 
     * @return 
     */
    private Address getAddressFromForm()
    {
        // Getting address information
        Address tempAd = new Address();
        tempAd.setAddress( jAddress.getText() );
        tempAd.setNumber( !jNumber.getText().equals( "" ) ? Integer.parseInt( jNumber.getText() ) : null );
        tempAd.setComplement( jComplement.getText() );
        tempAd.setNeighborhood( jNeighborhood.getText() );
        tempAd.setCity( jCity.getText() );
        tempAd.setUf( jComboUF.getSelectedItem().toString() );
        tempAd.setCep( jFormattedCEP.getValue() != null ? jFormattedCEP.getText() : null );
        
        return tempAd;
    }
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    private Bankaccount getAccountFromForm() throws SQLException
    {
        // Getting bank account information
        Bankaccount tempAccount = new Bankaccount();
        tempAccount.setAgency( jAgency.getText() );
        tempAccount.setChecking( jChecking.getText() );
        tempAccount.setBank( bankDAO.findByName( jComboBank.getSelectedItem().toString() ) );
        
        return tempAccount;
    }
    
    /**
     * 
     * @return
     * @throws SQLException
     * @throws ParseException 
     */
    private Person getPersonFromForm() throws SQLException, ParseException
    {
        // Getting person information
        Person temp = new Person();
        temp.setName( jName.getText() );
        temp.setCpf( jFormattedCPF.getText() );
        temp.setBirthdate( jFormattedBirthDate.getValue() != null ? DateFormat.getDateInstance().parse( jFormattedBirthDate.getText() ) : null );
        temp.setRg( jRG.getText() );
        temp.setWichorgan( jWichOrgan.getText() != null ? jWichOrgan.getText() : null );
        temp.setEmail( jEmail.getText() );
        temp.setGender( jComboGender.getSelectedItem().toString() );
        temp.setPispasep( jFormattedPIS.getValue() != null ? jFormattedPIS.getText() : null );
        temp.setSiape( !jSiape.getText().equals("") ? jSiape.getText() : null );
        temp.setPhone1( jFormattedPhone1.getText() );
        temp.setPhone2( jFormattedPhone2.getValue() != null ? jFormattedPhone2.getText() : null );
        temp.setPhone3( jFormattedPhone3.getValue() != null ? jFormattedPhone3.getText() : null );
        
        temp.setUftlink( getSelectedRadioBox() );
        if( temp.getUftlink().equals( jRadioOutsourced.getText() ) )
            temp.setStocking( !jOutsourced.getText().equals("") ? jOutsourced.getText() : null );
        else if( temp.getUftlink().equals( jRadioExternal.getText() ) )
            temp.setStocking( !jExternal.getText().equals("") ? jExternal.getText() : null );
        else
            temp.setStocking( !jStocking.getText().equals("") ? jStocking.getText() : null );
        
        temp.setAddress( address );
        temp.setBankaccount( account );
        
        return temp;
    }
    
    /**
     * Desativa todos os botões para que o usuário não altere 
     * as informações do banco de dados
     */
    public void disableButtons()
    {
        jButtonFirst.setEnabled( false );
        jButtonPrevious.setEnabled( false );
        jButtonNext.setEnabled( false );
        jButtonLast.setEnabled( false );
        jButtonNew.setEnabled( false );
        jButtonAdd.setEnabled( false );
        jButtonDelete.setEnabled( false );
        jButtonSearch.setEnabled( false );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jFormattedPIS = new javax.swing.JFormattedTextField();
        jFormattedBirthDate = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jRG = new javax.swing.JTextField();
        jWichOrgan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboGender = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jFormattedCPF = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jFormattedPhone1 = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jFormattedPhone2 = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jFormattedCEP = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jAddress = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jNumber = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jComplement = new javax.swing.JTextField();
        jNeighborhood = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jCity = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboUF = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jFormattedPhone3 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jAgency = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jComboBank = new javax.swing.JComboBox();
        jChecking = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jOutsourced = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jExternal = new javax.swing.JTextField();
        jButtonBank = new javax.swing.JButton();
        jRadioProfessor = new javax.swing.JRadioButton();
        jRadioTechnician = new javax.swing.JRadioButton();
        jRadioStudent = new javax.swing.JRadioButton();
        jRadioTrainee = new javax.swing.JRadioButton();
        jRadioColleger = new javax.swing.JRadioButton();
        jRadioOutsourced = new javax.swing.JRadioButton();
        jRadioExternal = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        jSiape = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jEmail = new javax.swing.JTextField();
        jStocking = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButtonSearch = new javax.swing.JButton();
        jButtonPrevious = new javax.swing.JButton();
        jButtonFirst = new javax.swing.JButton();
        jButtonLast = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonNew = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButtonAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Pessoal");
        setResizable(false);

        jLabel2.setText("DADOS PESSOAIS");

        jLabel3.setText("Nome Completo:");

        jLabel4.setText("CPF:");

        try {
            jFormattedPIS.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.#####.##-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedBirthDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel5.setText("Data de Nascimento:");

        jLabel6.setText("R.G.:");

        jLabel7.setText("Orgão Expeditor:");

        jLabel8.setText("Sexo:");

        jComboGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Masculino", "Feminino" }));

        jLabel9.setText("PIS/PASEP:");

        try {
            jFormattedCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Telefone Contato 1:");

        try {
            jFormattedPhone1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel11.setText("Telefone Contato 2:");

        try {
            jFormattedPhone2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel12.setText("Telefone Contato 3:");

        try {
            jFormattedCEP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel13.setText("DADOS DE ENDEREÇO");

        jLabel14.setText("Endereço:");

        jLabel15.setText("N°/Lote");

        jLabel16.setText("Complemento:");

        jLabel17.setText("Bairro:");

        jLabel18.setText("Cidade:");

        jLabel19.setText("UF:");

        jComboUF.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        jLabel20.setText("CEP:");

        try {
            jFormattedPhone3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel21.setText("DADOS BANCARIOS");

        jLabel22.setText("Banco:");

        jLabel23.setText("Agência:");

        jLabel24.setText("Conta Corrente:");

        jLabel25.setText("VINCULO COM A UFT");

        jButtonBank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/new.png"))); // NOI18N
        jButtonBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioProfessor);
        jRadioProfessor.setText("Professor");

        buttonGroup1.add(jRadioTechnician);
        jRadioTechnician.setText("Técnico Administrativo");

        buttonGroup1.add(jRadioStudent);
        jRadioStudent.setText("Aluno Matriculado");

        buttonGroup1.add(jRadioTrainee);
        jRadioTrainee.setText("Aluno Estagiário da UFT");

        buttonGroup1.add(jRadioColleger);
        jRadioColleger.setText("Aluno Bolsista da UFT");

        buttonGroup1.add(jRadioOutsourced);
        jRadioOutsourced.setText("Terceirizado - Empresa:");
        jRadioOutsourced.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioOutsourcedStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioExternal);
        jRadioExternal.setText("Externo - OBS:");
        jRadioExternal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioExternalStateChanged(evt);
            }
        });

        jLabel26.setText("Siape:");

        jLabel27.setText("E-mail:");

        jLabel28.setText("Lotação:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedCPF))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRG, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jWichOrgan, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jFormattedPIS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSiape, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedPhone1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedPhone2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedPhone3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(jAddress)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(jNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(jComplement, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(jNeighborhood))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(jCity)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(jComboUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBank, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBank, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(jAgency, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jChecking, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioOutsourced)
                        .addGap(18, 18, 18)
                        .addComponent(jOutsourced))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioExternal)
                        .addGap(18, 18, 18)
                        .addComponent(jExternal))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioProfessor)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel28)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioTechnician)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioStudent)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioTrainee)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioColleger)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jStocking))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jFormattedCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jFormattedBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jWichOrgan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(jSiape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jComboGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jFormattedPIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jFormattedPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jFormattedPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jFormattedPhone3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComplement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jNeighborhood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jComboUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jFormattedCEP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBank, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBank, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(jAgency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)
                        .addComponent(jChecking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioProfessor)
                    .addComponent(jRadioTechnician)
                    .addComponent(jRadioStudent)
                    .addComponent(jRadioTrainee)
                    .addComponent(jRadioColleger))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jStocking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jOutsourced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioOutsourced))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jExternal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioExternal))
                .addGap(23, 23, 23))
        );

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/search.png"))); // NOI18N
        jButtonSearch.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jButtonPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/previous.png"))); // NOI18N
        jButtonPrevious.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jButtonFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/first.png"))); // NOI18N
        jButtonFirst.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFirstActionPerformed(evt);
            }
        });

        jButtonLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/last.png"))); // NOI18N
        jButtonLast.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLastActionPerformed(evt);
            }
        });

        jButtonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/next.png"))); // NOI18N
        jButtonNext.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/delete.png"))); // NOI18N
        jButtonDelete.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/new.png"))); // NOI18N
        jButtonNew.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copesevisitor/view/images/save.png"))); // NOI18N
        jButtonAdd.setPreferredSize(new java.awt.Dimension(50, 25));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(jButtonFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonLast, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonFirst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cadastrar Banco
     * @param evt 
     */
    private void jButtonBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankActionPerformed
        new BankForm( jComboBank ).setVisible( true );
    }//GEN-LAST:event_jButtonBankActionPerformed

    /**
     * Terceirizado selecionado
     * @param evt 
     */
    private void jRadioOutsourcedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioOutsourcedStateChanged
        if( jRadioOutsourced.isSelected() )
        {
            jOutsourced.setEnabled( true );
            jStocking.setEnabled( false );
        }
        else if( jRadioExternal.isSelected() )
        {
            jOutsourced.setEnabled( false );
            jStocking.setEnabled( false );
        }
        else
        {
            jStocking.setEnabled( true );
            jExternal.setEnabled( false );
            jOutsourced.setEnabled( false );
        }
    }//GEN-LAST:event_jRadioOutsourcedStateChanged

    /**
     * Externo selecionado
     * @param evt 
     */
    private void jRadioExternalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioExternalStateChanged
        if( jRadioExternal.isSelected() )
        {
            jExternal.setEnabled( true );
            jStocking.setEnabled( false );
        }
        else if( jRadioOutsourced.isSelected() )
        {
            jExternal.setEnabled( false );
            jStocking.setEnabled( false );
        }
        else
        {
            jStocking.setEnabled( true );
            jExternal.setEnabled( false );
            jOutsourced.setEnabled( false );
        }
    }//GEN-LAST:event_jRadioExternalStateChanged

    /**
     * Busca selecionada
     * @param evt 
     */
    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        
    }//GEN-LAST:event_jButtonSearchActionPerformed

    /**
     * Pessoa Anterior selecionada
     * @param evt 
     */
    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPreviousActionPerformed
        try{ 
            person = personDAO.previous( person ); 
            address = person.getAddress();
            account = person.getBankaccount();
        }
        catch( SQLException e ){ JOptionPane.showMessageDialog( null, e.getMessage() ); }
        updateView();
    }//GEN-LAST:event_jButtonPreviousActionPerformed

    /**
     * Primeira Pessoa selecionada
     * @param evt 
     */
    private void jButtonFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFirstActionPerformed
        try{ 
            person = personDAO.first(); 
            address = person.getAddress();
            account = person.getBankaccount();
        }
        catch( SQLException e ){ JOptionPane.showMessageDialog( null, e.getMessage() ); }
        updateView();
    }//GEN-LAST:event_jButtonFirstActionPerformed

    /**
     * Ultima Pessoa selecionada
     * @param evt 
     */
    private void jButtonLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLastActionPerformed
        try{ 
            person = personDAO.last(); 
            address = person.getAddress();
            account = person.getBankaccount();
        }
        catch( SQLException e ){ JOptionPane.showMessageDialog( null, e.getMessage() ); }
        updateView();
    }//GEN-LAST:event_jButtonLastActionPerformed

    /**
     * Próxima Pessoa selecionada
     * @param evt 
     */
    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        try{
            person = personDAO.next( person ); 
            address = person.getAddress();
            account = person.getBankaccount();
        }
        catch( SQLException e ){ JOptionPane.showMessageDialog( null, e.getMessage() ); }
        updateView();
    }//GEN-LAST:event_jButtonNextActionPerformed

    /**
     * Deletar Pessoa selecionada
     * @param evt 
     */
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        try{ 
            addressDAO.delete( address );
            accountDAO.delete( account );
            personDAO.delete( person );
            
            person = personDAO.first();
            account = person.getBankaccount();
            address = person.getAddress();
        } catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        updateView();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    /**
     * Nova Pessoa selecionada
     * @param evt 
     */
    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        cleanForm();
    }//GEN-LAST:event_jButtonNewActionPerformed

    /**
     * Adicionar Pessoa selecionada
     * @param evt 
     */
    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        try
        {
            // If is an update or a new instance
            // Check for the address
            if( address == null )
            {
                address = getAddressFromForm();
                addressDAO.insert( address );
            }
            else
            {
                Integer id = address.getId();
                address = getAddressFromForm();
                address.setId( id );
                addressDAO.update( address );
            }
            
            // Check for the account
            if( account == null )
            {
                account = getAccountFromForm();
                accountDAO.insert( account );
            }
            else
            {
                Integer id = account.getId();
                account = getAccountFromForm();
                account.setId( id );
                accountDAO.update( account );
            }
            
            // Check for the person
            if( person == null )
            {
                person = getPersonFromForm();
                personDAO.insert( person );
            }
            else
            {
                Integer id = person.getId();
                person = getPersonFromForm();
                person.setId( id );
                personDAO.update( person );
            }
            
            JOptionPane.showMessageDialog( null, "Pessoa salva com sucesso" );
        }
        catch( SQLException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
        catch( ParseException e ) { JOptionPane.showMessageDialog( null, e.getMessage() ); }
    }//GEN-LAST:event_jButtonAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField jAddress;
    private javax.swing.JTextField jAgency;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBank;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonFirst;
    private javax.swing.JButton jButtonLast;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrevious;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JTextField jChecking;
    private javax.swing.JTextField jCity;
    private javax.swing.JComboBox jComboBank;
    private javax.swing.JComboBox jComboGender;
    private javax.swing.JComboBox jComboUF;
    private javax.swing.JTextField jComplement;
    private javax.swing.JTextField jEmail;
    private javax.swing.JTextField jExternal;
    private javax.swing.JFormattedTextField jFormattedBirthDate;
    private javax.swing.JFormattedTextField jFormattedCEP;
    private javax.swing.JFormattedTextField jFormattedCPF;
    private javax.swing.JFormattedTextField jFormattedPIS;
    private javax.swing.JFormattedTextField jFormattedPhone1;
    private javax.swing.JFormattedTextField jFormattedPhone2;
    private javax.swing.JFormattedTextField jFormattedPhone3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jName;
    private javax.swing.JTextField jNeighborhood;
    private javax.swing.JTextField jNumber;
    private javax.swing.JTextField jOutsourced;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jRG;
    private javax.swing.JRadioButton jRadioColleger;
    private javax.swing.JRadioButton jRadioExternal;
    private javax.swing.JRadioButton jRadioOutsourced;
    private javax.swing.JRadioButton jRadioProfessor;
    private javax.swing.JRadioButton jRadioStudent;
    private javax.swing.JRadioButton jRadioTechnician;
    private javax.swing.JRadioButton jRadioTrainee;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField jSiape;
    private javax.swing.JTextField jStocking;
    private javax.swing.JTextField jWichOrgan;
    // End of variables declaration//GEN-END:variables
}
