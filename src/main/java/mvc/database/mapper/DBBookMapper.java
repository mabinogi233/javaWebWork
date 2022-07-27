package mvc.database.mapper;

import mvc.database.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DBBookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);

    List<Book> selectAll();
}