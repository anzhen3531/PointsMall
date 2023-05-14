package xyz.ziang.pointsmallservice.service.impl;

import xyz.ziang.common.service.impl.MpCrudBaseServiceImpl;
import xyz.ziang.entity.SysPermission;
import xyz.ziang.pointsmallservice.mapper.SysPermissionMapper;
import xyz.ziang.pointsmallservice.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Service()
public class SysPermissionServiceImpl extends MpCrudBaseServiceImpl<SysPermission, SysPermissionMapper>
    implements SysPermissionService {

    /**
     * 通过有参构造器传入Mapper对象
     *
     * @param repository
     */
    public SysPermissionServiceImpl(SysPermissionMapper repository) {
        super(repository);
    }
}
