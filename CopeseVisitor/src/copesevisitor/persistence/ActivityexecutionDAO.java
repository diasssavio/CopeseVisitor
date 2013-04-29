package copesevisitor.persistence;

import copesevisitor.model.Activityexecution;
import copesevisitor.model.Person;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class ActivityexecutionDAO 
{
    private Connection connection = null;
    
    public ActivityexecutionDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Activityexecution execution ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM activityexecution" );
        if( id != null ) execution.setId( id + 1 );
        else execution.setId( 1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO activityexecution (id, description, campus, edict, hoursworked, status, person_id) VALUES (?, ?, ?, ?, ?, ?, ?)" );
        
        int value = 1;
        statement.setObject( value++, execution.getId() );
        statement.setObject( value++, execution.getDescription() );
        statement.setObject( value++, execution.getCampus() );
        statement.setObject( value++, execution.getEdict() );
        statement.setObject( value++, execution.getHoursworked() );
        statement.setObject( value++, execution.getStatus() );
        statement.setObject( value++, execution.getPerson().getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Activityexecution execution ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE activityexecution SET id=?, description=?, campus=?, edict=?, hoursworked=?, status=?, person_id=? WHERE id=?" );
        
        int value = 1;
        statement.setObject( value++, execution.getId() );
        statement.setObject( value++, execution.getDescription() );
        statement.setObject( value++, execution.getCampus() );
        statement.setObject( value++, execution.getEdict() );
        statement.setObject( value++, execution.getHoursworked() );
        statement.setObject( value++, execution.getStatus() );
        statement.setObject( value++, execution.getPerson().getId() );
        statement.setObject( value++, execution.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Activityexecution execution ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM activityexecution WHERE id=?" );
        
        statement.setObject( 1, execution.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Activityexecution selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activityexecution WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Activityexecution execution = null;
        if( result.next() )
        {
            execution = new Activityexecution( id );
            execution.setDescription( (String) result.getObject( "description" ) );
            execution.setCampus( (String) result.getObject( "campus" ) );
            execution.setEdict( (String) result.getObject( "edict" ) );
            execution.setHoursworked( (Float) result.getObject( "hoursworked" ) );
            execution.setStatus( (Boolean) result.getObject( "status" ) );
            execution.setPerson( new PersonDAO( connection ).selectPK( (Integer) result.getObject( "person_id" ) ) );
        }
        statement.close();
        
        return execution;
    }
    
    public List<Activityexecution> listExecutionByPerson( Person person ) throws SQLException
    {
        List<Activityexecution> executions = new ArrayList<Activityexecution>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activityexecution WHERE person_id=?" );
        
        statement.setObject( 1, person.getId() );
        
        ResultSet result = statement.executeQuery();
        while( result.next() )
        {
            Activityexecution toAdd = new Activityexecution( result.getInt( "id" ) );
            toAdd.setDescription( (String) result.getObject( "description" ) );
            toAdd.setCampus( (String) result.getObject( "campus" ) );
            toAdd.setEdict( (String) result.getObject( "edict" ) );
            toAdd.setHoursworked( (Float) result.getObject( "hoursworked" ) );
            toAdd.setStatus( (Boolean) result.getObject( "status" ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( (Integer) result.getObject( "person_id" ) ) );
            executions.add( toAdd );
        }
        
        statement.close();
        return executions;
    }
    
    /**
     * Lista as atividades de uma pessoa por pedaço de nome
     * @param piece pedaço de nome
     * @param person pessoa
     * @return Lista de atividades
     * @throws SQLException 
     */
    public List<Activityexecution> listByPiece( String piece, Person person ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM activityexecution WHERE description LIKE '" + piece + "%' AND person_id = ?" );
        
        statement.setObject( 1, person.getId() );
        
        ResultSet result = statement.executeQuery();
        List<Activityexecution> activities = new ArrayList<Activityexecution>();
        while( result.next() )
        {
            Activityexecution toAdd = new Activityexecution( result.getInt( "id" ), result.getString( "description" ), result.getString( "campus" ),
                    result.getString( "edict" ), result.getFloat( "hoursworked" ), (Boolean) result.getObject( "status" ) );
            toAdd.setPerson( person );
            activities.add( toAdd );
        }
        statement.close();
        
        return activities;
    }
}
