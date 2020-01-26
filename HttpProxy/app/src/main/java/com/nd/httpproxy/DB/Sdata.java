package com.nd.httpproxy.DB;

public class Sdata {

    static public int port = 8081;
    static public String nameOfServer = "Jarvis";
    static public boolean serverStatus = false;
    static public String username = "username";
    static public String password = "password";
    static public double speed = 0;


    public static double getSpeed() {
        return speed;
    }

    public static void setSpeed(double speed) {
        Sdata.speed = speed;
    }


    public static void setServerStatus(boolean serverStatus) {
        Sdata.serverStatus = serverStatus;
    }

    public static void setUsername(String username) {
        Sdata.username = username;
    }

    public static void setPassword(String password) {
        Sdata.password = password;
    }



    public static boolean isServerStatus() {
        return serverStatus;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }




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
