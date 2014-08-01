# CDR generator

The goal is to generate CDR based on different model and to allow model "mix in".  
To achieve that, the generation of the CDR is break down in differents generals steps and for each
steps, there are multiple implementations which respect a common interface. This implies
that we can use any implementation for a step very easily.  

# Generation Steps
- Generate the cells
- Generate the operator
- Generate the users (operators, cells, schedule, wallet,...)
- Generate the social network
- finally with all those informations use a simulator to simulate one day
    There are multiple simulators possible
    - Iterate over the social graph
    - Go through each id and generate the cdr's according to some distributions
    - Go through each edge of the social graph and generate random cdr for the 
	edge ( **BasicSimulator** )
    - ...

# Usage
To use the generator you must choose the generators you want for each step and
build a **Simulator** with those generators. Then you just need to call simulate 
on the simulator to generate the CDR.

		val simulator = new BasicSimulator(
			new BasicCellsGenerator(10),
			new HarcodedOperatorsGenerator(),
			new BasicUsersGenerator(50),
			new RandomSocialNetworkGenerator()
			)
		simulator.simulate(new DateTime).map(_.toString).saveAsTextFile("test.csv")

To compile and run the test :

	sbt "~test-quick"

To run :

	sbt run

To generate the scala doc (in target scala-../api) :

	sbt doc

# Existing implementation step
**CellsGenerator** :

- **BasicCellsGenerator** : Generate cells randomly in "square".

**OperatorsGenerator**:

- **HardcodedOperatorsGenerator** : Generate 2 hardcoded operators.

**UsersGenerator** : 

- **BasicUsersGenerator** : Generate DumUsers which call from only one cell

**SocialNetworkGenerator**:

- **RandomSocialNetworkGenerator** : Generate a social network based on the logNormalGraph 
    generator the graphx (generate a graph for witch each user has a random 
    (logNormal distribution) number of edges.

**Simulator** :

- **BasicSimulator** : Generate cdr in one pass over the edges of the social graph
   
# Structure :
Source structure :

	Generator/
		- Operator
		- Cells
		- Users 
	    - Schedule
		- Market
		- Social network
	Simulator/
	IO/
	Model/
	Config
	Main
    
#Next ?
##Streaming mode: 
###Idea 1:
Use a social graph and :

- for each minute compute for each user the call and sms that
    they will made during this minute.
- Update the graph to ensure that there are no one who make 2 phone call simultaneously
- Then collect all the cdr's
- Each second send to the application the cdr's for the second

