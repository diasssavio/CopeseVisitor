/*
 * DBManager.java
 *
 * Created on march 4th, 2013
 */

package copesevisitor.persistence;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Classe utilitaria para conexao com o banco de dados
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
public class DBManager
{
    private static final long serialVersionUID = 1L;
    private static final String url = "jdbc:mysql://localhost:3306/copesevisitor";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static DBManager instance;
    private Connection conn = null;
    private static String username = "root", password = "savio";

    private DBManager() {
        try{
          Class.forName( driver );
          setConnection();
        }catch(ClassNotFoundException e){
          System.exit(1);
        }
    }

    /**
     * Retorna a classe, evitando que a classe seja varias vezes instanciada
     * @return classe instanciada
     */
    public static synchronized DBManager getInstance() {
       if( instance == null ) instance = new DBManager();
       return instance;
    }
  
    /**
     * Fecha a conexao com o Banco de dados
     */
    @Override
    public void finalize(){
            try{
                this.conn.close();
                this.conn = null;
            }catch(SQLException e){
            }
    }

    /**
     * @return conexao com o banco de dados
     */
    public Connection getConnection(){
       setConnection();
       return this.conn;
    }

    /**
     * Cria conexao com o banco de dados
     */
    private void setConnection() {
         try{
           if (this.conn == null) {
                 this.conn = DriverManager.getConnection( url, username, password );
                 this.conn.setAutoCommit(true);
           } else if(this.conn.isClosed()){
                 this.conn = DriverManager.getConnection( url, username, password);
                 this.conn.setAutoCommit(true);
           }
         }catch (SQLException e) {
             try{
                 createDatabase();
             }catch (Exception ex){
                 this.conn = null;
                 System.exit(1);
             }
         }
    }
   
    /**
     * Cria o banco de dados (vazio)
     */
    private void createDatabase() throws Exception {
        if(this.conn == null){
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/mysql", username, "");
            this.conn.setAutoCommit(true);
        }
        Statement stmt = this.conn.createStatement();

        BufferedReader reader = new BufferedReader(new InputStreamReader( DBManager.class.getResourceAsStream("/br/edu/uft/jabuti/persistence/jabuti.sql") ));
        StringBuilder sb = new StringBuilder();
        String delimiter = ";";// reader.readLine();
        //if(delimiter != null){
            String line = "";
            while ( (line = reader.readLine()) != null) {
                sb.append(line);
            }
            for(String sql : sb.toString().split(delimiter)){
                stmt.executeUpdate(sql);
            }
        //}
        reader.close();
    }

    /**
     * @return lista de tabelas contidas no banco de dados
     */
    private List<String> getTablesNames() throws SQLException {
        List<String> list = new Vector<String>();
        DatabaseMetaData metaData = this.conn.getMetaData();
        ResultSet rs = metaData.getTables(getDatabaseName(), null, null, null);
        while( rs.next() )
            list.add(rs.getString("TABLE_NAME"));
        return list;
    }

    /**
     * Grava os dados de um ResultSet em um arquivo
     * @param rs ResultSet com os dados a serem gravados
     * @param directory pasta de destino do arquivo
     * @param fileName nome do arquivo a ser gravado com os dados do ResultSet
     */
    private void resultsetToFile(ResultSet rs, String directory, String fileName) throws IOException, SQLException {
        ObjectOutputStream out;
        List<Object> list;
        if((rs != null) && (rs.next())){
            out = new ObjectOutputStream(new FileOutputStream(new File(directory, fileName)));
            list = new ArrayList<Object>();
            int columnCount = rs.getMetaData().getColumnCount();
            for(int i = 1; i <= columnCount; i++)
                list.add(rs.getMetaData().getColumnName(i));
            out.writeObject(list.toArray());
            rs.beforeFirst();
            while(rs.next()){
                list.clear();
                for(int i = 1; i <= columnCount; i++)
                    list.add(rs.getObject(i));
                out.writeObject(list.toArray());
            }
            out.close();
        }
    }

    /**
     * Faz o backup das tabelas do banco de dados
     * @return pasta com os arquivos de backup
     */
    public File backup(){
        File path = null;
        try{
            path = new File(System.getProperty("java.io.tmpdir"), "J"+System.currentTimeMillis());
            if(path.mkdir()){
                for(String tableName : getTablesNames())
                    resultsetToFile(getResultSet("SELECT * FROM "+tableName), path.getAbsolutePath(), tableName+".obj");
            }
        }catch(Exception ex){
        }
        return path;
    }

