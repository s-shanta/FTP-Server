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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    
    Socket sock;
    BufferedReader in;
    DataOutputStream out;
    Boolean check = false;

    ClientHandler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        try {
            while (true) {
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String input = in.readLine();
                String[] input2 = input.split("#");
                if (input2[0].equals("login")) {

                    BufferedReader br = new BufferedReader(new FileReader("name&pass.txt"));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        if (line.equals(input2[1])) {
                            check = true;
                            out = new DataOutputStream(sock.getOutputStream());
                            out.writeBytes("True" + '\n');
                            break;
                        }
                    }
                    if (check == false) {
                        out = new DataOutputStream(sock.getOutputStream());
                        out.writeBytes("False" + '\n');
                    }
                }
                else if (input2[0].equals("signup")) {
                    BufferedReader br = new BufferedReader(new FileReader("name&pass.txt"));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                         if (line.equals(input2[1])) {
                             check = true;
                             out = new DataOutputStream(sock.getOutputStream());
                             out.writeBytes("False" + '\n');
                             break;
                         }
                    }
                    if(check == true){
                           try {
                               PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("name&pass.txt", true)));
                               out.println(input2[1]);
                               out.close();
                            } catch (IOException e) {
                                      //exception handling left as an exercise for the reader
                            }
                            out = new DataOutputStream(sock.getOutputStream());
                            out.writeBytes("True" + '\n');
                    }
                }
                else if(input2[0].equals("upload")){
                    File src = new File(input2[1]);
                    File target = new File(input2[3] + "\\" + input2[2]);
                    InputStream in = new FileInputStream(src);
                    OutputStream ot = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while((length = in.read(buffer)) > 0){
                        ot.write(buffer, 0, length);
                    }
                    out = new DataOutputStream(sock.getOutputStream());
                    out.writeBytes("Uploaded" + '\n');
                    
                }
                else if(input2[0].equals("download")){
                    File src = new File(input2[1]);
                    File target = new File(input2[2]);
                    InputStream in2 = new FileInputStream(src);
                    OutputStream ot2 = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while((length = in2.read(buffer)) > 0){
                        ot2.write(buffer, 0, length);
                    }
                    out = new DataOutputStream(sock.getOutputStream());
                    out.writeBytes("Downloaded" + '\n');
                }
                else if(input2[0].equals("logout")){
                    break;
                }
            }
            out = new DataOutputStream(sock.getOutputStream());
            out.writeBytes("Closing connection" + '\n');
            sock.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
