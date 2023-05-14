package xyz.ziang.pointsmallservice.service.impl;

import xyz.ziang.common.service.impl.MpCrudBaseServiceImpl;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallservice.mapper.SysUserMapper;
import xyz.ziang.pointsmallservice.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台管理员用户 服务实现类
 * </p>
 *
 * @author anzhen
 * @since 2023-05-14
 */
@Service("SysUserService")
public class SysUserServiceImpl extends MpCrudBaseServiceImpl<SysUser, SysUserMapper> implements SysUserService {

    /**
     * 通过有参构造器传入Mapper对象
     *
     * @param repository
     */
    public SysUserServiceImpl(SysUserMapper repository) {
        super(repository);
    }
}
