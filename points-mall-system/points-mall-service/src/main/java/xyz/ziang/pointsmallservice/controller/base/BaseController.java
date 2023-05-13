package xyz.ziang.pointsmallservice.controller.base;

import org.springframework.web.bind.annotation.*;

import xyz.ziang.common.entity.MpBaseEntity;
import xyz.ziang.common.result.ApiResult;
import xyz.ziang.common.service.MpCrudBaseService;
import xyz.ziang.entity.SysRole;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
    public ApiResult<R> findById(@PathVariable("id") Long id) {
        return ApiResult.success(service.findOne(id).orElse(null));
    }

    /**
     * 创建账号
     *
     * @param r
     * @return
     */
    @PostMapping()
    public ApiResult<R> create(@RequestBody R r) {
        return ApiResult.success(service.create(r));
    }

    /**
     * 修改账号
     *
     * @param r
     * @return
     */
    @PutMapping("/{id}")
    public ApiResult<R> update(@RequestBody R r) {
        return ApiResult.success(service.updateAllProperties(r));
    }

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResult<SysRole> delete(@PathVariable("id") Long id) {
        service.achieve(id);
        return ApiResult.success();
    }
}
