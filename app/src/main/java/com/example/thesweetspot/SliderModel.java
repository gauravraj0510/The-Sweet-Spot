package com.example.thesweetspot;

public class SliderModel {
    private int banner;
    private String backgroundColour;

    public SliderModel(int banner, String backgroundColour) {
        this.banner = banner;
        this.backgroundColour = backgroundColour;
    }

    public int getBanner() {
        return banner;
    }

    public void setBanner(int banner) {
        this.banner = banner;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }
}
