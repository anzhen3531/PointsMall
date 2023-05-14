package xyz.ziang.pointsmallservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.ziang.common.service.impl.MpCrudBaseServiceImpl;
import xyz.ziang.entity.SysRole;
import xyz.ziang.pointsmallservice.mapper.SysRoleMapper;
import xyz.ziang.pointsmallservice.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Service("SysRoleService")
public class SysRoleServiceImpl extends MpCrudBaseServiceImpl<SysRole, SysRoleMapper> implements SysRoleService {

    /**
     * 通过有参构造器传入Mapper对象
     *
     * @param repository
     */
    public SysRoleServiceImpl(SysRoleMapper repository) {
        super(repository);
    }
}
