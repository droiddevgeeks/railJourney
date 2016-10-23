package com.droiddevgeeks.railjourney.memory;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by kishan.maurya on 11-07-2016.
 */
public class JsonStorage
{

    public static void saveJsonToFile(Context context, String jsonString, String jsonFileKey)
    {
        FileOutputStream fop = null;
        File jsonFilePath;
        File jsonFile;

        File defaultDirectoryPath = context.getExternalCacheDir();
        jsonFilePath = new File(defaultDirectoryPath.toString() , "/json/");
        jsonFilePath.mkdirs();
        jsonFile = new File( jsonFilePath, jsonFileKey +".txt");
        try
        {
            jsonFile.createNewFile();
            fop = new FileOutputStream( jsonFile );
            // get the content in bytes
            byte[] contentInBytes = jsonString.getBytes(Charset.forName( "UTF-8" ));
            fop.write( contentInBytes );
            fop.flush();
            fop.close();


        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if ( fop != null )
                {
                    fop.close();
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }


    public static String getJsonFileData(Context context ,String jsonFileKey)
    {
        StringBuffer jsonString = null;
        File jsonFilePath;
        File jsonFile;
        try
        {
            File defaultDirectoryPath = context.getExternalCacheDir();
            jsonFilePath = new File(defaultDirectoryPath.toString() , "/json/");
            jsonFile = new File( jsonFilePath, jsonFileKey +".txt");
            if(jsonFile.exists())
            {
                BufferedReader bufferedReader = new BufferedReader( new FileReader( jsonFile ) );
                jsonString = new StringBuffer();
                String line;
                while ( (line = bufferedReader.readLine()) != null )
                {
                    jsonString.append( line );
                }
            }
            else
            {
                return null;
            }

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return jsonString.toString();
    }
}
