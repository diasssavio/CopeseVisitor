package copesevisitor.persistence;
import copesevisitor.model.Bankaccount;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class BankaccountDAO {
    private Connection connection = null;
    
    public BankaccountDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Bankaccount account ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM bankaccount" );
        if( id != null ) account.setId( id + 1 );
        else account.setId( 1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO bankaccount (id, agency, checking, bank_id) VALUES (?, ?, ?, ?)" );
        
        statement.setObject( 1, account.getId() );
        statement.setObject( 2, account.getAgency() );
        statement.setObject( 3, account.getChecking() );
        statement.setObject( 4, account.getBank().getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Bankaccount account ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE bankaccount SET id=?, agency=?, checking=?, bank_id=? WHERE id=?" );
        
        statement.setObject( 1, account.getId() );
        statement.setObject( 2, account.getAgency() );
        statement.setObject( 3, account.getChecking() );
        statement.setObject( 4, account.getBank().getId() );
        statement.setObject( 5, account.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Bankaccount account ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM bankaccount WHERE id=?" );
        
        statement.setObject( 1, account.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Bankaccount selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT agency, checking, bank_id FROM bankaccount WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Bankaccount account = new Bankaccount( id );
        if( result.next() )
        {
            account.setAgency( (String) result.getObject( "agency" ) );
            account.setChecking( (String) result.getObject( "checking" ) );
            account.setBank( new BankDAO( connection ).selectPK( (Integer) result.getObject( "bank_id" ) ) );
        }
        statement.close();
        
        return account;
    }
}
