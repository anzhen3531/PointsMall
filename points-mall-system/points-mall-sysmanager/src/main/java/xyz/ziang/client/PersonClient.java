package xyz.ziang.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import xyz.ziang.entity.Person;

@FeignClient(name = "points-mall-service",path = "/person",contextId = "manager-person-client")
public interface PersonClient {
    /**
     * 通过id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Person findById(@PathVariable("id") Long id);

    /**
     * 创建账号
     *
     * @param r
     * @return
     */
    @PostMapping("")
    Person create(@RequestBody Person r);

    /**
     * 修改账号
     *
     * @param r
     * @return
     */
    @PutMapping("")
    Person update(@RequestBody Person r);

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Person delete(@PathVariable("id") Long id);
}
