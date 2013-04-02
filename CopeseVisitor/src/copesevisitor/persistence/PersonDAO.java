package copesevisitor.persistence;
import copesevisitor.model.Person;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class PersonDAO {
    private Connection connection = null;
    
    public PersonDAO( Connection connection )
    {
        this.connection = connection;
    }
    
    public void insert( Person person ) throws SQLException
    {
        Integer id = ( Integer ) DBManager.getInstance().getValue( "SELECT MAX(id) FROM person" );
        if( id != null ) person.setId( id + 1 );
        else person.setId( ( Integer )1 );
        
        PreparedStatement statement = connection.prepareStatement( "INSERT INTO person (id, cpf, name, birthdate, rg, wichorgan, email, gender, pispasep, siape, phone1, phone2, phone3, uftlink, bankaccount_id, address_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );
        
        statement.setObject( 1, person.getId() );
        statement.setObject( 2, person.getCpf() );
        statement.setObject( 3, person.getName() );
        statement.setObject( 4, person.getBirthdate() );
        statement.setObject( 5, person.getRg() );
        statement.setObject( 6, person.getWichorgan() );
        statement.setObject( 7, person.getEmail() );
        statement.setObject( 8, person.getGender() );
        statement.setObject( 9, person.getPispasep() );
        statement.setObject( 10, person.getSiape() );
        statement.setObject( 11, person.getPhone1() );
        statement.setObject( 12, person.getPhone2() );
        statement.setObject( 13, person.getPhone3() );
        statement.setObject( 14, person.getUftlink() );
        statement.setObject( 15, person.getBankaccount().getId() );
        statement.setObject( 16, person.getAddress().getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update( Person person ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "UPDATE person SET id=?, cpf=?, name=?, birthdate=?, rg=?, wichorgan=?, email=?, gender=?, pispasep=?, siape=?, phone1=?, phone2=?, phone3=?, uftlink=?, place_id=?, bankaccount_id=?, address_id=? WHERE id=?" );
        
        statement.setObject( 1, person.getId() );
        statement.setObject( 2, person.getCpf() );
        statement.setObject( 3, person.getName() );
        statement.setObject( 4, person.getBirthdate() );
        statement.setObject( 5, person.getRg() );
        statement.setObject( 6, person.getWichorgan() );
        statement.setObject( 7, person.getEmail() );
        statement.setObject( 8, person.getGender() );
        statement.setObject( 9, person.getPispasep() );
        statement.setObject( 10, person.getSiape() );
        statement.setObject( 11, person.getPhone1() );
        statement.setObject( 12, person.getPhone2() );
        statement.setObject( 13, person.getPhone3() );
        statement.setObject( 14, person.getUftlink() );
        statement.setObject( 15, person.getPlace() != null ? person.getPlace().getId() : null );
        statement.setObject( 16, person.getBankaccount().getId() );
        statement.setObject( 17, person.getAddress().getId() );
        statement.setObject( 18, person.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete( Person person ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "DELETE FROM person WHERE id=?" );
        
        statement.setObject( 1, person.getId() );
        
        statement.executeUpdate();
        statement.close();
    }
    
    public Person selectPK( Integer id ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM person WHERE id=?" );
        
        statement.setObject( 1, id );
        
        ResultSet result = statement.executeQuery();
        Person person = null;
        if( result.next() )
        {
            person = new Person( id );
            person.setCpf( (String) result.getObject( "cpf" ) );
            person.setName( (String) result.getObject( "name" ) );
            person.setBirthdate( (Date) result.getObject( "birthdate" ) );
            person.setRg( (String) result.getObject( "rg" ) );
            person.setWichorgan( (String) result.getObject( "wichorgan" ) );
            person.setEmail( (String) result.getObject( "email" ) );
            person.setGender( (String) result.getObject( "gender" ) );
            person.setPispasep( (String) result.getObject( "pispasep" ) );
            person.setSiape( (String) result.getObject( "siape" ) );
            person.setPhone1( (String) result.getObject( "phone1" ) );
            person.setPhone2( (String) result.getObject( "phone2" ) );
            person.setPhone3( (String) result.getObject( "phone3" ) );
            person.setUftlink( (String) result.getObject( "uftlink" ) );
            person.setPlace( new PlaceDAO( connection ).selectPK( (Integer) result.getObject( "place_id" ) ) );
            person.setBankaccount( new BankaccountDAO( connection ).selectPK( (Integer) result.getObject( "bankaccount_id" ) ) );
            person.setAddress( new AddressDAO( connection ).selectPK( (Integer) result.getObject( "address_id" ) ) );
        }
        statement.close();
        
        return person;
    }
    
    public Person first() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MIN(id) FROM person" );
        return selectPK( id );
    }
    
    public Person last() throws SQLException
    {
        Integer id = (Integer) DBManager.getInstance().getValue( "SELECT MAX(id) FROM person" );
        return selectPK( id );
    }
    
    public Person next( Person person ) throws SQLException
    {
        if( person != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM person WHERE id > ? AND id <= (SELECT MAX(id) FROM person) ORDER BY id LIMIT 1", new Object[]{ person.getId() }) );
        else
            return null;
    }
    
    public Person previous( Person person ) throws SQLException
    {
        if( person != null )
            return selectPK( (Integer) DBManager.getInstance().getValue( "SELECT id FROM person WHERE id < ? AND id >= (SELECT MIN(id) FROM person) ORDER BY id DESC LIMIT 1", new Object[]{ person.getId() } ) );
        else
            return null;
    }
    
    /**
     * Lista todas as pessoas
     * @param name
     * @return
     * @throws SQLException 
     */
    public List<Person> listAllPeople() throws SQLException
    {
        List<Person> people = new ArrayList<Person>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM person" );
        ResultSet result = statement.executeQuery();
        
        while( result.next() )
        {
            Person toAdd = new Person( result.getInt( "id" ) );
            toAdd.setCpf( result.getString( "cpf" ) );
            toAdd.setName( result.getString( "name" ) );
            toAdd.setBirthdate( (Date) result.getObject( "birthdate" ) );
            toAdd.setRg( result.getString( "rg" ) );
            toAdd.setWichorgan( result.getString( "wichorgan" ) );
            toAdd.setEmail( result.getString( "email" ) );
            toAdd.setGender( result.getString( "gender" ) );
            toAdd.setPispasep( result.getString( "pispasep" ) );
            toAdd.setSiape( result.getString( "siape" ) );
            toAdd.setPhone1( result.getString( "phone1" ) );
            toAdd.setPhone2( result.getString( "phone2" ) );
            toAdd.setPhone3( result.getString( "phone3" ) );
            toAdd.setUftlink( result.getString( "uftlink" ) );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setBankaccount( new BankaccountDAO( connection ).selectPK( result.getInt( "bankaccount_id" ) ) );
            toAdd.setAddress( new AddressDAO( connection ).selectPK( result.getInt( "address_id" ) ) );
            
            people.add( toAdd );
        }     
                                
        statement.close();
        return people;
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws SQLException 
     */
    public Person findByName( String name ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM person WHERE name=?" );
        
        statement.setObject( 1, name );
        
        Person person = null;
        ResultSet result = statement.executeQuery();
        if( result.next() )
        {
            person = new Person( result.getInt( "id" ) );
            person.setCpf( result.getString( "cpf" ) );
            person.setName( result.getString( "name" ) );
            person.setBirthdate( (Date) result.getObject( "birthdate" ) );
            person.setRg( result.getString( "rg" ) );
            person.setWichorgan( result.getString( "wichorgan" ) );
            person.setEmail( result.getString( "email" ) );
            person.setGender( result.getString( "gender" ) );
            person.setPispasep( result.getString( "pispasep" ) );
            person.setSiape( result.getString( "siape" ) );
            person.setPhone1( result.getString( "phone1" ) );
            person.setPhone2( result.getString( "phone2" ) );
            person.setPhone3( result.getString( "phone3" ) );
            person.setUftlink( result.getString( "uftlink" ) );
            person.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            person.setBankaccount( new BankaccountDAO( connection ).selectPK( result.getInt( "bankaccount_id" ) ) );
            person.setAddress( new AddressDAO( connection ).selectPK( result.getInt( "address_id" ) ) );
        }
        
        statement.close();
        return person;
    }
    
    /**
     * Retorna a primeira pessoa por um pedaço de nome para busca
     * @param piece pedaço de nome
     * @return primeira pessoa que contém piece no nome
     * @throws SQLException 
     */
    public Person getPersonByPieceOfName( String piece ) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM person WHERE name LIKE '" + piece + "%'" );
        
        ResultSet result = statement.executeQuery();
        Person person = null;
        if( result.next() )
        {
            person = new Person( (Integer) result.getObject( "id" ) );
            person.setCpf( (String) result.getObject( "cpf" ) );
            person.setName( (String) result.getObject( "name" ) );
            person.setBirthdate( (Date) result.getObject( "birthdate" ) );
            person.setRg( (String) result.getObject( "rg" ) );
            person.setWichorgan( (String) result.getObject( "wichorgan" ) );
            person.setEmail( (String) result.getObject( "email" ) );
            person.setGender( (String) result.getObject( "gender" ) );
            person.setPispasep( (String) result.getObject( "pispasep" ) );
            person.setSiape( (String) result.getObject( "siape" ) );
            person.setPhone1( (String) result.getObject( "phone1" ) );
            person.setPhone2( (String) result.getObject( "phone2" ) );
            person.setPhone3( (String) result.getObject( "phone3" ) );
            person.setUftlink( (String) result.getObject( "uftlink" ) );
            person.setPlace( new PlaceDAO( connection ).selectPK( (Integer) result.getObject( "place_id" ) ) );
            person.setBankaccount( new BankaccountDAO( connection ).selectPK( (Integer) result.getObject( "bankaccount_id" ) ) );
            person.setAddress( new AddressDAO( connection ).selectPK( (Integer) result.getObject( "address_id" ) ) );
        }
        statement.close();
        
        return person;
    }
    
    /**
     * Lista as pessoas por um pedaço de nome para busca
     * @param piece pedaço de nome
     * @return Lista de pessoas que contém piece no nome
     * @throws SQLException
     * @throws ParseException 
     */
    public List<Person> getPeopleByPieceOfName( String piece ) throws SQLException, ParseException
    {
        List<Person> people = new ArrayList<Person>();
        
        PreparedStatement statement = connection.prepareStatement( "SELECT * FROM person WHERE name LIKE '" + piece + "%'" );
        
        ResultSet result = statement.executeQuery();
        while( result.next() )
        {
            Person toAdd = new Person( result.getInt( "id" ) );
            toAdd.setCpf( result.getString( "cpf" ) );
            toAdd.setName( result.getString( "name" ) );
            toAdd.setBirthdate( (Date) result.getObject( "birthdate" ) );
            toAdd.setRg( result.getString( "rg" ) );
            toAdd.setWichorgan( result.getString( "wichorgan" ) );
            toAdd.setEmail( result.getString( "email" ) );
            toAdd.setGender( result.getString( "gender" ) );
            toAdd.setPispasep( result.getString( "pispasep" ) );
            toAdd.setSiape( result.getString( "siape" ) );
            toAdd.setPhone1( result.getString( "phone1" ) );
            toAdd.setPhone2( result.getString( "phone2" ) );
            toAdd.setPhone3( result.getString( "phone3" ) );
            toAdd.setUftlink( result.getString( "uftlink" ) );
            toAdd.setPlace( new PlaceDAO( connection ).selectPK( result.getInt( "place_id" ) ) );
            toAdd.setBankaccount( new BankaccountDAO( connection ).selectPK( result.getInt( "bankaccount_id" ) ) );
            toAdd.setAddress( new AddressDAO( connection ).selectPK( result.getInt( "address_id" ) ) );
            
            people.add( toAdd );
        }
        
        statement.close();
        return people;
    }
}
