package ru.ezhov

import java.util.logging.Logger
import java.util.logging.Level

/**
 * Выполняем указанный файл
 * @author ezhov_da
 */
class FileExecutor {
    
    private static final Logger LOG = Logger.getLogger(FileExecutor.class.getName());
    
    private String pathToFile;
    
    def soutToLOG = {text -> 
        LOG.log(Level.INFO, text.toString())
    }
    
    void execute(){
        
        //println pathToFile
        LOG.log(Level.INFO, pathToFile)
        ConfigSlurper configSlurper = new ConfigSlurper()
        
        def configObject = configSlurper.parse(new File(pathToFile).text)

        def mapFrom = configObject.data["from"]
        def mapTo = configObject.data["to"]
        
        //println mapFrom
        //LOG.log(Level.INFO, mapFrom)
        //println mapTo
        //LOG.log(Level.INFO, mapTo)
        
        def mapObjectFrom = [:]
        
        User userFrom = new User(login: mapFrom["USER"])
        mapObjectFrom[(Labels.USER)] = userFrom     
        mapObjectFrom[(Labels.PASS)] = mapFrom["PASS"]
        mapObjectFrom[(Labels.CONNECTION_STRING)] = mapFrom["CONNECTION_STRING"]
        mapObjectFrom[(Labels.DRIVER)] = mapFrom["DRIVER"]
        mapObjectFrom[(Labels.QUERY)] = mapFrom["QUERY"]
        mapObjectFrom[(Labels.NAME_FIELD)] = "\"${mapFrom["NAME_FIELD"].replace(',', '","').replace(' ', '')}\""
        
        //print mapFrom["NAME_FIELD"]
        
        def mapObjectTo = [:]
        User userTo = new User(login: mapTo["USER"])
        mapObjectTo[(Labels.USER)] = userTo
        mapObjectTo[(Labels.PASS)] = mapTo["PASS"]   
        mapObjectTo[(Labels.BATCH_INSERT)] = mapTo["BATCH_INSERT"]   
        mapObjectTo[(Labels.CONNECTION_STRING)] = mapTo["CONNECTION_STRING"]   
        mapObjectTo[(Labels.DRIVER)] = mapTo["DRIVER"]   
        mapObjectTo[(Labels.QUERY)] = mapTo["QUERY"]   
        mapObjectTo[(Labels.NAME_FIELD)] = mapTo["NAME_FIELD"]   
        
        //println mapObjectFrom
        //println mapObjectTo
        
        DBExecutor dBExecutor;
        try{
            dBExecutor = new DBExecutor()
            dBExecutor.execute(mapObjectFrom, mapObjectTo, soutToLOG);
        } catch(any){
            soutToLOG(any.message)
            if (dBExecutor != null){
                dBExecutor.close();
            }
        }
    }
}

