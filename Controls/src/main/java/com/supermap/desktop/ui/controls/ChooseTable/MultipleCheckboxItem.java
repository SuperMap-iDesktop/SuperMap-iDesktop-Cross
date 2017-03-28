package com.supermap.desktop.ui.controls.ChooseTable;

/**
 * Created by lixiaoyao on 2017/3/20.
 */
public class MultipleCheckboxItem {
    private boolean isSelected;
    private String text;

    public MultipleCheckboxItem(){
        this.text="";
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean getSelected() {
        return this.isSelected;
    }

    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return this.text;
    }
}
