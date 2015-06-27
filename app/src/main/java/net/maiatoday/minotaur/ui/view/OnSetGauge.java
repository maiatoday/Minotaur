package net.maiatoday.minotaur.ui.view;

/**
 * Interface to set the values of the big dial
 * Created by maia on 2015/06/24.
 */
public interface OnSetGauge {
    void setStaticFields(final String title, final String subtitle, final String message, final boolean updateValueText);
    void setGaugeTitle(final String text);
    void setGaugeSubtitle(final String text);
    void setDialValue(final float value, final float min, final float max);
    void setValueText(final String text);
    void setMessage(final String text);
}
