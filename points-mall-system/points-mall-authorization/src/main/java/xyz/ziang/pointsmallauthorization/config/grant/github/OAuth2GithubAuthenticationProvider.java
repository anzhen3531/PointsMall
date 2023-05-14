package xyz.ziang.pointsmallauthorization.config.grant.github;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallauthorization.util.OAuth2AuthenticationProviderUtils;

/**
 * github登录提供者
 */
@Slf4j
public class OAuth2GithubAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);
    public static final AuthorizationGrantType GITHUB = new AuthorizationGrantType("github");
    private final UserDetailsService userDetailService;
    private final GitHubAutoLogin gitHubAutoLogin;
    private final PasswordEncoder passwordEncoder;

    public OAuth2GithubAuthenticationProvider(OAuth2AuthorizationService authorizationService,
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailService,
        GitHubAutoLogin gitHubAutoLogin, PasswordEncoder passwordEncoder) {
        this.authorizationService = authorizationService;
        this.userDetailService = userDetailService;
        this.tokenGenerator = tokenGenerator;
        this.gitHubAutoLogin = gitHubAutoLogin;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 认证
     * 
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2GithubAuthenticationToken githubAuthenticationToken = (OAuth2GithubAuthenticationToken)authentication;
        // 获取client主体
        OAuth2ClientAuthenticationToken clientPrincipal =
            OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(githubAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        // 判断认证方式是否是github
        if (!registeredClient.getAuthorizationGrantTypes().contains(GITHUB)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
        // 先进行登录 github 之后转换username and password 登录
        Authentication usernamePasswordAuthentication = getUsernamePasswordAuthentication(githubAuthenticationToken);
        Set<String> authorizedScopes = registeredClient.getScopes(); // Default to configured scopes
        Set<String> requestedScopes = githubAuthenticationToken.getScopes();
        //
        // 判断客户端登录的域 是否和当前用户登录的域相同
        if (!CollUtil.isEmpty(requestedScopes)) {
            Set<String> unauthorizedScopes = requestedScopes.stream()
                .filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
                .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(unauthorizedScopes)) {
                throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
            }
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }

        // 构建token上下文
        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(GITHUB)
                .authorizationGrant(githubAuthenticationToken);
        // @formatter:on

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        // 构建AccessToken
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        // 构建认证
        // @formatter:off
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(GITHUB)
                .authorizedScopes(authorizedScopes)
                .attribute(Principal.class.getName(), usernamePasswordAuthentication);
        // @formatter:on
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken,
                (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                    ((ClaimAccessor)generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
        // Do not issue refresh token to public client
            !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken)generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        // ----- ID token -----
        OidcIdToken idToken;
        if (requestedScopes.contains(OidcScopes.OPENID)) {
            // @formatter:off
            tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())	// ID token customizer may need access to the access token and/or refresh token
                    .build();
            // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the ID token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                generatedIdToken.getExpiresAt(), ((Jwt)generatedIdToken).getClaims());
            authorizationBuilder.token(idToken,
                (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }
        OAuth2Authorization authorization = authorizationBuilder.build();

        // 保存认证信息
        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = Collections.emptyMap();
        if (idToken != null) {
            additionalParameters = new HashMap<>();
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
            additionalParameters);
    }

    /**
     * 构建认证对象
     * 
     * @param githubAuthenticationToken
     * @return
     */
    private Authentication
        getUsernamePasswordAuthentication(OAuth2GithubAuthenticationToken githubAuthenticationToken) {
        String code = githubAuthenticationToken.getCode();
        UsernamePasswordAuthenticationToken result;
        SysUser sysUser = gitHubAutoLogin.getGitHubUsernameByCode(code);
        if (ObjectUtil.isNull(sysUser)) {
            // 表示登录失败
            log.debug("GitHub 登录失败！！");
            // 如果当前数据库中没有的话 直接进行创建即可
            sysUser = new SysUser();
            sysUser.setUsername("anzhen");
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(sysUser.getUsername());
        result = UsernamePasswordAuthenticationToken.authenticated(userDetails,
            githubAuthenticationToken.getCredentials(), userDetails.getAuthorities());
        result.setDetails(githubAuthenticationToken.getDetails());
        return result;
    }

    /**
     * 支持
     * 
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2GithubAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
