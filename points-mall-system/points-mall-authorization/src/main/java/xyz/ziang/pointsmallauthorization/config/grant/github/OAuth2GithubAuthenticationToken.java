package xyz.ziang.pointsmallauthorization.config.grant.github;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;
import java.util.Set;

/**
 * GitHub token
 */
public class OAuth2GithubAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    public static final AuthorizationGrantType GITHUB = new AuthorizationGrantType("github");
    @Getter
    private final String code;
    @Getter
    private final Set<String> scopes;

    /**
     * @param code 认证类型
     * @param clientPrincipal 客户端主体信息
     * @param additionalParameters 添加的参数
     * @param scopes
     */
    protected OAuth2GithubAuthenticationToken(String code, Authentication clientPrincipal,
        Map<String, Object> additionalParameters, Set<String> scopes) {
        super(GITHUB, clientPrincipal, additionalParameters);
        this.code = code;
        this.scopes = scopes;
    }
}
