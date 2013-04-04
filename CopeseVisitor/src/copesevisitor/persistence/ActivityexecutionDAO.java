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
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO activityexecution (id, description, institution, hoursworked, year, person_id) VALUES (?, ?, ?, ?, ?, ?)" );
        
        statement.setObject( 1, execution.getId() );
        statement.setObject( 2, execution.getDescription() );
        statement.setObject( 3, execution.getInstitution() );
        statement.setObject( 4, execution.getHoursworked() );
        statement.setObject( 5, execution.getYear() );
        statement.setObject( 6, execution.getPerson().getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Activityexecution execution ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE activityexecution SET id=?, description=?, institution=?, hoursworked=?, year=?, person_id=? WHERE id=?" );
        
        statement.setObject( 1, execution.getId() );
        statement.setObject( 2, execution.getDescription() );
        statement.setObject( 3, execution.getInstitution() );
        statement.setObject( 4, execution.getHoursworked());
        statement.setObject( 5, execution.getYear() );
        statement.setObject( 6, execution.getPerson().getId() );
        statement.setObject( 7, execution.getId() );
        
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
            execution.setInstitution( (String) result.getObject( "institution" ) );
            execution.setHoursworked( (Float) result.getObject( "hoursworked" ) );
            execution.setYear( (Integer) result.getObject( "year" ) );
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
            toAdd.setInstitution( (String) result.getObject( "institution" ) );
            toAdd.setHoursworked( (Float) result.getObject( "hoursworked" ) );
            toAdd.setYear( (Integer) result.getObject( "year" ) );
            toAdd.setPerson( new PersonDAO( connection ).selectPK( (Integer) result.getObject( "person_id" ) ) );
            executions.add( toAdd );
        }
        
        statement.close();
        return executions;
    }
}
