package xyz.ziang.entity;

import xyz.ziang.common.entity.MpBaseEntity;

public class SysUser extends MpBaseEntity {
    private Long personId;
    private String username;
    private String password;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
