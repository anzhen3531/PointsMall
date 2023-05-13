package xyz.ziang.pointsmallauthorization.config.authserver;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * 实现Redis保存令牌的颁发记录
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
    /**
     * 保存令牌
     *
     * @param authorization
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        // 需要注入RedisTemplate
    }

    /**
     * 删除令牌
     *
     * @param authorization
     */
    @Override
    public void remove(OAuth2Authorization authorization) {

    }

    /**
     * 查找单个Redis
     *
     * @param id
     * @return
     */
    @Override
    public OAuth2Authorization findById(String id) {
        return null;
    }

    /**
     * 通过token以及类型查找
     *
     * @param token
     * @param tokenType
     * @return
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return null;
    }
}
