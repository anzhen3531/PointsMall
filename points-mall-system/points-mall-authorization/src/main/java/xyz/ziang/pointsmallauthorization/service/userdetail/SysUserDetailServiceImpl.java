package xyz.ziang.pointsmallauthorization.service.userdetail;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallauthorization.service.SysUserService;

import java.util.List;

@Service
public class SysUserDetailServiceImpl implements UserDetailsService {
    @Resource
    private SysUserService service;

    /**
     * 实际业务没有编写
     * 
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = service.findByUserByName(username);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UsernameNotFoundException("账号或者密码不存在");
        }
        // 通过账号查询角色或者是权限
        return new User(sysUser.getUsername(), sysUser.getPassword(), List.of());
    }
}
