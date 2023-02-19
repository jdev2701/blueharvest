
  
# Blue Harvest Assignment    
 The  project consists of 3 backend sub projects and 1 frontend project.    
    
 - Account Service    
 - Transaction Service    
 - Eureka Naming Service    
 - single page index.html   
    
## Requirements    
    
- JDK 1.8+    
 - Gradle
    
    
## Services    
 The server consists from 3 services.     
    
 - **Transaction Service:** Responsible from creating and listing transactions of an account. By default settings runs on port 8000.    
 - **Account Service:** Responsible from creating and listing customers and accounts. Also sends request to transaction service for transaction related operations. By default settings runs on port 8080.    
 - **Eureka Naming Service:** Responsible from discovering services and allows to connect them to each other via naming. By default settings runs on port 8761.    

**Note:** Account service requires transaction service for transaction related operations. `account-service` still runs even if the `transaction-service` is not up yet, but transaction related operations fails until `transaction-service` is up. In such case it may still take a few seconds to run transaction related operations after `transaction-service` is up and running. 
  
### frontend Side  

A very simple user interface. Inside the project directory, open index.html in the browser.   
