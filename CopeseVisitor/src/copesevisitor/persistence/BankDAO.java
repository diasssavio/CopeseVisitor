package copesevisitor.persistence;
import copesevisitor.model.Bank;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class BankDAO {
    private Connection connection = null;
    
    public BankDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Bank bank ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO bank (id, name, acronym) VALUES (?, ?, ?)" );
        
        statement.setObject( 1, bank.getId() );
        statement.setObject( 2, bank.getName() );
        statement.setObject( 3, bank.getAcronym() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Bank bank ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE bank SET id=?, name=?, acronym=? WHERE id=?" );
        
        statement.setObject( 1, bank.getId() );
        statement.setObject( 2, bank.getName() );
        statement.setObject( 3, bank.getAcronym() );
        statement.setObject( 4, bank.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Bank bank ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM bank WHERE id=?" );
        
        statement.setObject( 1, bank.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Bank selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT name, acronym FROM bank WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Bank bank = new Bank( id );
        if( result.next() )
        {
            bank.setName( (String) result.getObject( "name" ) );
            bank.setAcronym( (String) result.getObject( "acronym" ) );
        }
        statement.close();
        
        return bank;
    }
    
    public Bank findByName( String name ) throws SQLException
    {
        Bank toReturn = null;
        for( Bank bank : listAllBanks() )
            if( bank.getName().equals( name ) )
                toReturn = bank;
        
        return toReturn;
    }
    
    public List<Bank> listAllBanks() throws SQLException
    {
        List<Bank> banks = new ArrayList<Bank>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM bank" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
            banks.add( new Bank( result.getInt( "id" ), result.getString( "name" ), result.getString( "acronym" ) ) );
        
        statement.close();
        return banks;
    }
}
