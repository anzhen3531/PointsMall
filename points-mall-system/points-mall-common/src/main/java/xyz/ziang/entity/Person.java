package xyz.ziang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.ziang.common.entity.MpBaseEntity;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author anzhen
 * @since 2023-05-13
 */
public class Person extends MpBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String personName;

    private String idCard;

    private String phoneNumber;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
