package lam.ladya.chromaspiral.chroma;


public record RGBColor(int r, int g, int b) {

    public int toPackedRGB() {
        return (r << 16) | (g << 8) | b;
    }
}
