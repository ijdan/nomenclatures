package com.ijdan.training.nomenclatures.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("databases")
public class DatabasesProperties {
    private H2 h2 = new H2();
    private String t = "d";

    public String getT() {
        return t;
    }

    public H2 getH2() {
        return h2;
    }
    public void setH2(H2 h2) {
        this.h2 = h2;
    }

    public static class H2 {
        private String url;
        private String username;
        private String password;
        private String driver;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        @Override
        public String toString() {
            return "H2{" +
                    "url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", driver='" + driver + '\'' +
                    '}';
        }
    }
}
