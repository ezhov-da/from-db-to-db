package ru.ezhov

import java.lang.reflect.Field
import java.util.logging.Logger
import java.util.logging.Level

/**
 *
 * @author ezhov_da
 */
class ActiveLibraryPath {
    
    private static final Logger LOG = Logger.getLogger(Loader.class.getName());
    
    static final synchronized void setLibraryPath(String path)
    {
        System.setProperty("java.library.path", path)
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths")
        fieldSysPath.setAccessible(true)
        fieldSysPath.set(null, null)
    }	
    
    
    static final synchronized void setPath()
    {
        ConfigObject configObject = ConfigReader.instance.configObject
        def path = configObject.dllLibrary

        
        LOG.info("устанавливаем пути dll")
        def libx64 = path + "/x64"
        def libx86 = path + "/x86"

        String ocArch = System.getProperty("sun.arch.data.model")
        String pathFull = null
        if ("64".equals(ocArch))
        {
            def x64 = path + File.separator + libx64
            def filex64 = new File(x64);
            
            pathFull = filex64.getAbsolutePath()
            LOG.log(Level.INFO, "64: {0}", pathFull)
        } else if ("32".equals(ocArch))
        {
            def x86 = path + File.separator + libx86
            def filex86 = new File(x86);
            pathFull = filex86.getAbsolutePath();
            LOG.log(Level.INFO, "32: {0}", pathFull);
        }
        setLibraryPath(pathFull);
    }    
}

