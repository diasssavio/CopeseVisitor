package copesevisitor.persistence;
import copesevisitor.model.Address;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class AddressDAO
{
    private Connection connection = null;
    
    public AddressDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Address address ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM address" );
        if( id != null ) address.setId( id + 1 );
        else address.setId( 1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO address (id, address, number, complement, neighborhood, city, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" );
        
        statement.setObject( 1, address.getId() );
        statement.setObject( 2, address.getAddress() );
        statement.setObject( 3, address.getNumber() != null ? address.getNumber() : null );
        statement.setObject( 4, address.getComplement() != null ? address.getComplement() : null );
        statement.setObject( 5, address.getNeighborhood() );
        statement.setObject( 6, address.getCity() );
        statement.setObject( 7, address.getUf() );
        statement.setObject( 8, address.getCep() != null ? address.getCep() : null );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Address address ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE address SET id=?, address=?, number=?, complement=?, neighborhood=?, city=?, uf=?, cep=? WHERE id=?" );
        
        statement.setObject( 1, address.getId() );
        statement.setObject( 2, address.getAddress() );
        statement.setObject( 3, address.getNumber() );
        statement.setObject( 4, address.getComplement() );
        statement.setObject( 5, address.getNeighborhood() );
        statement.setObject( 6, address.getCity() );
        statement.setObject( 7, address.getUf() );
        statement.setObject( 8, address.getCep() );
        statement.setObject( 9, address.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Address address ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM address WHERE id=?" );
        
        statement.setObject( 1, address.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Address selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT address, number, complement, neighborhood, city, uf, cep FROM address WHERE id=?" );
        
        statement.setObject( 1, id );
        ResultSet result = statement.executeQuery();
        
        Address address = new Address( id );
        if( result.next() )
        {
            address.setAddress( (String) result.getObject( "address" ) );
            address.setNumber( (Integer) result.getObject( "number" ) );
            address.setComplement( (String) result.getObject( "complement" ) );
            address.setNeighborhood( (String) result.getObject( "neighborhood" ) );
            address.setCity( (String) result.getObject( "city" ) );
            address.setUf( (String) result.getObject( "uf" ) );
            address.setCep( (String) result.getObject( "cep" ) );
        }
        statement.close();
        
        return address;
    }
    
    public Address first() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MIN(id) FROM address" );
        return selectPK( id );
    }
    
    public Address last() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MAX(id) FROM address" );
        return selectPK( id );
    }
    
    public Address next( Address address ) throws SQLException
    {
        if( address != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM address WHERE id > ? AND id <= (SELECT MAX(id) FROM person) ORDER BY id LIMIT 1", new Object[]{ address.getId() }) );
        else
            return null;
    }
    
    public Address previous( Address address ) throws SQLException
    {
        if( address != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM address WHERE id < ? AND id >= (SELECT MIN(id) FROM person) ORDER BY id DESC LIMIT 1", new Object[]{ address.getId() } ) );
        else
            return null;
    }
}
