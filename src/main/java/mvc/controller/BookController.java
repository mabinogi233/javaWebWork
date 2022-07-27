package mvc.controller;


import com.alibaba.fastjson.JSONObject;
import mvc.database.entity.Book;
import mvc.database.utils.LuceneCompent;
import mvc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("bookController")
@CrossOrigin
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService service;

    @RequestMapping("/select")
    public String getBookAdminPage(@RequestParam("p")int page,
                                   @RequestParam("s")int size,
                                   @RequestParam(value = "name",required = false)String _bname,
                                   @RequestParam(value = "author",required = false)String _author,
                                   Model model){
        Map<String,Object> maps = new HashMap<>();
        try{
            String bname = _bname;
            String author = _author;
            if(bname==null || bname.equals("")){
                bname = null;
                model.addAttribute("name","");
            }else {
                model.addAttribute("name",bname);
            }
            if(author==null || author.equals("")){
                author = null;
                model.addAttribute("author","");
            }else {
                model.addAttribute("author",author);
            }
            List<Book> books = service.getBookAdminPage(page, size, bname, author);

            List<Book> _books = service.getBookAdminPage(page+1, size, bname, author);


            if(page==0){
                model.addAttribute("haslast",false);
            }else {
                model.addAttribute("haslast",true);
                model.addAttribute("lp",page-1);
            }
            if(_books.size()!=0){
                model.addAttribute("hasnext",true);
                model.addAttribute("np",page+1);
            }else {
                model.addAttribute("hasnext",false);
            }


            System.out.println(books.size());
            if(books.size()!=0) {
                maps.put("code", "success");
                maps.put("books",books);
                model.addAttribute("books",books);
                model.addAttribute("p",page+1);
            }else {
                maps.put("code", "fail");
                maps.put("books","");
                model.addAttribute("p",page+1);
            }
        }catch (Exception e){
            e.printStackTrace();
            maps.put("code", "fail");
            maps.put("books","");
            model.addAttribute("p",page+1);
        }
        return "admin";
    }

    @RequestMapping("/selectOne")
    public String getBookOne(@RequestParam("id")int id,Model model){
        Map<String,Object> maps = new HashMap<>();
        try{
            Book book = service.getBook(id);
            if(book!=null) {
                maps.put("code", "success");
                maps.put("book",book);
                model.addAttribute("book",book);
            }else {
                maps.put("code", "fail");
                maps.put("book","");
            }
        }catch (Exception e){
            e.printStackTrace();
            maps.put("code", "fail");
            maps.put("book","");
        }
        return "update";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("id")int id){
        Map<String,Object> maps = new HashMap<>();
        try{
            boolean b = service.delete(id);
            if(b) {
                maps.put("code", "success");
                return "succ";
            }else {
                maps.put("code", "fail");
                return "failop";
            }
        }catch (Exception e){
            e.printStackTrace();
            maps.put("code", "fail");
            return "failop";
        }
    }

    @RequestMapping("/insert")
    public String getBookAdminPage(@RequestParam("stock")int stock,
                                   @RequestParam("name")String bname,
                                   @RequestParam("author")String author){
        Map<String,Object> maps = new HashMap<>();
        try{
            boolean f = service.insert(bname,author,stock);
            if(f) {
                maps.put("code", "success");
                return "succ";
            }else {
                maps.put("code", "fail");
                return "failop";
            }
        }catch (Exception e){
            e.printStackTrace();
            maps.put("code", "fail");
            return "failop";
        }

    }

    @RequestMapping("/update")
    public String update(@RequestParam("id")int id,
                                   @RequestParam("stock")int stock,
                                   @RequestParam("name")String bname,
                                   @RequestParam("author")String author){
        Map<String,Object> maps = new HashMap<>();
        try{
            boolean f = service.update(id,bname,author,stock);
            if(f) {
                maps.put("code", "success");
                return "succ";
            }else {
                maps.put("code", "fail");
                return "failop";
            }
        }catch (Exception e){
            e.printStackTrace();
            maps.put("code", "fail");
            return "failop";
        }
    }

    @RequestMapping("/init")
    public String init(){
        service.init();
        return "succ";
    }

    @RequestMapping("/admin")
    public String admin(){
        System.out.println("admin into");
        return "admin";
    }

    @RequestMapping("/insertPage")
    public String insertPage(){
        return "create";
    }

    @RequestMapping("/removeall")
    public String removeall(){
        try {
            LuceneCompent.removeAll();
            return "succ";
        }catch (Exception e){
            e.printStackTrace();
            return "failop";
        }
    }
}
