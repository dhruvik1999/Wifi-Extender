package com.nd.httpproxy.DB;

public class Sdata {

    static public int port = 8081;
    static public String nameOfServer = "Jarvis";
    static public boolean serverStatus = false;

    public static int getPort() {
        return port;
    }

    public static String getNameOfServer() {
        return nameOfServer;
    }

    public static void setPort(int port) {
        Sdata.port = port;
    }

    public static void setNameOfServer(String nameOfServer) {
        Sdata.nameOfServer = nameOfServer;
    }

    public static void setServerOff(){
        Sdata.serverStatus=false;
    }

    public static void setServerOn(){
        Sdata.serverStatus=true;
    }

    public static boolean getServerStatus(){
        return Sdata.serverStatus;
    }







}
