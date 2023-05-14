package xyz.ziang.pointsmallservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ziang.entity.SysPermission;
import xyz.ziang.pointsmallservice.controller.base.BaseController;
import xyz.ziang.pointsmallservice.service.SysPermissionService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/permission")
public class SysPermissionController extends BaseController<SysPermissionService, SysPermission> {
    public SysPermissionController(SysPermissionService service) {
        super(service);
    }
}
