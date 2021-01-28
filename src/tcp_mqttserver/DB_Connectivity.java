/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_mqttserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Administrator
 */
public class DB_Connectivity {
           String level = "";
    int count=0;
    
    public int deviceAuthentication(String device_id,String user_password){
    int task_id=0;
    try{        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
         "jdbc:mysql://localhost:3306/survey_database","root","CXKyE2ZpT%HjbP!4c$");        
        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select id from device_registration where device_id='"+device_id+"' and password='"+user_password+"' and active='Y'");  
        while(rs.next()){
//        String user_name_server = rs.getString("user_name");
//        String user_password_server = rs.getString("user_password");
//        System.out.println("user name ::  "+user_name_server+" and user pass ::  "+user_password_server);
//        if(device_id.equals(user_name_server) && user_password.equals(user_password_server)){
//        str="Yes";
//        }else{
//            System.out.println("wrong credential ::");
//            str="NO";
//        }

         int device_registration_id = rs.getInt("device_registration_id");
          task_id = getDeviceTask(device_registration_id);
        }
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }    
    return task_id;
    }
     public String waterLeveldata(String waterlevelvalue) throws ParseException {
        String level1 = "";
        String datetime = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
        String current_date = formatter.format(date);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = df.parse(current_date);
         Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 10);
        String newTime = df.format(cal.getTime());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smart_meter_survey?useSSL=false", "root", "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select remark,date_time from ohlevel order by ohlevel_id desc limit 1 ");
            while (rs.next()) {

                level1 = rs.getString("remark");
                datetime = rs.getString("date_time");

                    if (level1.equals(waterlevelvalue)) {
                        
                        count++;
                        if(count>=10)
                        {
                        level1 = "63635";
                        
                        }
                        count=0;
                    } else {
                        level1 = level1;
                    }
                

            }
            
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:333: checkUserAndPass()" + e);
        }

        return level1;
    }
     
     public List<String> Ipaddressdata() {
        System.out.println("inside command 1 insert start mode");
        String ip_data = "";
        List<String> list = new ArrayList<String>();
        //   UpdatePrevTaskStatus(device_id); 
//        String latest_task = getDeviceLatestTask(device_id);
//        if(latest_task.equalsIgnoreCase("empty")){
//        latest_task = "task_1";
//        }else{
//          int split_value = Integer.parseInt(latest_task.split("_")[1]);
//          String  split_value1 =  String.valueOf(split_value + 1);
//          latest_task = "task_"+split_value1;         
//        }
        try {
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");      

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select ip,port from ip_name");
            while (rs.next()) {

                ip_data = rs.getString("ip") + "_" + rs.getString("port");
                list.add(ip_data);

            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error in method:8: checkUserAndPass()" + e);
        }
        return list;
    }
    
    
//      public int relayData(int device_id){
//    int relay_state=0;
//    try{        
//       Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");
//    
//        Statement stmt=con.createStatement();  
//        ResultSet rs=stmt.executeQuery("select relay_state from water_data where device_status_id='"+device_id+"'  and active='Y'");  
//        while(rs.next()){
//
//
//          relay_state = rs.getInt("relay_state");
//         
//        }
//        con.close(); 
//           
//    }catch(Exception e){
//        System.out.println("Error in method:: checkUserAndPass()"+e);
//    }    
//    return relay_state;
//    }
//          
    
    
    
    
    public void UpdateLiveStatus(String client_ip){    
    try{        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
         "jdbc:mysql://localhost:3306/mqtt_server?useSSL=false","root","CXKyE2ZpT%HjbP!4c$");                   
        String query = " update device_status set active = ? , is_authenticate = ? where device_connected_ip = ? and active ='Y'";       
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString(1, "N");   
      preparedStmt.setString(2, "No");   
      preparedStmt.setString (3, client_ip);     
     
      // execute the preparedstatement
      preparedStmt.execute();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }
    }
    
    public void UpdatePrevTaskStatus(String device_id){    
    try{        
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//         "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");     
         Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/mqtt_server","root","root");    
        String query = " update device_status set active = ? , is_authenticate = ? where device_id = ?";       
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString(1, "N");   
      preparedStmt.setString(2, "No");   
      preparedStmt.setString (3, device_id);     
     
      // execute the preparedstatement
      preparedStmt.execute();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }
    }
     
    public void updatedevices() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");

            String query = "select * from device_status m where  m.active='Y'";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            ResultSet rset = preparedStmt.executeQuery();
            while (rset.next()) {
                int id = rset.getInt("device_status_id");

                String query2 = "Select coalesce(max(d.device_data_id), 0) from device_data d where d.device_status_id=?";

                PreparedStatement preparedStmt2 = con.prepareStatement(query2);
                preparedStmt2.setInt(1, id);
                ResultSet rset2 = preparedStmt2.executeQuery();
                if (rset2.next()) {

                    int devicestatus = rset2.getInt("coalesce(max(d.device_data_id), 0)");

                    String query3 = "Select * from device_data d where d.device_data_id=?";

                    PreparedStatement preparedStmt3 = con.prepareStatement(query3);
                    preparedStmt3.setInt(1, devicestatus);
                    ResultSet rset3 = preparedStmt3.executeQuery();
                    if (rset3.next()) {

                        String created_date = rset3.getString("created_date");
                        String device_id = rset3.getString("device_status_id");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String current_date = formatter.format(date);

                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = formatter.parse(created_date);
                            d2 = formatter.parse(current_date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

// Get msec from each, and subtract.
                        long diff = d2.getTime() - d1.getTime();
                        long diffSeconds = diff / 1000;

                        if (diffSeconds > 60) {
                            UpdateDeviceStatus(device_id);
                        }
                    }

                }

            }
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:: checkUserAndPass()" + e);
        }

    }
    
    public void UpdateDeviceStatus(String device_status_id) {
        try {
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//         "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");     
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");
            String query = " update device_status set active = ? , is_authenticate = ? where device_status_id = ?";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, "N");
            preparedStmt.setString(2, "No");
            preparedStmt.setString(3, device_status_id);

            // execute the preparedstatement
            preparedStmt.execute();
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:: checkUserAndPass()" + e);
        }
    }
    
    public void insertLatlong(String latitude1,String longitude1,String height, int client_equipment_id, String task, String is_live,String time){
    int utc_hr = Integer.parseInt(time.substring(0, 2));
    int utc_min = Integer.parseInt(time.substring(2, 4));
    String utc_sec = time.substring(4, 6);
    int ist_hr = utc_hr + 5;
    int ist_min = utc_min + 30;
    String IST_Time = String.valueOf(ist_hr)+":"+String.valueOf(ist_min)+":"+utc_sec;
    
    try{      
        int position_type_id=1;
        
                String latitude = latitude1;
                String arr1[] = latitude.split("\\.");
                String beforePoint = arr1[0];
                String firsthalf = beforePoint.substring(0, 2);
                String secondhalf = beforePoint.substring(2, 4);
                String afterPoint = arr1[1];
                String finalSubString = (secondhalf+afterPoint);
                int value = (Integer.parseInt(finalSubString))/60;
                String afterMultiply =  Integer.toString(value);
                String finalString = firsthalf+"."+afterMultiply;

                String longitude = longitude1;
                String arr2[] = longitude.split("\\.");
                String beforePoint2 = arr2[0];
                String firsthalf2 = beforePoint2.substring(0, 3);
                String secondhalf2 = beforePoint2.substring(3, 5);
                String afterPoint2 = arr2[1];
                String finalSubString2 = (secondhalf2+afterPoint2);
                int value2 = (Integer.parseInt(finalSubString2))/60;
                String afterMultiply2 =  Integer.toString(value2);
                String finalString2 = firsthalf2+"."+afterMultiply2;
        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/tcp","root","root");              
        String query = " INSERT INTO position (client_equipment_id, position_data1, position_data2, position_data3, position_type_id, task, is_live, time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setInt (1, client_equipment_id);
      preparedStmt.setString (2, finalString);     
      preparedStmt.setString (3, finalString2);     
      preparedStmt.setString (4, height);     
      preparedStmt.setInt (5, position_type_id);
      preparedStmt.setString (6, task);
      preparedStmt.setString (7, is_live);
      preparedStmt.setString (8, IST_Time);
      // execute the preparedstatement
      preparedStmt.execute();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }
    }
        
    
     public void insertDeviceLatLong(int device_status_id,String latitude1,String longitude1,String fuel_level,String fuel_temp, String dummy1,String dummy2, String dummy3, String date_time,String water_service,String gngga_status){
    // String current_task = getDeviceLatestTask(device_id);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Date date = new Date();  
    System.out.println(formatter.format(date));  
    String current_date = formatter.format(date);
    String finalString="";
    String ggstatus="";
    if(gngga_status.equals("no"))
    {
    ggstatus="N";
    }
    else
    {
    ggstatus="Y";
    }
    String finalString2="";
    if(!latitude1.equalsIgnoreCase("")){
    String latitude = latitude1;
String arr1[] = latitude.split("\\.");
String beforePoint = arr1[0];
String firsthalf = beforePoint.substring(0, 2);
String secondhalf = beforePoint.substring(2, 4);
String afterPoint = arr1[1];
String finalSubString = (secondhalf + afterPoint);
int value = (Integer.parseInt(finalSubString)) / 60;
String afterMultiply = Integer.toString(value);
finalString = firsthalf + "." + afterMultiply;

String longitude = longitude1;
String arr2[] = longitude.split("\\.");
String beforePoint2 = arr2[0];
String firsthalf2 = beforePoint2.substring(0, 3);
String secondhalf2 = beforePoint2.substring(3, 5);
String afterPoint2 = arr2[1];
String finalSubString2 = (secondhalf2 + afterPoint2);
int value2 = (Integer.parseInt(finalSubString2)) / 60;
String afterMultiply2 = Integer.toString(value2);
finalString2 = firsthalf2 + "." + afterMultiply2;
    } 
    try{ 
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/mqtt_server","root","root");    

        
        // dummy1,2,3 not known have to check 
        
        
        String query = " INSERT INTO device_data (device_status_id, latitude, longitude, fuel_level, fuel_temperature, battery_voltage, engine_status, gpss_accuracy,date_time,created_date,water_level,gngga_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";       
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
     // preparedStmt.setInt (1, 1);
      preparedStmt.setInt (1, device_status_id);     
      preparedStmt.setString (2, finalString);     
      preparedStmt.setString (3, finalString2);     
      preparedStmt.setString (4, fuel_level);     
      preparedStmt.setString (5, fuel_temp);     
      preparedStmt.setString (6, dummy1);     
      preparedStmt.setString (7, dummy2);     
      preparedStmt.setString (8, dummy3);     
      preparedStmt.setString (9, date_time);     
      preparedStmt.setString (10, current_date); 
       preparedStmt.setString (11, water_service);
       preparedStmt.setString (12, ggstatus);
    // preparedStmt.setString (9, current_task);     
    
      // execute the preparedstatement
      preparedStmt.execute();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: insertDeviceLatLong()"+e);
    }
    }
     
     
     //For command "12" insertion
     
      public void insertDeviceLatLongg(int device_status_id,String latitude1,String longitude1,String fuel_level,String fuel_temp, String battery_voltage,String engine_status,String door_status, String unused_input,String accuracy,String fuel_intensity,String water_level,String water_temperature,String water_intensity,String software_version,String connectivity,String speed,String service,String crc,String time,String gngga_type){
    // String current_task = getDeviceLatestTask(device_id);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Date date = new Date();  
    System.out.println(formatter.format(date));  
    String current_date = formatter.format(date);
    String finalString="";
    String ggstatus="";
    if(gngga_type.equals("no"))
    {
    gngga_type="N";
    }
    else
    {
    gngga_type="Y";
    }
    String finalString2="";
    if(!latitude1.equalsIgnoreCase("")){
    String latitude = latitude1;
String arr1[] = latitude.split("\\.");
String beforePoint = arr1[0];
String firsthalf = beforePoint.substring(0, 2);
String secondhalf = beforePoint.substring(2, 4);
String afterPoint = arr1[1];
String finalSubString = (secondhalf + afterPoint);
int value = (Integer.parseInt(finalSubString)) / 60;
String afterMultiply = Integer.toString(value);
finalString = firsthalf + "." + afterMultiply;

String longitude = longitude1;
String arr2[] = longitude.split("\\.");
String beforePoint2 = arr2[0];
String firsthalf2 = beforePoint2.substring(0, 3);
String secondhalf2 = beforePoint2.substring(3, 5);
String afterPoint2 = arr2[1];
String finalSubString2 = (secondhalf2 + afterPoint2);
int value2 = (Integer.parseInt(finalSubString2)) / 60;
String afterMultiply2 = Integer.toString(value2);
finalString2 = firsthalf2 + "." + afterMultiply2;
    } 
    try{ 
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/mqtt_server","root","root");    

        String query = " INSERT INTO device_data (device_status_id, latitude, longitude, fuel_level, fuel_temperature, battery_voltage, engine_status, door_status,unused_input,gpss_accuracy,fuel_intensity,created_date,water_level,water_temperature,water_intensity,software_version,connectivity,speed,service,date_time,gngga_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?)";       
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
     // preparedStmt.setInt (1, 1);
      preparedStmt.setInt (1, device_status_id);     
      preparedStmt.setString (2, finalString);     
      preparedStmt.setString (3, finalString2);     
      preparedStmt.setString (4, fuel_level);     
      preparedStmt.setString (5, fuel_temp);     
      preparedStmt.setString (6, battery_voltage);     
      preparedStmt.setString (7, engine_status);     
      preparedStmt.setString (8, door_status);     
      preparedStmt.setString (9, unused_input); 
      preparedStmt.setString (10,accuracy); 
      preparedStmt.setString (11, fuel_intensity); 
       preparedStmt.setString (12, current_date); 
       preparedStmt.setString (13, water_level);
       preparedStmt.setString (14, water_temperature);
       preparedStmt.setString (15, water_intensity);
       preparedStmt.setString (16, software_version); 
       preparedStmt.setString (17, connectivity); 
       preparedStmt.setString (18, speed);
       preparedStmt.setString (19, service);
       preparedStmt.setString (20, time); 
       preparedStmt.setString (21, gngga_type); 
       
    // preparedStmt.setString (9, current_task);     
    
      // execute the preparedstatement
      preparedStmt.execute();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: insertDeviceLatLong()"+e);
    }
    }
     
     
      public String relayData(int device_id) {
        String relay_state = "";

        String relaystate1 = "";
        String relaystate2 = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select relay_state,relay_state_2 from water_data where device_status_id='" + device_id + "' and active='Y'  ");
            while (rs.next()) {

                relaystate1 = rs.getString("relay_state");
                relaystate2 = rs.getString("relay_state_2");

                relay_state = relaystate1 + "_" + relaystate2;

            }
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:222: checkUserAndPass()" + e);
        }
        return relay_state;
    }
     
       public void insertDevicewaterdata(int device_status_id, String water_level, String water_temperature, String water_intensity, String software_version, String connectivity, String service, String crc, String time, String phase_voltage_R, String phase_voltage_Y, String phase_voltage_B, String phase_current_R, String phase_current_Y, String phase_current_B, String relay_state, String relay_state2, String magnetic_sensor_value) {
        // String current_task = getDeviceLatestTask(device_id);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
        String current_date = formatter.format(date);
           SimpleDateFormat formatter1 = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        Date date1 = new Date();
        System.out.println(formatter.format(date1));
        String current_date1 = formatter1.format(date1);
//    String finalString="";
//    String ggstatus="";
//    if(gngga_type.equals("no"))
//    {
//    gngga_type="N";
//    }
//    else
//    {
//    gngga_type="Y";
//    }
//    String finalString2="";
//    if(!latitude1.equalsIgnoreCase("")){
//    String latitude = latitude1;
//String arr1[] = latitude.split("\\.");
//String beforePoint = arr1[0];
//String firsthalf = beforePoint.substring(0, 2);
//String secondhalf = beforePoint.substring(2, 4);
//String afterPoint = arr1[1];
//String finalSubString = (secondhalf + afterPoint);
//int value = (Integer.parseInt(finalSubString)) / 60;
//String afterMultiply = Integer.toString(value);
//finalString = firsthalf + "." + afterMultiply;
//
//String longitude = longitude1;
//String arr2[] = longitude.split("\\.");
//String beforePoint2 = arr2[0];
//String firsthalf2 = beforePoint2.substring(0, 3);
//String secondhalf2 = beforePoint2.substring(3, 5);
//String afterPoint2 = arr2[1];
//String finalSubString2 = (secondhalf2 + afterPoint2);
//int value2 = (Integer.parseInt(finalSubString2)) / 60;
//String afterMultiply2 = Integer.toString(value2);
//finalString2 = firsthalf2 + "." + afterMultiply2;
//    } 
        try {
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");

            String query = " INSERT INTO water_data (device_status_id,water_level,water_temperature,water_intensity,phase_voltage_R,phase_voltage_Y,phase_voltage_B,phase_current_R,phase_current_Y,phase_current_B,software_version,connectivity,service,date_time,created_date,crc,relay_state,relay_state_2,magnetic_sensor_value) VALUES (?,? ,?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            // preparedStmt.setInt (1, 1);
            preparedStmt.setInt(1, device_status_id);
            preparedStmt.setString(2, water_level);
            preparedStmt.setString(3, water_temperature);
            preparedStmt.setString(4, water_intensity);
            preparedStmt.setString(5, phase_voltage_R);
            preparedStmt.setString(6, phase_voltage_Y);
            preparedStmt.setString(7, phase_voltage_B);
            preparedStmt.setString(8, phase_current_R);
            preparedStmt.setString(9, phase_current_Y);
            preparedStmt.setString(10, phase_current_B);
            preparedStmt.setString(12, connectivity);
            preparedStmt.setString(11, software_version);
            preparedStmt.setString(13, service);   
//            if(device_status_id==6044 || device_status_id==6054){    
//             preparedStmt.setString(14, current_date1);
//            }else{
            preparedStmt.setString(14, time);
         //   }
            preparedStmt.setString(16, crc);

            preparedStmt.setString(15, current_date);
            preparedStmt.setString(17, relay_state);
            preparedStmt.setString(18, relay_state2);
            preparedStmt.setString(19, magnetic_sensor_value);

            // execute the preparedstatement
            preparedStmt.execute();
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:: insertDeviceLatLong()" + e);
        }
    }

    public void insertDeviceenergydata(int device_status_id, String total_active_power, String consumed_energy_mains, String active_energy_dg, String total_active_energy, String software_version, String connectivity, String service, String crc, String time, String phase_voltage_R, String phase_voltage_Y, String phase_voltage_B, String phase_current_R, String phase_current_Y, String phase_current_B, String relay_state) {
        // String current_task = getDeviceLatestTask(device_id);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
        String current_date = formatter.format(date);
//    String finalString="";
//    String ggstatus="";
//    if(gngga_type.equals("no"))
//    {
//    gngga_type="N";
//    }
//    else
//    {
//    gngga_type="Y";
//    }
//    String finalString2="";
//    if(!latitude1.equalsIgnoreCase("")){
//    String latitude = latitude1;
//String arr1[] = latitude.split("\\.");
//String beforePoint = arr1[0];
//String firsthalf = beforePoint.substring(0, 2);
//String secondhalf = beforePoint.substring(2, 4);
//String afterPoint = arr1[1];
//String finalSubString = (secondhalf + afterPoint);
//int value = (Integer.parseInt(finalSubString)) / 60;
//String afterMultiply = Integer.toString(value);
//finalString = firsthalf + "." + afterMultiply;
//
//String longitude = longitude1;
//String arr2[] = longitude.split("\\.");
//String beforePoint2 = arr2[0];
//String firsthalf2 = beforePoint2.substring(0, 3);
//String secondhalf2 = beforePoint2.substring(3, 5);
//String afterPoint2 = arr2[1];
//String finalSubString2 = (secondhalf2 + afterPoint2);
//int value2 = (Integer.parseInt(finalSubString2)) / 60;
//String afterMultiply2 = Integer.toString(value2);
//finalString2 = firsthalf2 + "." + afterMultiply2;
//    } 
        try {
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mqtt_server", "root", "root");

            String query = " INSERT INTO energy_data (device_status_id,total_active_power,Cons_energy_mains,active_energy_dg,total_active_energy,phase_voltage_R,phase_voltage_Y,phase_voltage_B,phase_current_R,phase_current_Y,phase_current_B,software_version,connectivity,service,date_time,created_date,crc,relay_state) VALUES (?,? ,?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            // preparedStmt.setInt (1, 1);
            preparedStmt.setInt(1, device_status_id);
            preparedStmt.setString(2, total_active_power);
            preparedStmt.setString(3, consumed_energy_mains);
            preparedStmt.setString(4, active_energy_dg);
            preparedStmt.setString(5, total_active_energy);
            preparedStmt.setString(6, phase_voltage_R);
            preparedStmt.setString(7, phase_voltage_Y);
            preparedStmt.setString(8, phase_voltage_B);
            preparedStmt.setString(9, phase_current_R);
            preparedStmt.setString(10, phase_current_Y);
            preparedStmt.setString(11, phase_current_B);
            preparedStmt.setString(13, connectivity);
            preparedStmt.setString(12, software_version);
            preparedStmt.setString(14, service);
            preparedStmt.setString(15, time);
            preparedStmt.setString(17, crc);

            preparedStmt.setString(16, current_date);
            preparedStmt.setString(18, relay_state);
            // execute the preparedstatement
            preparedStmt.execute();
            con.close();

        } catch (Exception e) {
            System.out.println("Error in method:: insertDeviceLatLong()" + e);
        }
    }
     
     
     public void insertDeviceLatLongForTarun(int device_status_id,String latitude1,String longitude1,String fuel_level,String fuel_temp, String dummy1,String dummy2, String dummy3, String date_time,String water_service){
    // String current_task = getDeviceLatestTask(device_id);
    String finalString="";
    String finalString2="";
    if(!latitude1.equalsIgnoreCase("")){
    String latitude = latitude1;
String arr1[] = latitude.split("\\.");
String beforePoint = arr1[0];
String firsthalf = beforePoint.substring(0, 2);
String secondhalf = beforePoint.substring(2, 4);
String afterPoint = arr1[1];
String finalSubString = (secondhalf + afterPoint);
int value = (Integer.parseInt(finalSubString)) / 60;
String afterMultiply = Integer.toString(value);
finalString = firsthalf + "." + afterMultiply;

String longitude = longitude1;
String arr2[] = longitude.split("\\.");
String beforePoint2 = arr2[0];
String firsthalf2 = beforePoint2.substring(0, 3);
String secondhalf2 = beforePoint2.substring(3, 5);
String afterPoint2 = arr2[1];
String finalSubString2 = (secondhalf2 + afterPoint2);
int value2 = (Integer.parseInt(finalSubString2)) / 60;
String afterMultiply2 = Integer.toString(value2);
finalString2 = firsthalf2 + "." + afterMultiply2;
    } 
    try{ 
        int a=0;
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/spring_fire","root","CXKyE2ZpT%HjbP!4c$");  
         int vehicle_id=2;
         int vehicle_rev=2;
         
          Date dt = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String cut_dt = df1.format(dt);
      //  String query = " INSERT INTO current_status_vehicle (device_status_id, latitude, longitude, fuel_level, fuel_temperature, dummy1, dummy2, dummy3) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; 
        String query = " INSERT INTO current_status_vehicle (revision_no_current,status_id,active_current,fire_retardent_level,fuel_level,vehicle_latitude,vehicle_longitude,vehicle_id_revision_no_vehicle,vehicle_id_vehicle_id,status,created_at,date_time,accuracy) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)"; 
        String query2="Select coalesce(max(m.status_id), 0) from current_status_vehicle m where active_current='Y' and vehicle_id_vehicle_id="+vehicle_id+" and vehicle_id_revision_no_vehicle="+vehicle_rev;
        String query4="Select coalesce(max(m.revision_no_current), 0) from current_status_vehicle m where active_current='Y' and status_id=?";
         String query3="UPDATE spring_fire.current_status_vehicle SET active_current='N' WHERE status_id=? and revision_no_current=? and active_current='Y'";
         String query5="Select coalesce(max(m.status_id), 0) from current_status_vehicle m where active_current='Y'";
         
         
         
        PreparedStatement ptst2=con.prepareStatement(query2);
        
        PreparedStatement ptst4=con.prepareStatement(query4);
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      
       PreparedStatement ptst3 = con.prepareStatement(query3);
     // preparedStmt.setInt (1, 1);
      ResultSet rs2= ptst2.executeQuery();
      
      while(rs2.next())
      {
      
      a=rs2.getInt("coalesce(max(m.status_id), 0)");
              }
      
      if(a<1)
      {
        PreparedStatement ptst5=con.prepareStatement(query5);
        ResultSet rs5=ptst5.executeQuery();
        while(rs5.next())
      {
      
      a=rs5.getInt("coalesce(max(m.status_id), 0)");
              }
        
        int rev1=0;
        preparedStmt.setInt (1, rev1);     
        preparedStmt.setInt (2, (a+1));
        
      }
      else
      {
          ptst4.setInt(1, a);
      ResultSet rs4= ptst4.executeQuery();
      int rev=0;
       while(rs4.next())
      {
      
      rev=rs4.getInt("coalesce(max(m.revision_no_current), 0)");
              }
      ptst3.setInt(1, a);
      ptst3.setInt(2, rev);
      ptst3.executeUpdate();
      int i=rev+1;
        preparedStmt.setInt (1, i);     
        preparedStmt.setInt (2, (a));
      
      }
      
    
      preparedStmt.setString (3, "Y");     
      preparedStmt.setInt (4, Integer.parseInt(water_service));     
      preparedStmt.setInt (5, Integer.parseInt(fuel_level));     
      preparedStmt.setString (6, finalString);     
      preparedStmt.setString (7, finalString2);     
      preparedStmt.setInt (8, vehicle_rev);  
      preparedStmt.setInt (9, vehicle_id);
      preparedStmt.setString (10, "RTK_MAYANK");
      preparedStmt.setString (11,cut_dt);
        preparedStmt.setString (12,date_time);
        preparedStmt.setString (13, (dummy3));
        
     // preparedStmt.setString (9, current_task);     
    
      // execute the preparedstatement
      preparedStmt.executeUpdate();             
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: insertDeviceLatLong()"+e);
    }
    }
             
    
     public void insertDeviceAuth(String device_id,String client_ip,int task_id){
         System.out.println("inside command 1 insert start mode");
        UpdatePrevTaskStatus(device_id); 
//        String latest_task = getDeviceLatestTask(device_id);
//        if(latest_task.equalsIgnoreCase("empty")){
//        latest_task = "task_1";
//        }else{
//          int split_value = Integer.parseInt(latest_task.split("_")[1]);
//          String  split_value1 =  String.valueOf(split_value + 1);
//          latest_task = "task_"+split_value1;         
//        }
    try{              
//        Class.forName("com.mysql.jdbc.Driver");  
//        Connection con=DriverManager.getConnection(  
//        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");      

           Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/mqtt_server","root","root");   
        String query = " INSERT INTO device_status (device_id, is_authenticate, active, task_id, device_connected_ip) VALUES (?, ?, ?, ?, ?)";
        
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString (1, device_id);
      preparedStmt.setString (2, "Yes");           
      preparedStmt.setString (3, "Y");           
      preparedStmt.setInt (4, task_id);           
      preparedStmt.setString (5, client_ip);           
      // execute the preparedstatement
      preparedStmt.execute();   
        System.out.println("inside command 1 insert exit mode");
        con.close();            
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }
    }
                  
     public int checkDeviceAuth(String device_id){
    int message=0;
    try{        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
     //  "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
       "jdbc:mysql://localhost:3306/mqtt_server","root","root");       
        String query = " select device_status_id from device_status where device_id='"+ device_id +"' and active='Y' and is_authenticate='Yes'";
        
      PreparedStatement preparedStmt = con.prepareStatement(query);
      ResultSet rset = preparedStmt.executeQuery();
            if (rset.next()) {
                 message = rset.getInt("device_status_id");   
            }          
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }    
    return message;   
    }
     
      public int getDeviceTask(int device_registration_id){
    int message=0;
    try{        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
       "jdbc:mysql://localhost:3306/survey_databse","root","CXKyE2ZpT%HjbP!4c$");       
        String query = " select task_id from task where device_registration_id='"+ device_registration_id +"' and active='Y'";
        
      PreparedStatement preparedStmt = con.prepareStatement(query);
      ResultSet rset = preparedStmt.executeQuery();
            if (rset.next()) {
                 message = rset.getInt("task_id");   
            }          
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }    
    return message;   
    }
    
    public String getDeviceLatestTask(String device_id){
    String message="empty";
    try{
        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/mqtt_server","root","CXKyE2ZpT%HjbP!4c$");       
        String query = " select current_task from device_status where device_id='"+device_id+"' order by current_task desc limit 1";
        
      PreparedStatement preparedStmt = con.prepareStatement(query);
      ResultSet rset = preparedStmt.executeQuery();
            if (rset.next()) {
                 message = rset.getString("current_task");   
            }          
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }
    
    return message;
    
    }
                        
    public String getLatestTaskName(int client_equipment_id){
    String check = null;
    try{
        
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/tcp","root","root");       
        String query = " select task1 from position "
                + " where client_equipment_id='"+client_equipment_id+"' and active='Y' group by task1 order by task1 desc limit 1";
        
      PreparedStatement preparedStmt = con.prepareStatement(query);
      ResultSet rset = preparedStmt.executeQuery();
             if(rset.next()){
                 check  = rset.getString("task1");                                         
            } else{  
                 check = "NO";
             }         
        con.close(); 
           
    }catch(Exception e){
        System.out.println("Error in method:: checkUserAndPass()"+e);
    }  
    return check ;
    
    }
          
    public void insertImage(String test) throws FileNotFoundException, IOException{
        System.out.println("image data is ...  "+test);
    int firstStartDataPosition = 0;
            int endLastDataPosition = 0;
            int minimumDatabytes = 6;
            int initialflag1 = 0;
            int endflag1 = 0;
            int initialflag2 = 0;
            int endflag2 = 0;    
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
    Date date = new Date();  
    System.out.println(formatter.format(date));  
    String current_date = formatter.format(date);
    String[] brkdate=current_date.split(" ");
    String[] brkday=brkdate[0].split("/");
    String[] brktime=brkdate[1].split(":");
    String imgname=brkday[0]+brkday[1]+brkday[2]+"_"+brktime[0]+brktime[1]+brktime[2];
    System.out.println("img name fom time is : "+imgname); 
         OutputStream out = null;
         char [] ch = test.toCharArray();
         try{
        // if(test.contains("STOP")){
        for (int i = 0; i <= (ch.length - minimumDatabytes) && (initialflag1 != 1 || initialflag2 != 1); i++) {
                    if (ch[i] == '$' && (initialflag1 != 1 || initialflag2 != 1)) {
                        if (ch[i + 1] == '$') {
                            firstStartDataPosition = i + 2;
                            initialflag2 = 1;
                        } 
                    }
                }
       //  if (initialflag1 == 1 || initialflag2 == 1) {
//             for (int i = firstStartDataPosition; i < (ch.length) - 1 && endflag2 != 1; i++) {
//                        if (ch[i] == '#') {
//                            if (ch[i + 1] == '#') {                             
//                                endLastDataPosition = i;
//                                endflag2 = 1;                                                              
//                            }
//                        }
//                    }
             
          //    if(endflag2 == 1){
//               String test1 = test.substring(firstStartDataPosition+1, ch.length-4);
//               String cmd = test1.split(",")[0];
//               String d_id = test1.split(",")[1];
//                  System.out.println("in image model ...() "+cmd + " and d id is .. " +d_id);
//                  if(cmd.equalsIgnoreCase("03")){
//               String newStr2 = test1.replaceAll(",0000,####", "");
//               String newStr3 = newStr2.replaceAll("$$$$,03,F00001,01,", "");
//               String getBackEncodedString = newStr3;
//               byte[] imageAsBytes = new BASE64Decoder().decodeBuffer(getBackEncodedString);                            
//               String destination_path = "C:\\Users\\Administrator\\Documents\\soni_test_images";
//               String file = destination_path + "/" + "test1" +".jpg";
//               //fileList.add(new File(file));
//               out = new FileOutputStream(file);
//               out.write(imageAsBytes);
//               out.close();
//                  }                  
           //   }
//             
             
        // }
        
        
         if(test.contains(",0000,####")){
              // String newStr1 = test.replaceAll("START", "");
              // String newStr2 = test.replaceAll("STOP", "");
               String newStr2 = test.replaceAll(",0000,####", "");
               String newStr3 = newStr2.replaceAll("$$$$,03,F00001,01,", "");
               String getBackEncodedString = newStr3;
               byte[] imageAsBytes = new BASE64Decoder().decodeBuffer(getBackEncodedString);                            
               String destination_path = "C:\\Users\\Administrator\\Documents\\soni_test_images";
               String file = destination_path + "/" + "test1_"+imgname +".jpg";
               //fileList.add(new File(file));
               out = new FileOutputStream(file);
               out.write(imageAsBytes);
               out.close();
        }
//         
    }catch(Exception e){
    System.out.print("exception ... "+e);
    }
    
}
}
