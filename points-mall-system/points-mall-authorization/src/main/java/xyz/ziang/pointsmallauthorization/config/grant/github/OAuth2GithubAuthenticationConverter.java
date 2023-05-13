package xyz.ziang.pointsmallauthorization.config.grant.github;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import xyz.ziang.pointsmallauthorization.util.OAuth2EndpointUtils;

/**
 * GitHub 认证转换器
 */
public class OAuth2GithubAuthenticationConverter implements AuthenticationConverter {
    public static final String GRANT_TYPE = "github";

    /**
     * 转换器
     * 
     * @param request
     * @return
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 验证方式是否为github
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        // 如果不是github类型的登录方式的话直接退出
        if (!GRANT_TYPE.equals(grantType)) {
            return null;
        }
        // 通过上下文获取客户端对象
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // 需要授权码
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);
        if (!StringUtils.hasText(code) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CODE,
                OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        Set<String> requestedScopes = null;
        String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 验证额外参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID)
                && !key.equals(OAuth2ParameterNames.CODE) && !key.equals(OAuth2ParameterNames.REDIRECT_URI)) {
                additionalParameters.put(key, value.get(0));
            }
        });
        // 创建github登录token
        return new OAuth2GithubAuthenticationToken(code, clientPrincipal, additionalParameters, requestedScopes);
    }
}
