package filter;


import mvc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilter implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    private static final String[] FILTER_URI = {"/book"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //System.out.println("handle handle");
        //默认不拦截
        boolean flag = false;
        //获取请求路径进行判断
        String servletPath = request.getServletPath();
        for (String s: FILTER_URI) {
            if(servletPath.contains(s)) {
                flag = true;
                break;
            }
        }
        //不包含拦截路径
        if(!flag) {
            return true;
        }

        String status=(String)request.getSession().getAttribute("token");

        if (status==null || status.equals("")){
            status = check_auto_login(request);
        }

        System.out.println("token:"+status);
        if (status==null || status.equals("")) {
            request.getRequestDispatcher("/login/home").forward(request,response);
            return false;
        }else {
            if(loginService.checklog(status)){
                return true;
            }else {
                request.getRequestDispatcher("/login/home").forward(request,response);
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 自动登录检测，满足则返回用户的token，不满足则返回null
     * @param request
     * @return
     */
    public static synchronized String check_auto_login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for (Cookie c : cookies) {
                if ("token".contentEquals(c.getName())) {
                    if (c.getValue() != null) {
                        String token = c.getValue();
                        if (token != null) {
                            return token;
                        }
                    }
                }
            }
        }
        return null;
    }
}
