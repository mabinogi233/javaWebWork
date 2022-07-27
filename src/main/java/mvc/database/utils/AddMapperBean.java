package mvc.database.utils;


import mvc.database.mapper.DBBookMapper;
import mvc.database.mapper.DBUserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddMapperBean {

    private static SqlSession sqlSession = null;

    public static SqlSession getSqlSession() {
        return sqlSession;
    }

    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis/config.xml"));
            sqlSession = sqlSessionFactory.openSession();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean(name = "bookMapper_mybatis")
    public DBBookMapper getDBBookMapper(){
        try{
            return sqlSession.getMapper(DBBookMapper.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Bean(name = "userMapper_mybatis")
    public DBUserMapper getDBUserMapper(){
        try{
            return sqlSession.getMapper(DBUserMapper.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
