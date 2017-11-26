package ru.ezhov.panels

import java.awt.GridLayout
import java.awt.Insets
import java.util.logging.*
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import ru.ezhov.*

class PanelDb extends JPanel
{
    private static final Logger LOG = Logger.getLogger(PanelDb.class.getName());
    
    protected JLabel userLabel
    protected JComboBox userText = new JComboBox()

    protected JLabel passLabel
    protected JPasswordField passText = new JPasswordField(25)

    protected JLabel sizeBatchLabel
    protected JTextField sizeBatchText = new JTextField(15)

    protected JLabel connectLabel
    protected JComboBox connectText = new JComboBox()

    protected JLabel driverLabel
    protected JComboBox driverText = new JComboBox()

    protected JLabel queryLabel
    protected JEditorPane queryText = new JEditorPane()

    protected JLabel nameFieldLabel
    protected JTextField nameFieldText = new JTextField()
    
    protected JPanel panelTextPane = new JPanel(new BorderLayout())

    PanelDb(Map<Labels, String> mapLabel)
    {
        JScrollPane textScrollPane = new JScrollPane(queryText) 
        textScrollPane.setAutoscrolls(true)
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS)        
        panelTextPane.add(textScrollPane, BorderLayout.CENTER)
        queryText.setContentType("text/sql")//выставляем тип Java, JavaScript, Properties, Groovy, C, C++, XML, SQL, Ruby , Python
        
        userLabel = new JLabel(mapLabel[Labels.USER])
        passLabel = new JLabel(mapLabel.get(Labels.PASS))
        sizeBatchLabel = new JLabel(mapLabel.get(Labels.BATCH_INSERT))
        connectLabel = new JLabel(mapLabel.get(Labels.CONNECTION_STRING))
        driverLabel = new JLabel(mapLabel.get(Labels.DRIVER))
        queryLabel = new JLabel(mapLabel.get(Labels.QUERY))
        nameFieldLabel = new JLabel(mapLabel.get(Labels.NAME_FIELD))
        
        setTooltips()
    }
    
    private void setTooltips(){
        def text = """ 
            users {
                map = [
                    [login: "", pass: ""]
                ]
            }
        """
        
        def toFileText = "<html><p>Корректируется в конфигурационном файле <strong>&quot;config.groovy&quot;</strong></p><pre>${-> text}</pre>"

        userLabel.toolTipText = toFileText
        userText.toolTipText = toFileText       
        passLabel.toolTipText = toFileText
        passText.toolTipText = toFileText
        text = """ 
        batch = 5000 - кол-во строк для одновременной вставки
        """
        sizeBatchLabel.toolTipText = toFileText
        sizeBatchText.toolTipText = toFileText
        
        text = """ 
        URL - такой же как в JAVA
        connectionsStrings {
            list =  [
                "jdbc:sqlserver://;servername=OTZ-prod1;integratedSecurity=true",
                "jdbc:teradata://teradata/CHARSET=UTF16,TMODE=ANSI,LOGMECH=LDAP",
                "jdbc:teradata://tddev/CHARSET=UTF16,TMODE=ANSI,LOGMECH=LDAP",		
                "jdbc:teradata://tdtva/tmode=ANSI,charset=UTF8,LOGMECH=LDAP"
            ]
        }
        """
        connectLabel.toolTipText = toFileText
        connectText.toolTipText = toFileText
        text = """
        drivers {
            list = [
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                    "com.teradata.jdbc.TeraDriver"
            ]
        }
        """
        driverLabel.toolTipText = toFileText
        driverText.toolTipText = toFileText
        
        toFileText = "<html><pre>${-> text}</pre>"
        text = """
        Для SELECT можно не указывать названия столбцов.\n\
        Пример SELECT:
        select * from table
        или
        select column1 from table

        Для INSERT названия столбцов желательны - это уменьшит Ваши шансы на ошибку.\n\
        Пример INSERT:\n\
        insert into table (column1) values (?)
        """
        queryLabel.toolTipText = toFileText
        queryText.toolTipText = toFileText
        
        
        toFileText = "<html><pre>${-> text}</pre>"
        text = """
        Названия столбцов, которые будут вставляться в таблицу "Получатель" из таблицы "Источник".
        Указываются через запятую:
        column1, column2, column3 и т.д.
        """
        nameFieldLabel.toolTipText = toFileText
        nameFieldText.toolTipText = toFileText
    }
    
    void setModels(){
        ConfigObject  configObject = ConfigReader.instance.configObject
        
        sizeBatchText.text = configObject.batch
        
        def users =  configObject.users
        LOG.log(Level.INFO, users as String)
        
        def usersResult
        if (users?.map){
            LOG.log(Level.INFO, "create users list")
            def usersMap = users.map
            def usersList = usersMap.each{
                it.collect{ k, v 
                    -> new User(login: k, pass: v) }}
            //println usersList
            usersResult = usersList as User[] 
        } else {
            //println "create user"
            //println users?.map
            usersResult = new User(login: System.getProperty("user.name"), pass: "")
        }
        //println usersResult
        def defModel = new DefaultComboBoxModel(usersResult)
        userText.setModel(defModel);
        
        passText.text = defModel.getElementAt(0).pass
        
        def connectionsStrings =  configObject.connectionsStrings.list as String[]
        connectText.setModel(new DefaultComboBoxModel(connectionsStrings));
        
        def drivers =  configObject.drivers.list as String[]
        driverText.setModel(new DefaultComboBoxModel(drivers));
    }
    
    Map<Labels, String> getData(){
        
        def map = [:]
        
        def modelUser = userText.getModel() as DefaultComboBoxModel
        //println modelUser.getSelectedItem()
        map[(Labels.USER)] = modelUser.getSelectedItem()
              
        //println passText.text
        map[(Labels.PASS)] = passText.text
        
        //println sizeBatchText.text
        map[(Labels.BATCH_INSERT)] = sizeBatchText.text
        
        def modelConnect = connectText.getModel() as DefaultComboBoxModel
        //println modelConnect.getSelectedItem()
        map[(Labels.CONNECTION_STRING)] = modelConnect.getSelectedItem()
        
        def modelDriver = driverText.getModel() as DefaultComboBoxModel
        //println modelDriver.getSelectedItem()    
        map[(Labels.DRIVER)] = modelDriver.getSelectedItem()  
        
        //println queryText.text
        map[(Labels.QUERY)] = queryText.text

        //println nameFieldText.text
        
        def field = "\"${nameFieldText.text.replace(',', '","').replace(' ', '')}\""
        LOG.info("fields from forms: " + field)
        map[(Labels.NAME_FIELD)] = field
        
        map
    }
}
