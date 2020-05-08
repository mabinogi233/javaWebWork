import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//登陆类
@WebServlet("/login")
public class login extends HttpServlet {

    public login() {
        super();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名，密码和验证码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String inputcheckcode = request.getParameter("checkcode");
        //获取真实验证码
        HttpSession session = request.getSession();
        String checkcode= (String) session.getAttribute("checkcode");
        //验证用户名,密码,验证码
        if( loginCheck(username, password)&&check(checkcode,inputcheckcode)){
            //返回html界面，表示登录成功
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>"+ request.getParameter("username")+" 登陆成功！</h1>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();
        } else {
            //重定向首页
            response.sendRedirect("index.jsp");
        }
    }
    //判断验证码
    private boolean check(String yanZheng,String inputYanZheng){
        if(yanZheng!=null&&inputYanZheng!=null) {
            //忽略大小写比较
            if (yanZheng.equalsIgnoreCase(inputYanZheng)) {
                return true;
            }
        }
        return false;
    }

    //比较用户名和密码，暂时无限制
    private boolean loginCheck(String username, String password){
        if( username!=null && password != null){
            //非空
            if(!username.equals("")&&!password.equals("")) {
                return true;
            }
        }
        return false;
    }
}
