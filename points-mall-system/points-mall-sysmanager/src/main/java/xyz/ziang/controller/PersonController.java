package xyz.ziang.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import xyz.ziang.common.result.ApiResult;
import xyz.ziang.client.PersonClient;
import xyz.ziang.entity.Person;
import xyz.ziang.entity.SysRole;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Resource
    PersonClient client;

    /**
     * 通过id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiResult<Person> findById(@PathVariable("id") Long id) {
        return ApiResult.success(client.findById(id));
    }

    /**
     * 创建账号
     *
     * @param r
     * @return
     */
    @PostMapping()
    public ApiResult<Person> create(@RequestBody Person r) {
        return ApiResult.success(client.create(r));
    }

    /**
     * 修改账号
     *
     * @param r
     * @return
     */
    @PutMapping("/{id}")
    public ApiResult<Person> update(@RequestBody Person r, @PathVariable("id") Long id) {
        return ApiResult.success(client.update(r));
    }

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResult<SysRole> delete(@PathVariable("id") Long id) {
        client.delete(id);
        return ApiResult.success();
    }

}
