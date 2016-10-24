package com.github.denisura.nytseacher.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Article {

    @SerializedName("headline")
    @Expose
    public Headline headline;
    @SerializedName("multimedia")
    @Expose
    public List<Multimedium> multimedia = new ArrayList<>();
    @SerializedName("web_url")
    @Expose
    public String webUrl;

    /**
     *
     * @return
     * The headline
     */
    public String getHeadline() {
        return headline.getMain();
    }

    /**
     *
     * @param headline
     * The headline
     */
    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getThumbnail() {
        if (multimedia.isEmpty()){
            return "";
        }
       return  multimedia.get(0).getUrl();
    }

    /**
     *
     * @return
     * The multimedia
     */
    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public boolean hasMultimedia() {
        return (!multimedia.isEmpty());
    }

    /**
     *
     * @param multimedia
     * The multimedia
     */
    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    /**
     *
     * @return
     * The webUrl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     *
     * @param webUrl
     * The web_url
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}