/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_mqttserver;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class tcp_model {

    String currentTime;
    DateFormat dateFormat;
    private int noOfRequestReceived;
    private Calendar cal;
    String abc = "true";
    String testing="testing";
    int mayank5req = 0;
    static String devicedata = "";

    public static JSONObject json1 = new JSONObject();

    public String receivedBytes(byte[] receivedBytes, String client_ip) {
        String response = "";
        DB_Connectivity db = new DB_Connectivity();
        try {
            byte[] bytes = receivedBytes;
            int read = bytes.length;
            noOfRequestReceived++;
            System.out.println("number of bytes actualy read: " + read);
            System.out.println("received Request No: " + noOfRequestReceived);
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.cal = Calendar.getInstance();
            this.currentTime = this.dateFormat.format(this.cal.getTime());
            System.out.println("cal -currentTime--" + this.currentTime);

            byte[] device_data1 = bytes;
            String string_device_data1 = new String(device_data1);
            System.out.println("String data........: " + string_device_data1);
     
            String img_req = "";
            if (abc.equalsIgnoreCase("true")) {
                //db.insertImage(string_device_data1);
                //img_req = string_device_data1.split(",")[1];
                if (img_req.equalsIgnoreCase("03")) {
                    abc = "true1";
                }
                //abc = "";
            }

            int firstStartDataPosition = 0;
            int endLastDataPosition = 0;
            int minimumDatabytes = 6;
            int initialflag1 = 0;
            int endflag1 = 0;
            int flag_test = 0;
            int initialflag2 = 0;
            int endflag2 = 0;
             int rel = 0;
         //    int relay_after_update=0;
                 String relay_after_update="";
            try {
                for (int i = 0; i <= (bytes.length - minimumDatabytes) && (initialflag1 != 1 || initialflag2 != 1); i++) {
                    if (bytes[i] == 36 && (initialflag1 != 1 || initialflag2 != 1)) {
                        if (bytes[i + 1] == 36) {
                            firstStartDataPosition = i + 2;
                            initialflag2 = 1;
                        } else {
                            firstStartDataPosition = i + 1;
                            initialflag1 = 1;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("ClientResponseReader readClientResponse initial delimiter Exception: " + e);
            }

            if (initialflag1 == 1 || initialflag2 == 1) {

                for (int i = firstStartDataPosition; i < (bytes.length) - 1 && endflag2 != 1; i++) {
            //        flag_test =1;
                    if (bytes[i] == 35) {
                        if (bytes[i + 1] == 35) {
                            //  if(mayank5req >12){
                            endLastDataPosition = i;
                            endflag2 = 1;
                            mayank5req = 0;
                            //  }
                            mayank5req++;
                        }
                    }
                }

                if (endflag2 == 1) {
                    byte[] device_data = Arrays.copyOfRange(bytes, firstStartDataPosition + 1, endLastDataPosition - 1);
                    String string_device_data = new String(device_data);
                    String device_array[] = string_device_data.split(",");
                    String test = string_device_data1;
                    String command = device_array[0];
                    System.out.println("command idd from web service -"+command);
                    int array_length = device_array.length;

                     if (command.equalsIgnoreCase("01")) {
//                          abc = "true1";
                          
                        System.out.println("In Command....." + command);
                        String device_id = device_array[1];
                        String no_of_parameter = device_array[2];
                        String password = device_array[3];
                        String crc = device_array[4];
                        //int task_id=db.deviceAuthentication(device_id,password);                       
                        int task_id = 1;
                        //if(task_id !=0){
                        //if(password.equalsIgnoreCase("P_38085")){                       
                        db.insertDeviceAuth(device_id, client_ip, task_id);
                        response = "$$$$," + command + "," + device_id + ",01,01,12,####\r\n";
                        // }
                        // else{
                        // response= "$$$$,"+command+","+device_id+",01,00,12,####\r\n";
                        // }
                    }
                     if (command.equalsIgnoreCase("abhijeet")) {
//                          abc = "true1";
                          
                        System.out.println("In Command....." + command);
                     
                        response = "The programming language Python was conceived in the late 1980s, and its implementation was started in December 1989 by Guido van Rossum at CWI in the Netherlands as a successor to ABC capable of exception handling and interfacing with the Amoeba operating system.Van Rossum is Python's principal author, and his continuing central role in deciding the direction of Python is reflected in the title given to him by the Python community, Benevolent Dictator for Life (BDFL). (However, van Rossum stepped down as leader on July 12, 2018.) Python was named for the BBC TV show Monty Python's Flying Circus.Python 2.0 was released on October 16, 2000, with many major new features, including a cycle-detecting garbage collector (in addition to reference counting) for memory management and support for Unicode. However, the most important change was to the development process itself, with a shift to a more transparent and community-backed process.Python 3.0, a major, backwards-incompatible release,12.";
                   
                        
                    }


                    if (command.equalsIgnoreCase("02")) {
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);
                        for (int i = 0; i < multipleRequest.length; i++) {
                            String device_id = multipleRequest[i].split(",")[1];
                            String no_of_parameter = multipleRequest[i].split(",")[2];
                            String fuel_level = multipleRequest[i].split(",")[3];
                            String fuel_temp = multipleRequest[i].split(",")[4];
                            String time = multipleRequest[i].split(",")[5];
                            String latitude = multipleRequest[i].split(",")[6];
                            String longitude = multipleRequest[i].split(",")[7];
                            String dummy1 = multipleRequest[i].split(",")[8];
                            String dummy2 = multipleRequest[i].split(",")[9];
                            String dummy3 = multipleRequest[i].split(",")[10];
                            String water_service = multipleRequest[i].split(",")[11];
                            String crc = multipleRequest[i].split(",")[12];
                            int device_status_id = db.checkDeviceAuth(device_id);
                            if (device_status_id != 0) {
//                           db.insertDeviceLatLong(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                                db.insertDeviceLatLongForTarun(device_status_id, latitude, longitude, fuel_level, fuel_temp, dummy1, dummy2, dummy3, time, water_service);
                                response = "$$$$," + command + "," + device_id + ",01,01,12,####\r\n";
                            } else {
                                System.out.println("Authentication errror....." + device_status_id);
                                response = "$$$$," + command + "," + device_id + ",01,00,12,####\r\n";
                            }

                        }
                    }

                    if (command.equalsIgnoreCase("03")) {
                        System.out.println("In Command....." + command);
                        //String test_image = device_array[4];
                        //int test_range = test_image.length();
                        //db.insertImage(test_image);
                    }

                    if (command.equalsIgnoreCase("08")) {
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);
                        //       for(int i = 0 ; i < multipleRequest.length ; i++){
                        //String device_id = multipleRequest[i].split(",")[1];
                        String device_id = device_array[1];
                        //String no_of_parameter = multipleRequest[i].split(",")[2];
                        String no_of_parameter = device_array[2];
                        //String fuel_level = multipleRequest[i].split(",")[3];
                        String fuel_level = device_array[3];
                        //String fuel_temp = multipleRequest[i].split(",")[4];                      
                        String fuel_temp = device_array[4];
                        //String time = multipleRequest[i].split(",")[5];                         
                        String time = device_array[5];
                        String string_type = device_array[6];

                        String dummy1 = device_array[array_length - 6];
                        System.out.println("device array at 22 -" + device_array[array_length - 5]);
                        //String dummy2 = multipleRequest[i].split(",")[22];                      
                        String dummy2 = device_array[array_length - 5];
                        //String service = multipleRequest[i].split(",")[22];  

                        String water_service = device_array[array_length - 4];
                        //String crc = multipleRequest[i].split(",")[23];                       
                        String crc = device_array[array_length - 1];

                        System.out.println("Before GNGGA string type= " + string_type);

                        String latitude = "", longitude = "", gps_qua_indi = "", hdop = "", dummy3 = "", gngga_status = "";
                        if (!(string_type.equals("$GNGGA"))) {
                            gngga_status = "no";
                        } else {
                            gngga_status = "yes";
                            System.out.println("After GNGGA string type=" + string_type);
                            //String latitude = multipleRequest[i].split(",")[8];                      
                            latitude = device_array[8];
                            //String longitude = multipleRequest[i].split(",")[10];                      
                            longitude = device_array[10];
                            //String gps_qua_indi = multipleRequest[i].split(",")[12];                      
                            gps_qua_indi = device_array[12];
                            //String hdop = multipleRequest[i].split(",")[14]; 
                            hdop = device_array[14];
                            System.out.println("gpss quality indicator....." + gps_qua_indi + " and hdop value ..." + hdop);
                            dummy3 = "";
                            if (gps_qua_indi.equalsIgnoreCase("0")) {

                            } else if (gps_qua_indi.equalsIgnoreCase("4")) {
                                dummy3 = String.valueOf(Double.parseDouble(hdop) * 2);
                            } else if (gps_qua_indi.equalsIgnoreCase("5")) {
                                dummy3 = String.valueOf(Double.parseDouble(hdop) * 20);
                            } else if ((gps_qua_indi.equalsIgnoreCase("1")) || (gps_qua_indi.equalsIgnoreCase("2"))) {
                                dummy3 = String.valueOf(Double.parseDouble(hdop) * 250);
                            }

                        }

                        //String dummy1 = multipleRequest[i].split(",")[21];                      
                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
                            db.insertDeviceLatLong(device_status_id, latitude, longitude, fuel_level, fuel_temp, dummy1, dummy2, dummy3, time, water_service, gngga_status);
                            // db.insertDeviceLatLongForTarun(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                            response = "$$$$," + command + "," + device_id + ",01,01,12,####\r\n";
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response = "$$$$," + command + "," + device_id + ",01,00,12,####\r\n";
                        }

                        //  }
                    }

                    if (command.equalsIgnoreCase("12")) {
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];

                        String no_of_parameter = device_array[2];

                        String fuel_level = device_array[3]; //Sensor1 level

                        String fuel_temperature = device_array[4]; //Sensor1 temperature

                        String time = device_array[5];

                        String string_type = device_array[6];

                        String latitude = device_array[8];

                        String longitude = device_array[10];

                        String gps_quality_indicator = device_array[12];

                        String hdop = device_array[14];
                        String accuracy = "";
                        if (gps_quality_indicator.equalsIgnoreCase("0")) {

                        } else if (gps_quality_indicator.equalsIgnoreCase("4")) {
                            accuracy = String.valueOf(Double.parseDouble(hdop) * 2);
                        } else if (gps_quality_indicator.equalsIgnoreCase("5")) {
                            accuracy = String.valueOf(Double.parseDouble(hdop) * 20);
                        } else if ((gps_quality_indicator.equalsIgnoreCase("1")) || (gps_quality_indicator.equalsIgnoreCase("2"))) {
                            accuracy = String.valueOf(Double.parseDouble(hdop) * 250);
                        }

                        String battery_voltage = device_array[22];

                        String digital_input = device_array[23];

                        String fuel_intensity = device_array[24];

                        String water_level = device_array[25];

                        String water_temperature = device_array[26];

                        String water_intensity = device_array[27];
//                        String connectivity = "1", speed = "72.34", service = "0", crc = "0", software_version = "f9.1";
//
                     String software_version = device_array[28];
                     String connectivity = device_array[29];
                     //   String software_version = device_array[31];

                      //  String connectivity = device_array[32];
                     String speed = device_array[30];
                       // String speed = device_array[33];
                     String service = device_array[31];
                        //String service = device_array[34];
                    String crc = device_array[32];
                      //  String crc = device_array[35];
                        String hex = Integer.toHexString(Integer.parseInt(digital_input));

                        String hexbreak = HexToBin(hex);

                        char[] splithex = hexbreak.toCharArray();

                        String engine_status = String.valueOf(splithex[3]);

                        String door_status = String.valueOf(splithex[2]);

                        String unused_input = String.valueOf(splithex[1]);

                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
//                             devicedata = "fuel_level=" + fuel_level + ",fuel_temperature=" + fuel_temperature + ",date_time=" + time + ",lat=" + latitude + ",long=" + longitude
//                                    + ",accuracy=" + accuracy + ",voltage=" + battery_voltage + ",engine_status=" + engine_status + ",door_status=" + door_status
//                                    + ",unused_input=" + unused_input + ",fuel_intensity=" + fuel_intensity
//                                    + ",water_level=" + water_level + ",water_temperature=" + water_temperature + ",water_intensity=" + water_intensity
//                                    + ",software_version=" + software_version + ",connectivity=" + connectivity + ",speed=" + speed + ",service=" + service + ",crc=" + crc;
                            devicedata =  fuel_level+ ","+ fuel_temperature + "," + time + "," + latitude + "," + longitude
                                    + "," + accuracy + "," + battery_voltage + "," + engine_status + "," + door_status
                                    + "," + unused_input + "," + fuel_intensity
                                    + "," + water_level + "," + water_temperature + "," + water_intensity
                                    + "," + software_version + "," + connectivity + "," + speed + "," + service + "," + crc;
                             json1.put(device_id, devicedata);

//                            String str="hfsaj";
//                            //sending data to local 
////                            URL url = new URL("http://192.168.1.107:8084/api/apiServices/mqttdata");
//                            URL url = new URL("http://192.168.1.107:8084/api/apiServices/testingdata");
//                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                            con.setDoOutput(true);
//                            con.setConnectTimeout(0);
//                            con.setRequestMethod("POST");
//                            con.setRequestProperty("Content-Type", "application/json; utf-8");
//                            con.setRequestProperty("Success", "application/json");
//
//                            byte[] bytes1 = (byte[]) json1.get("device_id");
//
//                            String myStr = "This is my string";
//                            //DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                            OutputStream os = con.getOutputStream();
//                            os.write(bytes1);
//                            os.flush();
//
//
//                            int responseCode = con.getResponseCode();
//                            System.out.println("Sending 'POST' request to URL : " + url);
//                            System.out.println("Response Code : " + responseCode);
//
//                            String base64Encoded = DatatypeConverter.printBase64Binary(bytes);
//                            System.out.println("Encoded Json:\n");
//                            System.out.println(base64Encoded + "\n");
//
//                            byte[] base64Decoded = DatatypeConverter.parseBase64Binary(base64Encoded);
//                            System.out.println("Decoded Json:\n");
//                            System.out.println(new String(base64Decoded));
////        

                            //  db.insertDeviceLatLong(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service,gngga_status);
                           // db.insertDeviceLatLongForTarun(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                            db.insertDeviceLatLongg(device_status_id, latitude, longitude, fuel_level, fuel_temperature, battery_voltage, engine_status, door_status, unused_input, accuracy, fuel_intensity, water_level, water_temperature, water_intensity, software_version, connectivity, speed, service, crc, time, string_type);

                            response = "$$$$," + "201" + "," + device_id + ",01,01,12,####\r\n";
                            System.out.println("Response of command 12 :-"+response);
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response = "$$$$," + "201" + "," + device_id + ",01,00,12,####\r\n";
                        }

                    }
                    
                   
                    
                       if (command.equalsIgnoreCase("200")) {
                         System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];

                        String no_of_parameter = device_array[2];

                        String time_date = device_array[3]; 
                        

                        String water_level = device_array[4]; 
                 
//  Double diff=waterlevel
                        String water_temperature = device_array[5];

                        String water_intensity = device_array[6];

                        String phase_voltage_R = device_array[7];

                        String phase_voltage_Y = device_array[8];

                        String phase_voltage_B = device_array[9];
                        
                        String phase_current_R = device_array[10];
                        
                        String phase_current_Y = device_array[11];
                        
                        String phase_current_B = device_array[12];
                        
                       String Total_active_power = device_array[13];
                       
                       String cosumed_energy_mains = device_array[14];
                       
                       String active_power_dg =  device_array[15];
                       
                       String total_active_energy = device_array[16];
                        
                        
                          String connectivity = device_array[17];
                        
                        String software_version = device_array[18];
                        
                        String relay_status = device_array[19];
                        
                        String magnetic_sensor = device_array[20];
                        
                        String relaystate2 = device_array[21];
                        
                        String waterlevelvalue = device_array[22];
                        
                        String service = device_array[23];
                        
                        String crc = device_array[24];
                        
                        
                        
//                        String hdop = device_array[14];
//                        String accuracy = "";
//                       
//                        String battery_voltage = device_array[22];
//
//                        String digital_input = device_array[23];
//
//                        String fuel_intensity = device_array[24];
//
//                   
                        
                       // String hex = Integer.toHexString(Integer.parseInt(digital_input));

                       // String hexbreak = HexToBin(hex);

//                        char[] splithex = hexbreak.toCharArray();
//
//                        String engine_status = String.valueOf(splithex[3]);
//
//                        String door_status = String.valueOf(splithex[2]);
//
//                        String unused_input = String.valueOf(splithex[1]);


                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
  // db.insertDeviceLatLongForTarun(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                           db.insertDevicewaterdata(device_status_id, water_level, water_temperature, water_intensity, software_version, connectivity, service, crc, time_date,phase_voltage_R,phase_voltage_Y,phase_voltage_B,phase_current_R,phase_current_Y,phase_current_B,relay_status,relaystate2,magnetic_sensor);
                           db.insertDeviceenergydata(device_status_id, Total_active_power, cosumed_energy_mains, active_power_dg, total_active_energy, software_version, connectivity, service, crc, time_date, phase_voltage_R, phase_voltage_Y, phase_voltage_B, phase_current_R, phase_current_Y, phase_current_B,relay_status);
                            relay_after_update = db.relayData(device_status_id);
                            
                            if(relay_after_update.equals(""))
                            {
                            relay_after_update="0_0";
                            }
                            
                            String relay_final[] = relay_after_update.split("_");
                            
                            String relay1 = relay_final[0];
                            String relay2 = relay_final[1];
                            
                            if(relay2.equals("0") && relay1.equals("0"))
                            {
                               relay_after_update="0";
                                rel = Integer.parseInt(relay_after_update);
                            }
                            else if(relay2.equals("0") && relay1.equals("1"))
                                    {
                                    relay_after_update="1";
                                rel = Integer.parseInt(relay_after_update);

                                    }
                              else if(relay2.equals("1") && relay1.equals("0"))
                                    {
                                    relay_after_update="2";
                                rel = Integer.parseInt(relay_after_update);

                                    }
                              else if(relay2.equals("1") && relay1.equals("1"))
                                    {
                                    relay_after_update="3";
                                rel = Integer.parseInt(relay_after_update);

                                    }
                            String waterleveldata = db.waterLeveldata(waterlevelvalue);
                           
                           response = "$$$$," + "201" + "," + device_id + ",01," + relay_after_update +"1"+ ",200," + waterleveldata +",####\r\n";
                       System.out.println(response);
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response = "$$$$," + "201" + "," + device_id + ",01," + relay_after_update +"0"+",200,####\r\n";
                        System.out.println(response);
                        }



                    }

                    
                    
                    
                        if (command.equalsIgnoreCase("202")) {
                           
                           
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];

                        
                        String crc = device_array[2];
                        
                        String overheadTankminValue = "20";
                        String overheadTankmaxValue = "300";
                        
                        String magneticSensorminValue = "1";
                        String magneticSensormaxValue = "2";
                        
//                        String hdop = device_array[14];
//                        String accuracy = "";
//                       
//                        String battery_voltage = device_array[22];
//
//                        String digital_input = device_array[23];
//
//                        String fuel_intensity = device_array[24];
//
//                   
                        
                       // String hex = Integer.toHexString(Integer.parseInt(digital_input));

                       // String hexbreak = HexToBin(hex);

//                        char[] splithex = hexbreak.toCharArray();
//
//                        String engine_status = String.valueOf(splithex[3]);
//
//                        String door_status = String.valueOf(splithex[2]);
//
//                        String unused_input = String.valueOf(splithex[1]);


                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
  // db.insertDeviceLatLongForTarun(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                         
                           response = "$$$$," + "202" + "," + device_id +","+"1"+","+overheadTankminValue+","+overheadTankmaxValue+ "," + magneticSensorminValue +","+magneticSensormaxValue+","+crc+",####\r\n";
                       System.out.println(response);
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response = "$$$$," + "202" + "," + device_id +","+"0"+","+overheadTankminValue+","+overheadTankmaxValue+ "," + magneticSensorminValue +","+magneticSensormaxValue+","+crc+",####\r\n";
                        System.out.println(response);
                        }

                    }

                        
                       if (command.equalsIgnoreCase("204")) {
                           
                            String valid="";
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];
                        String overheadTankminValue = device_array[2];
                        String overheadTankmaxValue = device_array[3];
                        
                        String magneticSensorminValue = device_array[4];
                        String magneticSensormaxValue = device_array[5];
                        
                        
                        String crc = device_array[6];
                        
                     
                        if (overheadTankminValue.equals("20"))
                        {
                            if(overheadTankmaxValue.equals("300"))
                            {
                                if(magneticSensorminValue.equals("1"))
                            {
                            
                                if(magneticSensormaxValue.equals("2"))
                            {
                            
                               valid = "1";
                            }
                            }
                                
                            }
                        }
                        else
                        {
                            valid= "0";
                        }
                        

                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
  // db.insertDeviceLatLongForTarun(device_status_id,latitude,longitude,fuel_level,fuel_temp,dummy1,dummy2,dummy3,time,water_service);
                         
                           response = "$$$$," + "204" + "," + device_id +","+valid+","+crc+",####\r\n";
                       System.out.println(response);
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response =  "$$$$," + "204" + "," + device_id +","+valid+","+crc+",####\r\n";
                        System.out.println(response);
                        }

                    }
 if (command.equalsIgnoreCase("206")) {
                           
                           SimpleDateFormat formatter1 = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        Date date1 = new Date();
       
        String current_date1 = formatter1.format(date1);  
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];
                        String time = device_array[2];
                        String date = device_array[3];
                        String crc = device_array[4];
                        
                    
                       
                         
                           response = "$$$$," + "206" + "," + device_id +","+current_date1+","+crc+",####\r\n";
                       System.out.println(response);
                        

                    }
                       if (command.equalsIgnoreCase("20")) {
                        System.out.println("In Command....." + command);
                        String multipleRequest[] = string_device_data.split("\\*");
                        System.out.println("multi req data..." + multipleRequest.length);

                        String device_id = device_array[1];

                        String no_of_parameter = "6";
                        
                           List<String> ip_details = db.Ipaddressdata();
                        
                        String ipdata1 = ip_details.get(0);
                        String[] ipdata1_1 = ipdata1.split("_");
                        String ipaddress1 = ipdata1_1[0];
                        String port1 = ipdata1_1[1];
                        
                        String ipdata2 = ip_details.get(1);
                        String[] ipdata2_2 = ipdata2.split("_");
                        String ipaddress2 = ipdata2_2[0];
                        String port2 = ipdata2_2[1];
                      
                        String logspeed = "40";
                        String uploadspeed = "5";
                        int device_status_id = db.checkDeviceAuth(device_id);
                        if (device_status_id != 0) {
                       response = "$$$$," + "19" + "," + device_id + ","+no_of_parameter + ","+ipaddress1 +"," +port1 + ","+ipaddress2 +"," +port2 +","+logspeed +"," +uploadspeed +",0,####\r\n";
                       System.out.println(response);
                        } else {
                            System.out.println("Authentication errror....." + device_status_id);
                            response = "$$$$," + "19" + "," + device_id +","+ no_of_parameter + ","+logspeed +"," +uploadspeed +",0,####\r\n";
                        System.out.println(response);
                        }

                    }
                    
                   if (command.equals("50")) {
                      response = devicedata;

                    }

                    }   
                    

                
            } else {
                try {
                    for (int i = minimumDatabytes; i < (bytes.length) - 1 && endflag2 != 1; i++) {
                        
                        if (bytes[i] == 35) {
                            if (bytes[i + 1] == 35) {
                                endflag2 = 1;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("ClientResponseReader readClientResponse last delimiter Exception: " + e);
                }
                if (endflag2 == 1) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@ Sent Error: Starting delimiters not found  @@@@@@@@@@@@@@@@@@@@@@");
                } else {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@ Delimiters not found  @@@@@@@@@@@@@@@@@@@@@@");
                    // sendResponse("126 126 126 126 16 2 4 8 0 0 0 0 125 125");
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in received bytes ..." + e);
        }
        return response;
    }

    static String HexToBin(String hex) {
        int i = 0;

        while (true) {

            switch (hex) {
                case "0":
                    return ("0000");

                case "1":
                    return ("0001");

                case "2":
                    return ("0010");

                case "3":
                    return ("0011");

                case "4":
                    return ("0100");

                case "5":
                    return ("0101");

                case "6":
                    return ("0110");

                case "7":
                    return ("0111");

                case "8":
                    return ("1000");

                case "9":
                    return ("1001");

                case "A":
                case "a":
                    return ("1010");

                case "B":
                case "b":
                    return ("1011");

                case "C":
                case "c":
                    return ("1100");

                case "D":
                case "d":
                    return ("1101");

                case "E":
                case "e":
                    return ("1110");

                case "F":
                case "f":
                    return ("1111");

                default:
                    break;
            }
            i++;
        }
    }

}
