/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_mqttserver;

import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Administrator
 */
public class SchedularTasks extends TimerTask{
   
    @Override
public void run() {

    System.out.println(Thread.currentThread().getName()+" the task has executed successfully "+ new Date());

    DB_Connectivity db=new DB_Connectivity();

    db.updatedevices();
      try {

          
      Thread.sleep(30000);

      } catch (InterruptedException e) {

        // TODO Auto-generated catch block

        e.printStackTrace();

      }


}
}