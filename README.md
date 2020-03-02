# Transferer
A RESTful API (including data model and the backing implementation) for money transfers between accounts.



# Assumptions
- Back Account Transfer

java -jar target/Transferer-1.0-SNAPSHOT.jar server
java -jar "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8080" target/Transferer-1.0-SNAPSHOT.jar server

Account class
account name
account number
sort code
account id
bigdecimal balance
opendate
//getBalance
//deposit
//withdraw

check if account exist if not, create a new one

add amount, and reference message

- POST
transfer class
transfer id 
amount
reference
account{}

transfer class
transfer id 
amount
reference
account{}
transaction type - credit/debit
transaction status - completed/cancelled/pending/INSUFFICIENT_FUNDS,

Restendpoint
- post transfer
- get transfer
- get all transfers(history)
- get account
- 

=== check using in memory database example
=== check account transfer data model/ database structure
=== Adding lightweight DI like Guice?
=== CQRS


Not supporting
- currency conversions and multi-currency operations