
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
        String nondeliver_r;
        String nondeliver_m;
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
		
                WebClient client = new WebClient();
		page = client.getPage("http://41.204.247.247/IPSWeb");
		HtmlInput user = page.getElementByName("Username");
		user.setValueAttribute(username);
		HtmlInput pass = page.getElementByName("Password");
		pass.setValueAttribute(passw);
		HtmlButton btn =  (HtmlButton) page.getElementById("btnLogin");
		page = btn.click();
                if(page.asText().contains("Incorrect username or password"))
                {
                 Platform.runLater(new Runnable(){
                 public void run()
                 {
                 tarea.appendText("Incorrect username or password");
                 }
                 });
                 return;
                }
		HtmlAnchor tracktrace = page.getAnchorByText("Letters");
		page = tracktrace.click();
		HtmlAnchor loc_office_rec = page.getAnchorByText("Receive letters at local delivery office (EMG)");
		page = loc_office_rec.click();
		HtmlCheckBoxInput conditionPin = (HtmlCheckBoxInput)page.getElementByName("ConditionPinCheckBox");
		page =  conditionPin.click();
		HtmlSelect condition  =  page.getElementByName("Condition");
                if(condition.equals("Item integrity confirmed"))
		condition.setSelectedIndex(1);
                if (condition.equals("Damaged or torn"))
                    condition.setSelectedIndex(2);
                if(condition.equals("Item violated")) condition.setSelectedIndex(3);
                if(useFile)
                {
                ExcelReader xReader =  new ExcelReader();
		tracknos = xReader.readFromExcel(file,recCount);
                }
                int count = 0;
		while(count < recCount)
                {
		 if(count < btchsz)
                 {
                 String value = tracknos.get(count);
                 String response = addItem(value);
                 if(response.contains(value))
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
                 Thread.sleep(5000);
                 count++;
                 } 
                 else 
                 {
                     String response = storeitems();
                     if(response.contains("error occurred while")) 
                     {
                         count=count-batchSize;
                         btchsz = count + batchSize;
                         Thread.sleep(retrialCycle);
                     }
                     else if(response.contains("The operation was successful"))
                     {  
                         
                         btchsz=batchSize+count;
                         Platform.runLater(new Runnable(){
                         public void run()
                         {
                         tarea.appendText("Store successful..commencing next batch!!\n");
                         }
                         });
                     
                     }
                 } 
                 
                
                }
		//System.out.println(page.asText());
		
		//for(String value : values) System.out.println(value);
		
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());	
		}
			
	}
        public void  startTask()
        {
        Runnable task = new Runnable(){
        @Override
        public void run()
        {
        processHandler();
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
                System.out.println(page.asText());
		HtmlButton addbtn = (HtmlButton)page.getElementById("btnStore");
		page = addbtn.click();	
                pagehtml = page.asText();
		}
		catch(Exception ex)
		{
	     ex.printStackTrace();
		}
            return pagehtml;
        }


}


