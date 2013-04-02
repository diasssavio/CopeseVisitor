package copesevisitor.persistence;
import copesevisitor.model.Place;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class PlaceDAO {
    private Connection connection = null;
    
    public PlaceDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Place place ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM place" );
        if( id != null ) place.setId( id + 1 );
        else place.setId( 1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO place (id, name, category_id) VALUES (?, ?, ?)" );
        
        statement.setObject( 1, place.getId() );
        statement.setObject( 2, place.getName() );
        statement.setObject( 3, place.getCategory().getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Place place ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE place SET id=?, name=?, category_id=? WHERE id=?" );
        
        statement.setObject( 1, place.getId() );
        statement.setObject( 2, place.getName() );
        statement.setObject( 3, place.getCategory().getId() );
        statement.setObject( 4, place.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Place place ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM place WHERE id=?" );
        
        statement.setObject( 1, place.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Place selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT name, category_id FROM place WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Place place = null;
        if( result.next() )
        {
            place = new Place( id );
            place.setName( (String) result.getObject( "name" ) );
            place.setCategory( new CategoryDAO( connection ).selectPK( result.getInt( "category_id" ) ) );
        }
        statement.close();
        
        return place;
    }
    
    public Place first() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MIN(id) FROM place" );
        return selectPK( id );
    }
    
    public Place last() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MAX(id) FROM place" );
        return selectPK( id );
    }
    
    public Place next( Place place ) throws SQLException
    {
        if( place != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM place WHERE id > ? AND id <= (SELECT MAX(id) FROM place) ORDER BY id LIMIT 1", new Object[]{ place.getId() }) );
        else
            return null;
    }
    
    public Place previous( Place place ) throws SQLException
    {
        if( place != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM place WHERE id < ? AND id >= (SELECT MIN(id) FROM place) ORDER BY id DESC LIMIT 1", new Object[]{ place.getId() } ) );
        else
            return null;
    }
    
    public List<Place> listAllPlaces() throws SQLException
    {
        List<Place> places = new ArrayList<Place>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM place" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
            places.add( new Place( result.getInt( "id" ), result.getString( "name" ) ) );
        
        statement.close();
        return places;
    }
    
    public Place findByName( String name ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM place WHERE name=?" );
        
        statement.setObject( 1, name );
        
        ResultSet result = statement.executeQuery();
        Place place = null;
        if( result.next() )
            place = new Place( result.getInt( "id" ), result.getString( "name" ), new CategoryDAO( connection ).selectPK( result.getInt( "category_id" ) ) );
        
        return place;
    }
}
