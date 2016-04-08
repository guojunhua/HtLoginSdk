package org.mobile.htloginsdk.bean;

/**
 * Created by 郭君华 on 2016/1/18.
 * Email：guojunhua3369@163.com
 */
public class LoginBean {
    /**
     * code : 0
     * msg : success
     * data : {"uid":"100156","name":"259208662443959#device","photo":"http://www.gravatar.com/avatar/848abe1c5f08788d4d76feed0b53e256?s=80&d=identicon","bind":{"device":{"auth_id":"259208662443959","auth_name":"android"}},"token":"e2l5tF2wrtAccG0ql03JSnZu3V9N9aXNdMoX75ueJY3ocCHPeJnkdnNh5dkM1wvx"}
     */

    private int code;
    private String msg;
    /**
     * uid : 100156
     * name : 259208662443959#device
     * photo : http://www.gravatar.com/avatar/848abe1c5f08788d4d76feed0b53e256?s=80&d=identicon
     * bind : {"device":{"auth_id":"259208662443959","auth_name":"android"}}
     * token : e2l5tF2wrtAccG0ql03JSnZu3V9N9aXNdMoX75ueJY3ocCHPeJnkdnNh5dkM1wvx
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataEntity {
        private String uid;
        private String name;
        private String photo;
        /**
         * device : {"auth_id":"259208662443959","auth_name":"android"}
         */

        private BindEntity bind;
        private String token;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setBind(BindEntity bind) {
            this.bind = bind;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public BindEntity getBind() {
            return bind;
        }

        public String getToken() {
            return token;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "uid='" + uid + '\'' +
                    ", name='" + name + '\'' +
                    ", photo='" + photo + '\'' +
                    ", bind=" + bind +
                    ", token='" + token + '\'' +
                    '}';
        }

        public static class BindEntity {
            /**
             * auth_id : 259208662443959
             * auth_name : android
             */

            private DeviceEntity device;
            private EmailEntity email;
            private FacebookEntity facebook;

            public void setDevice(DeviceEntity device) {
                this.device = device;
            }

            public DeviceEntity getDevice() {
                return device;
            }

            public EmailEntity getEmail() {
                return email;
            }

            public void setEmail(EmailEntity email) {
                this.email = email;
            }

            public FacebookEntity getFacebook() {
                return facebook;
            }

            public void setFacebook(FacebookEntity facebook) {
                this.facebook = facebook;
            }

            @Override
            public String toString() {
                return "BindEntity{" +
                        "device=" + device +
                        ", email=" + email +
                        ", facebook=" + facebook +
                        '}';
            }

            public static class DeviceEntity {
                private String auth_id;
                private String auth_name;

                public void setAuth_id(String auth_id) {
                    this.auth_id = auth_id;
                }

                public void setAuth_name(String auth_name) {
                    this.auth_name = auth_name;
                }

                public String getAuth_id() {
                    return auth_id;
                }

                public String getAuth_name() {
                    return auth_name;
                }

                @Override
                public String toString() {
                    return "DeviceEntity{" +
                            "auth_id='" + auth_id + '\'' +
                            ", auth_name='" + auth_name + '\'' +
                            '}';
                }
            }

            public static class EmailEntity {
                private String auth_id;
                private String auth_name;

                public void setAuth_id(String auth_id) {
                    this.auth_id = auth_id;
                }

                public void setAuth_name(String auth_name) {
                    this.auth_name = auth_name;
                }

                public String getAuth_id() {
                    return auth_id;
                }

                public String getAuth_name() {
                    return auth_name;
                }

                @Override
                public String toString() {
                    return "EmailEntity{" +
                            "auth_id='" + auth_id + '\'' +
                            ", auth_name='" + auth_name + '\'' +
                            '}';
                }
            }

            public static class FacebookEntity {
                private String auth_id;
                private String auth_name;

                public void setAuth_id(String auth_id) {
                    this.auth_id = auth_id;
                }

                public void setAuth_name(String auth_name) {
                    this.auth_name = auth_name;
                }

                public String getAuth_id() {
                    return auth_id;
                }

                public String getAuth_name() {
                    return auth_name;
                }

                @Override
                public String toString() {
                    return "FacebookEntity{" +
                            "auth_id='" + auth_id + '\'' +
                            ", auth_name='" + auth_name + '\'' +
                            '}';
                }
            }
        }
    }
}
