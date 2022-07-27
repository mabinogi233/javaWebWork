package mvc.database.mapper;

import mvc.database.entity.User;
import mvc.database.utils.JdbcTemplate;
import mvc.database.utils.LuceneCompent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;

@Component("userMapper_jdbc")
public class JdbcUserMapper implements DBUserMapper{

    @Autowired
    @Qualifier("datasource")
    private DataSource dataSource;

    @Override
    public User selectByPrimaryKey(Integer uid){
        JdbcTemplate template = new JdbcTemplate(){
            @Override
            protected Object runSql(ResultSet set) {
                try {
                    set.next();
                    User u = new User();
                    u.setUid(set.getInt("uid"));
                    u.setPassword(set.getString("password"));
                    u.setUsername(set.getString("username"));
                    return u;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
        try {
            String sql = "select uid,username,password from appusers where uid="+String.valueOf(uid)+";";
            Object o = template.run(dataSource.getConnection(),sql,"select");
            if(o==null){
                return null;
            }else {
                return (User) o;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer uid){
        JdbcTemplate template = new JdbcTemplate(){};
        try {
            LuceneCompent.delete(uid);
            String sql = "delete from appusers where uid="+String.valueOf(uid)+";";
            template.run(dataSource.getConnection(),sql,"delete");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateByPrimaryKey(User user){
        JdbcTemplate template = new JdbcTemplate(){};
        try {
            String sql = "update appusers set " +
                    "username='" + user.getUsername() + "'," +
                    "password='" + user.getPassword() + "' " +
                    "where uid=" + user.getUid() + ";";
            template.run(dataSource.getConnection(), sql, "update");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 自增主键
     * @param user
     * @return
     */
    @Override
    public int insert(User user){
        JdbcTemplate template = new JdbcTemplate(){};
        try {
            if(selectByUserName(user.getUsername())!=null){
                return 0;
            }
            String sql = "insert into appusers(username,password) values ('" +
                    user.getUsername()+"','"+
                    user.getPassword() + "')";
            template.run(dataSource.getConnection(), sql, "insert");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int insertSelective(User record) {
        return insert(record);
    }

    @Override
    public User selectByUserName(String username){
        JdbcTemplate template = new JdbcTemplate(){
            @Override
            protected Object runSql(ResultSet set) {
                try {
                    set.next();
                    User u = new User();
                    u.setUid(set.getInt("uid"));
                    u.setPassword(set.getString("password"));
                    u.setUsername(set.getString("username"));
                    return u;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
        try {
            String sql = "select uid,username,password from appusers where username='"+String.valueOf(username)+"';";
            Object o = template.run(dataSource.getConnection(),sql,"select");
            if(o==null){
                return null;
            }else {
                return (User) o;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
