/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_mqttserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class TCP_MQTTServer {

    /**
     * @param args the command line arguments
     */
    
    private static ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream inputStream1;
    public static JSONObject json1 = new JSONObject();
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            int serverPort = 8060;
            serverSocket = new ServerSocket(serverPort);
            TCP_MQTTServer tcpServer = new TCP_MQTTServer();
            
           SchedularTasks s1=new SchedularTasks();
            // check scheduler
           
          // end scheduler
          
       Timer t=new Timer();

   

      t.scheduleAtFixedRate(s1, 0,6000);
      
      tcpServer.interceptRequests();
            
        } catch (IOException ioEx) {
            System.out.println("TCPServer main() Error: " + ioEx);
        }
        
    }
    
    
 
    
    public void interceptRequests() {
        try {
            while (true) {
                System.out.println("Waiting for Client to connect...");
                //int size = serverSocket.getReceiveBufferSize(); 
                clientSocket = serverSocket.accept();              
                System.out.println("Client connected. IP: " + clientSocket.getInetAddress());               
                (new Thread(new ClientResponseReader(clientSocket))).start();
                
                // check
//                 ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
//                 exec.scheduleAtFixedRate(new Runnable() {
//                 @Override
//                  public void run() {
//                    int i =0 ;
//                    // do stuff
//                    System.out.println("count..... "+i);
//                    i++;
//                }
//            }, 0, 10, TimeUnit.SECONDS);   
                 // end
                 
            }
        } catch (IOException ioEx) {
            System.out.println("TCPServer main() Error: " + ioEx);
        }
    }       
}

class ClientResponseReader implements Runnable {

    private BufferedReader reader;
    private DataInputStream inputStream;
    private DataOutputStream outStream;
    private Socket targetSocket;
    public static byte[] arr1;
    public static String abhi_string="";
    public ClientResponseReader(Socket rec_socket) throws IOException {
        targetSocket=rec_socket;
        inputStream = new DataInputStream(targetSocket.getInputStream());
        outStream = new DataOutputStream(targetSocket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Reading data from Client..."); 
        DB_Connectivity db= new DB_Connectivity();
        //tcp_model db1= new tcp_model();
        tcp_model tcp_m= new tcp_model();
        //boolean isConnected=false;
        boolean isConnectionClosed = true;
        String base_request = "false";
        String rover_request = "false";
        String client_IP = null;
        String pub_IP = null;
        String sub_IP = null;
        String checkPubSub = "pub";
         String task_value = null;
         String clientAndEquipmentID = null;
        int client_equipment_id =0;
        int client_id_length = 0;
        int user_name_length =0;
        int password_length =0;
        //String abhi_string="";
        // https://www.youtube.com/watch?v=hCMVx9ywBqA
       while(isConnectionClosed){
             //byte[] bytes = new byte[700];
             byte[] bytes = new byte[95000];
             int FirstByte = 0;
             int dollarByte = 0;
            try {                         
            int upth= inputStream.read(bytes);
                System.out.println("bytes array length ... "+upth);
             InetAddress client_address= targetSocket.getInetAddress();                   
             client_IP = client_address.getHostAddress();                                    
            
            if(upth == -1){              
            System.out.println("client_ip:: "+client_IP);
            db.UpdateLiveStatus(client_IP);
            targetSocket.close();
            isConnectionClosed = false;
            }                   
            // test for soni
             String abc1 = "";            
             abc1 = tcp_m.abc;            
            // test for generic
            
//            BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream));
//                     String test_image = reader1.readLine();
//                     System.out.println(" image STRING ... :: "+test_image);
//                     int test_range = test_image.length();  
//                     if(test_range > 250){
//                     System.out.println("total data image and other :: "+test_range);
//                     db.insertImage(test_image);
//                     tcp_m.abc = "true";
//                     }
            
            String response = tcp_m.receivedBytes(bytes,client_IP);
            byte[] returnBytes = response.getBytes();
            outStream.write(returnBytes);
              
            if(abc1.equalsIgnoreCase("true1")){
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream));
                     String test_image = reader1.readLine();
                     System.out.println(" image STRING ... :: "+test_image);
                     int test_range = test_image.length();  
                     if(test_range > 250){
                     System.out.println("total data image and other :: "+test_range);
                     db.insertImage(test_image);
                     tcp_m.abc = "true";
                     abc1 = "true";
                     }
            }
            
           //  outStream.write(returnBytes);     
            
            } catch (Exception interruptedEx) {
                isConnectionClosed = false;
                System.out.println("ClientResponseReader run() Error: " + interruptedEx);
            }       
    }  
    }
}

