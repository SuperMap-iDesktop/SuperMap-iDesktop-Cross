package com.supermap.desktop.ui.controls;

import com.supermap.desktop.properties.CharsetProperties;

import javax.swing.*;

@SuppressWarnings("serial")
public class CharsetComboBox extends JComboBox<Object> {
    public CharsetComboBox() {
        setComboBoxModel();
    }

    private void setComboBoxModel() {
        //@formatter:off
        setModel(new DefaultComboBoxModel<Object>(new String[]{
                CharsetProperties.getString(CharsetProperties.ANSI),
                CharsetProperties.getString(CharsetProperties.DEFAULT),
                CharsetProperties.getString(CharsetProperties.OEM),
                CharsetProperties.getString(CharsetProperties.CHINESEBIG5),
                CharsetProperties.getString(CharsetProperties.GB18030),
                CharsetProperties.getString(CharsetProperties.CYRILLIC),
                CharsetProperties.getString(CharsetProperties.XIA5),
                CharsetProperties.getString(CharsetProperties.XIA5GERMAN),
                CharsetProperties.getString(CharsetProperties.XIA5NORWEGIAN),
                CharsetProperties.getString(CharsetProperties.XIA5SWEDISH),
                CharsetProperties.getString(CharsetProperties.MAC),
                CharsetProperties.getString(CharsetProperties.UNICODE),
                CharsetProperties.getString(CharsetProperties.UTF7),
                CharsetProperties.getString(CharsetProperties.UTF8),
                CharsetProperties.getString(CharsetProperties.UTF32),
                CharsetProperties.getString(CharsetProperties.WINDOWS1252),
                CharsetProperties.getString(CharsetProperties.ARABIC),
                CharsetProperties.getString(CharsetProperties.BALTIC),
                CharsetProperties.getString(CharsetProperties.GREEK),
                CharsetProperties.getString(CharsetProperties.JOHAB),
                CharsetProperties.getString(CharsetProperties.HANGEUL),
                CharsetProperties.getString(CharsetProperties.EASTEUROPE),
                CharsetProperties.getString(CharsetProperties.RUSSIAN),
                CharsetProperties.getString(CharsetProperties.SYMBOL),
                CharsetProperties.getString(CharsetProperties.KOREAN),
                CharsetProperties.getString(CharsetProperties.SHIFTJIS),
                CharsetProperties.getString(CharsetProperties.THAI),
                CharsetProperties.getString(CharsetProperties.TURKISH),
                CharsetProperties.getString(CharsetProperties.HEBREW),
                CharsetProperties.getString(CharsetProperties.VIETNAMESE),
        }));
        //@formatter:on
    }

    public void setSelectCharset(String charset) {
        this.setSelectedItem(CharsetProperties.getString("String_" + charset.toUpperCase()));
    }
}
