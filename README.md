# Services Android


## Requirements:
- Separate service connection from Activity
- Running in Background Thread Priority
- Communication between Client(Activity or Service) and Service
- Responses from Service to Client (Activity or Service)
- Indications from Service to Client
- Reusable code except for specific service interface.
- Hide details about how parameters are sent from Client and from Service.


## Design:
Split Service into three main classes:
- Service – Performs the actual workload.
- A Request interface – Handles communication form Client to Service
- A Confirm interface – Handles communication from Service to Client


## Arbitrary Enums / classes
- Respone_e - General Service response codes. Common for all service types.
- Result_e - General Interface response codes. Common for all interface functions.
- Parameters - General Keys for parameters send between service / client. Common for all services.


## Dependencies
- Activity needs some type of service from service A.
- Activity needs some type of service from service B.
- Service B needs some type of service from service A.


## Base classes
### BaseService
Common code for all types of services, handles setting up of background thread and Messenger object for receiving requests. It also provides imple subscriber implementation.

### BaseConfirm
Common code for confirm interface, i.e. sendToClient method.

### BaseReqeust
Common code for request interface. Connection to service handling and methods for sending requests to service.


## Services
### ServiceA
Simple service that handles some requests and may send indications if subscribed.

### ServiceA_Cnf
Confirm interface utilized by ServiceA class for sending response messages and indications.

### ServiceA_Req
Request interface for service A, utilized by a client. Provides callback interface for when responses and indications arrive.

### ServiceB
Simple service that handles some request and may send indications if subscribed. Depends on Service A if Request_D is sent to it. Stops depending on ServiceA if Request_E is sent to it.

### ServiceB_Cnf
Confirm interface utilized by ServiceA class for sending response messages and indications.

### ServiceB_Req
Request interface for service A, utilized by a client. Provides callback interface for when responses and indications arrive.


## Activity
Provide simple UI which with edit box, submit and a text view for showing information.
In the edit box it is possible to send the following commands:
- h , help, ? - Display help text
- 1 – Starts Service A
- 2 – Sops Service A
- 3 – Send Request A to Service A.
- 4 – Send Request B to Service A.
- 5 – Subscribe for Event C from Service A
- 6 - Unsubsribe for Event C from Service A
- 7 - Start Service B
- 8 – Stop Service B
- 9 – Send Request D to Service B
- 10 – Send Request E to Service B
- 11 – Subscribe to Event F from Service B
- 12 – Unsubscribe to Event F from Service B


## Use cases
### A> Use case for connecting to ServiceA:
#### Precondition: None
- User sends command 1 to activity
- Activity calls connect on ServiceA_Req
- ServiceA_Req calls internal method connectToService
- onCreate is called by framework on ServiceA
- onBind is called by framework on ServiceA
- onServiceConnected is called on ServiceA_Req by framework
- ServiceA_Req indicatates to client (Activity) that connection is up by calling onServiceResponse

### B> Use case subscribing to indications for Event C from ServiceA:
#### Precondition: Use case A
- User sends command 5 to activity
- Activity calls subscribe_C on ServiceA_Req
- ServiceA_Req calls internal method sendToClientIfPossible
- handleMessage is called on ServiceA, which processes the request stores the subscriber and set-ups indications with a certain time interval.
- ServiceA calls confirmSubscribe_C on Service_Cnf
- confirmSubscribe_C calls internal method sendToClient
- handleMessage is called on ServiceA_Req, which indicates to client through onServiceReponse that subscribe is in place.

### C> Use case indications is sent from ServiceA to Client(Activity)
#### Precondition: Use case A followed by use case B
- Timer for indications elapses and the Runnable's run method is called.
- BaseService.OnSend send method is called with proper indication parameters for every subscriber
- The send method calls indication_C on ServiceA_Cnf
- The indication_C calls internal method sendToClient
- handleMessage is called on ServiceA_Req, which indicates to client through onServiceIndication

### D> Use case unsubscribe to indications for Event C from ServiceA:
#### Precondition: Use case A
- As use case B but with User sending command 6 to activity
- activity calling unsubscribe_C
- ServiceA calling confirmUnsubscribe_C

### E> Use case unbinding from ServiceA:
####Precondition: Use case A
- User sends command 2 to activity
- Activity calls disconnect on ServiceA_Req
- ServiceA_Req calls disconnectFromService
- UnBind is called on ServiceA
- If service is completely terminated onDestroyed is also called on ServiceA
- ServiceA_Req indicates to client that connection is terminated through onServiceResponse
