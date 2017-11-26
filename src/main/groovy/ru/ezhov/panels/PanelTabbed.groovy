package ru.ezhov.panels

import groovy.swing.SwingBuilder
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import java.awt.*

/**
 *
 * @author ezhov_da
 */
class PanelTabbed extends JPanel{
    private JButton buttonAdd
    final JTabbedPane tabbedPane
    
    final JLabel labelInstruction;
    private int counter = 1;
    
    PanelTabbed(){
        super(new BorderLayout());
        buttonAdd = new JButton(
            text: "Добавить панель",
            actionPerformed: {addTab()}
        )
        add(buttonAdd, BorderLayout.NORTH)
        
        tabbedPane = new JTabbedPane()
        addTab()
        add(tabbedPane, BorderLayout.CENTER)
        
        labelInstruction = new JLabel("<html><font color=\"blue\">Инструкция по запуску через CMD</font>")
        labelInstruction.setHorizontalAlignment(SwingConstants.RIGHT)
        labelInstruction.setCursor(new Cursor(Cursor.HAND_CURSOR))
        labelInstruction.addMouseListener(new MouseAdapter(){
                void mousePressed(MouseEvent e){
                    File file = new File("index.html")
                    Desktop desctop = Desktop.getDesktop() 
                    desctop.open(file)
                }
            });
        
        
        add(labelInstruction, BorderLayout.SOUTH)
    }
    
    def addTab(){
        new SwingBuilder().doOutside{
            tabbedPane.addTab("Tab - ${String.valueOf(counter)}", new PanelExecutor())
            counter++
        }
    }
}

