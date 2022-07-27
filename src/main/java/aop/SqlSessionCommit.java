package aop;


import mvc.database.utils.AddMapperBean;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
public class SqlSessionCommit {

    public void after(JoinPoint point){
        //mybatis 提交
        if(!point.getSignature().getDeclaringType().getName().toLowerCase().contains("jdbc")){
            AddMapperBean.getSqlSession().commit();
            System.out.println("aop commit mybatis from sqlSession");
        }
    }
}
