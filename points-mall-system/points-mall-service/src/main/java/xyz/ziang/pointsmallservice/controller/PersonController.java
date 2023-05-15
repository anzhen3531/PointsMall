package xyz.ziang.pointsmallservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.ziang.entity.Person;
import xyz.ziang.pointsmallservice.controller.base.BaseController;
import xyz.ziang.pointsmallservice.service.PersonService;

/**
 * <p>
 * 人员表 前端控制器
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/person")
public class PersonController extends BaseController<PersonService, Person> {
    public PersonController(PersonService service) {
        super(service);
    }


}
