package xyz.ziang.pointsmallauthorization.service;

import org.springframework.stereotype.Service;
import xyz.ziang.common.service.impl.MpCrudBaseServiceImpl;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallauthorization.mapper.SysUserMapper;

@Service
public class SysUserServiceImpl extends MpCrudBaseServiceImpl<SysUser, SysUserMapper> implements SysUserService {
    /**
     * 通过有参构造器传入Mapper对象
     *
     * @param repository
     */
    public SysUserServiceImpl(SysUserMapper repository) {
        super(repository);
    }

    @Override
    public SysUser findByUserByName(String username) {

        return null;
    }
}
