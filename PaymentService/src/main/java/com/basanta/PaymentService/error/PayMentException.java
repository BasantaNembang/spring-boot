package com.basanta.PaymentService.error;

public class PayMentException extends RuntimeException{

     public PayMentException(){
        super("Error in Payment Service");
    }


    public PayMentException(String msg){
        super("Error in Payment Service   "+msg);
    }

}
