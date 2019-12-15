
package fxmlexample;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javafx.application.Platform;
import javafx.scene.control.*;





public class HtmlBrowser {
	public List<String> tracknos = new ArrayList<String>();
        private HtmlPage page;
        String file;
        int batchSize;
        int recCount;
        int retrialCycle;
        String username;
        String passw;
        String condition;
        int nondeliver_r;
        int nondeliver_m;
        javafx.scene.control.TextArea tarea;
        boolean useFile = false;
	

	public static void main(String[] args) 
	{
            
		

	}
	protected String addItem(String entry)
	{
		try
		{
                 
                HtmlTextInput identifier = (HtmlTextInput)page.getElementById("MailitemIdentifier");
		identifier.setValueAttribute(entry);
		HtmlButton addbtn = (HtmlButton)page.getElementById("btnAddOrStore");
		page = addbtn.click();
                Thread.sleep(5000);
		}
		catch(Exception ex)
		{
	     ex.printStackTrace();
		}
                return page.asText();
	}
	public void processHandler()
	{
		int btchsz = batchSize;
               try 
		{
		 if(!isConnected())
                 {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Internet Connectivity Broken");
                 }
                 });
                 return;
                 }
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Establishing connection to IPS Servers......\n");
                 }
                 });
                WebClient client = new WebClient(BrowserVersion.FIREFOX_60);
                client.getOptions().setTimeout(120000);
		page = client.getPage("http://41.204.247.247/IPSWeb/EN/Users/Login");
		HtmlInput user = page.getElementByName("Username");
		user.setValueAttribute(username);
		HtmlInput pass = page.getElementByName("Password");
		pass.setValueAttribute(passw);
		HtmlButton btn =  (HtmlButton) page.getElementById("btnLogin");
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Loggin in.......\n");
                 }
                 });
		page = btn.click();
                if(page.asText().contains("Incorrect username or password"))
                {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Incorrect username or password\n");
                 }
                 });
                 return;
                }
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Logged In\n");
                 }
                 });
		HtmlAnchor tracktrace = page.getAnchorByText("Letters");
		page = tracktrace.click();
		HtmlAnchor loc_office_rec = page.getAnchorByText("Receive letters at local delivery office (EMG)");
		page = loc_office_rec.click();
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Setting Capture parameters\n");
                 }
                 });
		HtmlCheckBoxInput conditionPin = (HtmlCheckBoxInput)page.getElementByName("ConditionPinCheckBox");
		page =  conditionPin.click();
		HtmlSelect condition  =  page.getElementByName("Condition");
                /*if(condition.equals("Item integrity confirmed"))
		condition.setSelectedIndex(1);
                if (condition.equals("Damaged or torn"))
                    condition.setSelectedIndex(2);
                if(condition.equals("Item violated"))*/
                condition.setSelectedIndex(1);
                if(useFile)
                {
                ExcelReader xReader =  new ExcelReader();
		tracknos = xReader.readFromExcel(file,recCount);
                }
                //Thread.sleep(2000);
                int count = 0;
		while(count < recCount)
                {
		 
                 String value = tracknos.get(count);
                 String response = addItem(value);
                 System.out.println(response);
                 if(response.contains(value) && page.getElementsByTagName("table").size()>0)
                 {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText(value+ " added\n");
                 }
                 });
                 }
                 else 
                 {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("could not add "+value+"\n");
                 }
                 });
                 
                 }
                 Thread.sleep(500);
                 count++;
                 if(count==btchsz)
                 {
                     String result = storeitems();
                     if(result.contains("error occurred while")) 
                     {
                         count=count-batchSize;
                         btchsz = count + batchSize;
                         Thread.sleep(retrialCycle);
                     }
                     if(result.contains("The operation was successful"))
                     {  
                         
                         btchsz=batchSize+count;
                         Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("Store successful..commencing next batch!!\n");
                         }
                         });
                     
                     }
                     if(result.equals(""))
                     {
                         btchsz=batchSize+count;
                         Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("Could not commit batch...\n");
                         }
                         });
                     }
                     else 
                         btchsz = batchSize + count;
                 
                 }
                  
                
                
                }
		Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("REGISTERING FOR UNSUCCESSFUL DELIVERY........\n");
                         }
                         });
		
		//for(String value : values) System.out.println(value);
		
		}
		catch(Exception ex)
		{
		System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(null,"Service is down");
                Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.clear();
                 }
                 });
                   
		}
			
	}
        protected void unsuccessful()
        {
        int btchsz = batchSize;
        try
        {
        HtmlAnchor foInb = page.getAnchorByText("Inbound");
        page = foInb.click();
        HtmlAnchor unsuc = page.getAnchorByText("Record unsuccessful delivery (EMH)");
        page = unsuc.click();
        HtmlCheckBoxInput conditionPin = (HtmlCheckBoxInput)page.getElementByName("NonDeliveryReasonPinCheckBox");
	page =  conditionPin.click();
	HtmlSelect condition  =  page.getElementByName("NonDeliveryReason");
        if(nondeliver_r==0)
        condition.setSelectedIndex(21);
        if(nondeliver_r==1)
        condition.setSelectedIndex(20);
        HtmlCheckBoxInput nondelmeas = (HtmlCheckBoxInput)page.getElementByName("NonDeliveryMeasurePinCheckBox");
        nondelmeas.click();
        HtmlSelect nondelmeasact  =  page.getElementByName("NonDeliveryMeasure");
        if(nondeliver_m==0)
        nondelmeasact.setSelectedIndex(1);
        if(nondeliver_m==1)
        nondelmeasact.setSelectedIndex(2);
        int count = 0;
		while(count < recCount)
                {
		 
                 String value = tracknos.get(count);
                 String response = addItem(value);
                 System.out.println(response);
                 if(response.contains(value) && page.getElementsByTagName("table").size()>0)
                 {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText(value+ " added\n");
                 }
                 });
                 }
                 else 
                 {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("could not add "+value+"\n");
                 }
                 });
                 
                 }
                 Thread.sleep(500);
                 count++;
                 if(count==btchsz)
                 {
                     String result = storeitems();
                     if(result.contains("error occurred while")) 
                     {
                         count=count-batchSize;
                         btchsz = count + batchSize;
                         Thread.sleep(retrialCycle);
                     }
                     if(result.contains("The operation was successful"))
                     {  
                         
                         btchsz=batchSize+count;
                         Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("Store successful..commencing next batch!!\n");
                         }
                         });
                     
                     }
                     if(result.equals(""))
                     {
                         btchsz=batchSize+count;
                         Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("Could not commit batch...\n");
                         }
                         });
                     }
                     else 
                         btchsz = batchSize + count;
                 
                 }
                  
                
                
                }
		JOptionPane.showMessageDialog(null, "Processing Complete");
		
        
        }
        catch(Exception ex)
        {
        
        }
        }
        public void  startTask()
        {
        Runnable task = new Runnable(){
        @Override
        public void run()
        {
        processHandler();
        //unsuccessful();
        }
        };
        Thread worker = new Thread(task);
        worker.setDaemon(true);
        worker.start();
        
        }
        protected String storeitems()
        {
              String pagehtml = "";
            try
		{
                //System.out.println(page.asText());
                 
		HtmlButton addbtn = (HtmlButton)page.getElementById("btnStore");
                if(addbtn!=null)
                {
		page = addbtn.click();
                Thread.sleep(5000);
                pagehtml = page.asText();
                }
               
                
                
		}
		catch(Exception ex)
		{
	         //ex.printStackTrace();
             
		}
            return pagehtml;
        }
        public boolean isConnected()
    {
    boolean connected = false;
    try {
         URL url = new URL("http://192.168.8.1");
         URLConnection connection = url.openConnection();
         connection.connect();
         connected = true;
      } catch (MalformedURLException e) {
         
      } catch (IOException e) {
        
      }
    return connected;
    }


}


