package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Kết nối đến server thành công!");

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Nhập username của bạn: ");
            String username = userInput.readLine();
            out.println(username);

            // Thread để nhận tin nhắn từ server và in ra màn hình
            Thread serverListener = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = serverInput.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Lỗi khi đọc tin nhắn từ server: " + e.getMessage());
                }
            });
            serverListener.start();

            // Gửi tin nhắn từ client đến server
            String clientMessage;
            while ((clientMessage = userInput.readLine()) != null) {
                out.println(clientMessage);
            }

            socket.close();
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối đến server: " + e.getMessage());
        }
    }
}
