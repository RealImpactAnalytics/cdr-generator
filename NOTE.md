# CDR generator

# General steps of the model
For the generator there are multiple ways to implement them. So we could impose them
to implement an interface (see [Architecture](#Architecture)) and make them pluggable.
So the main logic of the model is simply to call all the generator and them to use
the generate information to simulate one day.

- Generate the cells
- Generate the operator
- Generate the users (operators, cells, schedule,...)
- Generate the social network
- finally with all those informations use a simulator to simulate one day
    There are multiple simulators possible
    - Iterate over the social graph
    - Go through each id and generate the cdr's according to some distributions
    ...
   
# Architecture
2 possibilities :

- Create a model which need an object for each generator

- Create a model with for each generator a trait
	- The trouble is that depending on the implementation of the generator, the
		parameters can be really different. 
    - We could force to have some variable when using a generator traits. But 
         I'm afraid that we'll have a lot of parameters variables in the model.
		 We could probably extract them in a config traits.

Anyway, there we'll be a lot of parameters which we'll depend on the generator
we choose.


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

## Users
Should be able to answer : 

- What's your id
- From which cell do you call at that hour
- Can you send a sms or do a call in function of your wallet
- What's your operator
- What's the probability for you to call at that hour ?

## Cells
Should be able to answer :

- What's your id
- What's your location
- What's your Delaunay's neighbors

## Social network
Should generate a graph from the list of users. The edges could have a weight 
which represent the "closeness" between the 2 users.

# Randomness
There are multiple ways to ensure some randomness.

- Each generator can have some randomness.
- Before the simulation we can randomly change the generated informations.
- After the generation of the cdr we could change some to create outliers and 
    put errors in the data,...


# General constants
Each generator could take specific parameters (map ?).

For example :

- Number of clients
- Number of cells
- Number of operators (share ?)
- Number of days
- Starting date ?
- Type of hours, Schedule

    time_types = [ [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ],
      ["h","h","h","h","h","h","h","h","r","w","w","w","r","r", "w", "w","w", "w", "r", "r", "h", "h", "h", "h"]

# Question
- Scala Test ?
- Create the class for the user,... in the generators ? Or in a separate Model folder ?
- How to generate the cells graph ? (bench vs scala)
- Handle different type of  customers ? Responsibilities of the model or of the 
	users generator ?
	- Different pass or one pass with different customer ?
	- Generate randomly different customers object with different schedule and wallet

#Next ?
## Simulator
We could do the same process for the simulator.
There could be different type of simulator. The one which iterate over a graph,
an other which take all the user and their connection and generate randomly the cdrs,...

And for the one that iterate over the social graph, we could probably decompose some step.
For example, before or after each iteration we could change the graph,...
So we could shut down some cells or change the operator of some user,...
##Batch mode: 

###Idea 1:
Use a social graph and :

- for each minute compute for each user the call and sms that
    they will made during this minute.
- Update the graph to ensure that there are no one who make 2 phone call simultaneously
- Then collect all the cdr's
- Each second  send to the application the cdr's for the second

