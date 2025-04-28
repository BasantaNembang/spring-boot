package com.basanta.OrderService.error;



public class OrderExceptionHandler extends RuntimeException{

    public OrderExceptionHandler(){
        super("Something went wrong..!");
    }

    public OrderExceptionHandler(String msg){
        super(msg);
    }


}
