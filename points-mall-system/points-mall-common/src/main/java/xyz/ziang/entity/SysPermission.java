package xyz.ziang.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.ziang.common.entity.MpBaseEntity;

/**
 * <p>
 * 
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermission extends MpBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String permissionMode;

    private String permissionScope;
}
