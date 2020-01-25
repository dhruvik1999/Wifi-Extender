package com.nd.httpproxy;

import android.util.Log;

import com.nd.httpproxy.DB.Sdata;

import java.util.Deque;
import java.util.LinkedList;

public class LogBuilder {

    private static int maxLength = 50;
    private static Deque<String> logs = new LinkedList<String>();
    private static String nameOfServer = Sdata.getNameOfServer();


    public static String logBuild(String label, String disc) {
        String respoce = "Server [" + nameOfServer + "] $ " + label + " : " + disc;
        return respoce;
    }

    synchronized public static void addLog(String label, String disc) {
        Log.d(label, disc);
        try {
            logs.addFirst(logBuild(label, disc));
            if (logs.size() > maxLength) {
                logs.removeLast();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static String getLog() {
        String respoce = "NO LOGS AVAILABLE";
        try {
            int cnt = 0;
            for (String log : logs) {
                if (cnt == 0) {
                    respoce = log;
                    cnt++;
                } else {
                    respoce = respoce + "\n" + log;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respoce;
    }
}
