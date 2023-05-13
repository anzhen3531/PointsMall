package xyz.ziang.pointsmallservice.controller;

import org.springframework.web.bind.annotation.*;
import xyz.ziang.entity.SysRole;
import xyz.ziang.pointsmallservice.controller.base.BaseController;
import xyz.ziang.pointsmallservice.service.SysRoleService;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController<SysRoleService, SysRole> {
    public SysRoleController(SysRoleService service) {
        super(service);
    }
}
