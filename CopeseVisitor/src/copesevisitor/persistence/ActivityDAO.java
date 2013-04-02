package copesevisitor.persistence;
import copesevisitor.model.Activity;
import copesevisitor.model.Person;
import copesevisitor.model.Place;
import java.util.Date;
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
public class ActivityDAO {
    private Connection connection = null;
    
    public ActivityDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Activity activity ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM activity" );
        if( id != null ) activity.setId( id + 1 );
        else activity.setId( 1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO activity (id, description, entrancetime, place_id, person_id, event_id) VALUES (?, ?, ?, ?, ?, ?)" );
        
        statement.setObject( 1, activity.getId() );
        statement.setObject( 2, activity.getDescription() );
        statement.setObject( 3, activity.getEntrancetime() );
        statement.setObject( 4, activity.getPlace().getId() );
        statement.setObject( 5, activity.getPerson().getId() );
        statement.setObject( 6, activity.getEvent() != null ? activity.getEvent().getId() : null );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Activity activity ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE activity SET id=?, description=?, entrancetime=?, departuretime=?, place_id=?, person_id=?, event_id=? WHERE id=?" );
        
        statement.setObject( 1, activity.getId() );
        statement.setObject( 2, activity.getDescription() );
        statement.setObject( 3, activity.getEntrancetime() );
        statement.setObject( 4, activity.getDeparturetime() );
        statement.setObject( 5, activity.getPlace().getId() );
        statement.setObject( 6, activity.getPerson().getId() );
        statement.setObject( 7, activity.getEvent() != null ? activity.getEvent().getId() : null );
        statement.setObject( 8, activity.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Activity activity ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM activity WHERE id=?" );
        
        statement.setObject( 1, activity.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Activity selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT description, entrancetime, departuretime, place_id, person_id, event_id FROM activity WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Activity activity = null;
        if( result.next() )
        {
            activity = new Activity( id );
            activity.setDescription( (String) result.getObject( "description" ) );
            activity.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            activity.setDeparturetime( (Date) result.getObject( "departuretime" ) );
            activity.setPlace( new PlaceDAO( connection ).selectPK( (Integer) result.getObject( "place_id" ) ) );
            activity.setPerson( new PersonDAO( connection ).selectPK( (Integer) result.getObject( "person_id" ) ) );
            activity.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
        }
        statement.close();
        
        return activity;
    }
    
    public Activity selectFK( Place place, Person person ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT id, description, entrancetime, departuretime, event_id FROM activity WHERE place_id=? AND person_id=?" );
        
        statement.setObject( 1, place.getId() );
        statement.setObject( 2, person.getId() );
        
        ResultSet result = statement.executeQuery();
        Activity activity = null;
        if( result.next() )
        {
            activity = new Activity( result.getInt( "id" ) );
            activity.setDescription( result.getString( "description" ) );
            activity.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            activity.setDeparturetime( result.getObject( "departuretime" ) != null ? (Date)result.getObject( "departuretime" ) : null );
            activity.setPlace( place );
            activity.setPerson( person );
            activity.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
        }
        
        return activity;
    }
    
    /**
     * Seleciona a atividade que a pessoa person está fazendo no momento
     * @param person pessoa a verificar se está realizando a atividade
     * @return a atividade que person está fazendo no momento
     * @throws SQLException 
     */
    public Activity selectOpenActivity( Person person ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT id, description, entrancetime, departuretime, place_id, event_id FROM activity WHERE person_id=? AND departuretime IS NULL" );
        
        statement.setObject( 1, person.getId() );
        
        ResultSet result = statement.executeQuery();
        Activity activity = null;
        if( result.next() )
        {
            activity = new Activity( result.getInt( "id" ) );
            activity.setDescription( result.getString( "description" ) );
            activity.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            activity.setDeparturetime( null );
            activity.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            activity.setPerson( person );
            activity.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
        }
        
        return activity;
    }
    
    /**
     * Lista todas as atividades abertas (que estão acontecendo)
     * @return atividades abertas
     * @throws SQLException 
     */
    public List<Activity> listAllOpenActivities() throws SQLException
    {
        List<Activity> activities = new ArrayList<Activity>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activity WHERE departuretime IS NULL" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
        {
            Activity toAdd = new Activity( result.getInt( "id" ) );
            toAdd.setDescription( result.getString( "description" ) );
            toAdd.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            toAdd.setDeparturetime( null );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( result.getInt( "person_id" ) ) );
            toAdd.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
            
            activities.add( toAdd );
        }
                                
        statement.close();
        return activities;
    }
    
    /**
     * Lista todas as atividades executadas (que já ocorreram)
     * @return atividades fechadas
     * @throws SQLException 
     */
    public List<Activity> listAllClosedActivities() throws SQLException
    {
        List<Activity> activities = new ArrayList<Activity>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activity WHERE departuretime IS NOT NULL" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
        {
            Activity toAdd = new Activity( result.getInt( "id" ) );
            toAdd.setDescription( result.getString( "description" ) );
            toAdd.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            toAdd.setDeparturetime( (Date) result.getObject( "departuretime" ) );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( result.getInt( "person_id" ) ) );
            toAdd.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
            
            activities.add( toAdd );
        }
                                
        statement.close();
        return activities;
    }
    
    /**
     * Lista todas as atividades
     * @return todas as atividades
     * @throws SQLException 
     */
    public List<Activity> listAllActivities() throws SQLException
    {
        List<Activity> activities = new ArrayList<Activity>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activity" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
        {
            Activity toAdd = new Activity( result.getInt( "id" ) );
            toAdd.setDescription( result.getString( "description" ) );
            toAdd.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            toAdd.setDeparturetime( (Date) result.getObject( "departuretime" ) );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( result.getInt( "person_id" ) ) );
            toAdd.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
            
            activities.add( toAdd );
        }
                                
        statement.close();
        return activities;
    }
    
    /**
     * Lista todas as atividades executadas de uma pessoa
     * @param person pessoa a verificar atividades
     * @return todas as atividades executadas de person
     * @throws SQLException 
     */
    public List<Activity> listAllClosedActivitiesFromPerson( Person person ) throws SQLException
    {
        List<Activity> activities = new ArrayList<Activity>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activity WHERE person_id=? AND departuretime IS NOT NULL" );
        
        statement.setObject( 1, person.getId() );
        
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
        {
            Activity toAdd = new Activity( result.getInt( "id" ) );
            toAdd.setDescription( result.getString( "description" ) );
            toAdd.setEntrancetime( (Date) result.getObject( "entrancetime" ) );
            toAdd.setDeparturetime( (Date) result.getObject( "departuretime" ) );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( result.getInt( "person_id" ) ) );
            toAdd.setEvent( new EventDAO( connection ).selectPK( (Integer) result.getObject( "event_id" ) ) );
            
            activities.add( toAdd );
        }
                                
        statement.close();
        return activities;
    }
}
