package xyz.ziang.pointsmallservice.controller.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.web.bind.annotation.*;

import xyz.ziang.common.entity.MpBaseEntity;
import xyz.ziang.common.service.MpCrudBaseService;

public class BaseController<T extends MpCrudBaseService<R>, R extends MpBaseEntity> {
    private final T service;

    public BaseController(T service) {
        this.service = service;
        // 获取范型子类的类型
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
    }

    /**
     * 通过id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        return service.findOne(id).orElse(null);
    }

    /**
     * 创建账号
     *
     * @param r
     * @return
     */
    @PostMapping()
    public R create(@RequestBody R r) {
        return service.create(r);
    }

    /**
     * 修改账号
     *
     * @param r
     * @return
     */
    @PutMapping("/{id}")
    public R update(@RequestBody R r) {
        return service.updateAllProperties(r);
    }

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.achieve(id);
    }
}
