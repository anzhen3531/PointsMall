package xyz.ziang.pointsmallservice.service.impl;

import xyz.ziang.common.service.impl.MpCrudBaseServiceImpl;
import xyz.ziang.entity.Person;
import xyz.ziang.pointsmallservice.mapper.PersonMapper;
import xyz.ziang.pointsmallservice.service.PersonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@Service("PersonService")
public class PersonServiceImpl extends MpCrudBaseServiceImpl<Person, PersonMapper> implements PersonService {

    /**
     * 通过有参构造器传入Mapper对象
     *
     * @param repository
     */
    public PersonServiceImpl(PersonMapper repository) {
        super(repository);
    }
}
