package xyz.ziang.entity;

import xyz.ziang.common.entity.MpBaseEntity;

public class SysUser extends MpBaseEntity {
    private String person_id;
    private String username;
    private String password;

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
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
