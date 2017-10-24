package com.ijdan.training.nomenclatures.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "databases")
public class DatabasesProperties {
    private H2Conf H2 = new H2Conf();

    public DatabasesProperties() {
    }

    public H2Conf getH2() {
        return H2;
    }

    public void setH2(H2Conf h2) {
        H2 = h2;
    }

    public static class H2Conf {
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
                    "username='" + username + '\'' +
                    "password='" + password + '\'' +
                    ", driver='" + driver + '\'' +
                    '}';
        }
    }
}
