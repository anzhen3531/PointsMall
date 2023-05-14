package xyz.ziang.pointsmallservice.mapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.ziang.common.mapper.MpBaseMapper;
import xyz.ziang.entity.SysUser;

/**
 * <p>
 * 后台管理员用户 Mapper 接口
 * </p>
 *
 * @author anzhen
 * @since 2023-05-14
 */
@Mapper
public interface SysUserMapper extends MpBaseMapper<SysUser> {

}
