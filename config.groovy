dllLibrary = "dll"

jarLibrary = "jars"

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

batch = 5000

users {
    map = [
        [login: "ezhov_da", pass: ""],
    ]
}
    
connectionsStrings {
    list =  [
        "jdbc:sqlserver://;servername=[serve-rname];integratedSecurity=true",
    ]
}
    
drivers {
    list = [
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "com.teradata.jdbc.TeraDriver"
    ]
}
    
