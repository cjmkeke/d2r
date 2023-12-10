package com.cjmkeke.mymemo.modelClass;

import java.util.HashMap;
import java.util.Map;

public class ModelMemoWrite {

    private String title;
    private String mainText;
    private String profile;
    private String token;
    private String email;
    private String date;
    private String recyclerDate;
    private long recyclerDateList;
    private String template;
    private String name;
    private ModelImages boardImages;
    private String images0;
    private String images1;
    private String images2;
    private String images3;
    private String images4;
    private String images5;
    private String images6;
    private String colorTitle;
    private String colorMainText;
    private boolean publicKey;
    private boolean remove;
    private String backgroundColor;
    private String titleColor;
    private String editLog;
    private HashMap<String, String> canlendarDayToMemo = new HashMap<>();
    private HashMap<String, String> canlendarDay = new HashMap<>();
    private Map<String, ConnectUser> connectUser;

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public Map<String, ConnectUser> getConnectUser() {
        return connectUser;
    }

    public void setConnectUser(Map<String, ConnectUser> connectUser) {
        this.connectUser = connectUser;
    }

    public HashMap<String, String> getCanlendarDayToMemo() {
        return canlendarDayToMemo;
    }

    public void setCanlendarDayToMemo(HashMap<String, String> canlendarDayToMemo) {
        this.canlendarDayToMemo = canlendarDayToMemo;
    }

    public HashMap<String, String> getCanlendarDay() {
        return canlendarDay;
    }

    public void setCanlendarDay(HashMap<String, String> canlendarDay) {
        this.canlendarDay = canlendarDay;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getEditLog() {
        return editLog;
    }

    public void setEditLog(String editLog) {
        this.editLog = editLog;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    //    private Map<String, String> images;
//
//    public void setImages(Map<String, String> images) {
//        this.images = images;
//    }

    public ModelImages getBoardImages() {
        return boardImages;
    }


    public String getColorTitle() {
        return colorTitle;
    }

    public void setColorTitle(String colorTitle) {
        this.colorTitle = colorTitle;
    }

    public String getColorMainText() {
        return colorMainText;
    }

    public void setColorMainText(String colorMainText) {
        this.colorMainText = colorMainText;
    }

    public void setBoardImages(ModelImages boardImages) {
        this.boardImages = boardImages;
    }

    public long getRecyclerDateList() {
        return recyclerDateList;
    }

    public void setRecyclerDateList(long recyclerDateList) {
        this.recyclerDateList = recyclerDateList;
    }

    public boolean isPublicKey() {
        return publicKey;
    }

    public void setPublicKey(boolean publicKey) {
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecyclerDate() {
        return recyclerDate;
    }

    public void setRecyclerDate(String recyclerDate) {
        this.recyclerDate = recyclerDate;
    }

    public String getImages0() {
        return images0;
    }

    public void setImages0(String images0) {
        this.images0 = images0;
    }

    public String getImages1() {
        return images1;
    }

    public void setImages1(String images1) {
        this.images1 = images1;
    }

    public String getImages2() {
        return images2;
    }

    public void setImages2(String images2) {
        this.images2 = images2;
    }

    public String getImages3() {
        return images3;
    }

    public void setImages3(String images3) {
        this.images3 = images3;
    }

    public String getImages4() {
        return images4;
    }

    public void setImages4(String images4) {
        this.images4 = images4;
    }

    public String getImages5() {
        return images5;
    }

    public void setImages5(String images5) {
        this.images5 = images5;
    }

    public String getImages6() {
        return images6;
    }

    public void setImages6(String images6) {
        this.images6 = images6;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
