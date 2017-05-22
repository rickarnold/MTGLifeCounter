package com.stick.mtglifecounter;


public enum Color {
    BLACK("Black", 0xFF606060, 0xFF3b3b3b, R.drawable.black_symbol, R.raw.o_fortuna),
    BLUE("Blue", 0xFFa0d4f2, 0xFF0080a3, R.drawable.blue_symbol, R.raw.topgun),
    GREEN("Green", 0xFF98e38a, 0xFF148200, R.drawable.green_symbol, R.raw.medieval_one),
    RED("Red", 0xFFff8080, 0xFF940000, R.drawable.red_symbol, R.raw.firestarter),
    WHITE("White", 0xFFf2f1bb, 0xFF9c9b76, R.drawable.white_symbol, R.raw.angel_of_death);

    private String displayName;
    private int backgroundColor;
    private int textColor;
    private int manaDrawable;
    private int songResource;

    private Color (String displayName, int backgroundColor, int textColor, int manaDrawable, int songResource)
    {
        this.displayName = displayName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.manaDrawable = manaDrawable;
        this.songResource = songResource;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public int getBackgroundColor()
    {
        return this.backgroundColor;
    }

    public int getTextColor()
    {
        return this.textColor;
    }

    public int getManaDrawable()
    {
        return this.manaDrawable;
    }

    public int getSongResource() { return this.songResource; }

    public static Color getColorFromPosition(int position)
    {
        return Color.values()[position];
    }
}
