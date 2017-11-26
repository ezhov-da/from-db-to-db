package ru.ezhov

/**
 *
 * @author ezhov_da
 */

@Singleton
class ConfigReader {
    ConfigObject configObject

    void reload() {
        load()
    }

    private load() {
        ConfigSlurper configSlurper = new ConfigSlurper()
        def pathFull = 'config.groovy'
        File file = new File(pathFull)
        configObject = configSlurper.parse(file.text)
    }


    static void main(String[] args) {
        ConfigObject configObject = ConfigReader.instance.configObject
        //println configObject
        //println configObject.users.map
        //println configObject.connectionsStrings.list
        //println configObject.drivers.list

        ConfigReader.instance.reload()
        configObject = ConfigReader.instance.configObject
        //println configObject
        //println configObject.users.map
        //println configObject.connectionsStrings.list
        //println configObject.drivers.list        
    }
}

