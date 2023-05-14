package xyz.ziang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.ziang.common.entity.MpBaseEntity;
import xyz.ziang.common.mapper.MpBaseMapper;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole extends MpBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleName;
}
