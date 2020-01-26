package com.nd.httpproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.common.collect.Lists;
import com.nd.httpproxy.DB.Sdata;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

public class ProxyServer extends Thread {
    private static final String CONNECT = "CONNECT";
    private static final String HTTP_OK = "HTTP/1.1 200 OK\n";
    private static final String TAG = "ProxyServer";
    private ExecutorService threadExecutor;
    public ServerSocket serverSocket;
    private int port;
    private String nameOfServer = "Jarvis";
    private Context mAct;
    SocketSender socketSender;


    public ProxyServer(int port, String nameOfServer, Context context) {
        this.port = port;
        this.nameOfServer = nameOfServer;
        this.mAct = context;
        socketSender = new SocketSender();
        socketSender.start();
    }

    @Override
    public void run() {
        try {
            LogBuilder.addLog("Server", "Service Started");
            serverSocket = new ServerSocket(port);
            LogBuilder.addLog("Server", "successfully started on port : " + port);

            threadExecutor = new ThreadPoolExecutor(10000, 100000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            if (serverSocket != null) {
                while (serverSocket.isClosed()==false) {
                    try {
                        Socket socket = serverSocket.accept();
                        String ip_addr =   socket.getRemoteSocketAddress().toString();
                        ip_addr = ip_addr.split(":")[0];
                        Log.d("IIIIIIIPPPPPPP" , socket.getRemoteSocketAddress().toString());
                        SpeedController.addPacketToQueue( ip_addr , socket );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                closeServe();
            } else {
                LogBuilder.addLog("Server", "Failed to start server on port : " + port);
                mAct.startActivity(new Intent(mAct, MainActivity.class));
                return;
            }
        } catch (SocketException e) {
            Log.e(TAG, "Failed to start proxy server", e);
            socketSender.stop();
            closeServe();
            LogBuilder.addLog("Server", "Failed to start proxy server");
        } catch (IOException e1) {
            Log.e(TAG, "Failed to start proxy server", e1);
            LogBuilder.addLog("Server", "Failed to start proxy server");

        }
    }

    public void closeServe() {
        if(serverSocket.isClosed()==false) {
            try {
                serverSocket.close();
                LogBuilder.addLog("Server", "Successfully closed the server");
            }catch (Exception e){
                LogBuilder.addLog("Server" , "Failed to close server");
            }
            return;
        }
    }





    private void deb(String str) {
        Log.d("DEBUG", " [*] " + str);
    }

}




class SocketSender extends Thread{

    List<Socket> packetsToSend;
    Socket socket;
    private ExecutorService threadExecutor;

    public SocketSender(){

        threadExecutor = new ThreadPoolExecutor(10000, 100000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    }

    public void run(){

        while (true){

            try{
                Thread.sleep( (int)(100-Sdata.getPort())*100 );
            }catch (Exception e){
                e.printStackTrace();
            }


            packetsToSend = SpeedController.removePacketsFromQueue();

            for( int ii=0;ii<packetsToSend.size();ii++ ){
                socket = packetsToSend.get(ii);
                LogBuilder.addLog("Client" , socket.getInetAddress().toString() + " Connected Successfully");
                Log.d(" Server to web ",socket.toString());
                if (!socket.getInetAddress().isLoopbackAddress()) {
                    ProxyConnection parser = new ProxyConnection(socket);
                    threadExecutor.execute(parser);
                } else {
                    LogBuilder.addLog("Client", "Requested for 127.0.0.1 ");
                }
            }
        }
    }
}

class ProxyConnection implements Runnable {
    private Socket connection;

    ProxyConnection(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            String requestLine = getLine(connection.getInputStream());
            LogBuilder.addLog("Request", requestLine);

//            deb("REQUEST ::: " + requestLine);
            if (requestLine == null) {
                connection.close();
                return;
            }
            String[] splitLine = requestLine.split(" ");
            if (splitLine.length < 3) {
                connection.close();
                return;
            }
            String requestType = splitLine[0];
            String urlString = splitLine[1];
            String host = "";
            int port = 80;
            if (requestType.equals("CONNECT")) {
                String[] hostPortSplit = urlString.split(":");
                host = hostPortSplit[0];
                try {
                    port = Integer.parseInt(hostPortSplit[1]);
                } catch (NumberFormatException nfe) {
                    port = 443;
                }
                urlString = "Https://" + host + ":" + port;
            } else {
                try {
                    URI url = new URI(urlString);
                    host = url.getHost();
                    port = url.getPort();
                    if (port < 0) {
                        port = 80;
                    }
                } catch (URISyntaxException e) {
                    connection.close();
                    return;
                }
            }
            List<Proxy> list = Lists.newArrayList();
            try {
                list = ProxySelector.getDefault().select(new URI(urlString));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Socket server = null;
            for (Proxy proxy : list) {
                try {
                    if (!proxy.equals(Proxy.NO_PROXY)) {
                        InetSocketAddress inetSocketAddress =
                                (InetSocketAddress) proxy.address();
                        server = new Socket(inetSocketAddress.getHostName(),
                                inetSocketAddress.getPort());
                        sendLine(server, requestLine);
                    } else {
                        server = new Socket(host, port);
                        if (requestType.equals("CONNECT")) {
                            while (getLine(connection.getInputStream()).length() != 0) ;
                            sendLine(connection, "HTTP/1.1 200 OK\n");
                        } else {
                            sendLine(server, requestLine);
                        }
                    }
                } catch (IOException ioe) {
                }
                if (server != null) {
                    break;
                }
            }
            if (server == null) {
                server = new Socket(host, port);
                if (requestType.equals("CONNECT")) {
                    while (getLine(connection.getInputStream()).length() != 0) ;
                    sendLine(connection, "HTTP/1.1 200 OK\n");
                } else {
                    sendLine(server, requestLine);
                }
            }
            SocketConnect.connect(connection, server);
        } catch (IOException e) {
       //     Log.d(TAG, "Problem Proxying", e);
            LogBuilder.addLog("Client","Fail to connect remote server due to slow internet connection");
        }
        try {
            connection.close();
        } catch (IOException ioe) {

        }
    }

    private String getLine(InputStream inputStream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        try {
            int byteBuffer = inputStream.read();
            if (byteBuffer < 0) return "";
            do {
                if (byteBuffer != '\r') {
                    buffer.append((char) byteBuffer);
                }
                byteBuffer = inputStream.read();
            } while ((byteBuffer != '\n') && (byteBuffer >= 0));
        }catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private void sendLine(Socket socket, String line) throws IOException {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(line.getBytes());
            os.write('\r');
            os.write('\n');
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
