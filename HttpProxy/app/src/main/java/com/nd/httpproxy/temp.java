package com.nd.httpproxy;

import java.net.ServerSocket;

public class temp {
}

//    private void startServer() {
//        this.serverThread = new Thread(new ServerThread());
//        this.serverThread.start();
//        deb("Server Thread Get Started ...");
//    }
//
//
//class ServerThread implements Runnable {
//
//    public void run() {
//
//        try {
//            serverSocket = new ServerSocket(port);
//            deb("Server Created Successfully On Port " + port + "...");
//        } catch (IOException e) {
//            deb("FAIL : Server Not Created On Port " + port);
//            e.printStackTrace();
//        }
//
//        while (serverSocket.isClosed() == false) {
//            try {
//                deb("Server Is Accepting client ...");
//                Socket socket = serverSocket.accept();
//                new Thread(new CommunicationThread(socket)).start();
//                deb("Client : " + socket.getInetAddress().toString() + " Connected To The Port : " + port);
//            } catch (IOException e) {
//                deb("FAIL : Server is not accepting client ");
//                e.printStackTrace();
//            }
//        }
//        closeServer();
//    }
//}
//
//class CommunicationThread implements Runnable {
//
//    private Socket clientSocket;
//    private BufferedReader input;
//
//    public CommunicationThread(Socket clientSocket) {
//        this.clientSocket = clientSocket;
//        try {
//            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//        try {
//            String buffer = "";
//            String firstLine = "";
//
//            while (input.ready()) {
//                if (firstLine.equals("")) {
//                    firstLine = input.readLine();
//                    buffer = firstLine;
//                } else {
//                    buffer = buffer + "\n" + input.readLine();
//                }
//            }
//
//
//
//            deb("REQ : " + buffer);
//
//            requestConverter(firstLine, buffer, clientSocket);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void requestConverter(String firstLine, String buffer, Socket clientSocket) {
//        try {
//            String temp = null;
//            String url = firstLine.split("\\s")[1];
//            deb("URL : " + url);
//            int httpPos = url.indexOf("://");
//            deb("POS :// " + httpPos);
//
//            if (httpPos == -1) {
//                temp = url;
//            } else {
//                temp = "";
//                for (int i = httpPos + 3; i < url.length(); i++) {
//                    temp = temp + url.charAt(i);
//                }
//            }
//            temp.trim();
//            deb("TEMP : " + temp);
//            int portPos = temp.indexOf(':');
//            deb("port pos : " + portPos);
//
//            int webServerPos = temp.indexOf('/');
//            if (webServerPos == -1) {
//                webServerPos = temp.length();
//            }
//            deb("Web Serve pos : " + webServerPos);
//
//            String webserver = "";
//            int port = -1;
//
//            if (portPos == -1 || webServerPos < portPos) {
//                port = 80;
//                for (int i = 0; i < webServerPos; i++) {
//                    webserver = webserver + temp.charAt(i);
//                }
//                deb("PORT ..." + port);
//                deb("WEB SERVER ..." + webserver);
//
//
//            } else {
//                String tempport = "", tempPort = "";
//                for (int i = portPos + 1; i < temp.length(); i++) {
//                    tempport = tempport + temp.charAt(i);
//                }
//                for (int i = 0; i < webServerPos - portPos - 1; i++) {
//                    tempPort = tempPort + tempport.charAt(i);
//                }
//                port = valueOf(tempPort);
//                deb("PORT -->" + port);
//                for (int i = 0; i < portPos; i++) {
//                    webserver = webserver + temp.charAt(i);
//                }
//                deb("WEB SERVER -->" + webserver);
//            }
//
//            webserver.trim();
//            proxyServer(webserver, port, clientSocket, buffer);
//
//        } catch (Exception e) {
//            deb("FAIL : Request Converter has error");
//            e.printStackTrace();
//        }
//    }
//
//    private void proxyServer(String webServer, int port, Socket clientSocket, String buffer) {
//        try {
//            Socket s = new Socket(webServer, port);
//            if(s.isBound()){
//                deb("SOCKET :: " + s.getInetAddress().toString() + " connected" );
//            }
//            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
//            if(s.isBound()){
//                deb("SOCKET :: " + s.getInetAddress().toString() + " connected" );
//            }
//            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            dout.writeUTF(buffer);
//            deb("Data sent successfully to : " + webServer + " AT PORT : " + port);
//
//            dout = new DataOutputStream(clientSocket.getOutputStream());
//            while (true) {
//                String reply = "";
//                deb("REPLY :: " + input.readLine() + "\n" + input.readLine()+ "\n" + input.readLine()+ "\n" + input.readLine()+ "\n" + input.readLine()+ "\n" + input.readLine()+ "\n" + input.readLine());
////                    while (input.ready()) {
////                        if (reply.equals("")) {
////                            reply = input.readLine();
////                        } else {
////                            reply = reply + "\n" + input.readLine();
////                        }
////                    }
////                    deb("REPLY : " + reply);
//                if (reply.length() > 0) {
//
//                    dout.writeUTF(reply);
//                } else {
//                    break;
//                }
//
//            }
//
//
//            closeClient(s);
//            closeClient(clientSocket);
//            deb("------------------- SUCCESS ------------------");
//
//        } catch (Exception e) {
//            deb("FAIL DSS : " + webServer + " AT PORT : " + port);
//            closeClient(clientSocket);
//            e.printStackTrace();
//        }
//    }
//
//}
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        closeServer();
//    }
//
//    private void closeServer() {
//        if (serverSocket.isClosed() == false) {
//            try {
//                deb("Server Closed on port " + port);
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void closeClient(Socket socket) {
//        if (socket != null) {
//            try {
//                deb("Client socket  closed on port " + port);
//                socket.close();
//            } catch (IOException e) {
//                deb("Clint Socket Is Not Closed");
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void initiallization() {
//        mPort = (EditText) this.findViewById(R.id.port);
//        mOpenPort = this.findViewById(R.id.open_port);
//    }
//
//    private void deb(String str) {
//        Log.d("DEBUG", " [*] " + str);
//    }
