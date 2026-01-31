package com.example.code_be.enums;

public enum Mood {
    HAPPY("😊", "Vui vẻ"),
    SAD("😢", "Buồn"),
    ANGRY("😡", "Tức giận"),
    IN_LOVE("😍", "Yêu thương"),
    TIRED("😴", "Mệt mỏi"),
    EXCITED("🤩", "Hào hứng"),
    ANXIOUS("😰", "Lo lắng"),
    PEACEFUL("😌", "Bình yên");

    private final String emoji;
    private final String label;

    Mood(String emoji, String label) {
        this.emoji = emoji;
        this.label = label;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getLabel() {
        return label;
    }
}
