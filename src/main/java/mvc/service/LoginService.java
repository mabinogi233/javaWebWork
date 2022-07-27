package mvc.service;


import mvc.database.entity.User;
import mvc.database.mapper.DBUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service("loginService")
public class LoginService {

    private static Map<String,Map<String,Object>> user_param = new ConcurrentHashMap<>();

    //登陆保持三十天
    private static final int DEAD_DAY = 30;

    @Autowired
    @Qualifier("userMapper_jdbc")
    private DBUserMapper userMapper;

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    public boolean login(String username, String password, HttpSession session, HttpServletResponse response){
        try{
            if(username==null || password==null){
                return false;
            }
            User u = userMapper.selectByUserName(username);
            if(u==null || u.getUsername()==null || u.getPassword()==null){
                return false;
            }
            if(u.getPassword().equals(password)){
                if(user_param.containsKey(username)){
                    user_param.remove(username);
                }
                Map<String,Object> obj = new HashMap<>();
                obj.put("logindate",new Date());
                obj.put("token", UUID.randomUUID().toString().replace("-",""));
                session.setAttribute("token",obj.get("token"));
                Cookie cookie = new Cookie("token",(String) obj.get("token"));
                cookie.setMaxAge(60 * 60 * 24 * 30);
                cookie.setPath("/");
                response.addCookie(cookie);
                System.out.println("写入token："+(String) obj.get("token"));
                user_param.put(username,obj);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean checklog(String token){
        for(Map.Entry<String,Map<String,Object>> params:user_param.entrySet()){
            try {
                String t = (String) params.getValue().get("token");
                if(t!=null && t.equals(token)){
                    if(new Date().getTime() - ((Date) params.getValue().get("logindate")).getTime() > 1000L * 60 * 60 * 24 * DEAD_DAY){
                        return false;
                    }
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
