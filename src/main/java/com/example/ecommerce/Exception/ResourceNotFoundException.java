package com.example.ecommerce.Exception;

public class ResourceNotFoundException extends RuntimeException{
    // ResourceNotFoundException - your version ✅
        String resourceName;
        String fieldName;
        Long fieldValue;

        public ResourceNotFoundException(String resourceName,
                                         String fieldName,
                                         Long fieldValue) {
            super(resourceName + " not found with " + fieldName + ": " + fieldValue);
        }
    }

    // InvalidRequestException - simple is fine here
//    public class InvalidRequestException extends RuntimeException {
//        public InvalidRequestException(String message) {
//            super(message);
//        }
//    }
//
//    // APIException - simple is fine here
//    public class APIException extends RuntimeException {
//        public APIException(String message) {
//            super(message);
//        }
//    }

