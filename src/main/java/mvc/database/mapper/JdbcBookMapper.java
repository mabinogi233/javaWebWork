package mvc.database.mapper;

import mvc.database.entity.Book;
import mvc.database.utils.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component("bookMapper_jdbc")
public class JdbcBookMapper implements DBBookMapper{

    @Autowired
    @Qualifier("datasource")
    private DataSource dataSource;


    @Override
    public Book selectByPrimaryKey(Integer bid){
        JdbcTemplate template = new JdbcTemplate(){
            @Override
            protected Object runSql(ResultSet set) {
                try {
                    while(set.next()) {
                        Book book = new Book();
                        book.setBid(set.getInt("id"));
                        book.setBauthor(set.getString("author"));
                        book.setBstock(set.getInt("stock"));
                        book.setBname(set.getString("name"));
                        return book;
                    }
                    return null;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
        try {
            String sql = "select id,author,name,stock from book where id="+String.valueOf(bid)+";";
            Object o = template.run(dataSource.getConnection(),sql,"select");
            if(o==null){
                return null;
            }else {
                return (Book) o;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateByPrimaryKeySelective(Book record) {
        return updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer bid){
        JdbcTemplate template = new JdbcTemplate(){};
        try {
            String sql = "delete from book where id="+String.valueOf(bid)+";";
            template.run(dataSource.getConnection(),sql,"delete");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateByPrimaryKey(Book book){
        JdbcTemplate template = new JdbcTemplate(){};
        try {
            String sql = "update book set " +
                    "name='" + book.getBname() + "'," +
                    "stock=" + book.getBstock() + "," +
                    "author='" + book.getBauthor() + "' " +
                    "where id=" + book.getBid() + ";";
            template.run(dataSource.getConnection(), sql, "update");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Book> selectAll() {
        JdbcTemplate template = new JdbcTemplate() {
            @Override
            protected Object runSql(ResultSet set) {
                try {
                    List<Book> books = new ArrayList<>();
                    while (set.next()) {
                        Book book = new Book();
                        book.setBid(set.getInt("id"));
                        book.setBauthor(set.getString("author"));
                        book.setBstock(set.getInt("stock"));
                        book.setBname(set.getString("name"));
                        books.add(book);
                    }
                    return books;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        try{
            String sql = "select * from book";
            Object o = template.run(dataSource.getConnection(),sql,"select");
            if(o!=null){
                return (List<Book>) o;
            }
            return new ArrayList<>();
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 自增主键
     * @param book
     * @return
     */
    @Override
    public int insert(Book book){

        JdbcTemplate template = new JdbcTemplate(){};
        try {

            String sql = "insert into book(name,author,stock) values ('" +
                    book.getBname()+"','"+
                    book.getBauthor()+"',"+
                    book.getBstock() + ")";
            template.run(dataSource.getConnection(), sql, "insert");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public int insertSelective(Book record) {
        return insert(record);
    }

}
