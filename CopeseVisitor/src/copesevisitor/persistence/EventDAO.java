package copesevisitor.persistence;
import copesevisitor.model.Event;
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
public class EventDAO {
    private Connection connection = null;
    
    public EventDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Event event ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO event (id, name, description) VALUES (?, ?, ?)" );
        
        statement.setObject( 1, event.getId() );
        statement.setObject( 2, event.getName() );
        statement.setObject( 3, event.getDescription() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Event event ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE event SET id=?, name=?, description=? WHERE id=?" );
        
        statement.setObject( 1, event.getId() );
        statement.setObject( 2, event.getName() );
        statement.setObject( 3, event.getDescription() );
        statement.setObject( 4, event.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Event event ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM event WHERE id=?" );
        
        statement.setObject( 1, event.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Event selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT name, description FROM event WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Event event = null;
        if( result.next() )
        {
            event = new Event( id );
            event.setName( result.getString( "name" ) );
            event.setDescription( (String) result.getObject( "description" ) );
        }
        statement.close();
        
        return event;
    }
    
    public Event first() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MIN(id) FROM event" );
        return selectPK( id );
    }
    
    public Event last() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MAX(id) FROM event" );
        return selectPK( id );
    }
    
    public Event next( Event event ) throws SQLException
    {
        if( event != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM event WHERE id > ? AND id <= (SELECT MAX(id) FROM event) ORDER BY id LIMIT 1", new Object[]{ event.getId() }) );
        else
            return null;
    }
    
    public Event previous( Event event ) throws SQLException
    {
        if( event != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM event WHERE id < ? AND id >= (SELECT MIN(id) FROM event) ORDER BY id DESC LIMIT 1", new Object[]{ event.getId() } ) );
        else
            return null;
    }
    
    public Event findByName( String name ) throws SQLException
    {
        Event toReturn = null;
        for( Event event : listAllEvents() )
            if( event.getName().equals( name ) )
                toReturn = event;
        
        return toReturn;
    }
    
    public List<Event> listAllEvents() throws SQLException
    {
        List<Event> events = new ArrayList<Event>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM event" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
            events.add( new Event( result.getInt( "id" ), result.getString( "name" ), result.getString( "description" ) ) );
        
        statement.close();
        return events;
    }
}
