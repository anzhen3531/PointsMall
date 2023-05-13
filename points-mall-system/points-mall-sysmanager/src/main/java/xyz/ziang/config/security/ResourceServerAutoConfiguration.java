//package xyz.ziang.config.security;
//
//import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
//import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.client.RestOperations;
//
//import java.time.Duration;
//
//@Configuration
//public class ResourceServerAutoConfiguration {
//    /**
//     * 配置资源服务器过滤器链
//     *
//     * @param http
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .anyRequest().authenticated().and()
//                .oauth2ResourceServer().opaqueToken();
//        return http.build();
//    }
//
//    /**
//     * The resource server sets a connection and read timeout of 60 seconds to coordinate with the authorization server.
//     *
//     * @param builder
//     * @param properties
//     * @return
//     */
//    @Bean
//    public OpaqueTokenIntrospector introspector(RestTemplateBuilder builder, OAuth2ResourceServerProperties properties) {
//        RestOperations rest = builder
//                .basicAuthentication(properties.getOpaquetoken().getClientId(),
//                        properties.getOpaquetoken().getClientSecret())
//                .setConnectTimeout(Duration.ofSeconds(60))
//                .setReadTimeout(Duration.ofSeconds(60))
//                .build();
//        return new NimbusOpaqueTokenIntrospector(properties.getOpaquetoken().getIntrospectionUri(), rest);
//    }
//}
