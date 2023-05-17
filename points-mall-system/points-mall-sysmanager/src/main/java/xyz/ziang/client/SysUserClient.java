package xyz.ziang.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import xyz.ziang.entity.SysUser;

@FeignClient(name = "points-mall-service", path = "/user",contextId = "manager-user-client")
public interface SysUserClient {

    /**
     * 通过id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    SysUser findById(@PathVariable("id") Long id);

    /**
     * 创建账号
     *
     * @param user
     * @return
     */
    @PostMapping("")
    SysUser create(@RequestBody SysUser user);

    /**
     * 修改账号
     *
     * @param user
     * @return
     */
    @PutMapping("")
    SysUser update(@RequestBody SysUser user);

    /**
     * 删除账号
     *
     * @param id id
     * @return
     */
    @DeleteMapping("/{id}")
    SysUser delete(@PathVariable("id") Long id);

}
