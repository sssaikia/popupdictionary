package com.sstudio.popupdictionary;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.webkit.*;
import org.json.*;
import java.io.*;
import android.*;

public class Main2Activity extends Activity 
{

	private WebView web;
	private ProgressBar pb;
	private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {	CharSequence txt= null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
		Intent i= getIntent();
		if(i.getCharSequenceExtra
		(Intent.EXTRA_PROCESS_TEXT)!=null){
		 txt=i.getCharSequenceExtra
		 (Intent.EXTRA_PROCESS_TEXT);
		}
	
		if(i.getStringExtra("srchtxt")!=null){
			txt = i.getStringExtra("srchtxt");
		}
		web = findViewById(R.id.web);
		fl = findViewById(R.id.fl);
		pb = findViewById(R.id.pb);

		web.loadUrl("https://google.com/search?q=define " + txt);
		web.setWebViewClient(new WebViewClient(){

				@Override
				public void onPageFinished(WebView View, String url)
				{
					fl.removeView(pb);
				}
			});

		// Define the File Path and its Name
		File file = new File(this.getFilesDir(), "words.json");
		try
		{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuilder stringBuilder = new StringBuilder();
			try
			{
				String line = bufferedReader.readLine();
				while (line != null)
				{
					stringBuilder.append(line).append("\n");
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
				String responce = stringBuilder.toString();						
				if (!responce.contains(txt))
				{
					FileWriter fileWriter = new FileWriter(file, true);			
					fileWriter.append("\n" + txt);
					fileWriter.flush();
					fileWriter.close();
				}				
			}
			catch (IOException e)
			{
				
			}
		}
		catch (IOException e)
		{
			try{		
			FileWriter fileWriter = new FileWriter(file, true);			
			fileWriter.append("\n" + txt);
			fileWriter.flush();
			fileWriter.close();
			}catch(Exception ee){
				((Toast.makeText(this,"Something is really wrong "
				+ee,Toast.LENGTH_SHORT))).show();
			}
		}
    }
}
