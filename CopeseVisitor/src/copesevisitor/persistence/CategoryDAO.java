package copesevisitor.persistence;
import copesevisitor.model.Category;
import copesevisitor.model.Place;
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
public class CategoryDAO {
    private Connection connection = null;
    
    public CategoryDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Category category ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO category (id, name) VALUES (?, ?)" );
        
        statement.setObject( 1, category.getId() );
        statement.setObject( 2, category.getName() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Category category ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE category SET id=?, name=? WHERE id=?" );
        
        statement.setObject( 1, category.getId() );
        statement.setObject( 2, category.getName() );
        statement.setObject( 3, category.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Category category ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM category WHERE id=?" );
        
        statement.setObject( 1, category.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Category selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT name FROM category WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Category category = new Category( id );
        if( result.next() )
            category.setName( (String) result.getObject( "name" ) );
        statement.close();
        
        return category;
    }
    
    public Category findByName( String name ) throws SQLException
    {
        Category toReturn = null;
        for( Category category : listAllCategories() )
            if( category.getName().equals( name ) )
                toReturn = category;
        
        return toReturn;
    }
    
    public List<Category> listAllCategories() throws SQLException
    {
        List<Category> categories = new ArrayList<Category>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM category" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
            categories.add( new Category( result.getInt( "id" ), result.getString( "name" ) ) );
        
        statement.close();
        return categories;
    }
    
    public List<Place> listPlacesByCategory( Category category ) throws SQLException
    {
        List<Place> places = new ArrayList<Place>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT id, name FROM place WHERE place.category_id = ?" );
        statement.setObject( 1, category.getId() );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
            places.add( new Place( result.getInt( "id" ), result.getString( "name" ) ) );
        
        statement.close();
        return places;
    }
}
