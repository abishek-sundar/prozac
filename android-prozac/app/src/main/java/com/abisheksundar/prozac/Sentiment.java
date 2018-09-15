package com.abisheksundar.prozac;

/**
 * Created by devin_w2uj42a on 2018-09-15.
 */

public class Sentiment {
    public static final int NOT_RELEVANT = 0;
    public static final int SAD = 1;
    public static final int ANXIOUS = 2;
    private int _type;

    Sentiment(int type)
    {
        _type = type;
    }

    public int get_type()
    {
        return _type;
    }
}
