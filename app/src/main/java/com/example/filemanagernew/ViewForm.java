package com.example.filemanagernew;

public enum ViewForm {
    ROW(0),GRID(1);

     public int value;

     ViewForm(int value){
        this.value=value;
    }
    public int getValue(){return value;}
}
