package ru.ezhov

import java.util.logging.*
/**
 * Загрузчик драйверов
 * @author ezhov_da
 */
class Loader {
    private static final Logger LOG = Logger.getLogger(Loader.class.getName());
    
    static loadDll(){
        ActiveLibraryPath.setPath()
    }
        
    static loadJar(){
        ConfigObject configObject = ConfigReader.instance.configObject
        def path = configObject.jarLibrary
        LOG.log(Level.INFO, "Load path JARS: ${path}")
        def filePath = new File(path)
        def ap = filePath.absolutePath    
        LOG.log(Level.INFO, "Load absolute path JARS: ${ap}")
        
        def listFiles = filePath.list() as List
        LOG.log(Level.INFO, listFiles as String)
        
        def absolutePathJars = listFiles.collect{
            def apToJarUrl = new File("${ap}${File.separator}${it}").toURL()
            LOG.log(Level.INFO, "Absolute path to JAR: ${apToJarUrl}")
            ClassLoader.getSystemClassLoader().addURL(apToJarUrl)
            apToJarUrl
        }
        LOG.log(Level.INFO, "LoadJARS: ${absolutePathJars as String}")
    }
}

