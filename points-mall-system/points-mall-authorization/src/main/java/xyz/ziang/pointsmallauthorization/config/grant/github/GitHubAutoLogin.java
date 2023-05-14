package xyz.ziang.pointsmallauthorization.config.grant.github;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import xyz.ziang.entity.Person;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallauthorization.client.PersonClient;
import xyz.ziang.pointsmallauthorization.client.SysUserClient;

@Component
@Slf4j
@EnableConfigurationProperties({GithubProperties.class})
public class GitHubAutoLogin {
    private static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    @Resource
    RestTemplate restTemplate;
    @Resource
    GithubProperties githubProperties;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    PersonClient personClient;
    @Resource
    SysUserClient sysUserClient;

    /**
     * github登录
     * 
     * @param code
     * @return
     */
    public SysUser getGitHubUsernameByCode(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", githubProperties.getClientId());
        map.put("client_secret", githubProperties.getClientSecret());
        map.put("state", "points");
        map.put("code", code);
        map.put("redirect_uri", githubProperties.getRedirectUri());
        Map resp = restTemplate.postForObject(GITHUB_TOKEN_URL, map, Map.class);
        if (resp == null) {
            log.error("github 登录失败");
        }
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.add("Authorization", "token " + resp.get("access_token"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange =
            restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        log.info("返回得到的github Json信息 {} ", exchange.getBody());
        // 创建
        Map body = exchange.getBody();
        if (body != null) {
            SysUser sysUser = sysUserClient.findByUserName((String)body.get("login"));
            if (ObjectUtil.isNull(sysUser)) {
                // 创建这个用户和人员信息
                Person person = new Person();
                person.setPersonName((String)body.get("name"));
                person = personClient.create(person);
                sysUser.setPersonId(person.getId());
                // 随机生成密码
                sysUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                sysUserClient.create(sysUser);
            }
            return sysUser;
        }
        return null;
    }
}