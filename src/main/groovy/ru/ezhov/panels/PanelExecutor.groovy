package ru.ezhov.panels

import groovy.sql.Sql
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import java.util.logging.Logger
import java.util.logging.Level
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.*
import ru.ezhov.DBExecutor
import ru.ezhov.*

/**
 * Панель для конкретной заливки
 * @author ezhov_da
 */
class PanelExecutor extends JPanel{
    private static final Logger LOG = Logger.getLogger(GroovyGui.class.getName());
    
    private JSplitPane jsplitpane
    private JSplitPane jsplitpaneVertical
    private JTextPane textpane
    final PanelFrom panelFrom
    final PanelTo panelTo
    final JButton buttonExecute
    final JButton buttonCancel
    private DBExecutor currentDBExecutor
    
    def soutToPane = {text -> 
        new SwingBuilder().doLater{
            textpane.text =  "${text} \n ${textpane.text}"
            textpane.setCaretPosition(0)
        }
    }
       
    PanelExecutor(){
        super(new BorderLayout())
        
        def panelCenter = new JPanel(layout: new BorderLayout())
        
        def mapFrom = [
            (Labels.USER): "Пользователь:",
            (Labels.PASS):  "Пароль:",
            (Labels.BATCH_INSERT):  "Размер вставки:",
            (Labels.CONNECTION_STRING):  "Строка подключения:",
            (Labels.DRIVER):  "Драйвер:",
            (Labels.QUERY):  "Запрос на SELECT в источнике:",
            (Labels.NAME_FIELD):  "Название полей:",
            (Labels.TITLE):  "Источник:"]
        
        panelFrom = new PanelFrom(mapFrom)
        panelFrom.setModels()
        
        def mapTo = [
            (Labels.USER): "Пользователь:",
            (Labels.PASS): "Пароль:",
            (Labels.BATCH_INSERT): "Размер вставки:",
            (Labels.CONNECTION_STRING): "Строка подключения:",
            (Labels.DRIVER): "Драйвер:",
            (Labels.QUERY): "Запрос на INSERT в таблицу получателей:",
            (Labels.NAME_FIELD): "Название полей:",
            (Labels.TITLE):  "Получатель:"]
        panelTo = new PanelTo(mapTo)
        panelTo.setModels()

        jsplitpane = new JSplitPane()
        
        jsplitpane.setDividerLocation(0.5)
        jsplitpane.setResizeWeight(0.5)
        
        jsplitpane.setLeftComponent(panelFrom)
        jsplitpane.setRightComponent(panelTo)
        
        panelCenter.add(jsplitpane, BorderLayout.CENTER)
        
        buttonExecute = new JButton( text: "Ну что же, давайте начнем переливку ))",
            actionPerformed: {
                            
                buttonExecute.enabled = false
                buttonCancel.enabled = true

                new SwingBuilder().doOutside{
                    try{
                        def from =  panelFrom.getData()
                        def to = panelTo.getData()
                        currentDBExecutor = new DBExecutor()
                        currentDBExecutor.execute(from, to, soutToPane)
                    } catch(any){
                        soutToPane(any.message)
                        def nextException = any.getNextException()
                        if (nextException != null){
                            soutToPane(any.getNextException().message)
                        }
                        
                        LOG.log(Level.SEVERE, "Упс, ошибка: ", any);
                    } finally{

                        buttonExecute.enabled = true
                        buttonCancel.enabled = false
                        
                        currentDBExecutor.closeConnections()
                    }
                }
            })
        
        def panelButton = new JPanel()
        panelButton.add(buttonExecute)
        buttonCancel = new JButton(text: "Отменить выполнение",
            actionPerformed: {
                try{
                    if (currentDBExecutor){
                        currentDBExecutor.closeConnections()
                    }
                }finally{

                    buttonExecute.enabled = true
                    buttonCancel.enabled = false
                        
                    currentDBExecutor.closeConnections()
                }
            }
        )
        buttonCancel.enabled = false
        panelButton.add(buttonCancel)
        
        panelCenter.add(panelButton, BorderLayout.SOUTH)
        
        jsplitpaneVertical = new JSplitPane(orientation: JSplitPane.VERTICAL_SPLIT)

        jsplitpane.setDividerLocation(0.6)
        jsplitpane.setResizeWeight(0.6)
        
        jsplitpaneVertical.setLeftComponent(panelCenter)
        textpane = new JTextPane()
        
        jsplitpaneVertical.setRightComponent(new JScrollPane(textpane))
        
        add(jsplitpaneVertical, BorderLayout.CENTER)
    }
}


