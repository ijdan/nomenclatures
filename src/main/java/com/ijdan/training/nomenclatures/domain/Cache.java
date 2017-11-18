package com.ijdan.training.nomenclatures.domain;

public class Cache {
    private String enabled;
    private String expiration;
    private static long LOADED_TIME = 0;

    public Cache() {
    }

    private String getEnabled() {
        return enabled;
    }
    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public boolean isExpire(){
        long currentTimeMillis = System.currentTimeMillis();
        int numberOfSecondsPassed = (int) ((currentTimeMillis - this.getLoadedTime())/1000);
        return (numberOfSecondsPassed > Integer.parseInt(this.getExpiration()));
    }

    public static long getLoadedTime() {
        return LOADED_TIME;
    }

    public static void setLoadedTime(long loadedTime) {
        LOADED_TIME = loadedTime;
    }

    public boolean useCache (){
        if (this.isEnabled() && !this.isExpire()){
            return true;
        }else {
            this.setLoadedTime(System.currentTimeMillis());
            return false;
        }
    }
}
