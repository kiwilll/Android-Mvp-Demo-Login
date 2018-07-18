package com.bwton.metro.usermanager.entity;

/**
 * Created by hw on 17/12/7.<br>
 */
public class LoginResultInfo {

    private String token;
    private int pay_type;
    private int remain_charge;
    private int remain_card;
    private int coupon_num;
    private String shareId;
    private UserInfo user;

    public int getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(int coupon_num) {
        this.coupon_num = coupon_num;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getRemain_card() {
        return remain_card;
    }

    public void setRemain_card(int remain_card) {
        this.remain_card = remain_card;
    }

    public int getRemain_charge() {
        return remain_charge;
    }

    public void setRemain_charge(int remain_charge) {
        this.remain_charge = remain_charge;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }


    public class UserInfo {

        private String user_id;
        private String mobile_phone;
        private int user_status;
        private String reg_date;
        private String user_name;
        private int sex;
        private String birthday;
        private int id_type;
        private String id_no;
        private String nickname;
        private String profession;
        private String user_image_path;
        private String city_id;
        private String city_name;
        private int real_name_auth;
        private int real_name_reg;   //实名登记标志	0-未登记1-已登记 0要素
        private int real_name_chk;   //实名校验标志	0-已实名1-未实名 4要素
        private int real_name_open;  //是否开户标记	0-已开户1-未开户 2要素

        public int getReal_name_reg() {
            return real_name_reg;
        }

        public void setReal_name_reg(int real_name_reg) {
            this.real_name_reg = real_name_reg;
        }

        public int getReal_name_chk() {
            return real_name_chk;
        }

        public void setReal_name_chk(int real_name_chk) {
            this.real_name_chk = real_name_chk;
        }

        public int getReal_name_open() {
            return real_name_open;
        }

        public void setReal_name_open(int real_name_open) {
            this.real_name_open = real_name_open;
        }
        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCity() {
            return city_name;
        }

        public void setCity(String city) {
            this.city_name = city;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getId_no() {
            return id_no;
        }

        public void setId_no(String id_no) {
            this.id_no = id_no;
        }

        public String getReg_date() {
            return reg_date;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public int getId_type() {
            return id_type;
        }

        public void setId_type(int id_type) {
            this.id_type = id_type;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public int getReal_name_auth() {
            return real_name_auth;
        }

        public void setReal_name_auth(int real_name_auth) {
            this.real_name_auth = real_name_auth;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_image_path() {
            return user_image_path;
        }

        public void setUser_image_path(String user_image_path) {
            this.user_image_path = user_image_path;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getUser_status() {
            return user_status;
        }

        public void setUser_status(int user_status) {
            this.user_status = user_status;
        }
    }

}
