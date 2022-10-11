package com.dgsl.utility_package.ui.root_access_check;

public class RootItemResult {
    String checkContent;
    boolean status;

    public RootItemResult(String checkContent,boolean status){
        this.checkContent = checkContent;
        this.status = status;
    }
}
