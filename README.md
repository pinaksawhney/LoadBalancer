Technical Documentation for Custom Load Balancer based on Server Weights
============================================================================

****************************
High Level Idea
****************************

The input with server weights and names is parsed to create a hashmap with server name as key and server weight as value.
Hash is calculated for the parsed input based on formula 100*weight + 26*(server name - 'A'). The local directory is
searched to check if the saved metadata file exists. Metadata for calculating next random server name is persisted using
a concurrent Hashmap across runs. At each run if the 'data.ser' file exists computed hash is used as key to refer to the
generic random class object. If file does not exist new random collection object is assigned that accepts weight and names
and the object is put into the hashmap and saved as metadata to the 'data.ser' file.This way by saving hashmap as a
serializable byte array gives the benefit of running multiple inputs with different server names and weights at the
same time.

RandomCollection class is a generic helper class for generating next random server name based on weights as a percentage
It takes add method to insert the server names with corresponding weights and next method to retrived next name based on
total which keeps track of occurrence of names.


*****************************
Major Design Decisions
*****************************

-> metadata is persisted as a hashMap with hash of input as key and RandomCollection object as value
-> HashMap is serialized and saved as a byte array in 'data.ser'
-> For first run 'data.ser' won't exist for subsequent runs meta data HashMap is loaded each time from 'data.ser'
-> Hash of input string is calculated by formula 100*weight + 26*(server name - 'A') to avoid collisions for meta data
-> input is parsed as a HashMap of server names and capacities


*****************************
RUN
*****************************

- for running import the project in Java IDE such as Intellij and run with input as serverName: serverCapacity
        e.g. "A:3 B:2 C:4"

- Alternatively the app can be run as command line using
        javac Main.java RandomCollection.java

 Navigate to src folder and run: java com/company/Main and enter the input


*****************************
Includes
*****************************

Project root is src folder
Project documentation is in README
source code in com.company.Main.java and com.company.RandomCollection.java

In case, of any questions contact dev: pinaksawhney5@gmail.com
