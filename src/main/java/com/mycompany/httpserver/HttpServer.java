/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.httpserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 2108616
 */
public class HttpServer implements Runnable {
    private Socket socket;
    
    HttpServer(Socket number) {
        this.socket = number;
    }

    @Override
    public void run()  {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String path = in.readLine();
            String format, data;
            byte[] bytes;                      
            
            if(path != null){
                path = path.split(" ")[1];
                if(path.contains(".html")){
                    bytes = Files.readAllBytes(new File("./"+path).toPath());
                    data = "" + bytes.length;
                    format = "text/html";                   
                }else if(path.contains(".jpg")){
                    bytes = Files.readAllBytes(new File("./"+path).toPath());
                    data = "" + bytes.length;
                    format = "image/html";                                   
                }else{
                    bytes = Files.readAllBytes(new File("./index.html").toPath());
                    data = "" + bytes.length;
                    format = "text/html";                   
                }
            }else{
                bytes = Files.readAllBytes(new File("./"+path).toPath());
                data = "" + bytes.length;
                format = "text/html";                               
            }
            
            String encabezado = "HTTP/1.1 200 OK\r\n" 
                                + "Content-Type: " + format + "\r\n"
                                + "Content-Length: " + data
                                + "\r\n\r\n";
            
            byte [] bytesEn = encabezado.getBytes();
            byte[] res = new byte[bytes.length + bytesEn.length];
            for (int i = 0; i < bytesEn.length; i++) {res[i] = bytesEn[i];}
            for (int i = bytesEn.length; i < bytesEn.length + bytes.length; i++) {
                res[i] = bytes[i - bytesEn.length];
            }            
            socket.getOutputStream().write(res);
            socket.close();                                                                                                             
            
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
