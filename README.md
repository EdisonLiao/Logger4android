# Logger4android
An easy log util for android

we can use this log util like this:

Loggger loggger = new Loggger.LogBuilder().savePath(Environment.getExternalStorageDirectory()
                + File.separator + "haha").logByDay().build();  
                
        loggger.writeLog("log something");
        
        loggger.close();
        
        
        or like this:
        
        
Loggger loggger = new Loggger.LogBuilder().savePath(Environment.getExternalStorageDirectory()
                + File.separator + "haha").logByName("hh.txt").build();
                
        loggger.writeLog("log something");
        
        loggger.close();









