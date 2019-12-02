
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
import javax.swing.*;
import java.awt.*;





public class HtmlBrowser {
	private static List<String> tracknos = new ArrayList<String>();
	

	public static void main(String[] args) 
	{
            JFrame frame = new JFrame("My First GUI");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(500,300);
	    frame.setVisible(true);
		

	}
	protected  void addItem(HtmlPage page,String entry)
	{
		try
		{HtmlInput identifier = page.getElementByName("MailitemIdentifier");
		identifier.setValueAttribute(entry);
		HtmlButton addbtn = (HtmlButton)page.getElementById("btnAddOrStore");
		page = addbtn.click();	
		}
		catch(Exception ex)
		{
	     ex.printStackTrace();
		}
	}
	public void processHandler()
	{
		try 
		{
		WebClient client = new WebClient();
		HtmlPage page = client.getPage("http://41.204.247.247/IPSWeb");
		HtmlInput user = page.getElementByName("Username");
		user.setValueAttribute("robinson18");
		HtmlInput pass = page.getElementByName("Password");
		pass.setValueAttribute("Iken88");
		HtmlButton btn =  (HtmlButton) page.getElementById("btnLogin");
		page = btn.click();
		HtmlAnchor tracktrace = page.getAnchorByText("Letters");
		page = tracktrace.click();
		HtmlAnchor loc_office_rec = page.getAnchorByText("Receive letters at local delivery office (EMG)");
		page = loc_office_rec.click();
		HtmlCheckBoxInput conditionPin = (HtmlCheckBoxInput)page.getElementByName("ConditionPinCheckBox");
		page =  conditionPin.click();
		HtmlSelect condition  =  page.getElementByName("Condition");
		condition.setSelectedIndex(1);
		List<String> allValues = new ExcelReader().readFromExcel("D:\\sample.xls",2);
		for(String value : allValues)
		addItem(page,value);
		//System.out.println(page.asText());
		
		//for(String value : values) System.out.println(value);
		
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());	
		}
			
	}

}


