package com.example.sharetalks.Bean;

public class Post {
    private String Stock;
    private String BuyPrice;
    public Post(){}
    public Post(String stock, String buyPrice, long messageTime, String target1, String target2, String likes, String tradeType, String targetDays) {
        Stock = stock;
        BuyPrice = buyPrice;
        this.messageTime = messageTime;
        Target1 = target1;
        Target2 = target2;
        Likes = likes;
        TradeType = tradeType;
        TargetDays = targetDays;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    long messageTime;
    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        BuyPrice = buyPrice;
    }

    public String getTarget1() {
        return Target1;
    }

    public void setTarget1(String target1) {
        Target1 = target1;
    }

    public String getTarget2() {
        return Target2;
    }

    public void setTarget2(String target2) {
        Target2 = target2;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
    }

    public String getTargetDays() {
        return TargetDays;
    }

    public void setTargetDays(String targetDays) {
        TargetDays = targetDays;
    }
    private String Target1;
    private String Target2;
    private String Likes;
    private String TradeType;
    private String TargetDays;
}
