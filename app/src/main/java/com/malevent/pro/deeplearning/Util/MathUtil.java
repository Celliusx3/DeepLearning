package com.malevent.pro.deeplearning.Util;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public class MathUtil {

    public static float sigmoid(final float x) {
        return (float) (1. / (1. + Math.exp(-x)));
    }

    public static void softmax(final float[] vals) {
        float max = Float.NEGATIVE_INFINITY;
        for (final float val : vals) {
            max = Math.max(max, val);
        }
        float sum = 0.0f;
        for (int i = 0; i < vals.length; ++i) {
            vals[i] = (float) Math.exp(vals[i] - max);
            sum += vals[i];
        }
        for (int i = 0; i < vals.length; ++i) {
            vals[i] = vals[i] / sum;
        }
    }

}
