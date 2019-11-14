/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author PC
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
     public static void main(String[] args) {

        try {
            // TODO code application logic here
            ServerSocket server = new ServerSocket(6789);
            while (true) {

                Socket con = server.accept();
                ClientHandler ch = new ClientHandler(con);
                Thread th = new Thread(ch);
                th.start();
            }
        } catch (IOException ex) {
            System.out.println("No Connection Established");
        }
    }
    
}
