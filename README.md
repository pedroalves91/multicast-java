# multicast-java
Multicast communication in Java

### Java program for multicast communication in a VANET (Vehicular ad hoc network) application with store-carry-and-forward approach

This vehicular applications use threads to separate the process of sending and receiving alerts, so that it is possible to carry out these two functions simultaneously, so the application does not block waiting to receive something.
Each vehicle should receive alerts from the RSUs (Road Side Units), in the area where it is located, but also from all vehicles that cross him, it should also be able to send alerts to those same vehicles. To achieve this, it was necessary to use a multicast address, which in this case is FE02::1
The application, whenever it sends alerts, first obtains the list of valid addresses. This transmission is carried out periodically, in this case it is every 5 seconds.

Each vehicle has a list of received events, and each time an event is received a check will be made. First it is tested if the event list is empty, if it is, the event is immediately added. If the list is not empty, it will be checked if this event has been received previously. If it is already on the list, it will be discarded, otherwise it will be saved.
As previously mentioned, every 5 seconds each application will send its list of received events to all its neighbors, however, each event has an expiration time associated with it, after which it must be eliminated. Therefore, before sending the various events on the list, an update of the list is made, that will check if each event on the list has already exceeded its time limit.

MESSAGE STRUCTURE
| event type | position X | position Y | time |
