package xyz.ziang.pointsmallservice.controller;

import org.springframework.web.bind.annotation.*;
import xyz.ziang.entity.SysUser;
import xyz.ziang.pointsmallservice.controller.base.BaseController;
import xyz.ziang.pointsmallservice.service.SysUserService;

/**
 * <p>
 * 后台管理员用户 前端控制器
 * </p>
 *
 * @author anzhen
 * @since 2023-05-14
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController<SysUserService, SysUser> {
    public SysUserController(SysUserService service) {
        super(service);
    }
}
