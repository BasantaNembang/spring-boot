package com.basanta.ProductService.error;

public class ResourseNotFound  extends  RuntimeException{

    public ResourseNotFound(){
        super("Resourcse not found!!..");
    }


    public ResourseNotFound(String msg){
        super("Resourcse not found!!.. "+msg);
    }


}
