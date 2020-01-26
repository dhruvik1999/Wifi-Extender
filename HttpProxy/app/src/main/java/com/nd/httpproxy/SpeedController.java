package com.nd.httpproxy;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SpeedController {

    static Map<String, LinkedList<Socket>> map = new HashMap<String, LinkedList<Socket>>();
    boolean lock1,lock2;

    public static void addPacketToQueue(String ip, Socket socket) {
        if (!map.containsKey(ip)) {
            Queue<Integer> newQueue = new LinkedList<Integer>();
            map.put(ip, (LinkedList) newQueue);
        }
        synchronized (map.get(ip)) {
            map.get(ip).add(socket);
        }
    }

    public static ArrayList<Socket> removePacketsFromQueue() {
        List<Socket> packetsToSend = new ArrayList<Socket>();
        for (Map.Entry<String, LinkedList<Socket>> entry : map.entrySet()) {
            if (!entry.getValue().isEmpty())
                synchronized ( entry.getValue() ) {
                    packetsToSend.add(entry.getValue().remove());
                }
        }
        return (ArrayList<Socket>) packetsToSend;
    }

    public void displayQueue() {
        for (Map.Entry<String, LinkedList<Socket>> entry : map.entrySet())
            System.out.println(entry.getValue());
    }
}
