package com.sstudio.popupdictionary;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.webkit.*;
import org.json.*;
import java.io.*;
import android.*;
import android.view.View.*;
import android.view.*;
import android.transition.*;
import android.opengl.*;

public class Main2Activity extends Activity implements View.OnClickListener
{

	@Override
	public void onClick(View p1)
	{
		switch (p1.getId()) {

			case R.id.google:
				url="https://google.com/search?q=define "+txt;
				loadUrl(url);
				break;

			case R.id.bing:
				url="https://www.bing.com/search?q=define "+txt;	
				loadUrl(url);
				break;

			case R.id.urban:
				url="https://www.urbandictionary.com/define.php?term="+txt;
				loadUrl(url);
				break;

			case R.id.wiki:
				url="https://en.wikipedia.org/wiki/"+txt;
				loadUrl(url);
				break;

			case R.id.dict:
				url="https://www.dictionary.com/browse/"+txt;
				loadUrl(url);
				break;

			default:
				break;
		}
	}


	private WebView web;
	private ProgressBar pb;
	private FrameLayout fl;
	Button googleB, bingB, urbanB, wikiB, dictB;
	CharSequence txt= null;
	String url=null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {	
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

		googleB=findViewById(R.id.google);
		googleB.setOnClickListener(this);
		bingB=findViewById(R.id.bing);
		bingB.setOnClickListener(this);
		wikiB=findViewById(R.id.wiki);
		wikiB.setOnClickListener(this);
		dictB=findViewById(R.id.dict);
		dictB.setOnClickListener(this);
		urbanB=findViewById(R.id.urban);
		urbanB.setOnClickListener(this);
		
		url="https://google.com/search?q=define "+txt;		
		loadUrl(url);
					
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
	public void loadUrl(String addr){
		pb.setVisibility(ProgressBar.VISIBLE);
		web.loadUrl(addr);
		web.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageFinished(WebView View, String url)
				{
					pb.setVisibility(ProgressBar.INVISIBLE);
				}
			});
	}
}
