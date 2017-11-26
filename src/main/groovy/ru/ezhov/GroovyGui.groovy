package ru.ezhov

import groovy.swing.SwingBuilder
import javax.swing.*
import java.util.logging.*
import jsyntaxpane.*
import ru.ezhov.panels.*

class GroovyGui {

    private static
    final Logger LOG = Logger.getLogger(GroovyGui.class.getName())

    private SwingBuilder sb = new SwingBuilder()

    static void main(String[] args) {
        if (args.length > 0) {
            connectLogConsole()
        } else {
            connectLogGUI()
        }

        try {
            Loader.loadDll()
            Loader.loadJar()
        } catch (Throwable tr) {
            LOG.log(Level.WARNING, tr.message)
            return
        }

        if (args.length > 0) {
            executeCMD(args)
        } else {
            executeGUI()
        }
    }

    private static void executeGUI() {
        DefaultSyntaxKit.initKit()
        def groovyGUI = new GroovyGui()
        groovyGUI.execute()
    }

    private static void executeCMD(String[] args) {
        FileExecutor fileExecutor = new FileExecutor(pathToFile: args[0])
        fileExecutor.execute();
    }

    void execute() {
        sb.build {
            lookAndFeel('system')
            frame(
                    title: 'from db to db [ezhov_da]',
                    defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
                    show: true,
                    size: [900, 500],
                    locationByPlatform: true
            ) {
                panel(new PanelTabbed())
            }
        }
    }

    private static void connectLogGUI() {
        connectLog("gui_logger.properties")
    }

    private static void connectLogConsole() {
        connectLog("console_logger.properties")
    }

    private static void connectLog(String log) {
        try {
            LogManager
                    .getLogManager()
                    .readConfiguration(
                    GroovyGui
                            .class
                            .getResourceAsStream("""/ru/ezhov/${log}""")
            )
        } catch (Exception ex) {
            ex.printStackTrace()
        }
    }
}


