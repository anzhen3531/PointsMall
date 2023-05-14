package xyz.ziang.pointsmallservice.mapper;

import xyz.ziang.common.mapper.MpBaseMapper;
import xyz.ziang.entity.Person;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 人员表 Mapper 接口
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Mapper
public interface PersonMapper extends MpBaseMapper<Person> {

}
