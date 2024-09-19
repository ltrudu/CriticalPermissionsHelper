package com.zebra.criticalpermissionshelper;

enum EMessageType {
    VERBOSE("VERBOSE"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS"),
    DEBUG("DEBUG");

    String stringContent = "";
    EMessageType(String stringContent) { this.stringContent = stringContent;}

    public String toString() {
        return stringContent;
    }

    public static EMessageType fromString(String messageType)
    {
        switch(messageType) {
            case "VERBOSE":
                return VERBOSE;
            case "WARNING":
                return WARNING;
            case "Error":
                return ERROR;
            case "SUCCESS":
                return SUCCESS;
            case "DEBUG":
                return DEBUG;
            default:
                return null;
        }
    }
}
