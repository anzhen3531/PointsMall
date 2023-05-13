package xyz.ziang.pointsmallauthorization.service;

import xyz.ziang.common.service.MpCrudBaseService;
import xyz.ziang.entity.SysUser;

public interface SysUserService extends MpCrudBaseService<SysUser> {

    /**
     * 通过名称查询账号
     * 
     * @param username 用户名称
     * @return 账号对象
     */
    SysUser findByUserByName(String username);
}
