package ru.ezhov

import java.util.logging.Logger

/**
 *
 * @author ezhov_da
 */

@Singleton
class ConfigReader {
    final Logger LOG = Logger.getLogger(ConfigReader.class.getName())

    ConfigObject configObject

    void reload() {
        load()
    }

    private load() {
        ConfigSlurper configSlurper = new ConfigSlurper()
        def pathFull = 'config.groovy'
        File file = new File(pathFull)
        LOG.
        configObject = configSlurper.parse(file.text)
    }


    static void main(String[] args) {
        ConfigObject configObject = ConfigReader.instance.configObject
        println configObject
        println configObject.users.map
        println configObject.connectionsStrings.list
        println configObject.drivers.list

        ConfigReader.instance.reload()
        configObject = ConfigReader.instance.configObject
        println configObject
        println configObject.users.map
        println configObject.connectionsStrings.list
        println configObject.drivers.list
    }
}

