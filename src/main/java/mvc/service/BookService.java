package mvc.service;


import mvc.database.entity.Book;
import mvc.database.mapper.DBBookMapper;
import mvc.database.utils.LuceneCompent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("bookService")
public class BookService {

    @Autowired
    @Qualifier("bookMapper_mybatis")
    private DBBookMapper mapper;

    public List<Book> getBookAdminPage(int page, int size, String bname, String author){
        try {
            List<Integer> sql_ids;
            if((author==null||author.equals("all"))&&(bname==null)){
                //查询全部
                sql_ids = LuceneCompent.searchAll(page,size);
            }
            else if (author == null || author.equals("all")) {
                //名称查询
                sql_ids = LuceneCompent.searchByName(bname, 0, page, size);
            }else if(bname==null){
                //作者查询
                sql_ids = LuceneCompent.searchByAuthor(author,0,page,size);
            }else {
                //组合查询
                sql_ids = LuceneCompent.searchByName(bname,author,0,page,size);
            }
            //SQL查询
            List<Book> pages = new ArrayList<>();
            for(int i=0;i<sql_ids.size();i++){
                if(sql_ids.get(i)==null){
                    continue;
                }
                Book book = mapper.selectByPrimaryKey(sql_ids.get(i));
                pages.add(book);
            }
            return pages;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Book getBook(int id){
        return mapper.selectByPrimaryKey(id);
    }

    public boolean delete(int id){
        try{
            LuceneCompent.delete(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapper.deleteByPrimaryKey(id)!=0;
    }

    public boolean insert(String name,String author,int stock){
        Book book = new Book();
        book.setBname(name);
        book.setBauthor(author);
        book.setBstock(stock);
        int c = mapper.insert(book);
        try {
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
        return c!=0;
    }

    public boolean update(int id,String name,String author,int stock){
        Book book = new Book();
        book.setBname(name);
        book.setBauthor(author);
        book.setBstock(stock);
        book.setBid(id);
        try {
            LuceneCompent.update(book);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapper.updateByPrimaryKey(book)!=0;
    }

    public void init() {
        try {
            try {
                List<Book> o = mapper.selectAll();
                if (o == null) {
                    return;
                } else {
                    for (Book book :o) {
                        try {
                            if (LuceneCompent.searchById(book.getBid()) != null) {
                                LuceneCompent.update(book);
                            } else {
                                LuceneCompent.add(book);
                            }
                        } catch (Exception e) {
                            LuceneCompent.add(book);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
