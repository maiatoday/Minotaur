package net.maiatoday.minotaur.ui.view;

/**
 * Interface to set the values of the big dial
 * Created by maia on 2015/06/24.
 */
public interface SetGauge {
    void setGaugeTitle(String text);
    void setGaugeSubtitle(String text);
    void setValue(int value);
    void setValueText(String text);
    void setMessage(String text);
}
