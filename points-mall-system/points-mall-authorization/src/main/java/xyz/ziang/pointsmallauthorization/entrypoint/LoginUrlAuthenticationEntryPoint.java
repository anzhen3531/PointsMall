//package xyz.ziang.pointsmallauthorization.entrypoint;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.core.util.URLUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import xyz.ziang.constant.SecurityConstant;
//
//import java.io.IOException;
//import java.net.URI;
//
//public class LoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    public static final String LOGIN_URL = SecurityConstant.SSO_LOGIN_FORM_PAGE;
//
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    private final String loginPath;
//
//    public LoginUrlAuthenticationEntryPoint() {
//        this.loginPath = LOGIN_URL;
//    }
//
//    public LoginUrlAuthenticationEntryPoint(String loginPath) {
//        this.loginPath = loginPath;
//    }
//
//    /**
//     * @param request
//     * @param response
//     * @param authException
//     * @throws IOException
//     */
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        URI uri = URI.create(loginPath);
//        String query = uri.getQuery();
//        String redirectUri = request.getParameter("redirect_uri");
//        if (StrUtil.isBlank(redirectUri)) {
//            redirectStrategy.sendRedirect(request, response, loginPath);
//            return;
//        }
//        String url;
//        if (StrUtil.isBlank(query)) {
//            url = loginPath + "?redirect_uri=" + URLUtil.encodeAll(redirectUri);
//        } else {
//            url = loginPath + "&redirect_uri=" + URLUtil.encodeAll(redirectUri);
//        }
//        redirectStrategy.sendRedirect(request, response, url);
//    }
//}
