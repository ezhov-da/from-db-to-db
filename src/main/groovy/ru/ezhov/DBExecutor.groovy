package ru.ezhov

import groovy.sql.*
import java.util.logging.*
import java.sql.Types

/**
 *
 * @author ezhov_da
 */
class DBExecutor {
    private static final Logger LOG = Logger.getLogger(DBExecutor.class.getName());
    
    private Closure closureOutTextLog
    private Sql sqlFrom
    private Sql sqlTo
    
    void execute(Map mapFrom, Map mapTo, Closure closureOutTextLog ){
        
        this.closureOutTextLog = closureOutTextLog
        
        printClosure(mapFrom)
        printClosure(mapTo)
        
        ConfigObject configObject = ConfigReader.instance.configObject

        def urlFrom = mapFrom[(Labels.CONNECTION_STRING)] as String
        printClosure( "url from: ${urlFrom}")
        def userFrom = mapFrom[(Labels.USER)].login as String
        printClosure( "user from: ${userFrom}")
        def passwordFrom = mapFrom[(Labels.PASS)]
        
        def driverFrom = mapFrom[(Labels.DRIVER)] as String
        printClosure( "driver from: ${driverFrom}"  )         
        
        def fields = mapFrom[(Labels.NAME_FIELD)]
        printClosure( "fields from: ${fields}")
        
        //        def listFieldFrom = fields.split(',').collect{
        //            def i = it as String
        //            i = i.trim()
        //            i
        //        }
        def listFieldFrom = Eval.me("[${fields}]")

        //print listFieldFrom
        printClosure( "list field from: ${listFieldFrom}")
        
        def queryFrom = mapFrom[(Labels.QUERY)]
        printClosure( "query from: ${queryFrom}")

        def urlTo =  mapTo[(Labels.CONNECTION_STRING)]
        printClosure( "url to: ${urlTo}")
        def userTo = mapTo[(Labels.USER)].login
        printClosure( "user to: ${userTo}")
        def passwordTo = mapTo[(Labels.PASS)]
        def driverTo =  mapTo[(Labels.DRIVER)]
        printClosure( "driver to: ${driverTo}")
        def batch = mapTo[(Labels.BATCH_INSERT)] as int
        printClosure( "batch to: ${batch}"  )
        
        def queryTo = mapTo[(Labels.QUERY)] 
        printClosure( "query to: ${queryTo}")

        //EXECUTOR<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        printClosure( "start execute ${new Date()}")
        
        printClosure( "try connect...")
        printClosure( "try connect [from]...")
        sqlFrom = Sql.newInstance(urlFrom, userFrom, passwordFrom, driverFrom)
        printClosure( "connection [from] successful")
        printClosure( "try connect [to]...")
        sqlTo = Sql.newInstance(urlTo, userTo, passwordTo, driverTo)        
        printClosure( "connection [to] successful")
        printClosure( "all connections successful")
        
        def counter = 1
        //println listFromResult
        sqlTo.withBatch(batch, queryTo) { ps ->
            sqlFrom.query(queryFrom) { resultSet ->
    
                def rsmt = resultSet.getMetaData()     
                def colCount = rsmt.getColumnCount()
                def mapColumn = [:]
                printClosure("column count: " + colCount)
                1.upto(colCount){
                    def columnType = rsmt.getColumnType(it)
                    def originalNameColumn = rsmt.getColumnName(it)         
                    def labelNameColumn = rsmt.getColumnLabel(it)         
                    
                    printClosure(
                        "original name column: [" + 
                        originalNameColumn + 
                        "] column type: [" + 
                        columnType +
                        "] label: [" + 
                        labelNameColumn +"]")
                    def nameColumn = listFieldFrom[it - 1]
                    mapColumn[nameColumn] = columnType
                }
                printClosure("""from column map ${mapColumn}""")    
    
                while (resultSet.next()) {
                    def listFromResult = []  
                    listFieldFrom.each{
                        
                        def itString = it as String
                        //printClosure(itString)
                        def type = mapColumn[itString]
                        def data = resultSet.getObject(itString)
                        listFromResult << getInParameter(type, data)
                    }     
          
                    if (counter % batch == 0) {
                        printClosure( "set batch: ${counter} - date: ${new Date()}")
                    }
                    
                    ps.addBatch(listFromResult)
                    counter++
                }
            }
        }
        printClosure "end execute ${new Date()}"
        printClosure "~ ALL DONE ~"

        closeConnections()
    }
    
    private void printClosure(objectForPrint){
        //println text
        if (closureOutTextLog != null){
            closureOutTextLog("--> " + objectForPrint)
        }
    }
    
    private InParameter getInParameter(int type, Object object) {
        switch(type){
        case Types.ARRAY: return Sql.ARRAY(object)
        case Types.BIGINT: return Sql.BIGINT(object)
        case Types.BINARY: return Sql.BINARY(object)
        case Types.BIT: return Sql.BIT(object)
        case Types.BLOB: return Sql.BLOB(object)
        case Types.BOOLEAN: return Sql.BOOLEAN(object)
        case Types.CHAR: return Sql.CHAR(object)
        case Types.CLOB: return Sql.CLOB(object)
        case Types.DATALINK: return Sql.DATALINK(object)
        case Types.DATE: return Sql.DATE(object)
        case Types.DECIMAL: return Sql.DECIMAL(object)
        case Types.DISTINCT: return Sql.DISTINCT(object)
        case Types.DOUBLE: return Sql.DOUBLE(object)
        case Types.FLOAT: return Sql.FLOAT(object)
        case Types.INTEGER: return Sql.INTEGER(object)
        case Types.JAVA_OBJECT: return Sql.JAVA_OBJECT(object)
        case Types.LONGNVARCHAR: return Sql.LONGNVARCHAR(object)
        case Types.LONGVARBINARY: return Sql.LONGVARBINARY(object)
        case Types.LONGVARCHAR: return Sql.LONGVARCHAR(object)
        case Types.NCHAR: return Sql.NCHAR(object)
        case Types.NCLOB: return Sql.NCLOB(object)
        case Types.NULL: return Sql.NULL(object)
        case Types.NUMERIC: return Sql.NUMERIC(object)
        case Types.NVARCHAR: return Sql.NVARCHAR(object)
        case Types.OTHER: return Sql.OTHER(object)
        case Types.REAL: return Sql.REAL(object)
        case Types.REF: return Sql.REF(object)
        case Types.ROWID: return Sql.ROWID(object)
        case Types.SMALLINT: return Sql.SMALLINT(object)
        case Types.SQLXML: return Sql.SQLXML(object)
        case Types.STRUCT: return Sql.STRUCT(object)
        case Types.TIME: return Sql.TIME(object)
        case Types.TIMESTAMP: return Sql.TIMESTAMP(object)
        case Types.TINYINT: return Sql.TINYINT(object)
        case Types.VARBINARY: return Sql.VARBINARY(object)
        case Types.VARCHAR: return Sql.VARCHAR(object)
        default: throw new IllegalArgumentException("Некорректный номер параметра java.sql.Types: {$type}")
        } 
    }
    
    public void closeConnections(){
        if (sqlTo != null) sqlTo.close()
        if (sqlFrom != null)  sqlFrom.close() 
    }
}