    /**
     * Restaura o banco de dados
     * @directory pasta dos arquivos de backup
     */
    public void restore(String directory){
        ObjectInputStream in;
        StringBuilder sb;
        StringBuilder sb2;
        boolean hasNext;
        try{
            createDatabase();
            for(File file : new File(directory).listFiles(new FileFilter() {
                 public boolean accept(File pathname) {
                     return pathname.getName().toLowerCase().endsWith(".obj");
                 }
             })){
                 in = new ObjectInputStream( new FileInputStream(file) );
                 Object[] header = null;
                 hasNext = true;
                 try{
                     header = (Object[])in.readObject();
                 }catch(EOFException eof){ hasNext = false; }
                 if(hasNext){
                     sb = new StringBuilder();
                     sb2 = new StringBuilder();
                     for(int i = 1; i <= header.length; i++) {
                         if (i == header.length) {
                             sb.append("" + header[i-1]);
                             sb2.append("?");
                         } else {
                             sb.append(header[i-1] + ",");
                             sb2.append("?,");
                         }
                     }
                     String SQL = "INSERT INTO " + file.getName().substring(0, file.getName().length()-4) + " (" + sb.toString() + ") VALUES (" + sb2.toString() + ")";
                     while(hasNext){
                         try{
                             execute(SQL, (Object[])in.readObject());
                         }catch(EOFException eof){ hasNext = false; }
                     }
                 }
                 in.close();
             }
        }catch(Exception ex){
        }
    }

    /**
     * Executa SQL
     * @param sql SQL a ser executada
     */
     public void execute(String sql) throws SQLException {
         PreparedStatement ps = this.conn.prepareStatement( sql );
         ps.execute();
         ps.close();
     }

    /**
     * Executa SQL
     * @param sql SQL a ser executada
     * @param param vetor de parametros da SQL
     */
     public void execute(String sql, Object[] param) throws SQLException {
         PreparedStatement ps = this.conn.prepareStatement( sql );
         for(int i = 0; i < param.length; i++){
             ps.setObject(i+1, param[i]);
         }
         ps.execute();
         ps.close();
     }


    /**
     * Executa uma SQL e retorna um valor
     * @param sql SQL a ser executada
     * @return valor de retorno da SQL executada
     */
     public Object getValue(String sql){
          Object obj = null;
          try{
              ResultSet rs = getResultSet(sql);
              if(rs.next())
                  obj = rs.getObject(1);
          }catch(Exception e){}
          return obj;
     }

    /**
     * Executa uma SQL e retorna um valor
     * @param sql SQL a ser executada
     * @param param vetor de parametros da SQL
     * @return valor de retorno da SQL executada
     */
     public Object getValue(String sql, Object[] param){
          Object obj = null;
          try{
              ResultSet rs = getResultSet(sql, param);
              if(rs.next())
                  obj = rs.getObject(1);
          }catch(Exception e){}
          return obj;
     }

    /**
     * Executa SQL
     * @param sql SQL a ser executada
     * @param param vetor de parametros da SQL
     * @return ResultSet com os valores de retorno da SQL
     */
     public ResultSet getResultSet(String sql, Object[] param) throws SQLException {
            PreparedStatement ps = this.conn.prepareStatement( sql );
            for ( int i = 0; i < param.length; i++ ) {
               ps.setObject( i + 1, param[i] );
            }
            return ps.executeQuery();
     }

    /**
     * Executa SQL
     * @param sql SQL a ser executada
     * @return ResultSet com os valores de retorno da SQL
     */
     public ResultSet getResultSet(String sql) throws SQLException {
            PreparedStatement ps = this.conn.prepareStatement( sql );
            return ps.executeQuery();
     }

     /**
      * @return String nome do banco de dados
      */
     public static String getDatabaseName(){
         return "copesevisitor";
     }

     /**
      * Define usuario do banco de dados
      * @param name usuario do banco de dados
      */
     public static void setDatabaseUsername(String name){
         username = name;
     }

     /**
      * Define senha do usuario do banco de dados
      * @param pass senha do usuario do banco de dados
      */
     public static void setDatabasePassword(String pass){
         password = pass;
     }
}
