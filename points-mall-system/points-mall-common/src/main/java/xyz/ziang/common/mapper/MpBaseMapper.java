package xyz.ziang.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ziang.common.entity.MpBaseEntity;


/**
 * mybatis-plus 基础Mapper
 *
 * @param <T>
 */
public interface MpBaseMapper<T extends MpBaseEntity> extends BaseMapper<T> {
}
