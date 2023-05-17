package xyz.ziang.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import xyz.ziang.client.SysUserClient;
import xyz.ziang.common.result.ApiResult;
import xyz.ziang.entity.SysUser;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    SysUserClient client;

    /**
     * 通过id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiResult<SysUser> findById(@PathVariable("id") Long id) {
        return ApiResult.success(client.findById(id));
    }

    /**
     * 创建账号
     *
     * @param r
     * @return
     */
    @PostMapping()
    public ApiResult<SysUser> create(@RequestBody SysUser r) {
        return ApiResult.success(client.create(r));
    }

    /**
     * 修改账号
     *
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public ApiResult<SysUser> update(@RequestBody SysUser user, @PathVariable("id") Long id) {
        if (client.findById(id) != null) {
            return ApiResult.success(client.update(user));
        }
        return ApiResult.failed();
    }

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResult<SysUser> delete(@PathVariable("id") Long id) {
        client.delete(id);
        return ApiResult.success();
    }

}
