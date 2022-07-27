package mvc.database.mapper;

import mvc.database.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DBUserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUserName(String username);

}