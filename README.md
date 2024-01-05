# benvola
BenVola project for Microservices course INSA

BenVola projet is an application based on the aid for vulnerable persons.


To test our microservice architecture, you need firstly to execute the "ConfigServiceApplication". 

Then, you can execute the "EurekaServerApplication". 

And finaly, you can run the MissionService, UserService, and the OrchestratorService. 


MissionService :
- The goal of the micro-service Mission is to manipulate the mission
- It can create a mission, make severals modifications of a mission and show some List of missions
- Every modifications are stored into a database

UserService : 
- The micro-service User is quite the same thing but with some User objects

OrchestratorService :
- The orchestrator is the "main" application of our microservice architecture
- It can interact with every other micro-services
- The main goal of the orchestrator is to check if the active user have the corrects right to execute some action
- And then, it call the differents services to make some changes in the database.
