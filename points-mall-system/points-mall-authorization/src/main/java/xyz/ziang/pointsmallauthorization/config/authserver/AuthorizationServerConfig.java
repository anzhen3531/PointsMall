package xyz.ziang.pointsmallauthorization.config.authserver;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cn.hutool.core.util.ObjectUtil;
import xyz.ziang.common.constant.SecurityConstant;
import xyz.ziang.pointsmallauthorization.config.grant.github.GitHubAutoLogin;
import xyz.ziang.pointsmallauthorization.config.grant.github.OAuth2GithubAuthenticationConverter;
import xyz.ziang.pointsmallauthorization.config.grant.github.OAuth2GithubAuthenticationProvider;
import xyz.ziang.pointsmallauthorization.service.userdetail.SysUserDetailServiceImpl;

/**
 * OAuth Authorization Server Configuration.
 *
 * @author Steve Riesenberg
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    GitHubAutoLogin gitHubAutoLogin;

    @Bean
    public UserDetailsService sysUserDetailsService() {
        return new SysUserDetailServiceImpl();
    }

    /**
     * oauth2.0配置，需要托管给 HttpSecurity
     * <p>
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
        UserDetailsService sysUserDetailsService) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer);
        authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverters(
            authenticationConverters -> authenticationConverters.add(new OAuth2GithubAuthenticationConverter())));
        DefaultSecurityFilterChain chain = http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).csrf(CsrfConfigurer::disable)
            .build();
        addingAdditionalAuthenticationProvider(http, gitHubAutoLogin, passwordEncoder, sysUserDetailsService);
        return chain;
    }

    /**
     * 把资源拥有者授权确认操作保存到数据库, 对应 oauth2_authorization_consent 表 但是如果不进行授权呢
     * http://localhost:9000/oauth2/authorize?response_type=code&client_id=anzhen&scope=write&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
        RegisteredClientRepository registeredClientRepository) {
        RegisteredClient client = registeredClientRepository.findByClientId("anzhen");
        if (ObjectUtil.isNull(client)) {
            client = RegisteredClient.withId(UUID.randomUUID().toString()).clientId("anzhen")
                .clientSecret(passwordEncoder.encode("123456"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(new AuthorizationGrantType("github"))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized").scope(OidcScopes.OPENID).scope(OidcScopes.PROFILE)
                .scope("read").scope("write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();
            registeredClientRepository.save(client);
        }
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 注册客户端应用, 对应 oauth2_registered_client 表
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 令牌的发放记录, 对应 oauth2_authorization 表 可以采用Redis的方式进行存放 但是需要自己实现Redis的存放方式
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
        RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * AccessToken的提供者
     *
     * @return ProviderSettings
     */
    @Bean
    public AuthorizationServerSettings providerSettings() {
        // 此处为oauth授权服务的发行者，即此授权服务地址
        return AuthorizationServerSettings.builder()
            // 发布者的url地址,一般是本系统访问的根路径
            .issuer(SecurityConstant.ISSUER).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAKey rsaKey =
            new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 
     * @return
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 
     * @param http
     */
    private static void addingAdditionalAuthenticationProvider(HttpSecurity http, GitHubAutoLogin gitHubAutoLogin,
        PasswordEncoder passwordEncoder, UserDetailsService sysUserDetailsService) {
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
        OAuth2TokenGenerator<?> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
        OAuth2GithubAuthenticationProvider githubAuthenticationProvider = new OAuth2GithubAuthenticationProvider(
            authorizationService, tokenGenerator, sysUserDetailsService, gitHubAutoLogin, passwordEncoder);
        http.authenticationProvider(githubAuthenticationProvider);
    }
}