package com.electronics.store.exceptions;

import lombok.Builder;

@Builder
public class ResourcseNotFoundException extends RuntimeException {

       public ResourcseNotFoundException(){
           super("Resource Not Found !!");
       }

        public ResourcseNotFoundException(String message){
        super(message);
    }


}


