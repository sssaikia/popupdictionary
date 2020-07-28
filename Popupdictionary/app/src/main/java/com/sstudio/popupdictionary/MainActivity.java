package com.sstudio.popupdictionary;

import android.app.*;
import android.os.*;
import android.widget.*;
import org.json.*;
import java.io.*;
import android.*;
import android.content.pm.*;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity 
{	TextView t;
	String textData = null;
	final Integer READ_STORAGE_PERMISSION_REQUEST_CODE= 0x3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		t = findViewById(R.id.mainTextView);

		checkPermissionForReadExtertalStorage();
		try
		{			
			File file = new File(this.getFilesDir(), "words.json");				
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
				textData =  responce;
				t.setText(responce);
			}
			catch (IOException e)
			{t.setText("error1" + e.toString());}
		}
		catch (IOException e)
		{
			t.setText(e + "\n\nSelect a word anywhere,"
					  + " and from the context menu, click on meaning. \n" +
					  "Those word will be displayed here,"
					  + "and this error will be gone. \n Or import from a file.");
		}

		(findViewById(R.id.export))
			.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
				{
					checkPermissionForReadExtertalStorage();
					if (textData != null)
					{
						writeData(getStorageDir("words.json"));
					}
					else
					{
						(Toast.makeText(MainActivity.this,
										"something is wrong", Toast.LENGTH_SHORT))
							.show();
					}
                }
            });

		(findViewById(R.id.imprt))
			.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
				{
					checkPermissionForReadExtertalStorage();
					File file = new File(getStorageDir("words.json"));				
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
							textData =  responce;
							t.setText(responce);

							try
							{		
								File file1 = new File(MainActivity.this.getFilesDir(), "words.json");		
								FileWriter fileWriter = new FileWriter(file1, true);			
								fileWriter.append(responce);
								fileWriter.flush();
								fileWriter.close();
							}
							catch (Exception ee)
							{
								((Toast.makeText(MainActivity.
												 this, "Something is really wrong "
												 + ee, Toast.LENGTH_SHORT))).show();
							}
						}
						catch (IOException e)
						{t.setText("error1" + e.toString());}
					}
					catch (FileNotFoundException e)
					{((Toast.makeText(MainActivity.this, "error" + e, Toast.LENGTH_SHORT))).show();}	
                }
            });
    }

	public void checkPermissionForReadExtertalStorage()
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
			if (result == PackageManager.PERMISSION_GRANTED)
			{
				//  t.setText("permission granted");
			}
			else
			{
				//   t.setText("Storage ermission not granted");
				(Toast.makeText(MainActivity.this,
								"Storage access permission missing! "
								+ "Allow manually.", Toast.LENGTH_SHORT))
					.show();
				MainActivity.this.requestPermissions(new String[]
													 {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
													 001);
			}
        }
		else
		{
			//t.setText("grant storage permission");
			(Toast.makeText(MainActivity.this,
							"Storage access permission missing! "
							+ "Allow manually.", Toast.LENGTH_SHORT))
				.show();
		}
    }
	//write data to file
    public void writeData(String filePath)
	{
        try
		{
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(textData.getBytes());
            fileOutputStream.close();
			(Toast.makeText(MainActivity.this,
							"Export complete. Saved to " + filePath, Toast.LENGTH_SHORT))
				.show();
        }
		catch (FileNotFoundException e)
		{
            e.printStackTrace();
			(Toast.makeText(MainActivity.this,
							"Export failed " + e, Toast.LENGTH_SHORT))
				.show();
        }
		catch (IOException e)
		{
            e.printStackTrace();
			(Toast.makeText(MainActivity.this,
							"Failed " + e, Toast.LENGTH_SHORT))
				.show();
        }
    }
	
	public String getStorageDir(String fileName)
	{
        //create folder
        File file = new File(Environment.getExternalStorageDirectory(), "qDict");
        if (!file.mkdirs())
		{
            file.mkdirs();
        }
        String filePath = file.getAbsolutePath() + File.separator + fileName;
        return filePath;
	}
}
